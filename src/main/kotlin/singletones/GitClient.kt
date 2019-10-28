package singletones

import com.intellij.openapi.project.Project
import git4idea.GitLocalBranch
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryChangeListener
import git4idea.repo.GitRepositoryManager

object GitClient {

    val name: String
        get() {
            return currentBranch?.name ?: ""
        }

    private var gitRepository: GitRepository? = null
    private var currentBranch: GitLocalBranch? = null

    @Synchronized
    fun init(project: Project, onCheckout: () -> Unit) {
        if (gitRepository == null) {
            gitRepository = GitRepositoryManager.getInstance(project).repositories[0]
            gitRepository?.let {
                currentBranch = it.currentBranch
            }

            project
                .messageBus
                .connect()
                .subscribe(GitRepository.GIT_REPO_CHANGE,
                    GitRepositoryChangeListener {
                        if (isCheckout(it)) {
                            onCheckout()
                        }
                        currentBranch = it.currentBranch
                    })
        }
    }

    private fun isCheckout(repository: GitRepository): Boolean {
        return repository.currentBranch != currentBranch
    }
}