package org.acme

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.rest.client.inject.RestClient

@Path("/hello")
class GreetingResource(@RestClient private val testRestClient: TestRestClient) {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    suspend fun hello() = testRestClient.helloExternal()
}