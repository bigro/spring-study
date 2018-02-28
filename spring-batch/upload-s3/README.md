# upload-s3
AmazonS3にファイルアップロードするバッチです。


※ [spring-batch](../) のサブプロジェクトです。

## 作るもの
CSVをAmazonS3にアップロードする。

[Spring Cloud for Amazon Web Services](https://cloud.spring.io/spring-cloud-aws/) を使用しています。

## 動作環境
・ローカルの環境変数に以下を追加してください。
- AWS_ACCESS_KEY_ID
- AWS_SECRET_ACCESS_KEY
- AWS_REGION

※Macの場合、IDEAなどのIDEでアプリケーションを起動すると環境変数をうまく読み取れなくて落ちることがあります。

・`application.properties` の `cloud.aws.bucket` を自身が使用するバケット名に変更してください。

## UploadItemWriter.java
`ItemWriter` をimplementsしています。

`Person` のジェネリクスとしていますが、この実装ではなくても良いです。

本来のバッチであれば `ItemReader` から渡されたデータを使うため、必要となりますが、今回は渡されたデータ関係なく用意されたCSVをアップロードしているためです。