# getting-started
[Getting Started](https://spring.io/guides/gs/batch-processing/)

## 作るもの
CSVからデータをインポートしてコードで変換し、結果をデータベースに保存する。

## PersonItemProcessor.java
Personオブジェクトを受け取ってそれを大文字のPersonオブジェクトに変換する。
PersonItemProcessorはSpring BatchのItemProcessorインターフェイスを実装しています。
これによりバッチジョブのコードに簡単につなぐことができます。

## BatchConfiguration.java
上から3つのメソッドは、入力、プロセッサ、出力を定義しています。
4、5つめのメソッドは実際のジョブ構成に焦点を当ててます。

### @EnableBatchProcessing
ジョブをサポートする多くの重大なBeanを追加します。
今回はメモリベースのデータベースを使用していますが、これも@EnableBatchProcessingで提供されています。

### reader()
`ItemReader` を生成します。これは `sample-data.csv` というファイルを探し、各行項目を解析して `Person` に変換します。

### processor()
`PersonItemProcessor` のインスタンスを作成します。

### write(DataSource)
`ItemWriter` を作成します。
これはJDBCのdestinationを対象とし、 `@EnableBatchProcessing` によって作成されたdataSourceのコピーを自動的に取得します。
Java BeanのプロパティによってPersonをInsertするためのSQL文が含まれています。

### importUserJob()
ジョブを定義します。
ジョブはステップから構築され、各ステップにはリーダー、プロセッサー、ライターが必要です。

このジョブ定義では、ジョブがデータベースを使用して実行状態を維持するため、incrementerが必要です。
次に、各ステップをリストします。このジョブには1つのステップしかありません。
ジョブは終了し、Java APIは完全に構成されたジョブを生成します。

### step1()
1つのステップを定義します。

ステップ定義では、一度に書き込むデータ量を定義します。
この場合、一度に最大10個のレコードを書き込みます。(`chunk(10)` の記述)
次に、メソッドを使用してリーダ、プロセッサ、ライタを構成します。

## JobCompletionNotificationListener.java
このクラスは、ジョブがBatchStatus.COMPLETEDであることをリッスンし、JdbcTemplateを使用して結果を検査します。