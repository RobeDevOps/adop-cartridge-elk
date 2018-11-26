
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
    description("Create Index Patterns.")
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
        shell('''#!/bin/bash
        |set +x
        |index_patterns=(adop_nginx_* adop_container_* adop_cpu_* adop_info_* adop_network_*)
        |for index_pattern in "${index_patterns[@]}"
        |do
        |  id=$index_pattern
        |  time_field="@timestamp"
        |  curl -f -XPOST -H "Content-Type: application/json" -H "kbn-xsrf: pattern" "${KIBANA_HOST}/api/saved_objects/index-pattern/$id" -d"{\\"attributes\\":{\\"title\":\\"$index_pattern\\",\\"timeFieldName\\":\\"$time_field\\"}}"
        |  if [ $? -eq 0 ]; then
        |    curl -XPOST -H "Content-Type: application/json" -H "kbn-xsrf: pattern" "${KIBANA_HOST}/api/kibana/settings/defaultIndex" -d"{\\"value\\":\\"$id\\"}"
        |    if [ $? -eq 0 ]; then
        |       printf "\\n$index_pattern created successfully"
        |    fi
        |  fi
        |done  
        |set -x'''.stripMargin()
        )
    }
}