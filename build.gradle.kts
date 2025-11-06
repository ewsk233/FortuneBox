import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8
import io.izzel.taboolib.gradle.Basic
import io.izzel.taboolib.gradle.BukkitHook
import io.izzel.taboolib.gradle.I18n
import io.izzel.taboolib.gradle.Metrics
import io.izzel.taboolib.gradle.CommandHelper
import io.izzel.taboolib.gradle.Bukkit


plugins {
    java
    id("io.izzel.taboolib") version "2.0.27"
    id("org.jetbrains.kotlin.jvm") version "2.2.0"
}

taboolib {
    env {
        install(Basic)
        install(BukkitHook)
        install(I18n)
        install(Metrics)
        install(CommandHelper)
        install(Bukkit)
        install(BukkitUtil)
        install(BukkitUI)
        install(BukkitNMSItemTag)
    }
    description {
        name = "FortuneBox"
        contributors {
            name("ewsk")
        }
        dependencies{
            name("AzureFlow").optional(true)
            name("PlaceholderAPI").optional(true)
        }
    }
    relocate("ink.ptms.um", "org.spectrumflow.fortunebox.um")
    relocate("top.maplex.arim","org.spectrumflow.fortunebox.arim")
    version { taboolib = "6.2.3-d4a5f0ea" }
}

repositories {
    maven { url = uri("https://repo.tabooproject.org/repository/releases") }
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v12004:12004:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
    taboo("ink.ptms:um:1.1.5")
    taboo("top.maplex.arim:Arim:1.2.14")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JVM_1_8)
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}