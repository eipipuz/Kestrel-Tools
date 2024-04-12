import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.9.23"

plugins {
    java
    kotlin("jvm") version ("1.9.23")
    `maven-publish`
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    testImplementation("junit:junit:4.13.1")
    testImplementation(kotlin("test-junit"))
}

group = "com.eipipuz"
version = "0.1"
description = "com.eipipuz kestrel-tools"
java.sourceCompatibility = JavaVersion.VERSION_1_8

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.test {
    useJUnit()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-Xinline-classes")
}