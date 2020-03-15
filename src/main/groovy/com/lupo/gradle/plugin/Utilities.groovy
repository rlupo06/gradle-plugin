package com.lupo.gradle.plugin

import org.gradle.api.Project

class Utilities {

    static def listFromCsv(Project project, String key, List<String> defaults = []) {
        project.hasProperty(key) ? project.property(key).split(",") : defaults
    }
    static def propertyOrDefault(Project project, String key, Object defaultValue) {
        project.hasProperty(key) ? project.property(key) : defaultValue
    }
}
