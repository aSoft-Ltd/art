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
                withIosArm64()
                withIosSimulatorArm64()
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

    iosArm64()
    iosSimulatorArm64()

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
