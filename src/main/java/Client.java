public class Client {

    private Client connectedClient;

    public void call(Client client) {
    }

    public void accept(Client client) {
        client.onConnect(this);
        connectedClient = client;
    }

    public void onConnect(Client client) {
        connectedClient = client;
    }

    public void decline(Client client) {
        connectedClient = null;
    }

    public boolean isConnected(Client client) {
        return client == connectedClient;
    }
}
