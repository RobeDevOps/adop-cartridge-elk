
import pluggable.scm.*;
import adop.cartridge.properties.*;

SCMProvider scmProvider = SCMProviderHandler.getScmProvider("${SCM_PROVIDER_ID}", binding.variables)
CartridgeProperties cartridgeProperties = new CartridgeProperties("${CARTRIDGE_CUSTOM_PROPERTIES}");

// Folders
def workspaceFolderName = "${WORKSPACE_NAME}"
def projectFolderName = "${PROJECT_NAME}"
def projectScmNamespace = "${SCM_NAMESPACE}"

// Variables
// **The git repo variables can be changed to the users' git repositories when loading the cartridge and populating the CARTRIDGE_CUSTOM_PROPERTIES with "scm.code.repo.name" and "scm.test.repo.name" properties.
def skeletonAppgitRepo = cartridgeProperties.getProperty("scm.code.repo.name", "YOUR_APPLICATION_REPO");
def regressionTestGitRepo = cartridgeProperties.getProperty("scm.test.repo.name", "YOUR_REGRESSION_TEST_REPO");

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
        usernamePassword("KIBANA_CREDS", "kibana_creds")
    }
    steps {
        shell('''set +x
            |
            | echo ${KIBANA_CREDS} 
            |set -x '''.stripMargin()
    }
}