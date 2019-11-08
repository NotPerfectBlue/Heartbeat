package components

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import ext.containsJiraTaskName
import ext.extractJiraTaskName
import ext.subscribeToProjectTopic
import git4idea.GitLocalBranch
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryChangeListener
import git4idea.repo.GitRepositoryManager
import singletones.TickerStorage
import java.util.*

class GitService(project: Project) {

    private var gitRepository: GitRepository? = null

    private val timeStorage = TimeStorage.getInstance(project)

    private var currentBranch: GitLocalBranch? = null
        set(value) {
            if (value != null && field != value) {
                onCheckout(value.name)
            }
            field = value
        }

    private val onCheckout: (String) -> Unit = { branchName ->

        if (branchName.containsJiraTaskName()) {

            val taskName = branchName.extractJiraTaskName()

            timeStorage.addIfAbsent(taskName)

            TickerStorage.start(taskName)
        }
    }

    init {

        gitRepository = GitRepositoryManager.getInstance(project).repositories[0]
        gitRepository?.let {
            currentBranch = it.currentBranch
        }

        subscribeToProjectTopic(project, GitRepository.GIT_REPO_CHANGE) {
            GitRepositoryChangeListener {
                currentBranch = it.currentBranch
            }
        }
    }

    companion object {
        fun getInstance(project: Project): GitService {
            return ServiceManager.getService(project, GitService::class.java)
        }
    }
}
