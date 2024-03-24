# OOP Android API example

In this application we connect from an Android app to Rest APIs


## Information about building Android apps that can use Rest APIs

To be able to access network you need following lines in your AndroidManifest.xml

<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 

To be able to use Jackson library for parsin JSON, you need following dependency in your 
app/build.gradle file:

    implementation libs.jackson.core
    implementation libs.jackson.databind