package org.acme

import io.quarkus.logging.Log
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.lessThan
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.system.measureTimeMillis

@QuarkusTest
@QuarkusTestResource(MockServer::class)
class GreetingResourceTest {

    @Test
    fun testHelloEndpoint() {
        val timings = ConcurrentLinkedQueue<Long>()

        for (i in 1..10) {
            Log.info("Run $i of 10")
            runBlocking {
                (1..50).map { j ->
                    Log.info("Request $j")
                    async(Dispatchers.IO) {
                        val timing = measureTimeMillis {
                            given()
                                .`when`().get("/hello")
                                .then()
                                .statusCode(200)
                                .body(`is`("Hello from Wiremock"))
                        }
                        timings.add(timing)
                    }
                }.awaitAll()
            }
        }

        val averageTime = timings.sum() / timings.size
        Log.info("Average time: $averageTime ms")
        // execution speed depends on the machine but 1000ms should be a reasonable limit
        assertThat(averageTime, lessThan(1000))
    }
}