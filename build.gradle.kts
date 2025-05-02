plugins {
    kotlin("multiplatform") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"

    id("maven-publish")
    id("signing")
    id("org.jetbrains.dokka") version "2.0.0"
    id("com.diffplug.spotless").version("7.0.3")
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

group = "io.github.rkbalgi"
version = "1.0.2"

repositories {
    mavenCentral()
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("dokka"))
}


kotlin {
    jvm {

        //withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js {
        browser {

            commonWebpackConfig {
            }

            testTask {
                useMocha {
                    timeout = "5000"

                }

            }
        }
    }

    spotless {
        kotlin {
            target("src/commonMain/kotlin/**/*.kt", "src/jvmMain/kotlin/**/*.kt", "src/jsMain/kotlin/**/*.kt")
            ktfmt()

        }
    }


    sourceSets {
        val commonMain by getting {
            dependencies {


                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
                implementation("io.ktor:ktor-io:2.0.2")
                implementation("io.github.aakira:napier:${Versions.napierVersion}")
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
                implementation("ch.qos.logback:logback-classic:${Versions.logbackVersion}")
                implementation("ch.qos.logback:logback-core:${Versions.logbackVersion}")
                implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${Versions.jacksonVersion}")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jacksonVersion}")

                implementation("org.bouncycastle:bcprov-jdk18on:1.71")
            }
        }
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.2")
            }
        }

    }
}

nexusPublishing {
    repositories {
        sonatype {
            //only for users registered in Sonatype after 24 Feb 2021
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))


            username.set(findProperty("ossrhUsername") as String)
            password.set(findProperty("ossrhPassword") as String)

        }
    }
}

publishing {

    val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)

    val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
        dependsOn(dokkaHtml)
        archiveClassifier.set("javadoc")
        from(dokkaHtml.outputDirectory)
    }

    publications {

        withType<MavenPublication> {
            artifact(javadocJar)
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
                packaging = "jar"
                group = "io.github.rkbalgi"
            }

        }

    }

    signing {
        useInMemoryPgpKeys(
            findProperty("GPG_SIGNING_KEY") as String,
            properties["signing.password"] as String
        )
        sign(publishing.publications)
    }

}