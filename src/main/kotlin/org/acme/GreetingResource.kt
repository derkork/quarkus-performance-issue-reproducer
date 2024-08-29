package org.acme

import io.quarkus.logging.Log
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.rest.client.inject.RestClient
import kotlin.system.measureTimeMillis

@Path("/hello")
class GreetingResource(@RestClient private val testRestClient: TestRestClient) {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    suspend fun hello(): String {
        var result: String
        val millis = measureTimeMillis {
            result = testRestClient.helloExternal()
        }
        Log.info("Upstream call took $millis ms")
        return result
    }
}