import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UsersTest {

	private RequestSpecification requestSpecification;
	Utilities Utilities = new Utilities();

	@BeforeClass
	public void setup() {

		// Specifying the base URI conditions
		// Specifying the request , how our request should look like

		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecBuilder.setContentType(ContentType.JSON).setAccept(ContentType.JSON).setBaseUri(Constants.BASE_URL);

		// building the request specification
		requestSpecification = requestSpecBuilder.build();
	}

	// ============================= Getting all the users
	// ======================================

	@Test(dependsOnMethods = { "deletingExistingUser" })
	public void getUsersList() {

		try {

			System.out.println("************ getting all the users ***************");
			Response response = RestAssured.given().spec(requestSpecification).get("/users/");
			System.out.println(response.getBody().asPrettyString());

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	// ============================== Getting a particular user by ID
	// ============================

	@Test(dependsOnMethods = { "getUsersList" })
	public void getParticulatUser() {

		try {

			System.out.println("************ getting first user in list ***************");
			Response response = RestAssured.given().spec(requestSpecification)
					.get("/users/" + Constants.USER_TO_RETRIEVE);
			JsonPath jsonPathEvaluator = response.jsonPath();
			String username = jsonPathEvaluator.get("username");
			System.out.println("***************** The desired username is :" + username);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	// ============================ Post method to create a new user
	// =============================

	@Test()
	public void creatingNewUser() {

		try {

			System.out.println("************* Creating new user in list ***************");
			String jsonBody = Utilities.generateStringFromResource(
					System.getProperty("user.dir") + "\\src\\main\\resources\\Payload.Json");
			String responseBody = RestAssured.given().spec(requestSpecification).body(jsonBody).post("/users/")
					.getBody().asPrettyString();
			System.out.print(responseBody);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	// ============================ Delete method to Delete existing user
	// =============================

	@Test(dependsOnMethods = { "creatingNewUser" })
	public void deletingExistingUser() {

		try {

			System.out.println("************* Deleting existing user from list ***************");
			String responseBody = RestAssured.given().spec(requestSpecification).delete("/users/9").getStatusLine();
			System.out.print(responseBody);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}
}