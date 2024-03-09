import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {

    private Client connectedClient;
    private Client callingClient;
    public int receivedFrames;

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

    public void sendFrame() throws IOException {
        if (connectedClient == null) return;

        byte[] imageData = getImageData();
        sendImage(imageData);

        connectedClient.onFrame();
    }

    public void onFrame() {
        receivedFrames++;
    }

    private static void sendImage(byte[] imageData) throws IOException {
        try (DatagramSocket socket = new DatagramSocket()) {

            // Send the image data
            String receiverHost = "localhost";
            int receiverPort = 9001;
            InetAddress receiverAddress = InetAddress.getByName(receiverHost);

            int maxPacketSize = 1460;
            int offset = 0;

            while (offset < imageData.length) {
                int size = Math.min(maxPacketSize, imageData.length - offset);
                byte[] packetData = new byte[size];

                System.arraycopy(imageData, offset, packetData, 0, size);

                DatagramPacket packet = new DatagramPacket(packetData, size, receiverAddress, receiverPort);
                socket.send(packet);

                offset += size;
            }
        }
    }

    private static byte[] getImageData() throws IOException {
        byte[] imageData;
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenshot = robot.createScreenCapture(screenRect);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(screenshot, "jpg", baos);
        baos.flush();
        imageData = baos.toByteArray();
        baos.close();
        return imageData;
    }
}
