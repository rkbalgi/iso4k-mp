plugins {
    kotlin("multiplatform") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
}

group = "io.github"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val jacksonVersion by properties
val logbackVersion by properties
val guavaVersion by properties
val projectVersion by properties

val napierVersion = "2.6.1"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }

    }
    js(BOTH) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }


    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }


    sourceSets {
        val commonMain by getting {
            dependencies {


                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
                implementation("io.ktor:ktor-io:2.0.2")
                implementation("io.github.aakira:napier:$napierVersion")
                implementation("org.jetbrains.kotlin:atomicfu:1.6.21")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
                implementation("net.mamoe.yamlkt:yamlkt:0.10.2")


                //implementation("com.google.guava:guava:$guavaVersion")

            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))

            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.slf4j:slf4j-api:1.7.36")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
                implementation("ch.qos.logback:logback-core:$logbackVersion")
                implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

                implementation("org.bouncycastle:bcprov-jdk18on:1.71")
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                val localPath = rootDir.absolutePath + "/src/jsMain/js/specs"
                implementation(npm("specs", File("$localPath")))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.2")
            }
        }
        //val nativeMain by getting
        //val nativeTest by getting
    }
}
