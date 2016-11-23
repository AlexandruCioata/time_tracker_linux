package communication;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.awt.image.BufferedImage;
import java.io.*;

public class EchoServer {
    public static void main(String[] arstring) {

        System.setProperty("javax.net.ssl.keyStore", "mySrvKeystore");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");

        try {
            SSLServerSocketFactory sslserversocketfactory =
                    (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslserversocket =
                    (SSLServerSocket) sslserversocketfactory.createServerSocket(9999);
            SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();

            ObjectInputStream is = new ObjectInputStream(sslsocket.getInputStream());
            GlobalDataCollector message = (GlobalDataCollector)is.readObject();

            byte[] takenScreenshot = message.getTakenScreenshotAsByteArray();

            // convert byte array back to BufferedImage
            InputStream in = new ByteArrayInputStream(takenScreenshot);
            BufferedImage bImageFromConvert = ImageIO.read(in);

            ImageIO.write(bImageFromConvert, "jpg", new File(
                    "photo.jpg"));

            System.out.println("Message received!");
            System.out.println(message.toString());
            System.out.println("Exit...");

            Thread.sleep(10000);

          /*  InputStream inputstream = sslsocket.getInputStream();
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

            String string = null;
            while ((string = bufferedreader.readLine()) != null) {
                System.out.println(string);
                System.out.flush();
            }*/
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}