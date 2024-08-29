package org.acme

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import io.quarkus.logging.Log
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import java.util.*


class MockServer : QuarkusTestResourceLifecycleManager {
    private var wireMockServer: WireMockServer? = null

    override fun start(): Map<String, String> {
        Log.info("Starting the wiremock server")
        wireMockServer = WireMockServer(0) // use random port
        wireMockServer!!.start()

        WireMock.configureFor(wireMockServer!!.port())
        // for this specific url return the canned response
        WireMock.stubFor(
            WireMock.get(WireMock.urlMatching("/external/hello"))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("Hello from Wiremock")
                        .withFixedDelay(250)
                )
        )

        // overwrite the upstream server url for the test to point to the Wiremock server
        return Collections.singletonMap("%test.quarkus.rest-client.test-client.uri", wireMockServer!!.baseUrl())
    }

    override fun stop() {
        if (wireMockServer != null) {
            wireMockServer!!.stop()
        }
    }
}
