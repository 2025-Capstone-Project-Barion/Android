plugins {
    id("barrion.android.library")
    id("barrion.android.library.compose")
}

android {
    namespace = "com.example.ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
}