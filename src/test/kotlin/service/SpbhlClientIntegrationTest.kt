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
import org.junit.jupiter.api.assertDoesNotThrow
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
    }

    @AfterEach
    fun teardown() {
        wireMockServer.stop()
    }

    @Test
    fun `Get matches from SPBHL`() {
        wireMockServer.stubFor(
            get(urlPathEqualTo("/Schedule"))
                .withQueryParam("TeamID", equalTo("gorilla-team-id"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html")
                        .withBody(loadHtmlResponse())
                )
        )

        val spbhlClient = SpbhlClient("http://localhost:${wireMockServer.port()}", listOf("gorilla-team-id"))
        val matches = spbhlClient.getAllMatches()

        assertEquals(11, matches.size)
        assertTrue(matches.all { match -> match.teams.isNotEmpty() })
    }

    @Test
    fun `Should not throw not throw exception when fail to get matches`() {
        wireMockServer.stubFor(
            get(urlPathEqualTo("/Schedule"))
                .withQueryParam("TeamID", equalTo("gorilla-team-id"))
                .willReturn(
                    aResponse()
                        .withStatus(503)
                )
        )
        val spbhlClient = SpbhlClient("http://localhost:${wireMockServer.port()}", listOf("gorilla-team-id"))

        assertDoesNotThrow { spbhlClient.getAllMatches() }
    }


    private fun loadHtmlResponse(): String {
        val resource = javaClass.classLoader.getResource("schedule.html")
            ?: throw IllegalArgumentException("File not found")
        return Files.readString(Paths.get(resource.toURI()))
    }
}