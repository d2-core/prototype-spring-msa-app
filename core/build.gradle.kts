val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks
val jar: Jar by tasks

bootJar.enabled = false
jar.enabled = true

dependencies {
}
