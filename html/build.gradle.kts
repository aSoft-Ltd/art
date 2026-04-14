@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
    alias(kotlinz.plugins.dokka)
}

description = "The html renderer for the aSoft Rich Text Format"

kotlin {
    applyHierarchyTemplate {
        sourceSetTrees(KotlinSourceSetTree.main, KotlinSourceSetTree.test)
        common {
            withAndroidTarget()
            group("skiko") {
                withJvm()
                withJs()
                withWasmJs()
                withIosX64()
                withIosArm64()
                withIosSimulatorArm64()
                withMacosX64()
                withMacosArm64()
            }
        }
    }

    jvm {
        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }
    js(IR) {
        browser()
        nodejs()
    }

    wasmJs { browser() } // until coil and kotlinx-datetime supports this, we ain't gonna
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()

    sourceSets {
        commonMain.dependencies {
            api(projects.artCore)
            api(kotlinx.html)
        }

        commonTest.dependencies {
            implementation(projects.artBuilder)
            implementation(kotlin("test"))
            implementation(libs.kommander.core)
        }

        jvmTest.dependencies {
            implementation(kotlin("test-junit5"))
        }
    }
}
