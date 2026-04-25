package net.uoneweb.android.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.JsonParser

object DatabaseProvider {
    private var instance: AppDatabase? = null

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // 新しいカラムを追加
            db.execSQL("ALTER TABLE receipt_metadata ADD COLUMN receiptDate TEXT")
            db.execSQL("ALTER TABLE receipt_metadata ADD COLUMN receiptYearMonth TEXT")

            // 既存データのcontentからdateを抽出して設定
            val cursor = db.query("SELECT id, content FROM receipt_metadata")
            while (cursor.moveToNext()) {
                val id = cursor.getLong(0)
                val content = cursor.getString(1)
                try {
                    val jsonObj = JsonParser.parseString(content).asJsonObject
                    val receiptObj = jsonObj.getAsJsonObject("receipt")
                    val date = receiptObj?.get("date")?.asString
                    if (!date.isNullOrEmpty()) {
                        val yearMonth = date.take(7)
                        db.execSQL(
                            "UPDATE receipt_metadata SET receiptDate = ?, receiptYearMonth = ? WHERE id = ?",
                            arrayOf(date, yearMonth, id),
                        )
                    }
                } catch (e: Exception) {
                    Log.e("DatabaseProvider", "Migration failed to parse JSON for id=$id", e)
                }
            }
            cursor.close()

            // インデックスを作成
            db.execSQL("CREATE INDEX IF NOT EXISTS index_receipt_metadata_receiptYearMonth ON receipt_metadata(receiptYearMonth)")
        }
    }

    fun getDatabase(context: Context): AppDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database",
            )
                .addMigrations(MIGRATION_1_2)
                .build()
        }
        return instance!!
    }
}