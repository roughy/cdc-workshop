package io.reflectoring.user;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import io.reflectoring.User;
import io.reflectoring.UserRepository;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRestPactRunner.class)
@Provider("marco.userservice")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"server.port=8080"}
)
@PactBroker(host = "HOST", port = "443", protocol = "https",
        authentication = @PactBrokerAuth(username = "USERNAME", password = "PASSWORD"))
public class UserControllerProviderTest {

    @TestTarget
    public final Target target = new HttpTarget(8080);

    @MockBean
    private UserRepository userRepository;

    @State({"provider accepts a new person"})
    public void toCreatePersonState() {
        User user = new User();
        user.setId(42L);
        // user.setFirstName("Arthur");
        // user.setLastName("Dent");
        // when(userRepository.findOne(eq(42L))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
    }

}
