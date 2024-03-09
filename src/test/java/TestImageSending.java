import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestImageSending {
    @Test
    void clientCanRejectConnection() {
        var client1 = new Client();
        var client2 = new Client();

        client1.call(client2);
        client2.decline(client1);

        assertFalse(client1.isConnected(client2));
        assertFalse(client2.isConnected(client1));
    }

    @Test
    void clientCanAcceptConnection() {
        var client1 = new Client();
        var client2 = new Client();
        var client3 = new Client();

        client1.call(client2);
        client2.accept(client1);

        assertTrue(client2.isConnected(client1));
        assertTrue(client1.isConnected(client2));
        assertFalse(client1.isConnected(client3));
    }

    @Test
    void clientCannotAcceptNonCallingClient() {
        var client1 = new Client();
        var client2 = new Client();

        client2.accept(client1);

        assertFalse(client2.isConnected(client1));
    }

    @Test
    void clientCannotAcceptCallAfterDeclining() {
        var client1 = new Client();
        var client2 = new Client();

        client1.call(client2);
        client2.decline(client1);
        client2.accept(client1);

        assertFalse(client2.isConnected(client1));
    }

    @Test
    void clientCannotDeclineCallAfterAccepting() {
        var client1 = new Client();
        var client2 = new Client();

        client1.call(client2);
        client2.accept(client1);
        client2.decline(client1);

        assertTrue(client2.isConnected(client1));
    }
}
