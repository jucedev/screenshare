public class Client {

    private Client connectedClient;
    private Client callingClient;

    public void call(Client client) {
        client.callingClient = this;
    }

    public void accept(Client client) {
        if (client != callingClient) return;

        client.onConnect(this);
        connectedClient = client;
        callingClient = null;
    }

    public void onConnect(Client client) {
        connectedClient = client;
    }

    public void decline(Client client) {
        if (client != callingClient) return;

        connectedClient = null;
        callingClient = null;
    }

    public boolean isConnected(Client client) {
        return client == connectedClient;
    }
}
