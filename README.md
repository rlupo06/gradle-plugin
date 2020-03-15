# gradle-plugin

**Resource** 
- [Avast Gradle Plugin](https://github.com/avast/gradle-docker-compose-plugin)  
- [Transmode Gradle Plugin](https://github.com/Transmode/gradle-docker)
- [Testsets Gradle Plugin](https://github.com/unbroken-dome/gradle-testsets-plugin)

### Setup

Install [IntelliJ](https://www.jetbrains.com/idea/download)

**Homebrew**
```
ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```
**Git**
```
brew install git
```
**Java**
```
brew install java
```
**Gradle**
```
brew install gradle
```
### Quick Start
add the following to a projects build.gradle
```
buildscript {
    repositories {
        mavenLocal ()
        //TODO: implemented remote repositor
    }
    dependencies {
        classpath group: 'com.lupo', name: 'gradle-plugin', version: '1.0-SNAPSHOT'
    }
}

apply plugin: 'lupo-gradle-plugin'
```
Have three compose files in the projects root directory
- docker-compose.jenkins.yml (Services: Application)
- docker-compose.override.yml (Services: application dependencies with published ports)
- docker-compose.yml (Services include application dependencies with exposed ports, for example a database)

### gradle-plugin helpful gradle tasks
buildWebAppDockerImage
- builds an image of the application using the war file in the projects build/libs directory.

integrationTest
- Spins up the applcation and application dependency containers 
- Maps the random published ports to system and environment variable properties
- Executes the integration junit tests in the directory src/integrationTest/java

automationTest
- Spins up the applcation and application dependency containers 
- Maps the random published ports to system and environment variable properties
- Executes the automation junit tests in the directory src/automationTest/java

jacocoTestReport
- generates the test report

jacocoTestCoverageVerification
- uses the gradle property totalLineCoveragePercentage to validate the code coverage meets the threshold.

fullBuild
- runs buld, unit, integrationTest, automationTest, jacocoTestReport, jacocoTestCoverageVerification

artifactoryPublish
- publishes the projects war file to artifactory
