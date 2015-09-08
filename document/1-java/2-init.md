Gradleプロジェクトの作成
===

Gradleには[Build Init Plugin](https://docs.gradle.org/current/userguide/build_init_plugin.html)というGradleプロジェクトを生成するプラグインがあります。

`mvn archetype:generate`に比べて圧倒的にテンプレートが少ないというより、機能不足すぎるわけですが、最低限のプロジェクト構成を生成することができます。

なお、現在incubatingな機能なため、今後テンプレートタイプが詳細に指定できるようになるかもしれないし、taichiさんのQiitaの記事[Gradle で initタスクをカスタマイズする方法](http://qiita.com/taichi@github/items/a4caab3a31dd5949a045)にあるようにオレオレテンプレートを作る方法が一般的になるかもしれません。

とりあえず、プロジェクトを作るのにいちいち`mkdir`コマンドを叩くのが面倒なときに使うとよいでしょう。

演習
===

### 1. typeの指定なしでプロジェクトをinitする

`java-projects/init-project/no-type`ディレクトリーに移動して、次のコマンドを入力してください。

```
$ gradle init
```

---

次のようなディレクトリーが作成されます。

```
.
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
└── settings.gradle
```

なお、この`init`タスクは

* コメントアウトされたスクリプトしか書かれていない`build.gradle`
* プロジェクトの名前を定義しただけの`settings.gradle`
* 使っているバージョンのwrapperファイル

という、あまり意味のないものだけが生成されるちょっと悲しい機能です。

---

### 2. java library type

次は少しまともな`init`タスクを実行します。

`java-projects/init-project/java-lib`ディレクトリーに移動してください。

次のコマンドを入力してください。

```
$ gradle init --type java-library
```

---

次のようなディレクトリーが作成されます。

```
.
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    ├── main
    │   └── java
    │       └── Library.java
    └── test
        └── java
            └── LibraryTest.java
```

この`init`で生成されるプロジェクトは次のようなものです。

* `java`プラグインが適用されている
* repositoryに`jcenter`が指定されている
* `slf4j`の`1.7.12`を`compile`スコープで依存している
* `junit`の`4.12`を`testCompile`スコープで依存している

また、最初に紹介したプロジェクトの基本構成が自動的に生成されます。

最初のtype指定なしよりはまともそうなプロジェクトですね。

---

### 3. groovy library type

次はGroovyタイプのテンプレートを作成します。

`javajo-gradle/java-projects/init-project/groovy-lib`ディレクトリーに移動してください。

次のコマンドを入力してください。

```
$ gradle init --type groovy-library
```

---

次のようなディレクトリーが作成されます。

```
.
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    ├── main
    │   └── groovy
    │       └── Library.groovy
    └── test
        └── groovy
            └── LibraryTest.groovy
```

この`init`で生成されるプロジェクトは次のようなものです。

* `groovy`プラグインが適用されている
* レポジトリーに`jcenter`が指定されている
* Groovyに`groovy-all`の`2.4.4`が指定されている
* テスト用のライブラリーに`spock`の`1.0-groovy-2.4`が用いられている
* テスト用のライブラリーに`junit`の`4.12`が用いられている(transitive dependencyで実は指定する必要がないのは内緒だよ)

### 4. Scala library type

Gradleはsbtほど厳密ではない(らしい)けど、Scalaプロジェクトのビルドも可能です。

ということで、Scalaタイプのプロジェクトの`init`タスクもあります。

`javajo-gradle/java-projects/init-project/scala-lib`ディレクトリーに移動してください。

次のコマンドを入力してください。

```
$ gradle init --type scala-library
```

---

次のようなディレクトリーが作成されます。

```
.
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    ├── main
    │   └── scala
    │       └── Library.scala
    └── test
        └── scala
            └── LibrarySuite.scala
```

この`init`タスクで生成されるプロジェクトは次のようなものです。

* `scala`プラグインが適用されている
* `jcenter`がレポジトリーに指定されている
* Scalaのバージョンは`2.11.7`
* テスト用のライブラリーに`scalatest_2.11`のバージョン`2.2.5`が用いられている
* テスト用のライブラリーに`junit`の`4.12`が用いられている
* テストのランタイムライブラリーに`scala-xml_2.11`のバージョン`1.0.5`が用いられている

---

テンプレート作成後
===

混みいったプロジェクトでなければ、次のような作業の後にプロジェクトの作成がスムーズに行きます。

* プロジェクトの情報を`build.gradle`に入力。
* `gradle.properties`にデーモン利用フラグを設定する。
* いらないファイル`Library.(java|groovy|scala)`を削除する
* パッケージ階層を構築
* お好みの依存ライブラリーを設定する
* お好みのプラグインを適用する