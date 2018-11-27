
import pluggable.scm.*;
import adop.cartridge.properties.*;

SCMProvider scmProvider = SCMProviderHandler.getScmProvider("${SCM_PROVIDER_ID}", binding.variables)

// Folders
def projectFolderName = "${PROJECT_NAME}"

// ** The logrotator variables should be changed to meet your build archive requirements
def logRotatorDaysToKeep = 7
def logRotatorBuildNumToKeep = 7
def logRotatorArtifactsNumDaysToKeep = 7
def logRotatorArtifactsNumToKeep = 7

def (projectWorkspace, projectName) = projectFolderName.tokenize( '/' )

def scriptsPath = '/Cartridge_Management/Load_Cartridge/cartridge/jenkins/jobs/dsl/create_indexes/scripts'
def scriptFullPathPath = "/workspace/" + projectWorkspace + "/" + projectName + scriptsPath

// Jobs
def createIndexPatternJob = freeStyleJob(projectFolderName + "/Create_Indexes_Pattern")

createIndexPatternJob.with{
    description("Create Indexes Pattern.")
    logRotator {
        daysToKeep(logRotatorDaysToKeep)
        numToKeep(logRotatorBuildNumToKeep)
        artifactDaysToKeep(logRotatorArtifactsNumDaysToKeep)
        artifactNumToKeep(logRotatorArtifactsNumToKeep)
    }
    parameters{
        stringParam("KIBANA_HOST", 'http://kibana:5601', "Kibana Server")
        
    }
    steps {
        shell(readFileFromWorkspace(scriptFullPathPath + "/create_indexes.sh" ))
    }
    publishers{
        downstreamParameterized{
            trigger(projectFolderName + "/Import_Dashboards"){
                condition("UNSTABLE_OR_BETTER")
                parameters{
                    predefinedProp("KIBANA_HOST","http://kibana:5601")
                    predefinedProp("DASHBOARD_GIT_REPO","https://github.com/RobeDevOps/adop-dashboards.git")
                }
            }
        }
    }
}

queue(createIndexPatternJob)
