plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // Source: https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-java
    implementation("org.seleniumhq.selenium:selenium-java:4.49.0")
    testImplementation("org.assertj:assertj-core:3.26.3")

    // Source: https://mvnrepository.com/artifact/com.codeborne/selenide
    implementation("com.codeborne:selenide:7.18.1")

    testImplementation("io.rest-assured:rest-assured:5.4.0")
}

tasks.test {
    useJUnitPlatform()
}