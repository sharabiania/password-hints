# Publishing to Play Store

## Release Branch
- Create a Release branch (GitFlow):
- Update the version code and version name (optional) in app-level build.gradle.

## Update the Ad units Ids
- In `activity_add.xml` set `adUnitId="@string/unit_id_updateActivity"`.
- In `activity_update.xml` set `adUnitId="@string/unit_id_updateActivity"`.

## Generate signed APK
- Use the key located in `src/signing/key.jks`.
- key password: test123
- key alias: key0
- key store password: test123
- **DO NOT** lose this key!
- App ID in the Play Store is `com.passwordhints`
- You can never change this app id. However you can change the package names.
- Choose both `v1` and `v2` checkboxes.

### In case of trouble:
- run `keytool -list -keystore <key.jks>`
    - keytool is somewhere here: `C:\Program Files\Java\jdk.1.8.0._131\bin\keystore.exe`


