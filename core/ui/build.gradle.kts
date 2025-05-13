plugins {
    id("barrion.android.library")
    id("barrion.android.library.compose")
}

android {
    namespace = "com.example.ui"
}

dependencies {
    implementation(project(":core:common"))
}