plugins {
    id("java")
}

group = "ru.otus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}