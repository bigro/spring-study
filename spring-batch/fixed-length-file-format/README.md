# fixed-length-file-format
固定長のファイルを扱います。

※ [spring-batch](../) のサブプロジェクトです。

## ドキュメント
- [reader](https://docs.spring.io/spring-batch/4.0.x/reference/html/readersAndWriters.html#fixedLengthFileFormats)
- [writer](https://docs.spring.io/spring-batch/4.0.x/reference/html/readersAndWriters.html#fixedWidthFileWritingExample)

## 作るもの
固定長ファイルをドメインオブジェクトに読み込んで固定長ファイルとして書き出します。

## Reader
`FixedLengthTokenizer` と `FieldSetMapper` を実装したもの([OrderMapper](./src/main/java/hello/OrderMapper.java))を `DefaultLineMapper` にセットして `FlatFileItemReader` にセットする。

`FieldSet` という概念があるがあるが、これはJDBCの `ResultSet` のファイルを扱うものと思えば良いです。（ファイル操作のクライアント）

## Writer
`BeanWrapperFieldExtractor` を使ってドメインモデル([Order](./src/main/java/hello/Order.java))からフィールドを抽出します。

抽出したフィールドを `FormatterLineAggregator`で固定長のLineに集約していきます。

```
BeanWrapperFieldExtractor<Order> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"isin", "quantity", "price", "customer"});
        fieldExtractor.afterPropertiesSet();

FormatterLineAggregator<Order> lineAggregator = new FormatterLineAggregator<>();
        lineAggregator.setFormat("%12s%3s%5s%9s");
        lineAggregator.setFieldExtractor(fieldExtractor);
```

JavaのFormatの仕様をわかっておらず、 `LineAggregator` のフォーマットをドキュメント通り以下のようにしていたら例外が発生してハマりました。

```
lineAggregator.setFormat("%-9s%-2.0f");
```

フォーマットは自分が出力したい形に合わせてセットするようにしましょう。
