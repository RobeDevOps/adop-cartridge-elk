#!/bin/bash
set +x

if [ -d adop-dashboards ]
then
    rm -rf adop-dashboards
fi

git clone ${DASHBOARD_GIT_REPO} adop-dashboards
cd adop-dashboards


dashboards=(network sonar_metrics)

for dashboard in "${dashboards[@]}"
do
    curl -X POST -H "Content-Type: application/json" -H "kbn-xsrf: true" "${KIBANA_HOST}/api/kibana/dashboards/import?force=${OVERWRITE}" -d @${dashboard}.json
done
set -x