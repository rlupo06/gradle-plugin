package com.lupo.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class GradlePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.configure(project, {
            DockerSetup.include(project)
            TestExecutioner.include(project)
            TestReporter.include(project)
        })
    }

}
