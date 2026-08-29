plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.spotless)
}

val repo = property("repo")
val ghVersion = property("ghVersion")
val pulpogatoVersion = libs.versions.pulpogato.get()
val netflixDgsVersion = libs.versions.netflixDgs.get()

group =
    when (repo) {
        "jitpack" -> "com.github.pulpogato.pulpogato"
        else -> "io.github.pulpogato"
    }

val versionPrefix =
    when (repo) {
        "jitpack" -> "v"
        else -> ""
    }

version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    when (repo) {
        "jitpack" -> {
            maven {
                url = uri("https://jitpack.io")
                content {
                    includeGroup("com.github.pulpogato.pulpogato")
                }
            }
            mavenCentral {
                content {
                    excludeGroup("io.github.pulpogato")
                }
            }
        }

        "github" -> {
            maven {
                url = uri("https://maven.pkg.github.com/pulpogato/pulpogato")
                content {
                    includeGroup("io.github.pulpogato")
                }
                credentials {
                    username = property("gpr.user") as String
                    password = property("gpr.key") as String
                }
            }
            mavenCentral {
                content {
                    excludeGroup("io.github.pulpogato")
                }
            }
        }

        else -> {
            mavenCentral()
        }
    }
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(1, "hours")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter")
    implementation("$group:pulpogato-rest-$ghVersion:${versionPrefix}$pulpogatoVersion")
    implementation("$group:pulpogato-graphql-$ghVersion:${versionPrefix}$pulpogatoVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("${libs.netflix.dgs.get()}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

spotless {
    java {
        palantirJavaFormat()
    }
    yaml {
        prettier()
        target("**/*.yml")
        targetExclude("**/build/**", "**/node_modules/**")
    }
}