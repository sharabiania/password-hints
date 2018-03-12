# Publishing to Play Store

## Update the Ad units Ids
- In `activity_add.xml` set `adUnitId="@string/unit_id_updateActivity"`.
- In `activity_update.xml` set `adUnitId="@string/unit_id_updateActivity"`.

## Generate signed APK
- Use the key located in `src/signing/key.jks`.
- key password: test123
- key alias: key0
- **DO NOT** lose this key!
- App ID in the Play Store is `com.passwordhints`
- You can never change this app id. However you can change the package names.

### In case of trouble:
- run `keytool -list -keystore <key.jks>`
    - keytool is somewhere here: `C:\Program Files\Java\jdk.1.8.0._131\bin\keystore.exe`
