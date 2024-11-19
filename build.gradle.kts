import com.palantir.gradle.docker.DockerExtension

plugins {
    java
    id("org.springframework.boot") version "3.1.5" apply false
    id("io.spring.dependency-management") version "1.1.3" apply false
    id("com.palantir.docker") version "0.35.0" apply false
}

group = "com.d2"
java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

val coreProjectName = "core"
subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")

    repositories {
        mavenCentral()
    }

    dependencies {
        if (project.name != coreProjectName) {
            implementation(project(":core"))
        }
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter")

        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("io.micrometer:micrometer-registry-prometheus")

        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        annotationProcessor("jakarta.annotation:jakarta.annotation-api")
        annotationProcessor("jakarta.persistence:jakarta.persistence-api")

        testRuntimeOnly("com.h2database:h2")
        testImplementation(platform("org.junit:junit-bom:5.9.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    afterEvaluate {
        if (project.name != coreProjectName) {
            apply(plugin = "com.palantir.docker")
            configure<DockerExtension> {

                val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks
                name = "${rootProject.name}/${project.name}:${project.version}"

                //TODO: PATH CHANGE
                setDockerfile(file("$rootDir/../prototype-infra/spring-app/Dockerfile"))

                files(bootJar.outputs.files)

                buildArgs(mapOf("JAR_FILE" to bootJar.outputs.files.singleFile.name))
            }
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
