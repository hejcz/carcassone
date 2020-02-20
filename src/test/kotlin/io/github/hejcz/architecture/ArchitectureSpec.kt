package io.github.hejcz.architecture

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.ImportOptions
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


object ArchitectureSpec : Spek({

    describe("Architecture") {

        it("core does not depend on expansions") {
            classes().that().resideInAPackage("io.github.hejcz.core..")
                .should()
                .onlyDependOnClassesThat(JavaClass.Predicates.resideInAnyPackage(
                    "kotlin..", "java..", "org.jetbrains..", "io.github.hejcz.core.."))
                .check(ClassFileImporter(ImportOptions().with(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS))
                    .importPackages("io.github.hejcz.."))
        }

    }
})
