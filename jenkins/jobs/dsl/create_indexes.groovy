
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

def scriptsPath = '/Cartridge_Management/Load_Cartridge/cartridge/jenkins/jobs/dsl/create_index/scripts'
def scriptFullPathPath = "/workspace/" + projectWorkspace + "/" + projectName + scriptsPath

// Jobs
def createIndexPatternJob = freeStyleJob(projectFolderName + "/Create_Indexes_Pattern")
def importDashboard = freeStyleJob(projectFolderName + "/Import_Dashboards")

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
        shell('''#!/bin/bash
        |set +x
        |index_patterns=(adop_nginx_* adop_container_* adop_cpu_* adop_info_* adop_network_*)
        |for index_pattern in "${index_patterns[@]}"
        |do
        |  id=$index_pattern
        |  time_field="@timestamp"
        |  curl -f -XPOST -H "Content-Type: application/json" -H "kbn-xsrf: pattern" "${KIBANA_HOST}/api/saved_objects/index-pattern/$id" -d"{\\"attributes\\":{\\"title\\":\\"$index_pattern\\",\\"timeFieldName\\":\\"$time_field\\"}}"
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
