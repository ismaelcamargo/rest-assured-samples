package com.tyhurst.samples.resttests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Response;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.get;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RestCountriesJacksonTest {

    /**
     * This test uses Jackson for inspecting the JSON returned
     * by REST services.
     *
     * @throws java.io.IOException
     */
    @Test
    public void getCapitalHappyPath() throws java.io.IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Get info about european country.
        Response response = get("http://restcountries.eu/rest/v1/name/switzerland");
        JsonNode countryInfo = mapper.readTree(response.asInputStream()).get(0);

        // Confirm value of capital.
        JsonNode capital = countryInfo.get("capital");
        String capitalAsText = capital.textValue();
        assertEquals("Bern", capitalAsText);

        // Confirm French name for country.
        assertEquals("Suisse", countryInfo.path("translations").path("fr").asText());

        // 'path' avoids NullPointerException when item is missing
        assertEquals("", countryInfo.path("anUnknownPropertyZ").path("fr").asText());

        // or 'get' will throw NullPointerException
        assertEquals("Suisse", countryInfo.get("translations").get("fr").textValue());

        // Confirm population.
        assertTrue("Expected population > 8M.", 8000000 < countryInfo.get("population").asInt());

        // 'path' avoids NullPointerException.
        assertEquals(0, countryInfo.path("anUnknownPropertyZ").asInt());

        // Confirm measure of income distribution of a nation's residents.
        assertTrue("Expected Gini coefficient > 30", 30.0d < countryInfo.get("gini").asDouble());

        // 'path' avoids NullPointerException.
        assertEquals(0.0d, countryInfo.path("anUnknownPropertyZ").asDouble(), 0.001d);

    }

}
