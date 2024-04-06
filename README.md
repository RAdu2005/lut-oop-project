# OOP Android API example

## Summary

In this application we connect from an Android app to Rest APIs. Android app requires some special
settings in AndroidManifest.xml to be able to connect to internet and APIs. They are explained if 
you scroll down in this documentation.

MunicipalityDataRetriever fetches data from Statistics Finland.

WeatherDataRetriever fetched data from OpenWeather. For OpenWeather you need an API key.
- Register to https://openweathermap.org/ (it's free)
- Go to https://home.openweathermap.org/api_keys
- Copy the key value.
- In WeatherDataRetriever class, replace YOUR_API_KEY_GOES_HERE with the API key
you copied from OpenWeather site

More information about the settings and endpoints in this document.

## Information about building Android apps that can use Rest APIs

### AndroidManifest.xml changes

To be able to access network you need following lines in your AndroidManifest.xml

```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

 
In the case of OpenWeather API, you also need to add

        android:usesCleartextTraffic="true"

to the AndroidManifest.xml


### app/build.gradle changes

To be able to use Jackson library for parsing JSON, you need following dependency in your 
app/build.gradle file:

    implementation libs.jackson.core
    implementation libs.jackson.databind


## Statictics Finland API access

See the README.md file in https://version.lab.fi/Katja.Karhu/oop-api-and-json-example for detailed explanation

## OpenWeather API access
URL: https://openweathermap.org/

For accessing the OpenWeather APIs, you must register (it's free). Then you need to add your API
key to the project to be able to fetch weather data.

After registration/login, go to https://home.openweathermap.org/api_keys. Copy the key value.
You can also create a new key for your application if you want.

In WeatherDataRetriever class, replace YOUR_API_KEY_GOES_HERE with your API key
you copied from OpenWeather site.

### How OpenWeather API works

OpenWeather's current weather API endpoint is free to use. See documentation: https://openweathermap.org/current#one

The current weather information is fetched via latitude and longitude. This means that 
you need to convert the municipality name to latitude and longitude. OpenWeather offers
and API endpoint for this, it's called GeoCoding API, documentation here: 
https://openweathermap.org/api/geocoding-api

