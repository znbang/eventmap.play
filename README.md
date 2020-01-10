# eventmap

執行前需在環境參數或 .env 檔設定下列參數。
- EVENTMAP_SECRET
- GOOGLE_CLIENT_ID
- GOOGLE_CLIENT_SECRET
- GOOGLE_API_KEY
- FACEBOOK_CLIENT_ID
- FACEBOOK_CLIENT_SECRET

若不設定 EVENTMAP_SECRET，亦可執行
```
play secret
```
在 conf/application.conf 產生新的 application.secret 值。

執行
```
play deps
```
下載所需程式庫。

執行
```
play start --%prod
```
啟動程式。

若要減少啟動時間，執行
```
play precompile
```
預先編譯程式，再執行
```
play start --%prod -Dprecompiled=true
```
啟動程式。
