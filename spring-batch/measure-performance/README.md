# measure-performance
バッチのパフォーマンスを測定します。（テストデータ100万件）

[with-mybatis](../with-mybatis)を元にしています。

## MyBatisPagingItemReaderとMyBatisCursorItemReaderを比較
chunkを1000で指定。

### 測定結果
|Paging(1000)|Cursor|
|---|---|
|110896ms|51315ms|
