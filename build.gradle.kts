buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:9.1.1")
    }
}

plugins {
    alias(libs.plugins.kotlin.compose) apply false
}


