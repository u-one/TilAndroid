# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## プロジェクト概要

このプロジェクトは開発者自身が自分の勉強用に実装したり試行錯誤したりするためのAndroidアプリです。主にJetpack Composeで実装しており、ReceiptScreenなど一部実用的な機能も含まれています。

## よく使用されるコマンド

### ビルドとテスト
```bash
# アプリケーションをビルド
./gradlew assembleDebug

# リリースビルド
./gradlew assembleRelease

# 単体テストを実行
./gradlew test

# 計装テストを実行
./gradlew connectedAndroidTest

# コードフォーマット（Spotless）
./gradlew spotlessApply

# コードフォーマットチェック
./gradlew spotlessCheck

# 全体のクリーンビルド
./gradlew clean assembleDebug
```

### 開発時のコマンド
```bash
# 特定のモジュールをビルド
./gradlew :app:assembleDebug
./gradlew :receipt:assembleDebug
./gradlew :gis:assembleDebug

# 依存関係の確認
./gradlew dependencies
```

## プロジェクト構造

### マルチモジュール構成
- **app**: メインアプリケーションモジュール
- **receipt**: レシート機能モジュール（実用的な機能）
- **gis**: 地理情報システム関連機能モジュール

### 主要技術スタック
- **UI**: Jetpack Compose (Material 3)
- **Navigation**: Navigation Compose
- **DI**: Dagger Hilt
- **Database**: Room (receipt モジュール)
- **非同期処理**: Coroutines
- **ネットワーク**: Retrofit + OkHttp
- **画像処理**: Coil
- **権限管理**: CameraX, ML Kit (バーコードスキャン)

### アーキテクチャパターン
- **MVVM**: ViewModel + Compose
- **Repository Pattern**: データ層の抽象化
- **Unidirectional Data Flow**: Compose の状態管理

## 主要機能

### MainActivity (app:135)
- `TilApp`: メインのComposable関数
- `BottomBar` と `ModalNavigationDrawer` によるナビゲーション
- 12種類のスクリーン（Camera、Receipt、Map、Audio など）

### ReceiptScreen (receipt モジュール)
- レシート画像解析機能
- OSM（OpenStreetMap）データとの連携
- Tab UI（List、ReceiptInfo、MappingInfoList、MappingInfo）
- AI/ML機能との統合（OpenAI、Gemini API）

### 権限管理
MainActivity で以下の権限を要求:
- `CAMERA`: カメラ機能
- `RECORD_AUDIO`: 音声録音
- `WRITE_EXTERNAL_STORAGE`: Android P以下でのストレージアクセス

## 重要な設定

### Firebase設定
- `app/google-services.json` は repository に含まれていません
- Firebase Auth と Analytics を使用

### ビルド設定
- **compileSdk**: 35
- **minSdk**: 24  
- **targetSdk**: 34
- **Kotlin**: JVM target 11
- **Compose**: BOM管理で一元化

### Code Style
- Spotless + Ktlint (1.0.1) でコードフォーマット
- Kotlin code style: official

## 開発時の注意点

### 必要な設定ファイル
1. `app/google-services.json` を手動で追加する必要があります（Firebase用）
2. API キーなどの機密情報は適切に管理してください

### テスト
- JUnit 5 (Jupiter) を使用
- Google Truth ライブラリでアサーション
- Compose UI テスト対応

### モジュール間依存関係
- app モジュールが gis と receipt モジュールに依存
- 各モジュールは独立してビルド可能

## GitHub Actions CI/CD

### ワークフロー設定
- `.github/workflows/android-build.yml`: Android CI/CDパイプライン
- デバッグビルド: main/developブランチへのpush、mainブランチへのPR
- リリースビルド: mainブランチへのpushのみ

### 必要なSecrets
- `GOOGLE_SERVICES_JSON`: Firebase設定ファイル（Base64エンコード）
- `KEYSTORE_FILE`: Android署名用keystore（Base64エンコード）  
- `KEYSTORE_PASSWORD`: keystoreパスワード
- `KEY_ALIAS`: keystoreキーエイリアス
- `KEY_PASSWORD`: キーパスワード

詳細は `GITHUB_ACTIONS_SETUP.md` を参照してください。