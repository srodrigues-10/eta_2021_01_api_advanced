package tests;

import core.BaseTest;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.instanceOf;

public class CountriesQueryTest extends BaseTest {

    @Test
    public void shouldReturnAllCountries(){
        given().
                body("{\"query\":\"query {\\r\\n  countries\\r\\n  {\\r\\n    code\\r\\n    name\\r\\n  }\\r\\n}\",\"variables\":{}}").
        when().
                post().
        then().
                assertThat().
                statusCode(200).
                body("data.countries", instanceOf(List.class)).
                body("data.countries", Matchers.hasSize(250));
    }

    @Test
    public void mustReturnAllCountriesUsingUSDCurrencyThatAreNotUSA(){
        given()
                .body("{\"query\":\"query{\\r\\n    countries(filter:{\\r\\n        currency:{\\r\\n            in:[\\\"USD\\\"]\\r\\n        }\\r\\n        code:{\\r\\n            nin:[\\\"US\\\"]\\r\\n        }\\r\\n    }){\\r\\n        code\\r\\n        name\\r\\n        native\\r\\n        currency\\r\\n        capital\\r\\n    }\\r\\n}\",\"variables\":{}}")
        .when()
                .post()
        .then()
                .statusCode(200)
                .body("data.countries", instanceOf(List.class))
                .body("data.countries", Matchers.hasSize(15))
                .body("data.countries.name", Matchers.not(Matchers.hasItem("United States")));
    }

    @Test
    public void shouldReturnRequiredFieldsWithCorrectTypes(){
        given().
                body("{\"query\":\"query {\\r\\n  countries(filter: {code:{ in:[\\\"BR\\\"]}})\\r\\n  {\\r\\n    code\\r\\n    name\\r\\n    native\\r\\n    phone\\r\\n    continent{\\r\\n        code\\r\\n        name\\r\\n    }\\r\\n    capital\\r\\n    currency\\r\\n    languages{\\r\\n        code\\r\\n        name\\r\\n    }\\r\\n    emoji\\r\\n    emojiU\\r\\n    states {\\r\\n        code\\r\\n        name\\r\\n    }\\r\\n  }\\r\\n}\",\"variables\":{}}").
        when().
                post().
        then().
                statusCode(200).
                body("data.countries", instanceOf(List.class)).
                body("data.countries[0].code", instanceOf(String.class)).
                body("data.countries[0].name", instanceOf(String.class)).
                body("data.countries[0].native", instanceOf(String.class)).
                body("data.countries[0].phone", instanceOf(String.class)).
                body("data.countries[0].continent", instanceOf(HashMap.class)).
                body("data.countries[0].capital", instanceOf(String.class)).
                body("data.countries[0].currency", instanceOf(String.class)).
                body("data.countries[0].languages", instanceOf(List.class)).
                body("data.countries[0].emoji", instanceOf(String.class)).
                body("data.countries[0].emojiU", instanceOf(String.class)).
                body("data.countries[0].states", instanceOf(List.class));
    }

    @Test
    public void shouldReturnAllCountriesInSouthAmericaThatUsesEurCurrency(){
        given()
                .body("{\"query\":\"query{\\r\\n  countries(filter:{\\r\\n    continent:{\\r\\n      in:[\\\"SA\\\"]\\r\\n    }\\r\\n    currency:{\\r\\n    \\tin:[\\\"EUR\\\"]\\r\\n    }\\r\\n  })\\r\\n  {\\r\\n    code\\r\\n    name\\r\\n    currency\\r\\n  }\\r\\n}\",\"variables\":{}}")
        .when()
                .post()
        .then()
                .statusCode(200)
                .body("data.countries", instanceOf(List.class))
                .body("data.countries", Matchers.hasSize(1))
                .body("data.countries.code[0]", Matchers.is("GF"))
                .body("data.countries.name[0]", Matchers.is("French Guiana"))
                .body("data.countries.currency[0]", Matchers.is("EUR"));
    }


}

