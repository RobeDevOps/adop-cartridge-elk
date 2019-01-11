/**
  * This jobs create the whole pipeline view 
  */
String projectFolderName = "${PROJECT_NAME}"
short displayedBuildsValue = 5
short refreshFrequencyValue = 5
def pipelineView = buildPipelineView(projectFolderName + "/Pipeline View")

pipelineView.with{
    title( "Pipeline View" )
    displayedBuilds( displayedBuildsValue )
    selectedJob( "Create_Indexes_Pattern" )
    showPipelineParameters()
    showPipelineDefinitionHeader()
    refreshFrequency( refreshFrequencyValue )
}