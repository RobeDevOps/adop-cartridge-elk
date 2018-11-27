def projectFolderName = "${PROJECT_NAME}"

// ** The logrotator variables should be changed to meet your build archive requirements
def logRotatorDaysToKeep = 7
def logRotatorBuildNumToKeep = 7
def logRotatorArtifactsNumDaysToKeep = 7
def logRotatorArtifactsNumToKeep = 7

// Jobs
def importDashboardsJob = freeStyleJob(projectFolderName + "/Import_Dashboards")

importDashboardsJob.with{
    description("Import dashboards from json file")
    logRotator {
        daysToKeep(logRotatorDaysToKeep)
        numToKeep(logRotatorBuildNumToKeep)
        artifactDaysToKeep(logRotatorArtifactsNumDaysToKeep)
        artifactNumToKeep(logRotatorArtifactsNumToKeep)
    }
    parameters{
        stringParam("KIBANA_HOST", 'http://kibana:5601', "Kibana Server")
        stringParam("DASHBOARD_GIT_REPO", "https://github.com/RobeDevOps/adop-dashboards.git", "Git repository to pull dashboards json files")
    }
    steps {
        shell('''#!/bin/bash
        |set +x
        |git clone ${DASHBOARD_GIT_REPO} adop-dashboards
        |cd adop-dashboards
        |curl -X POST -H "Content-Type: application/json" -H "kbn-xsrf: true" http://kibana:5601/api/kibana/dashboards/import -d @network.json
        |set -x'''.stripMargin()
        )
    }
}

queue(importDashboardsJob)