package io.github.hejcz.architecture

import com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.ImportOptions
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ArchitectureSpec : Spek({

    describe("Architecture") {

        val externalPackages = arrayOf("kotlin..", "java..", "org.jetbrains..")

        val productionClasses = ClassFileImporter(ImportOptions().with(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS))
            .importPackages("io.github.hejcz..")

        it("api does not depend on anything") {
            classes().that().resideInAPackage("io.github.hejcz.api..")
                .should()
                .onlyDependOnClassesThat(resideInAnyPackage("io.github.hejcz.api..", *externalPackages))
                .check(productionClasses)
        }

        it("setup depends on api only") {
            classes().that().resideInAPackage("io.github.hejcz.setup..")
                .should()
                .onlyDependOnClassesThat(
                    resideInAnyPackage("io.github.hejcz.api..", "io.github.hejcz.setup..", *externalPackages))
                .check(productionClasses)
        }

        it("base depends on setup and api only") {
            classes().that().resideInAPackage("io.github.hejcz.base..")
                .should()
                .onlyDependOnClassesThat(
                    resideInAnyPackage("io.github.hejcz.api..", "io.github.hejcz.setup..", "io.github.hejcz.base..", *externalPackages))
                .check(productionClasses)
        }

        it("expansions depends on api, setup and base and not on each other") {
            val expansions = ClassFileImporter(ImportOptions().with(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS))
                .importPackages("io.github.hejcz.expansion")
                .map { it.packageName }
                .filter { name -> name.count { it == '.' } == 4 }
                .distinct()

            expansions.forEach {
                classes().that().resideInAPackage("$it..")
                    .should()
                    .onlyDependOnClassesThat(resideInAnyPackage(
                        "io.github.hejcz.api..", "io.github.hejcz.setup..", "io.github.hejcz.base..", "$it..", *externalPackages))
                    .check(productionClasses)
            }
        }
    }
})
