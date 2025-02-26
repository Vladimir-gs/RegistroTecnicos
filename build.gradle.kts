// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("androidx.room") version "2.6.1" apply false
    id("com.google.devtools.ksp") version "2.0.20-1.0.25" apply false        //aqui se busca la ultima version https://github.com/google/ksp/releases
    id("com.google.dagger.hilt.android") version "2.51" apply false
    kotlin("jvm") version "2.0.0" apply false // or kotlin("multiplatform") or any other kotlin plugin
    kotlin("plugin.serialization") version "2.0.0" apply false

}
