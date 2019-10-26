package singletones

import com.atlassian.jira.rest.client.api.JiraRestClient
import com.atlassian.jira.rest.client.api.domain.input.WorklogInput
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory
import org.joda.time.DateTime
import java.net.URI
import com.atlassian.jira.rest.client.api.domain.Issue

object JiraClient {

    private lateinit var jiraRestClient: JiraRestClient

    fun init(
        hostname: String,
        username: String,
        password: String
    ) {
        jiraRestClient = AsynchronousJiraRestClientFactory()
            .createWithBasicHttpAuthentication(uri(hostname), username, password)
    }

    fun logTime(issueKey: String, startDate: DateTime, timeInMinutes: Int) {

        val issue = getIssue(issueKey)

        issue?.let {
            jiraRestClient
                .issueClient
                .addWorklog(it.worklogUri, WorklogInput.create(
                    it.worklogUri,
                    "",
                    startDate,
                    timeInMinutes
                ))
        }
    }

    private fun uri(hostname: String) = URI.create(hostname)

    private fun getIssue(issueKey: String): Issue? {
        return jiraRestClient
            .issueClient
            .getIssue(issueKey)
            .claim()
    }

}