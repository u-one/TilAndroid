# GitHub Actions セットアップガイド

このドキュメントでは、GitHub ActionsでAndroidアプリのビルドを設定する手順を説明します。

## 必要なSecrets設定

GitHubリポジトリの Settings > Secrets and variables > Actions で以下のSecretsを設定してください。

### 1. GOOGLE_SERVICES_JSON（必須）
Firebase用の設定ファイルです。

**設定手順:**
1. Firebase Console から `google-services.json` をダウンロード
2. ファイル内容をBase64エンコード: `base64 -w 0 google-services.json`
3. エンコードされた文字列をGitHub SecretsのGOOGLE_SERVICES_JSONに設定

### 2. リリースビルド用Secrets（オプション）
リリースAPKの署名に必要です。

#### KEYSTORE_FILE
- Android署名用のkeystoreファイル
- Base64エンコードして設定: `base64 -w 0 keystore.jks`

#### KEYSTORE_PASSWORD
- keystoreファイルのパスワード

#### KEY_ALIAS
- keystoreのキーエイリアス

#### KEY_PASSWORD
- キーのパスワード

## ワークフロー概要

### buildジョブ
- **トリガー**: main/developブランチへのpush、mainブランチへのPR
- **実行内容**:
  - コードフォーマットチェック (Spotless)
  - 単体テスト実行
  - デバッグAPKビルド
  - アーティファクトのアップロード

### releaseジョブ
- **トリガー**: mainブランチへのpushのみ
- **実行内容**:
  - リリースAPKビルド（署名あり）
  - アーティファクトのアップロード

## キーストアの作成方法

リリースビルド用のkeystoreを作成する場合：

```bash
keytool -genkey -v -keystore keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias key0
```

## トラブルシューティング

### よくある問題

1. **google-services.jsonが見つからない**
   - GOOGLE_SERVICES_JSONが正しくBase64エンコードされているか確認
   - デコード処理が正常に動作しているか確認

2. **署名エラー**
   - keystore関連のSecretsが正しく設定されているか確認
   - keystoreファイルのパスが正しいか確認

3. **Gradle権限エラー**
   - `chmod +x gradlew` が実行されているか確認

### デバッグ方法

1. ワークフローログを確認
2. 必要に応じてSecretsの値を再設定
3. テストビルドでデバッグAPKが正常に生成されるか確認

## セキュリティ注意事項

- keystoreファイルやパスワードは絶対にコードにコミットしない
- Secretsは必要最小限の権限で設定
- 定期的にkeystoreのパスワードを変更することを推奨