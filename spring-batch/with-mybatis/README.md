# with-mybatis
[mybatis-spring](http://www.mybatis.org/spring/ja/batch.html)で[getting-started](../getting-started)を改造。
peopleテーブルから取得した名前を大文字にしてpeople_upper_caseテーブルに保存します。

※ [spring-batch](../spring-batch) のサブプロジェクトです。

## MyBatisBatchItemWriter
バッチ処理の前処理から渡ってきたList型のインスタンスを処理します。

### クエリの指定
`setStatementId` でMapperで定義したメソッド名を指定するとそのSQLが発行されます。

この例では`PersonMapper#insert` を動かしたいので、 `insert` を指定すれば良いのですが、 `DummyMapper` にも同じように `insert` メソッドがあります。

この場合は、完全修飾名で `hello.PersonMapper.insert` のように指定すれば良いです。

ドキュメントでは以下のように記載されている箇所になります。
>異なるネームスペースに属するクエリを指定する場合はネームスペースの指定を忘れないようにしてください。 

### パラメータの指定
この例では、前処理の`processor` が `Person` を返してるので、`#{firstName}` などのフィールド名で値をマッピングできます。
  
また、以下のコードのように `@Param` で `Person` のフィールド名を指定して引数で受け取る形にしておく方がバッチ以外からの呼び出しにも対応できます。
```
@Insert("INSERT INTO people (first_name, last_name) VALUES (#{firstName}, #{lastName})")
    void insert(@Param("lastName") String lastName, @Param("firstName") String firstName);
```

## MyBatisPagingItemReader
データベースからページ単位でレコードを読み出します。

### クエリの指定
`setQueryId` で指定します。指定の方法は `MyBatisBatchItemWriter` と同様です。

### パラメータの指定
`setParameterValues` に `Map<String, Object>` のインスタンスを渡します。
この例では `Map` のkeyにしている `firstName` がそのまま `#{firstName}` としてMapperで使用可能です。

ただし、`parameterValues` をセットした場合、ページ取得で使われている `_skiprows` や `_pagesize` がClassCastExceptionとなってしまうので、

以下のコードのように、 `@Param` で引数として受け取る必要があります。
```
    @Select("SELECT first_name, last_name FROM people WHERE first_name = #{firstName} ORDER BY person_id ASC LIMIT #{_skiprows}, #{_pagesize}")
    Person select(@Param("firstName") String firstName, @Param("_skiprows") int skipRows, @Param("_pagesize") int pageSize);
```

### ページサイズの指定
`setPageSize` で指定します。以下のようにSQLのLIMITに `#{_skiprows}` と `#{_pagesize}` を指定してなければ動作しません。
```
@Select("SELECT first_name, last_name FROM people ORDER BY person_id ASC LIMIT #{_skiprows}, #{_pagesize}")
```
SQLで上記のLIMITの記載がないとページサイズを超えた件数を取得した時にループしてしまいます。

ちなみに `chunk` と違う件数を指定してみると以下のようになります。

例えば、全体の件数が15件あるとしてページサイズが `5` で chunkが `10` の時は
1. SELECTを2回実行(5件 × 2)
2. INSERTを1回実行(10件 × 1) ここまででSTEPの全処理
3. SELECTを1回実行(5件 × 1)
4. INSERTを1回実行(5件 × 1)

のような手順で全15件が取得されます。

逆で、ページサイズが `10` で chunkが `5` の時は
1. SELECTを1回実行(10件 × 1)
2. INSERTを2回実行(5件 × 2) ここまででSTEPの全処理
3. SELECTを1回実行(5件 × 1)
4. INSERTを1回実行(5件 × 1)

こうなりました。

## MyBatisCursorItemReader
カーソルを使用してデータベースからレコードを取得します。

使い方は基本的に `MyBatisPagingItemReader` と同じです。
