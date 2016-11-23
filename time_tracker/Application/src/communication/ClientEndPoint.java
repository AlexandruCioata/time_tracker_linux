package communication;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.ObjectOutputStream;

/**
 * Created by admin on 11/23/16.
 */
public class ClientEndPoint {

    public static void main(String[] arstring) {

        System.setProperty("javax.net.ssl.trustStore", "mySrvKeystore");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");

        try {
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket("89.45.206.118", 9999);

            ObjectOutputStream os = new ObjectOutputStream(sslsocket.getOutputStream());

            System.out.println("Ok");
            GlobalDataCollector message = null;
            os.writeObject(message);
            System.out.println("Object sent!");


            Thread.sleep(10000);

           /* InputStream inputstream = System.in;
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

            OutputStream outputstream = sslsocket.getOutputStream();
            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
            BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);

            String string = null;
            while ((string = bufferedreader.readLine()) != null) {
                bufferedwriter.write(string + '\n');
                bufferedwriter.flush();
            }*/
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
