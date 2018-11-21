# Export standard variables
export TOKEN_WORKSPACE_FOLDER_NAME="${WORKSPACE_NAME}"
export TOKEN_PROJECT_FOLDER_NAME=$(echo "${PROJECT_NAME}" | cut -d'/' -f2)

export TOKEN_GIT_CREDENTIAL="adop-jenkins-master"
export TOKEN_GERRIT_SERVER="ADOP Gerrit"

# Export other variables
### YOUR TOKENS GO HERE ###