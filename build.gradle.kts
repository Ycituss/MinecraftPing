plugins {
    val kotlinVersion = "1.5.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.7.0"
}

group = "com.ycitus"
version = "1.1"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}
dependencies {
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("com.alibaba:fastjson:1.2.78")
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("net.sourceforge.htmlunit:htmlunit:2.53.0")
    implementation("org.seleniumhq.selenium:selenium-server-standalone:2.53.0")

}
