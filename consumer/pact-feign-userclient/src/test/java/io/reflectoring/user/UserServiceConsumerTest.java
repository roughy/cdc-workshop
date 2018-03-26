package io.reflectoring.user;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        "userservice.ribbon.listOfServers: localhost:8888"
})
public class UserServiceConsumerTest {
    private static final String PROVIDER = "marco.userservice";
    private static final String CONSUMER = "marco.userclient";

    @Rule
    public PactProviderRuleMk2 stubProvider = new PactProviderRuleMk2(PROVIDER, "localhost", 8888, this);

    @Autowired
    private UserClient userClient;

    @Pact(provider = PROVIDER, consumer = CONSUMER)
    public RequestResponsePact createPersonPact(PactDslWithProvider builder) {
        return builder
                .given("provider accepts a new person")
                .uponReceiving("a request to POST a person")
                .path("/user-service/users")
                .method("POST")
                .body(new PactDslJsonBody()
                        .nullValue("id")
                        .stringValue("firstName", "Zaphod")
                        .stringValue("lastName", "Beeblebrox"))
                .willRespondWith()
                .status(201)
                .body(new PactDslJsonBody()
                        .numberValue("id", 42)
                        .integerType ("id", 42))
                .toPact();
    }

    @Test
    @PactVerification(fragment = "createPersonPact")
    public void verifyCreatePersonPact() {
        User user = new User();
        user.setFirstName("Zaphod");
        user.setLastName("Beeblebrox");
        IdObject id = userClient.createUser(user);
        assertThat(id.getId()).isEqualTo(42);
    }
}
