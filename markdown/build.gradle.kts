@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
    alias(kotlinz.plugins.dokka)
}

description = "The markdown renderer for the aSoft Rich Text Format"

kotlin {
    applyHierarchyTemplate {
        sourceSetTrees(KotlinSourceSetTree.main, KotlinSourceSetTree.test)
        common {
            withAndroidTarget()
            group("skiko") {
                withJvm()
                withJs()
                withWasmJs()
                withIosArm64()
                withIosSimulatorArm64()
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

    iosArm64()
    iosSimulatorArm64()

    macosArm64()

    sourceSets {
        commonMain.dependencies {
            api(projects.artCore)
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
