plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.vaadin)
}
group = "dev.normlanguage"
version = rootProject.version
repositories { mavenCentral() }
java { toolchain { languageVersion = JavaLanguageVersion.of(25) } }
dependencies {
    api(project(":"))
    api(platform(libs.vaadin.bom))
    api("com.vaadin:vaadin-spring-boot-starter")
}
vaadin {
    productionMode = true
    forceProductionBuild = providers.gradleProperty("rebuildFrontend").map { it.toBoolean() }.getOrElse(false)
}
tasks.jar { dependsOn("vaadinBuildFrontend") }
tasks.processResources { from(rootProject.file("LICENSE")) { into("META-INF") } }
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
publishing {
    publications { create<MavenPublication>("library") {
        artifactId = "ui-web-demo"
        from(components["java"])
    } }
    repositories { maven { url = uri(rootProject.layout.buildDirectory.dir("repository")) } }
}
