## アプリケーション名
「じぶんメモ」

↓ メイン画面  
<img src="https://user-images.githubusercontent.com/83963218/131286668-7e757eac-7b7d-4605-b27d-b0f791bd8f55.png"  title="メイン画面" width="310" height="400">

## アプリケーション概要
体のサイズや、免許証の更新期限、住所、誕生日など身の回りのことをメモできるアプリです。

## コンセプト
このアプリのコンセプトは「２、３週間に１回使うメモ」です。  
一般的なメモアプリにメモするほど頻繁に必要になるものではないけど、つい忘れがちな身の回りの情報を手軽にメモできて、簡単に取り出すことができます。

## URL
https://play.google.com/store/apps/details?id=com.websarva.wings.android.zibunmemo  
（playストアで公開しています。）

## 使用技術
・kotlin 1.4.32  
・SQLite 3.32.2  
・Android Studio 4.1.3  

## 機能一覧
・テキストの入力  
・メモの削除  
・メモの保存  
・メモの新規追加  
・日付入力の際にカレンダーによる日付選択補助機能  
・メモしたサブスクリプションサービスの毎月支払い合計額の算出  
・メモしたパスワードをワンタッチでクリップボードにコピー  
・郵便番号検索APIを使用して、郵便番号から住所を取得（Addressmemo.ktに記載）  
・Http接続は非同期で処理（Addressmemo.ktに記載）

## 利用方法
メモする事柄に適した９つの入力欄を用意しています。  
誕生日や記念日を覚えておきたい場合はDATEボタンをタップすると、日付を入力するのに適したメモが表示されます。  
住所をメモしたい場合は、Addressボタンをタップして入力します。  

↓ DATEメモの画面  
<img src="https://user-images.githubusercontent.com/83963218/131286921-4f3a70fd-f86f-4ccd-9701-173f45a3e003.png"  title="DATEメモ画面" width="310" height="400">


## 目指した課題解決
一般的なメモアプリの場合、メモをどんどん追加していくと目的の情報が取り出しにくくなったり、そもそもメモしていること自体を忘れてしまうというデメリットがあります。  
メモしている内容が友達の誕生日やパスポートの更新日、知り合いの住所などの場合、頻繁に必要になるものではないため、いざ必要になったときに「あれっ、どこにメモしたんだっけ？」ということが起こります。  
「じぶんメモ」は身の回りのたまーに必要になる情報を、手軽にメモできて、すぐに取り出せることに特化しているため、忘れてまた調べなおすという手間をなくすことができます。  



















