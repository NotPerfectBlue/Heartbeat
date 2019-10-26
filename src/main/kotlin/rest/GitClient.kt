package rest

import com.intellij.openapi.project.Project
import git4idea.repo.GitRepository
import git4idea.repo.GitRepositoryManager

object GitClient {
    private fun listRepositories(project: Project): List<GitRepository> =
        GitRepositoryManager.getInstance(project).repositories

    fun currentBranch(project: Project) = listRepositories(project)[0].currentBranch
}