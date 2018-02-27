# externalized-configuration
外部プロパティについて色々。

[24. Externalized Configuration](https://docs.spring.io/spring-boot/docs/1.5.10.RELEASE/reference/html/boot-features-external-config.html)

## コマンドラインからプロパティを渡す（Gradle）
build.gradleに以下の記述をすることで、 `-Pargs="--hoge.fuga=HogeFuga"` のようにプロパティを渡すことができます。（[コマンド全文](./boot-run-with-properties.sh)）
```
bootRun {
	if (project.hasProperty('args')) { 
		args project.args.split('\\s+') 
	}
}
```
（[build.gradle全文](./build.gradle)）
