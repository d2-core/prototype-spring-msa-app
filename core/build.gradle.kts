val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks
val jar: Jar by tasks

bootJar.enabled = false
jar.enabled = true

dependencies {
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.h2database:h2")
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.691")
    implementation("org.redisson:redisson-spring-boot-starter:3.16.4")
    implementation("net.bramp.ffmpeg:ffmpeg:0.7.0")
    implementation("commons-io:commons-io:2.11.0")
}
