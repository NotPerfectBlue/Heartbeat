package ext

val JIRA_REGEX = Regex("((([A-Z]{1,10})-?)[A-Z]+-\\d+)")

fun String.containsJiraTaskName(): Boolean {
    return this.contains(JIRA_REGEX) || this.matches(JIRA_REGEX)
}

fun String.extractJiraTaskName(): String? {
    return JIRA_REGEX.find(this)?.value
}