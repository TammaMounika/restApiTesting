package org.automation.functional;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.automation.support.Util;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class AddingUsersTest {

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://6143a99bc5b553001717d06a.mockapi.io";
        System.out.println("BeforeClass SetUp");

    }

    @DataProvider(name = "postInputFiles")
    public Object[] usedInputPath() {
        return new Object[][]{{"testdata/postrequest1.json"}, {"testdata/postrequest2.json"}, {"testdata/postrequest3.json"}};
    }

    List<String> uniqueIds = new ArrayList<>();

    @Test(dataProvider = "postInputFiles")
    public void addUser1(String path) throws JSONException {
        RequestSpecification postRequest = RestAssured.given();
        postRequest.header("Content-Type", "application/json");
        JSONObject requestBody = Util.readJsonFile(path);

        postRequest.body(requestBody.toJSONString());
        Response postResponse = postRequest.post("/testapi/v1/Users");

        assertEquals(postResponse.getStatusCode(), 201);
        assertEquals(postResponse.getBody().jsonPath().get("employee_firstname"), requestBody.get("employee_firstname"));
        assertEquals(postResponse.getBody().jsonPath().get("employee_lastname"), requestBody.get("employee_lastname"));

        String uniqueId = postResponse.getBody().jsonPath().get("id");
        assertNotNull(uniqueId);
        uniqueIds.add(uniqueId);
        

        RequestSpecification getRequest = RestAssured.given();
        getRequest.header("Content-Type", "application/json");
        Response getResponse = getRequest.get("/testapi/v1/Users");
        assertEquals(getResponse.getStatusCode(), 200);

        JSONArray responseArray = Util.convertStringToJsonArray(getResponse.getBody().asString());
        JSONObject responseObject = Util.filterJSONArray(responseArray,"id", uniqueId);
        
        assertNotNull(responseObject);
        //asserting two json strings
        JSONAssert.assertEquals(postResponse.asString(), responseObject.toJSONString(), false);
    }

    @AfterClass
    public void tearDown() {
        System.out.println("TearDown AfterClass");
        if (uniqueIds.size() == 0)
            return;
        RequestSpecification request = RestAssured.given();
        uniqueIds.forEach(id -> {
            request.delete("/testapi/v1/Users/" + id);
            System.out.println("tearDown of " + id);
        });

        uniqueIds = Collections.emptyList();
    }

}
