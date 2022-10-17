package gatling.com.autentia.rvillalba.gatling.tutorial.java;

import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class NonPlayableCharacterSimulation extends Simulation{
    {
        ChainBuilder progressiveRandoms =
                exec(http("1 result").get("/randoms/1")).
                        exec(http("50 results").get("/randoms/50")).
                        exec(http("500 results").get("/randoms/500"));
        ChainBuilder massiveRandoms =
                exec(http("1000 results").get("/randoms/1000")).
                        exec(http("800 results").get("/randoms/800")).
                        exec(http("1300 results").get("/randoms/1300"));

        HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8023/characters")
                .header("Content-Type", "application/json")
                .header("Accept", "*/*");

        ScenarioBuilder progressiveCharactersScenario = scenario("Progressive Randoms").exec(progressiveRandoms);

        ScenarioBuilder randomCharactersScenario = scenario("Massive Randoms").exec(massiveRandoms);

        setUp(
                progressiveCharactersScenario.injectOpen(rampUsers(1000).during(10)),
                randomCharactersScenario.injectOpen(rampUsers(1000).during(10))
        ).protocols(httpProtocol);
    }

}