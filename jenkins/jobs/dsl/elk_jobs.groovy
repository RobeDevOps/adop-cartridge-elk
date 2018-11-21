
import pluggable.scm.*;
import adop.cartridge.properties.*;

SCMProvider scmProvider = SCMProviderHandler.getScmProvider("${SCM_PROVIDER_ID}", binding.variables)

// Folders
def workspaceFolderName = "${WORKSPACE_NAME}"
def projectFolderName = "${PROJECT_NAME}"
def projectScmNamespace = "${SCM_NAMESPACE}"

// ** The logrotator variables should be changed to meet your build archive requirements
def logRotatorDaysToKeep = 7
def logRotatorBuildNumToKeep = 7
def logRotatorArtifactsNumDaysToKeep = 7
def logRotatorArtifactsNumToKeep = 7

// Jobs
def createIndexPattern = freeStyleJob(projectFolderName + "/Create_Index_Pattern")

createIndexPattern.with{
    description("Create Index Pattern.")
    logRotator {
        daysToKeep(logRotatorDaysToKeep)
        numToKeep(logRotatorBuildNumToKeep)
        artifactDaysToKeep(logRotatorArtifactsNumDaysToKeep)
        artifactNumToKeep(logRotatorArtifactsNumToKeep)
    }
    parameters{
        stringParam("KIBANA_HOST", 'kibana:5601', "Kibana Server")
        stringParam("KIBANA_USR", "roberto", "Kibana username")
        // Change this to credentials
        stringParam("KIBANA_PSW", "c18c2602bc8b1391", "Kibana password")
        
    }
    steps {
        shell('''set +x
            | curl -u ${KIBANA_USR}:${KIBANA_PSW} -XGET "http://${KIBANA_HOST}/api/saved_objects/index-pattern/adop_container_* -H "Content-Type: application/json" -H "kbn-xsrf: true"
            |set -x '''.stripMargin()
        )
    }
}