package com.algaworks.algashop.ordering.presentation;

import com.algaworks.algashop.ordering.application.commons.AddressData;
import com.algaworks.algashop.ordering.application.customer.management.CustomerInput;
import com.algaworks.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.algaworks.algashop.ordering.application.customer.query.*;
import com.algaworks.algashop.ordering.domain.model.DomainException;
import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerEmailIsInUseException;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private CustomerManagementApplicationService customerManagementApplicationService;

    @MockitoBean
    private CustomerQueryService customerQueryService;

    @BeforeEach
    public void setupAll() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build());
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    public void createCustomerContract() {
        CustomerOutput customerOutput = CustomerOutputTestDataBuilder.existing().build();
        UUID customerId = UUID.randomUUID();
        when(customerManagementApplicationService.create(any(CustomerInput.class)))
                .thenReturn(customerId);
        when(customerQueryService.findById(any(UUID.class)))
                .thenReturn(customerOutput);

        String jsonInput = """
        {
          "firstName": "John",
          "lastName": "Doe",
          "email": "johndoe@email.com",
          "document": "12345",
          "phone": "1191234564",
          "birthDate": "1991-07-05",
          "promotionNotificationsAllowed": false,
          "address": {
            "street": "Bourbon Street",
            "number": "2000",
            "complement": "apt 122",
            "neighborhood": "North Ville",
            "city": "Yostfort",
            "state": "South Carolina",
            "zipCode": "12321"
          }
        }
        """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/v1/customers")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.CREATED.value())
                .header("Location",Matchers.containsString("/api/v1/customers/"+customerId.toString()))
                .body(
                        "id", Matchers.notNullValue(),
                        "registeredAt", Matchers.notNullValue(),
                        "firstName", Matchers.is("John"),
                        "lastName", Matchers.is("Doe"),
                        "email", Matchers.is("johndoe@email.com"),
                        "document", Matchers.is("12345"),
                        "phone", Matchers.is("1191234564"),
                        "birthDate", Matchers.is("1991-07-05"),
                        "promotionNotificationsAllowed", Matchers.is(false),
                        "loyaltyPoints", Matchers.is(0),
                        "address.street", Matchers.is("Bourbon Street"),
                        "address.number", Matchers.is("2000"),
                        "address.complement", Matchers.is("apt 122"),
                        "address.neighborhood", Matchers.is("North Ville"),
                        "address.city", Matchers.is("Yostfort"),
                        "address.state", Matchers.is("South Carolina"),
                        "address.zipCode", Matchers.is("12321")
                );
    }

    @Test
    public void createCustomerError400Contract() {
        String jsonInput = """
        {
          "firstName": "",
          "lastName": "",
          "email": "johndoe@email.com",
          "document": "12345",
          "phone": "1191234564",
          "birthDate": "1991-07-05",
          "promotionNotificationsAllowed": false,
          "address": {
            "street": "Bourbon Street",
            "number": "2000",
            "complement": "apt 122",
            "neighborhood": "North Ville",
            "city": "Yostfort",
            "state": "South Carolina",
            "zipCode": "12321"
          }
        }
        """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonInput)
                .when()
                .post("/api/v1/customers")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        "status", Matchers.is(HttpStatus.BAD_REQUEST.value()),
                        "type", Matchers.is("/errors/invalid-fields"),
                        "title", Matchers.notNullValue(),
                        "detail", Matchers.notNullValue(),
                        "instance", Matchers.notNullValue(),
                        "fields", Matchers.notNullValue()
                );

    }

    @Test
    public void findCustomerContract() {
        int sizeLimit = 2;
        int pageNumber = 0;

        CustomerSummaryOutput customer1 = CustomerSummaryOutputTestDataBuilder.existing().build();
        CustomerSummaryOutput customer2 = CustomerSummaryOutputTestDataBuilder.existingAlt1().build();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        when(customerQueryService.filter(any(CustomerFilter.class)))
                .thenReturn(new PageImpl<>(List.of(customer1, customer2)));

        RestAssuredMockMvc
                .given()
                  .accept(MediaType.APPLICATION_JSON)
                  .queryParam("size", sizeLimit)
                  .queryParam("page", pageNumber)
                .when()
                  .get("api/v1/customers")
                .then()
                  .assertThat()
                  .contentType(MediaType.APPLICATION_JSON_VALUE)
                  .statusCode(HttpStatus.OK.value())
                  .body(
                          "number", Matchers.equalTo(pageNumber),
                          "size", Matchers.equalTo(sizeLimit),
                          "totalPages", Matchers.equalTo(1),
                          "totalElements", Matchers.equalTo(2),
                          "content[0].id", Matchers.equalTo(customer1.getId().toString()),
                          "content[0].firstName", Matchers.equalTo(customer1.getFirstName()),
                          "content[0].lastName", Matchers.equalTo(customer1.getLastName()),
                          "content[0].email", Matchers.equalTo(customer1.getEmail()),
                          "content[0].document", Matchers.equalTo(customer1.getDocument()),
                          "content[0].phone", Matchers.equalTo(customer1.getPhone()),
                          "content[0].birthDate", Matchers.equalTo(customer1.getBirthDate().toString()),
                          "content[0].loyaltyPoints", Matchers.equalTo(customer1.getLoyaltyPoints()),
                          "content[0].promotionNotificationsAllowed", Matchers.equalTo(customer1.getPromotionNotificationsAllowed()),
                          "content[0].archived", Matchers.equalTo(customer1.getArchived()),
                          "content[0].registeredAt", Matchers.equalTo(formatter.format(customer1.getRegisteredAt())),

                          "content[1].id", Matchers.equalTo(customer2.getId().toString()),
                          "content[1].firstName", Matchers.equalTo(customer2.getFirstName()),
                          "content[1].lastName", Matchers.equalTo(customer2.getLastName()),
                          "content[1].email", Matchers.equalTo(customer2.getEmail()),
                          "content[1].document", Matchers.equalTo(customer2.getDocument()),
                          "content[1].phone", Matchers.equalTo(customer2.getPhone()),
                          "content[1].birthDate", Matchers.equalTo(customer2.getBirthDate().toString()),
                          "content[1].loyaltyPoints", Matchers.equalTo(customer2.getLoyaltyPoints()),
                          "content[1].promotionNotificationsAllowed", Matchers.equalTo(customer2.getPromotionNotificationsAllowed()),
                          "content[1].archived", Matchers.equalTo(customer1.getArchived()),
                          "content[1].registeredAt", Matchers.equalTo(formatter.format(customer2.getRegisteredAt()))
                  );

    }

    @Test
    void findByIdContract() {
        CustomerOutput customer1 = CustomerOutputTestDataBuilder.existing().build();
        AddressData address = customer1.getAddress();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        when(customerQueryService.findById(any())).thenReturn(customer1);

        RestAssuredMockMvc
                .given()
                  .accept(MediaType.APPLICATION_JSON)
                .when()
                  .get("/api/v1/customers/{customerId}", customer1.getId())
                .then()
                  .assertThat()
                  .contentType(MediaType.APPLICATION_JSON_VALUE)
                  .statusCode(HttpStatus.OK.value())
                  .body(
                          "id", Matchers.equalTo(customer1.getId().toString()),
                          "firstName", Matchers.equalTo(customer1.getFirstName()),
                          "lastName", Matchers.equalTo(customer1.getLastName()),
                          "email", Matchers.equalTo(customer1.getEmail()),
                          "document", Matchers.equalTo(customer1.getDocument()),
                          "birthDate", Matchers.equalTo(customer1.getBirthDate().toString()),
                          "loyaltyPoints",Matchers.equalTo(customer1.getLoyaltyPoints()),
                          "promotionNotificationsAllowed", Matchers.equalTo(customer1.getPromotionNotificationsAllowed()),
                          "archived", Matchers.equalTo(customer1.getArchived()),
                          "registeredAt",  Matchers.equalTo(formatter.format(customer1.getRegisteredAt())),


                          "address.street", Matchers.equalTo(address.getStreet()),
                          "address.number", Matchers.equalTo(address.getNumber()),
                          "address.neighborhood", Matchers.equalTo(address.getNeighborhood()),
                          "address.complement", Matchers.equalTo(address.getComplement()),
                          "address.city", Matchers.equalTo(address.getCity()),
                          "address.state", Matchers.equalTo(address.getState()),
                          "address.zipCode", Matchers.equalTo(address.getZipCode())
                  );
    }

    @Test
    void findByIdError404Contract() {
        UUID invalidCustomer = UUID.randomUUID();
        when(customerQueryService.findById(invalidCustomer)).thenThrow(CustomerNotFoundException.class);

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .when()
                .get("/api/v1/customers/{customerId}", invalidCustomer)
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(
                        "status", Matchers.is(HttpStatus.NOT_FOUND.value()),
                        "type",Matchers.is("/errors/not-found"),
                        "title",Matchers.notNullValue(),
                        "instance",Matchers.notNullValue()
                );
    }

    @Test
    public void createCustomerError409Contract() {
        when(customerQueryService.findById(any())).thenThrow(CustomerEmailIsInUseException.class);

        String jsonInput = """
        {
          "firstName": "John",
          "lastName": "Doe",
          "email": "johndoe@email.com",
          "document": "12345",
          "phone": "1191234564",
          "birthDate": "1991-07-05",
          "promotionNotificationsAllowed": false,
          "address": {
            "street": "Bourbon Street",
            "number": "2000",
            "complement": "apt 122",
            "neighborhood": "North Ville",
            "city": "Yostfort",
            "state": "South Carolina",
            "zipCode": "12321"
          }
        }
        """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/v1/customers")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.CONFLICT.value())
                .body(
                        "status", Matchers.is(HttpStatus.CONFLICT.value()),
                        "type",Matchers.is("/errors/conflict"),
                        "title",Matchers.notNullValue(),
                        "instance",Matchers.notNullValue()
                );

    }

    @Test
    public void createCustomerError422Contract() {
        when(customerQueryService.findById(any())).thenThrow(DomainException.class);

        String jsonInput = """
        {
          "firstName": "John",
          "lastName": "Doe",
          "email": "johndoe@email.com",
          "document": "12345",
          "phone": "1191234564",
          "birthDate": "1991-07-05",
          "promotionNotificationsAllowed": false,
          "address": {
            "street": "Bourbon Street",
            "number": "2000",
            "complement": "apt 122",
            "neighborhood": "North Ville",
            "city": "Yostfort",
            "state": "South Carolina",
            "zipCode": "12321"
          }
        }
        """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/v1/customers")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body(
                        "status", Matchers.is(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                        "type",Matchers.is("/errors/unprocessable-entity"),
                        "title",Matchers.notNullValue(),
                        "instance",Matchers.notNullValue()
                );
    }

    @Test
    public void createCustomerError500Contract() {
        when(customerQueryService.findById(any())).thenThrow(RuntimeException.class);

        String jsonInput = """
        {
          "firstName": "John",
          "lastName": "Doe",
          "email": "johndoe@email.com",
          "document": "12345",
          "phone": "1191234564",
          "birthDate": "1991-07-05",
          "promotionNotificationsAllowed": false,
          "address": {
            "street": "Bourbon Street",
            "number": "2000",
            "complement": "apt 122",
            "neighborhood": "North Ville",
            "city": "Yostfort",
            "state": "South Carolina",
            "zipCode": "12321"
          }
        }
        """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/v1/customers")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(
                        "status", Matchers.is(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                        "type",Matchers.is("/errors/internal"),
                        "title",Matchers.notNullValue(),
                        "instance",Matchers.notNullValue()
                );
    }

    @Test
    void DeleteCustomerContract() {
        UUID customerId = UUID.randomUUID();
        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/v1/customers/{customerId}", customerId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

    }

    @Test
    void deleteCustomerError422Contract() {
        UUID customerId = UUID.randomUUID();
        when(customerQueryService.findById(any())).thenThrow(DomainException.class);

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/v1/customers/{customerId}", customerId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body(
                        "status", Matchers.is(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                        "type",Matchers.is("/errors/unprocessable-entity"),
                        "title",Matchers.notNullValue(),
                        "instance",Matchers.notNullValue()
                );

    }

    @Test
    void updateCustomerContract() {
        CustomerOutput customerOutput = CustomerOutputTestDataBuilder.existing().build();
        UUID customerId = UUID.randomUUID();
        when(customerQueryService.findById(any(UUID.class)))
                .thenReturn(customerOutput);
        String jsonInput = """
        {
          "firstName": "",
          "lastName": "",
          "email": "johndoe@email.com",
          "document": "12345",
          "phone": "1191234564",
          "birthDate": "1991-07-05",
          "promotionNotificationsAllowed": false,
          "address": {
            "street": "Bourbon Street",
            "number": "2000",
            "complement": "apt 122",
            "neighborhood": "North Ville",
            "city": "Yostfort",
            "state": "South Carolina",
            "zipCode": "12321"
          }
        }
        """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/api/v1/customers/{customerId}",customerId)
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.OK.value())
                .body(
                        "id", Matchers.notNullValue(),
                        "registeredAt", Matchers.notNullValue(),
                        "firstName", Matchers.is("John"),
                        "lastName", Matchers.is("Doe"),
                        "email", Matchers.is("johndoe@email.com"),
                        "document", Matchers.is("12345"),
                        "phone", Matchers.is("1191234564"),
                        "birthDate", Matchers.is("1991-07-05"),
                        "promotionNotificationsAllowed", Matchers.is(false),
                        "loyaltyPoints", Matchers.is(0),
                        "address.street", Matchers.is("Bourbon Street"),
                        "address.number", Matchers.is("2000"),
                        "address.complement", Matchers.is("apt 122"),
                        "address.neighborhood", Matchers.is("North Ville"),
                        "address.city", Matchers.is("Yostfort"),
                        "address.state", Matchers.is("South Carolina"),
                        "address.zipCode", Matchers.is("12321")
                );
    }

    @Test
    public void updateCustomerError400Contract() {
        UUID customerId = UUID.randomUUID();
        String jsonInput = """
        {
          "firstName": "",
          "lastName": "",
          "email": "johndoe@email.com",
          "document": "12345",
          "phone": "1191234564",
          "birthDate": "1991-07-05",
          "promotionNotificationsAllowed": false,
          "address": {
            "street": "Bourbon Street",
            "number": "2000",
            "complement": "apt 122",
            "neighborhood": "North Ville",
            "city": "Yostfort",
            "state": "South Carolina",
            "zipCode": "12321"
          }
        }
        """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonInput)
                .when()
                .put("/api/v1/customers/{customerId}",customerId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        "status", Matchers.is(HttpStatus.BAD_REQUEST.value()),
                        "type", Matchers.is("/errors/invalid-fields"),
                        "title", Matchers.notNullValue(),
                        "detail", Matchers.notNullValue(),
                        "instance", Matchers.notNullValue(),
                        "fields", Matchers.notNullValue()
                );

    }

    @Test
    void updateCustomer404Contract() {
        UUID customerId = UUID.randomUUID();
        when(customerQueryService.findById(any(UUID.class))).thenThrow(CustomerNotFoundException.class);
        String jsonInput = """
        {
          "firstName": "John",
          "lastName": "Doe",
          "email": "johndoe@email.com",
          "document": "12345",
          "phone": "1191234564",
          "birthDate": "1991-07-05",
          "promotionNotificationsAllowed": false,
          "address": {
            "street": "Bourbon Street",
            "number": "2000",
            "complement": "apt 122",
            "neighborhood": "North Ville",
            "city": "Yostfort",
            "state": "South Carolina",
            "zipCode": "12321"
          }
        }
        """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/api/v1/customers/{customerId}",customerId)
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(
                        "status", Matchers.is(HttpStatus.NOT_FOUND.value()),
                        "type",Matchers.is("/errors/not-found"),
                        "title",Matchers.notNullValue(),
                        "instance",Matchers.notNullValue()
                );
    }

    @Test
    public void updateCustomerError409Contract() {
        UUID customerId = UUID.randomUUID();
        when(customerQueryService.findById(any())).thenThrow(CustomerEmailIsInUseException.class);

        String jsonInput = """
        {
          "firstName": "John",
          "lastName": "Doe",
          "email": "johndoe@email.com",
          "document": "12345",
          "phone": "1191234564",
          "birthDate": "1991-07-05",
          "promotionNotificationsAllowed": false,
          "address": {
            "street": "Bourbon Street",
            "number": "2000",
            "complement": "apt 122",
            "neighborhood": "North Ville",
            "city": "Yostfort",
            "state": "South Carolina",
            "zipCode": "12321"
          }
        }
        """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/api/v1/customers/{customerId}",customerId)
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.CONFLICT.value())
                .body(
                        "status", Matchers.is(HttpStatus.CONFLICT.value()),
                        "type",Matchers.is("/errors/conflict"),
                        "title",Matchers.notNullValue(),
                        "instance",Matchers.notNullValue()
                );

    }

    @Test
    public void updateCustomerError422Contract() {
        UUID customerId = UUID.randomUUID();
        when(customerQueryService.findById(any())).thenThrow(DomainException.class);

        String jsonInput = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "johndoe@email.com",
                  "document": "12345",
                  "phone": "1191234564",
                  "birthDate": "1991-07-05",
                  "promotionNotificationsAllowed": false,
                  "address": {
                    "street": "Bourbon Street",
                    "number": "2000",
                    "complement": "apt 122",
                    "neighborhood": "North Ville",
                    "city": "Yostfort",
                    "state": "South Carolina",
                    "zipCode": "12321"
                  }
                }
                """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/api/v1/customers/{customerId}",customerId)
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body(
                        "status", Matchers.is(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                        "type", Matchers.is("/errors/unprocessable-entity"),
                        "title", Matchers.notNullValue(),
                        "instance", Matchers.notNullValue()
                );
    }

    @Test
    public void updateCustomerError500Contract() {
        UUID customerId = UUID.randomUUID();
        when(customerQueryService.findById(any())).thenThrow(RuntimeException.class);

        String jsonInput = """
        {
          "firstName": "John",
          "lastName": "Doe",
          "email": "johndoe@email.com",
          "document": "12345",
          "phone": "1191234564",
          "birthDate": "1991-07-05",
          "promotionNotificationsAllowed": false,
          "address": {
            "street": "Bourbon Street",
            "number": "2000",
            "complement": "apt 122",
            "neighborhood": "North Ville",
            "city": "Yostfort",
            "state": "South Carolina",
            "zipCode": "12321"
          }
        }
        """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/api/v1/customers/{customerId}",customerId)
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(
                        "status", Matchers.is(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                        "type",Matchers.is("/errors/internal"),
                        "title",Matchers.notNullValue(),
                        "instance",Matchers.notNullValue()
                );
    }

}