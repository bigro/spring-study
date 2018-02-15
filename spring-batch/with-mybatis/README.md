# with-mybatis
[mybatis-spring](http://www.mybatis.org/spring/ja/batch.html)で[getting-started](../getting-started)を書き直し。

## MyBatisBatchItemWriter
バッチ処理の前処理から渡ってきたList型のインスタンスを処理します。


`setStatementId` でMapperで定義したメソッド名を指定するとそのSQLが発行されます。

この例では`PersonMapper` の `insert` を動かしたいので、 `insert` を指定すれば良いのですが、 `DummyMapper` にも同じように `insert` メソッドがあります。

この場合は、完全修飾名で `hello.PersonMapper.insert` のように指定すれば良いです。

ドキュメントでは
>異なるネームスペースに属するクエリを指定する場合はネームスペースの指定を忘れないようにしてください。 

このように記載されている箇所です。

この例では、前処理の`processor` が `Person` を返してるので、 `@Param` で `Person` のフィールド名を指定することによって、
`Person` のフィールドに格納された値にアクセスすることができます。
```
@Insert("INSERT INTO people (first_name, last_name) VALUES (#{firstName}, #{lastName})")
    void insert(@Param("lastName") String lastName, @Param("firstName") String firstName);
```
