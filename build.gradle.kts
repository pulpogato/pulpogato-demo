plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "io.github.pulpogato"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
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
