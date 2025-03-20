import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI
import net.uoneweb.android.til.ui.receipt.ResponseParser

@Composable
fun ReceiptScreen() {
    val context = LocalContext.current
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    //OpenAiUi()
    //AiResult()

    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri.value = uri
    }

    LaunchedEffect(Unit) {
    }

    val uploaded = remember { mutableStateOf(false) }
    val uploadedImageUrl = remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Column(modifier = Modifier.verticalScroll(scrollState)) {

        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Select Image")
        }
        selectedImageUri.value?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Selected image",
                modifier = Modifier.size(256.dp),
            )

            Button(
                onClick = {
                    selectedImageUri.value?.let { uri ->
                        val storage = Firebase.storage
                        val storageRef = storage.reference
                        val imagesRef = storageRef.child("images")
                        println("storageRef:: $storageRef")
                        val fileRef = imagesRef.child(uri.lastPathSegment!!)
                        val uploadTask = fileRef.putFile(uri)
                        uploadTask.addOnSuccessListener {
                            println("Upload successful")
                            uploaded.value = true
                        }.addOnFailureListener {
                            println("Upload failed")
                        }
                        uploadTask.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            fileRef.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val downloadUri = task.result
                                uploadedImageUrl.value = downloadUri.toString()
                                println("downloadUri: $downloadUri")
                            } else {
                                println("downloadUri: failed")
                            }
                        }
                    }
                },
                enabled = !uploaded.value,
            ) {
                if (uploaded.value) {
                    Text("Uploaded")
                } else {
                    Text("Upload Image")
                }
            }

            Text("Image url: ${uploadedImageUrl.value}")
        }

        AiResult(imageUrl = selectedImageUri.value)
    }
}


@Composable
fun AiResult(imageUrl: Uri?) {
    if (imageUrl == null) {
        return
    }
    val text = remember { mutableStateOf<String?>(null) }

    val prompt_text = """
あなたはレシート画像を読み取り、指定されたJSON形式でデータを出力するエキスパートです。

レシート画像が与えられたら、以下の手順で情報を抽出し、JSON形式で出力してください。

1. 画像からテキスト情報を正確に読み取ります。
2. 読み取ったテキスト情報から、以下の情報を抽出します。
    * 店舗情報 (店舗名、支店名、電話番号)
    * レシート情報 (レシートの種類、発行日、発行時刻、レジ番号、担当者名、レシート番号、発行者名、登録番号、店番号、責任者番号)
    * 商品情報 (商品コード、商品名、価格、数量、内税対象額、内税額)
    * 税情報 (軽減税率8%の対象金額と税額、標準税率10%の対象金額と税額)
    * 合計金額
    * 内訳 (8%対象金額、10%対象金額、消費税8%額、消費税10%額)
    * 支払い情報 (支払い方法、支払い金額、預かり金額、お釣り、カード情報(ブランド名、承認番号))
    * ポイント情報 (通常ポイント額、今回獲得ポイント)
    * 備考
    * 取引情報 (取引内容、取引番号)
3. 抽出した情報を以下のJSON形式に変換します。
    ```json
    {
      "store": {
        "name": "string",
        "branch": "string (optional)",
        "tel": "string"
      },
      "receipt": {
        "type": "string",
        "date": "string (YYYY-MM-DD)",
        "time": "string (HH:MM)",
        "register": "string",
        "cashier": "string",
        "number": "string",
        "issuer": "string (optional)",
        "registrationNumber": "string (optional)",
        "storeNumber": "string (optional)",
        "responsiblePerson": "string (optional)"
      },
      "items": [
        {
          "code": "string (optional)",
          "name": "string",
          "price": "number",
          "quantity": "number",
          "taxIncludedAmount": "number (optional)",
          "taxAmount": "number (optional)"
        }
      ],
      "tax": {
        "rate8": {
          "target": "number",
          "amount": "number"
        },
        "rate10": {
          "target": "number",
          "amount": "number"
        }
      },
      "total": "number",
      "breakdown": {
        "tax8": "number",
        "tax10": "number",
        "consumptionTax8": "number",
        "consumptionTax10": "number"
      },
      "payment": {
        "method": "string",
        "amount": "number",
        "tendered": "number",
        "change": "number",
        "card": {
          "brand": "string",
          "approval": "string"
        }
      },
      "points": {
        "normal": {
          "amount": "number",
          "earned": "number"
        },
        "earned": "number"
      },
      "remarks": [
        "string"
      ],
      "transaction": {
        "type": "string",
        "number": "string"
      }
    }
    ```
4. JSON形式で出力する際、以下の点に注意してください。
    * 存在しない情報は "null" または空文字 "" で示してください。
    * 数値は number 型で出力してください。
    * 日付は YYYY-MM-DD 形式、時刻は HH:MMTM 形式で出力してください。
    * 備考は文字列の配列で出力してください。
    * 必要に応じて適切なデータ型を選択してください。
    * 画像から読み取れない情報は推測しないでください。
5. 必ずJSON形式で出力し、**JSONのデータのみを出力してください。** JSONの前後に説明や文章は出力しないでください。
    * 最終応答は、"{"で始まり"}"で終わる。または"["で始まり"]"で終わるJSONのみを出力し、JSON以外の文字は一切応答に含めないでください。

レシート画像を渡します。JSON形式で出力してください。
    """.trimIndent()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val model = Firebase.vertexAI.generativeModel("gemini-2.0-flash")
        // selectedImageUri から bitmap を取得する
        val bitmap: Bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(imageUrl))
        val prompt = content {
            image(bitmap)
            text(prompt_text)
        }
        val response = model.generateContent(prompt)
        print(response.text)
        val parser = ResponseParser(response.text)
        print(parser.json())
        text.value = parser.json()
    }

    Text(text = text.value ?: "Loading...")
}

