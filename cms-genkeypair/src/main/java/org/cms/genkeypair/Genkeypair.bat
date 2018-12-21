
@E:

@mkdir  key

@cd key

@keytool -genkeypair -keysize 1024 -validity 3650 -alias "privateKey" -keystore "privateKeys.store" -storepass "Hy@123" -keypass "Hy@123" -dname "CN=Tyrone, OU=Tyrone, O=Tyrone, L=xian, ST=shanxi, C=CN"

@keytool -exportcert -alias "privateKey" -keystore "privateKeys.store" -storepass "Hy@123" -file "certfile.cer"

echo y | keytool -import -alias "publicCert" -file "certfile.cer" -keystore "publicCerts.store" -storepass "Hy@123"  


@pause