import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlint) apply false
}

subprojects {
    // Apply only to modules that have Kotlin
    pluginManager.withPlugin("org.jetbrains.kotlin.android") {
        pluginManager.apply("org.jlleitschuh.gradle.ktlint")
        configureKtlint()
    }
    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        pluginManager.apply("org.jlleitschuh.gradle.ktlint")
        configureKtlint()
    }
}

// A small helper to keep config in one place
fun Project.configureKtlint() {
    extensions.configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        android.set(true)          // match Android/Kotlin (Android Studio) style
        verbose.set(true)
        ignoreFailures.set(false)  // fail CI on style issues
        // Print issues to the Gradle/Build console
        outputToConsole.set(true)

        // Also keep file reports (nice for CI tools)
        reporters {
            reporter(ReporterType.PLAIN)       // human-readable in console
            reporter(ReporterType.CHECKSTYLE)  // XML, useful for CI integrations
        }
    }
}