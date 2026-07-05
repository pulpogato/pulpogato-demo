plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

val repo = if (project.hasProperty("repo")) project.property("repo") else "central"
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
                    username = project.findProperty("gpr.user")!! as String
                    password = project.findProperty("gpr.key")!! as String
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
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.+")
    implementation("$group:pulpogato-rest-${property("ghesVersion")}:${property("pulpogatoVersion")}")
    implementation("$group:pulpogato-graphql-${property("ghesVersion")}:${property("pulpogatoVersion")}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:${property("netflixDgsVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
