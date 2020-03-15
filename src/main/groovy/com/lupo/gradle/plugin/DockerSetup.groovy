package com.lupo.gradle.plugin

import org.gradle.api.Project

class DockerSetup {

    static void include(Project project) {

        project.configure(project, {
            apply plugin: 'docker-compose'
            apply plugin: 'docker'
            apply plugin: 'war'
            apply plugin: "org.springframework.boot"

            dockerCompose {
                final String projectRootDirectory = project.getRootProject()
                project.logger.info("using compose files at ${projectRootDirectory}")

                if (project.hasProperty('localRun') && project.property('localRun')) {
                    useComposeFiles = ["${projectRootDirectory}/docker-compose.yml", "${projectRootDirectory}/docker-compose.override.yml"]
                } else {
                    useComposeFiles = ["${projectRootDirectory}/docker-compose.yml", "${projectRootDirectory}/docker-compose.jenkins.yml"]
                }

                removeOrphans = true
                captureContainersOutputToFiles = "${projectRootDirectory}/docker-logs"
                environment.VERSION = project.version ?: 'LOCAL-SNAPSHOT'
            }

            project.task('buildWebAppDockerImage', type: Docker) {
                dependsOn bootWar
                final String dockerRegistry = project.hasProperty('dockerRepositoryHost') ? project.project('dockerRepositoryHost') : ''
                final String projectName = project.name.toLowerCase()
                baseImage = 'tomcat:9-jre11-slim'
                runCommand 'rm -rf /usr/local/tomcat/webapps/*'
                runCommand 'apt-get update && echo y | apt-get install curl'
                tag "${dockerRegistry}/${projectName}"
                addFile("${project.buildDir}/libs/${project.name}-${project.version}.war",
                        '/usr/local/tomcat/webapps/ROOT.war')
                exposePort(8080)
                push = project.hasProperty('pushAppDockerImage') && project.property('pushAppDockerImage')
            }

            composeUp.doFirst {
                println "Using the following compose files " + dockerCompose.useComposeFiles
            }
        })
    }
}