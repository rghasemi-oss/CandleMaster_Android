buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:9.1.1")
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:2.0.1")
        classpath("com.google.gms:google-services:4.5.0")
    }
}

plugins {
    alias(libs.plugins.kotlin.compose) apply false
}