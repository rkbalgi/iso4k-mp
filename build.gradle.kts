plugins {
    kotlin("multiplatform") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    id("maven-publish")
    id("signing")

    id("org.jetbrains.dokka") version "1.6.21"
}

group = "io.github.rkbalgi"
version = "0.0.2"

repositories {
    mavenCentral()
}

val jacksonVersion by properties
val logbackVersion by properties
val guavaVersion by properties
val projectVersion by properties

val napierVersion = "2.6.1"

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("dokka"))
}


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

publishing{
    repositories{
        maven{

            name="oss"
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

            credentials {
                username = properties["mavenCentralUsername"] as String
                password = properties["mavenCentralPassword"] as String
            }
        }

        }

    publications{

        withType<MavenPublication> {
            //artifact(dokkaHtmlJar)
            pom {
                name.set("iso4k")
                description.set("Kotlin Multiplatform library for ISO8583")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                url.set("http://github.com/rkbalgi/iso4k-mp")
                issueManagement {
                    system.set("Github")
                    url.set("https://github.com/rkbalgi/iso4k-mp/issues")
                }
                scm {
                    connection.set("https://github.com/rkbalgi/iso4k-mp.git")
                    url.set("https://github.com/rkbalgi/iso4k-mp")
                }
                developers {
                    developer {
                        name.set("Raghavendra Balgi")
                        email.set("rkbalgi@gmail.com")
                    }
                }
                packaging="jar"
                group="io.github.rkbalgi"
            }

        }

    }

    signing {
        println(properties["signing.keyId"] as String)
        useInMemoryPgpKeys(
            findProperty("GPG_SIGNING_KEY") as String,
            properties["signing.password"] as String
        )
        sign(publishing.publications)
    }

}