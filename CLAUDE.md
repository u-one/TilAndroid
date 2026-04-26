# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## プロジェクト概要

開発者自身の学習・試行錯誤用Androidアプリ。主にJetpack Composeで実装しており、レシート解析など実用的な機能も含む。

## よく使用されるコマンド

```bash
# ビルド
./gradlew assembleDebug
./gradlew assembleRelease

# テスト
./gradlew test
./gradlew connectedAndroidTest

# 特定テストのみ実行
./gradlew :receipt:test --tests "net.uoneweb.android.receipt.ClassName.methodName"

# コードフォーマット
./gradlew spotlessApply
./gradlew spotlessCheck

# 特定モジュールのみ
./gradlew :app:assembleDebug
./gradlew :receipt:assembleDebug
./gradlew :gis:assembleDebug
```

## プロジェクト構造

### モジュール構成
- **app**: ナビゲーションハブ（`TilApplication` @HiltAndroidApp、`MainActivity`）
- **receipt**: レシート解析メインモジュール（Room DB、AI API統合）
- **gis**: 地図・位置情報モジュール（MapLibre GL）

### 主要技術スタック
- **UI**: Jetpack Compose (Material 3)
- **DI**: Dagger Hilt
- **Database**: Room（receiptモジュール）
- **ネットワーク**: Retrofit + OkHttp
- **AI**: Firebase Gemini API（画像OCR）、OpenAI API（OSM情報推論）

## アーキテクチャ

### ナビゲーション
`MainActivity` にシールドクラス `Screen` でルートを定義。デフォルト起動は `Screen.Receipt`。`BottomBar`（DataStore で表示切替）と `ModalNavigationDrawer` を使用。

### receiptモジュールのデータフロー
1. **画像取得**: CameraX撮影 or ギャラリー選択 → EXIFから位置情報抽出
2. **OCR**: `GeminiReceiptCommunication` でレシート画像→JSON（`Receipt` ドメインモデル）
3. **OSM推論**: `OpenAiReceiptCommunication` でReceipt JSON→`ReceiptMappingInfo`（OSMタグ候補）
4. **保存**: Room DB（`ReceiptMetaDataEntity`、`ReceiptMappingInfoEntity`）

### Roomデータベース（v2）
- **receipt_metadata**: `id`, `content`（Receipt JSON）, `location`（Location JSON）, `filename`, `receiptDate`（YYYY-MM-DD）, `receiptYearMonth`（YYYY-MM、インデックス付き）
- **receipt_mapping_info**: `id`, `receiptId`（外部キー参照）, `content`（ReceiptMappingInfo JSON）
- **マイグレーション**: v1→v2で`receiptDate`/`receiptYearMonth`を追加（`DatabaseProvider` に定義）
- JSONシリアライズにGsonを使用（TypeConverter、`fromJson()`/`toJson()`）

### 注意点
- **Repositoryは非Hiltシングルトン**: `ReceiptMetaDataRepository`/`ReceiptMappingInfoRepository` はViewModelインスタンスごとに生成される（`DatabaseProvider` で遅延初期化）
- **月別フィルタリング**: `receiptYearMonth` カラムのインデックスで `ReceiptListViewModel` の月別絞り込みを実現

## 重要な設定

- `app/google-services.json` はリポジトリに含まれない（Firebase Auth・Analytics用）→ 手動追加が必要
- **compileSdk**: 35 / **minSdk**: 24 / **targetSdk**: 34
- **テスト**: JUnit 5 (Jupiter) + Google Truth

## CI/CD

`.github/workflows/android-build.yml` で自動ビルド。必要なSecretsは `GITHUB_ACTIONS_SETUP.md` 参照。