plugins {
    java
    id("org.springframework.boot") version "3.5.16"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "8.8.0"
}

val repo = property("repo")
val pulpogatoVersion = property("pulpogatoVersion")
val ghVersion = property("ghVersion")
val netflixDgsVersion = property("netflixDgsVersion")

group = when (repo) {
    "jitpack" -> "com.github.pulpogato.pulpogato"
    else -> "io.github.pulpogato"
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
    implementation("$group:pulpogato-rest-$ghVersion:$pulpogatoVersion")
    implementation("$group:pulpogato-graphql-$ghVersion:$pulpogatoVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:$netflixDgsVersion")
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
    }
}