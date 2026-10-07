plugins {
    `java-library`
    `maven-publish`
    id("com.vaadin") version "25.2.6"
    id("com.diffplug.spotless") version "8.10.2"
}
group = "dev.normlanguage"
version = "1"
repositories { mavenCentral() }
java { toolchain { languageVersion = JavaLanguageVersion.of(25) } }
dependencies {
    api(platform("com.vaadin:vaadin-bom:25.2.6"))
    api("com.vaadin:vaadin-spring-boot-starter")
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
spotless { java { googleJavaFormat("1.36.0") } }
vaadin { productionMode = true }
tasks.jar { dependsOn("vaadinBuildFrontend") }
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging { events("passed", "failed") }
}
tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
publishing {
    publications { create<MavenPublication>("library") { from(components["java"]) } }
    repositories { maven { url = uri(layout.buildDirectory.dir("repository")) } }
}
