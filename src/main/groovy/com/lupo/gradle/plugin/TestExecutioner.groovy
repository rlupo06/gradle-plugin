package com.lupo.gradle.plugin

import org.gradle.api.Project

class TestExecutioner {

    static void include(Project project) {
        project.configure(project, {
            apply plugin: 'org.unbroken-dome.test-sets'
            apply plugin: 'jacoco'

            testSets {
                integrationTest
                automationTest
            }

            //configures automationTest to send stdout to gradle output
            automationTest {
                dependsOn composeUp
                finalizedBy composeDown

                doFirst {
                    dockerCompose.exposeAsSystemProperties(automationTest)
                }
                testLogging {
                    showStandardStreams = true
                }
            }
            integrationTest {
                dependsOn composeUp
                finalizedBy composeDown

                doFirst {
                    dockerCompose.exposeAsSystemProperties(integrationTest)
                }
                jacoco {
                    destinationFile = file("$buildDir/jacoco/integration-test.exec")
                }
            }

            project.task('fullBuild') {
                group 'build'
                dependsOn build
                finalizedBy integrationTest
                finalizedBy automationTest
                finalizedBy jacocoTestReport
                finalizedBy jacocoTestCoverageVerification
            }
        })
    }
}
