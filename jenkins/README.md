# Jenkins jobs and views

All the jenkins jobs and views are into the dsl directory. No xml configuration is used but the directory is required by the **Load Cartridge** 

## Pipeline View.
Create the pipeline view with the name **Pipeline View** and use **Create_Indexes_Pattern** as started job.

## Create_Indexes_Pattern Job
This job generate all the kibana index patterns using the rest api. The script path is: ```dsl/create_indexes/scripts/create_indexes.sh```

* **adop_nginx_***: Contains all the logs from the proxy container.
* **adop_container_***: Indice for all the information related with containers. [Docker container metricset](https://www.elastic.co/guide/en/beats/metricbeat/current/metricbeat-metricset-docker-container.html)
* **adop_cpu_***: Indice for all the information related with containers cpu. [Docker cpu metricset](https://www.elastic.co/guide/en/beats/metricbeat/current/metricbeat-metricset-docker-cpu.html)  
* **adop_info_***: Indice for basic containers information. [Docker info metricset](https://www.elastic.co/guide/en/beats/metricbeat/current/metricbeat-metricset-docker-info.html)
* **adop_network_***: Indice for all the networking information. [Docker network metricset](https://www.elastic.co/guide/en/beats/metricbeat/current/metricbeat-metricset-docker-network.html)

## Import Dashboards Job
Once the indexes patterns are generated the dashboards are published using the rest api. The script path is: ```dsl/import_dashboards/scripts/import_dashboards.sh```

## TO-DO

* Validate the index-patterns were created successfully.
* Validate the dashboards were imported successfully.
* Automate the CARTRIDGE_LOAD dynamically. (avoid the copy/paste process in Load_Cartridge job)
* Export the common variables to environment variables: KIBANA_HOST, DASHBOARD_GIT_REPO
