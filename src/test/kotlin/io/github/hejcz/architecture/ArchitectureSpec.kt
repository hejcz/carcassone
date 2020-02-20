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

        it("core does not depend on expansions") {
            classes().that().resideInAPackage("io.github.hejcz.core..")
                .should()
                .onlyDependOnClassesThat(resideInAnyPackage("io.github.hejcz.core..", *externalPackages))
                .check(productionClasses)
        }

        it("expansions do not depend on each other") {
            val expansions = ClassFileImporter(ImportOptions().with(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS))
                .importPackages("io.github.hejcz.expansion")
                .map { it.packageName }
                .filter { name -> name.count { it == '.' } == 4 }
                .distinct()

            expansions.forEach {
                classes().that().resideInAPackage("$it..")
                    .should()
                    .onlyDependOnClassesThat(resideInAnyPackage(
                        "io.github.hejcz.core..", "io.github.hejcz.setup..", "$it..", *externalPackages))
                    .check(productionClasses)
            }
        }
    }
})
