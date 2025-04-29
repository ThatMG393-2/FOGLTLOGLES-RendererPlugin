plugins {
    alias(libs.plugins.android.gradle.plugin)
}

android {
    namespace = "com.fcl.plugin.thatmg393.fogle"
    compileSdk = 34

    ndkVersion = "28.1.13356709"

    defaultConfig {
        applicationId = "com.fcl.plugin.thatmg393.fogle"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.0.1"

        externalNativeBuild {
            cmake {
                arguments += listOf("-DCMAKE_BUILD_TYPE=Release", "-DANDROID_PLATFORM=29")
            }
        }

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }

        configureEach {
            manifestPlaceholders["des"] = "FOGLTLOGLES (OpenGL 4.x, 1.17+) (OpenGL ES 3.2 only)"
            manifestPlaceholders["renderer"] = "FOGLTLOGLES:libFOGLTLOGLES.so:libEGL.so"
            manifestPlaceholders["pojavEnv"] = mutableMapOf<String, String>().apply {
                put("LIBGL_ES", "3")
                put("POJAV_RENDERER", "opengles3")
                // put("DLOPEN", "libSPRIV-Tools-shared.so,libshaderc_shared.so")
            }.run {
                var env = ""
                forEach { (key, value) ->
                    env += "$key=$value:"
                }
                env.dropLast(1)
            }
            manifestPlaceholders["boatEnv"] = mutableMapOf<String, String>().apply {
                put("LIBGL_ES", "3")
                // put("DLOPEN", "libSPRIV-Tools-shared.so,libshaderc_shared.so")
            }.run {
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

    externalNativeBuild {
        cmake {
            path = file("../FOGLTLOGLES/CMakeLists.txt")
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
