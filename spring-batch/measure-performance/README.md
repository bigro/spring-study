# measure-performance
バッチのパフォーマンスを測定します。（テストデータ100万件）

[with-mybatis](../with-mybatis)を元にしています。

## 測定結果
chunkを1000で指定。

|Paging(1000)|Cursor|
|---|---|
|110896ms|51315ms|
