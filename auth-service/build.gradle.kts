val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks
val jar: Jar by tasks

project.version = "1.0.0"

bootJar.enabled = true
jar.enabled = false

dependencies {
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")

    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

}
val querydslDir = "src/main/generated"

sourceSets {
    getByName("main").java.srcDirs(querydslDir)
}

tasks.withType<JavaCompile> {
    options.generatedSourceOutputDirectory = file(querydslDir)
}

tasks.named("clean") {
    doLast {
        file(querydslDir).deleteRecursively()
    }
}

