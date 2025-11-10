plugins {
	java
	id("org.springframework.boot") version "3.5.7"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "FeedApp"
version = "0.0.1-SNAPSHOT"
description = "Software technology project FeedApp"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	runtimeOnly("com.h2database:h2")
	implementation("com.h2database:h2:2.3.232")
	implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")
	implementation("org.hibernate.orm:hibernate-core:7.1.1.Final")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.5.5")
    implementation("org.springframework.boot:spring-boot-starter-security")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
