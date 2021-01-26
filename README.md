# Multi Release JAR
複数のJavaのバージョンに1つのJARファイルで対応できるMulti Release JARという仕様がJEP-238としてJava 9に含まれました。

参照：http://openjdk.java.net/jeps/238

この仕様はMavenも対応していますが、以下のような問題が発生します。

- 標準のバージョン（通常は8）以外のJavaのバージョンに合わせたテストができない
- 異なるバージョンで同じクラスを作成するとIDEが「重複したクラスがある」とエラーメッセージを表示する

参照：https://maven.apache.org/plugins/maven-compiler-plugin/multirelease.html

これでは開発が行なえません。これらを回避するために以下のような構成にしました。

- 各バージョンごとにMavenのモジュールを作成
- Multi Release JARをビルドする用のモジュールを作成

このサンプルではJava 8を標準のバージョンとして、Java 11の実装を追加のバージョンとして含めています。
Java 11のモジュールはJava 8のモジュールに依存関係を持ちます。
これは多くの場合ではJava 8のモジュールの方がJava 11のスーパーセットになるため良くある構成でしょう。

これによって、親のMavenプロジェクトをビルドすると、以下のように処理されます。

- 各バージョンごとのMavenモジュールがコンパイル、テスト、ビルド
- 最後に上記の成果物からMulti Release JARを作成

IDEではMavenプロジェクトを開いてから、各モジュールを開くようにすればエラーメッセージ無く開けるようになります。

Javaのバージョンを変えて同じコードを実行すると以下の通り結果が変ります。
```
>java -version
openjdk version "1.8.0_262"
OpenJDK Runtime Environment (AdoptOpenJDK)(build 1.8.0_262-202007041711-b09)
OpenJDK 64-Bit Server VM (AdoptOpenJDK)(build 25.262-b09, mixed mode)

>java -cp build-multi-release-jar\target\multi-released-lib-1.0-SNAPSHOT.jar org.example.App
Hello Java 8

//Javaのバージョンを8から11へ変更

>java -version
openjdk version "11.0.2" 2019-01-15
OpenJDK Runtime Environment 18.9 (build 11.0.2+9)
OpenJDK 64-Bit Server VM 18.9 (build 11.0.2+9, mixed mode)

>java -cp build-multi-release-jar\target\multi-released-lib-1.0-SNAPSHOT.jar org.example.App
Hello Java 11

```