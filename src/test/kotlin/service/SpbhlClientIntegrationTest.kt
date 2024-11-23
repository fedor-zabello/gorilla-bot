package service

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.configureFor
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SpbhlClientIntegrationTest {

    private lateinit var wireMockServer: WireMockServer

    @BeforeEach
    fun setUp() {
        wireMockServer = WireMockServer(WireMockConfiguration.options().dynamicPort())
        wireMockServer.start()

        configureFor("localhost", wireMockServer.port())

        wireMockServer.stubFor(get(urlPathEqualTo("/Schedule"))
            .withQueryParam("TeamID", equalTo("gorilla-team-id"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "text/html")
                .withBody(loadHtmlResponse("schedule.html"))
            )
        )
    }

    @AfterEach
    fun teardown() {
        wireMockServer.stop()
    }

    @Test
    fun `Get matches from SPBHL`() {
        val spbhlClient = SpbhClient("http://localhost:${wireMockServer.port()}", listOf("gorilla-team-id"))
        val matches = spbhlClient.getAllMatches()

        assertEquals(11, matches.size)
        assertTrue(matches.all { match -> match.teams.isNotEmpty() })
    }


    private fun loadHtmlResponse(fileName: String): String {
        val resource = javaClass.classLoader.getResource(fileName)
            ?: throw IllegalArgumentException("File not found: $fileName")
        return Files.readString(Paths.get(resource.toURI()))
    }
}