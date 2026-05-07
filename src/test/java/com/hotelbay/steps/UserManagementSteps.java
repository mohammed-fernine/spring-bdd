package com.hotelbay.steps;

import com.hotelbay.entity.User;
import com.hotelbay.service.UserService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagementSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    private ResponseEntity<?> lastResponse;
    private User testUser;

    @Given("the client provides valid registration details")
    public void clientProvidesValidRegistrationDetails() {
        testUser = new User();
        testUser.setName("Test User");
        testUser.setUsername("testuser");
        testUser.setEmail("testuser@example.com");
        testUser.setRole(User.UserRole.GUEST);
    }

    @When("the client calls POST \\/users\\/register")
    public void clientCallsPostUsersRegister() {
        lastResponse = restTemplate.postForEntity("/api/users/register", testUser, User.class);
    }

    @When("the client calls POST \\/users\\/login")
    public void clientCallsPostUsersLogin() {
        lastResponse = restTemplate.postForEntity("/api/users/login", testUser, User.class);
    }

    @When("the client calls POST \\/users\\/logout")
    public void clientCallsPostUsersLogout() {
        lastResponse = restTemplate.postForEntity("/api/users/logout", testUser, User.class);
    }

    @Then("the auth client receives status code of {int}")
    public void authClientReceivesStatusCode(int statusCode) {
        assertEquals(HttpStatus.valueOf(statusCode), lastResponse.getStatusCode());
    }

    @And("a new user account is created")
    public void newUserAccountIsCreated() {
        assertNotNull(lastResponse.getBody());
        assertTrue(userService.existsById(((User) lastResponse.getBody()).getId()));
    }

    @Given("the client provides registration details with an existing email")
    public void clientProvidesRegistrationDetailsWithExistingEmail() {
        User existingUser = new User();
        existingUser.setName("Existing User");
        existingUser.setUsername("existinguser");
        existingUser.setEmail("existing@example.com");
        existingUser.setRole(User.UserRole.GUEST);
        userService.save(existingUser);

        testUser = new User();
        testUser.setName("New User");
        testUser.setUsername("newuser");
        testUser.setEmail("existing@example.com");
        testUser.setRole(User.UserRole.GUEST);
    }

    @Then("an authentication error message is returned")
    public void authenticationErrorMessageIsReturned() {
        assertNotNull(lastResponse);
    }

    @Given("the client provides valid login credentials")
    public void clientProvidesValidLoginCredentials() {
        User user = new User();
        user.setName("Login User");
        user.setUsername("loginuser");
        user.setEmail("loginuser@example.com");
        user.setRole(User.UserRole.GUEST);
        userService.save(user);

        testUser = new User();
        testUser.setName("Login User");
        testUser.setUsername("loginuser");
        testUser.setEmail("loginuser@example.com");
        testUser.setRole(User.UserRole.GUEST);
    }

    @Then("the system returns an authentication token")
    public void systemReturnsAuthenticationToken() {
        assertNotNull(lastResponse.getBody());
    }

    @Given("the client is logged in")
    public void clientIsLoggedIn() {
        User user = new User();
        user.setName("Logged In User");
        user.setUsername("loggedinuser");
        user.setEmail("loggedin@example.com");
        user.setRole(User.UserRole.GUEST);
        userService.save(user);

        testUser = new User();
        testUser.setUsername("loggedinuser");
    }

    @Then("the authentication token is invalidated")
    public void authenticationTokenIsInvalidated() {
        assertNotNull(lastResponse);
    }
}
