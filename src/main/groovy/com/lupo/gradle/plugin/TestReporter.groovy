package com.lupo.gradle.plugin

import org.gradle.api.Project

class TestReporter {

    static void include(Project project) {

        final def coverageExcludes = Utilities.listFromCsv(project, 'coverageExclusions', ['**/SpringConfig*'])
        project.configure(project, {
            apply plugin: 'jacoco'

            jacoco {
                toolVersion = "0.8.5"
            }

            jacocoTestReport {
                mustRunAfter integrationTest
                mustRunAfter automationTest
                mustRunAfter test
                executionData = fileTree(dir: "$buildDir/jacoco", include:'**/*exec')
                afterEvaluate {
                    classDirectories = files(classDirectories.files.collect {
                        fileTree(dir: it, exclude: coverageExcludes)
                    })
                }
            }

            jacocoTestCoverageVerification {
                mustRunAfter integrationTest
                mustRunAfter automationTest
                mustRunAfter test

                final BigDecimal totalLineCoveragePercentage = new BigDecimal(Utilities.propertyOrDefault(project, 'totalLineCoveragePercentage', ".85") as String)
                final BigDecimal classLinePercentage = new BigDecimal(Utilities.propertyOrDefault(project, 'classLineCoveragePercentage', ".85") as String)

                executionData = fileTree(dir: "$buildDir/jacoco", include:'**/*.exec')
                violationRules {
                    rule {
                        limit {
                            minimum = totalLineCoveragePercentage
                        }
                    }
                    rule {
                        element = 'CLASS'
                        excludes = coverageExcludes
                        limit {
                            minimum = classLinePercentage
                        }
                    }
                }
                afterEvaluate {
                    classDirectories = files(classDirectories.files.collect {
                        fileTree(dir: it, exclude: coverageExcludes)
                    })
                }
            }
        })
    }
}
