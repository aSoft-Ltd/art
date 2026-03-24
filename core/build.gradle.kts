@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree


plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
    alias(kotlinz.plugins.dokka)
}

description = "aSoft Core Rich Text Format"

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

//    androidTarget {
        // compilations.all {
        //    compileTaskProvider {
        //        compilerOptions.jvmTarget = JvmTarget.JVM_17
        //    }
        // }
//    }

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
            api(kotlinx.serialization.core)
            api(kotlinx.coroutines.core)?.because("We need to setup the supervisor job for a http polling connection")
        }

        commonTest.dependencies {
            implementation(projects.artBuilder)
            implementation(kotlin("test"))
            implementation(kotlinx.serialization.json)
            implementation(libs.kommander.core)
            implementation(libs.kommander.coroutines)
        }

        jvmTest.dependencies {
            implementation(kotlin("test-junit5"))
        }
    }
}
