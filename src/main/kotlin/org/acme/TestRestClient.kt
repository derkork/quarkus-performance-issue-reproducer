package org.acme

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@RegisterRestClient(configKey = "test-client")
interface TestRestClient {

    @GET()
    @Path("/external/hello")
    suspend fun helloExternal(): String
}