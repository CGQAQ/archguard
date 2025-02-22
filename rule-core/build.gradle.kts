plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
}

dependencies {
    api(project(":meta"))

    testImplementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")
    testImplementation("org.reflections:reflections:0.10.2")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
}
