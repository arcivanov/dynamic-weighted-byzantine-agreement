import java.util.List;
import java.net.InetSocketAddress;
import java.util.Random;
import static org.junit.Assert.*;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class TESTMultipleClientIntegration {
    Coordinator coordinator;
    List<InetSocketAddress> serverList;

    @BeforeClass
    public void setUp() throws Exception {
        serverList = Utils.createServerList(9001, 2);
        coordinator = Utils.setupNewTestServer();
    }

    @AfterClass
    public void tearDown() throws Exception {
        coordinator.shutDown();

    }

    @org.testng.annotations.Test(threadPoolSize = 5, invocationCount = 5,  timeOut = 100)
    public void validPlacements() {
        Random rand = new Random();
        int n = rand.nextInt(1000);
        Client client = new Client(serverList);

        String publisher = "cnn.com";
        String expectedResponse = client.responses.get("True");
        String response = validatePlacement(client, publisher);

        assertEquals(expectedResponse, response);
    }

    public String validatePlacement(Client client, String name){
        return client.getResponse(client.validatePlacement(name));
    }
}
