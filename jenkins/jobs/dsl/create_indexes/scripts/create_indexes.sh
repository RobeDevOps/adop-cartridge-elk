#!/bin/bash
set +x
index_patterns=(adop_nginx_* adop_container_* adop_cpu_* adop_info_* adop_network_* adop_sonar_*)

for index_pattern in "${index_patterns[@]}"
do
    id=$index_pattern
    time_field="@timestamp"
    curl -f -XPOST -H "Content-Type: application/json" -H "kbn-xsrf: pattern" "${KIBANA_HOST}/api/saved_objects/index-pattern/$id" -d"{\"attributes\":{\"title\":\"$index_pattern\",\"timeFieldName\":\"$time_field\"}}"
    if [ $? -eq 0 ]; then
        curl -XPOST -H "Content-Type: application/json" -H "kbn-xsrf: pattern" "${KIBANA_HOST}/api/kibana/settings/defaultIndex" -d"{\"value\":\"$id\"}"
        if [ $? -eq 0 ]; then
            printf "\n$index_pattern created successfully"
        fi
    fi
done
set -x