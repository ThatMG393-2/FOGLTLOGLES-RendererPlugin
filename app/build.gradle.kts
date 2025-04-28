plugins {
    alias(libs.plugins.android.gradle.plugin)
}

android {
    namespace = "com.thatmg393.fogle.rendererplugin"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.thatmg393.fogle.rendererplugin"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.0.1"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }

        configureEach {
            manifestPlaceholders["des"] = "Fast OpenGL Translation Layer (to) OpenGL ES"
            manifestPlaceholders["renderer"] = "FOGLTLOGLES:libFOGLTLOGLES.so:libFOGLTLOGLES.so"
            manifestPlaceholders["pojavEnv"] = mutableMapOf<String, String>().apply { }.run {
                var env = ""
                forEach { (key, value) ->
                    env += "$key=$value:"
                }
                env.dropLast(1)
            }
            manifestPlaceholders["boatEnv"] = mutableMapOf<String, String>().apply { }.run {
                var env = ""
                forEach { (key, value) ->
                    env += "$key=$value:"
                }
                env.dropLast(1)
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        jniLibs {
            useLegacyPackaging = true
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)

    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }
}
