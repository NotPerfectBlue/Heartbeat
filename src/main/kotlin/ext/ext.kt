package ext

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.util.messages.Topic
import javafx.application.Application

val JIRA_REGEX = Regex("((([A-Z]{1,10})-?)[A-Z]+-\\d+)")

fun String.containsJiraTaskName(): Boolean {
    return this.contains(JIRA_REGEX) || this.matches(JIRA_REGEX)
}

fun String.extractJiraTaskName(): String {
    return JIRA_REGEX.find(this)?.value ?: ""
}

fun invokeLater(task: () -> Unit) = ApplicationManager.getApplication().invokeLater(task)

fun <L> subscribeToAppTopic(topic: Topic<L>, listener: () -> L) {
    ApplicationManager
        .getApplication()
        .messageBus
        .connect()
        .subscribe(topic, listener())
}

fun <L> subscribeToProjectTopic(project: Project, topic: Topic<L>, listener: () -> L) {
    project
        .messageBus
        .connect()
        .subscribe(topic, listener())
}