# RoboGen-Android
Enthält den vollständigen Android-App-SourceCode für die RoboGen Tablet-App.
Wurde lediglich auf dem Amazon-Fire10-Tablet (10,1) im Landscape-Mode getestet, es wird daher empfohlen ein Tablet der gleichen Größe zum Testen zu verwenden

Applikation verbindet den Roboter Q.Bo One mit Anki Vector, der Fitbit-Uhr und Alexa-Eintscheidungsbäumen und dient daher als "Datendrehscheibe".

# Build Dependencies and Version Info

```
apply plugin: 'com.android.application'

android {
    compileSdkVersion 29
    buildToolsVersion "29.0.0"
    defaultConfig {
        applicationId "at.srfg.robogen"
        minSdkVersion 22
        targetSdkVersion 29
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'androidx.appcompat:appcompat:1.0.2'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation 'androidx.recyclerview:recyclerview:1.0.0'
    implementation 'com.google.android.material:material:1.0.0'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
    implementation 'com.google.code.gson:gson:2.6.2'
    implementation 'com.android.support:support-annotations:23.1.1'
    implementation 'com.amazonaws:aws-android-sdk-cognito:2.14.+'
    implementation 'com.amazonaws:aws-android-sdk-s3:2.14.+'
    implementation 'com.amazonaws:aws-android-sdk-ddb:2.14.+'
    implementation 'com.amazonaws:aws-android-sdk-mobileanalytics:2.14.+'
    implementation 'com.amazonaws:aws-android-sdk-core:2.14.+'
    implementation 'com.amazonaws:aws-android-sdk-lambda:2.14.+'
}
```
