package com.tyhurst.samples.resttests;

import com.jayway.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RestCountriesOrgJsonTest {

    /**
     * This test uses org.json classes for browsing through
     * the JSON response.
     */
    @Test
    public void getCapitalHappyPath() {
        // Get info about european country.
        Response resp = get("http://restcountries.eu/rest/v1/name/belgium");
        JSONArray jsonResponse = new JSONArray(resp.asString());

        // Confirm value of capital.
        String capital = jsonResponse.getJSONObject(0).getString("capital");
        assertEquals("Brussels", capital);

        // Confirm French name for country.
        JSONObject countryInfo = jsonResponse.getJSONObject(0);
        JSONObject translations = countryInfo.getJSONObject("translations");
        String frenchTranslation = translations.getString("fr");
        assertEquals("Belgique", frenchTranslation);
        // Same test in a single line.
        assertEquals("Belgique", jsonResponse.
                getJSONObject(0).
                getJSONObject("translations").
                getString("fr"));

        // Confirm population.
        assertTrue("Expected population > 8M.", 8000000 < countryInfo.getInt("population"));

        // Confirm measure of income distribution of a nation's residents.
        assertTrue("Expected Gini coefficient > 30", 30.0d < countryInfo.getDouble("gini"));
    }

    /**
     * See ValidatableResponseOptions at:
     * http://static.javadoc.io/com.jayway.restassured/rest-assured/2.9.0/index.html
     * for the kinds of validation that can be done with 'body'.
     */
    @Test
    public void capitalGivenWhenThenTestingFormat() {
        given().contentType("application/json").
                when().get("http://restcountries.eu/rest/v1/name/norway").
                then().statusCode(200).
                body(containsString("\"capital\":\"Oslo\"")). // string
                body(containsString("\"fr\":\"Norvège\"")).  // French translation
                body(containsString("\"population\":5176998")). // integer
                body(containsString("\"gini\":25.8")); // double
    }

}
