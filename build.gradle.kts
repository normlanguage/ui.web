plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.vaadin) apply false
    id("com.diffplug.spotless") version "8.10.2"
}
group = "dev.normlanguage"
version = "2"
repositories { mavenCentral() }
java { toolchain { languageVersion = JavaLanguageVersion.of(25) } }
dependencies {
    api(platform(libs.vaadin.bom))
    api("com.vaadin:vaadin-core")
    compileOnlyApi("jakarta.servlet:jakarta.servlet-api:6.1.0")
    testImplementation("jakarta.servlet:jakarta.servlet-api:6.1.0")
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
spotless { java { googleJavaFormat("1.36.0") } }
tasks.processResources {
    from("src/main/frontend") { into("META-INF/frontend") }
    from("LICENSE") { into("META-INF") }
}
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
tasks.withType<Test>().configureEach {
    dependsOn(tasks.jar)
    systemProperty("libraryJar", tasks.jar.flatMap { it.archiveFile }.get().asFile.absolutePath)
    useJUnitPlatform()
    testLogging { events("passed", "failed") }
}
tasks.test { exclude("**/ConsumerPackagingTest.class") }
tasks.register<Test>("consumerTest") {
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    dependsOn(":demo:jar", ":demo:generatePomFileForLibraryPublication")
    include("**/ConsumerPackagingTest.class")
    inputs.file(project(":demo").layout.buildDirectory.file("libs/demo-${project.version}.jar"))
    systemProperty("consumerJar", project(":demo").layout.buildDirectory.file("libs/demo-${project.version}.jar").get().asFile.absolutePath)
    systemProperty("consumerPom", project(":demo").layout.buildDirectory.file("publications/library/pom-default.xml").get().asFile.absolutePath)
}
tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
publishing {
    publications { create<MavenPublication>("library") {
        from(components["java"])
        pom { licenses { license { name = "Mozilla Public License 2.0"; url = "https://www.mozilla.org/MPL/2.0/" } } }
    } }
    repositories { maven { url = uri(layout.buildDirectory.dir("repository")) } }
}
