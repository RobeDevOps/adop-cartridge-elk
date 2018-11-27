/**
  * This jobs create the whole pipeline view 
  */
String projectFolderName = "${PROJECT_NAME}"
String pipelineName = "Dashboard Pipeline View"
String startJob = "Create_Index_Pattern"
short displayedBuildsValue = 5
short refreshFrequencyValue = 5
def pipelineView = buildPipelineView(projectFolderName  + "/" + pipelineName)

pipelineView.with{
    title( pipelineName )
    displayedBuilds( displayedBuildsValue )
    selectedJob( startJob )
    showPipelineParameters()
    showPipelineDefinitionHeader()
    refreshFrequency( refreshFrequencyValue )
}