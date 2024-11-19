val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks
val jar: Jar by tasks

project.version = "1.0.0"

bootJar.enabled = true
jar.enabled = false

dependencies {
}
