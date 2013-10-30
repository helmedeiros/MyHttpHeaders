import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Auxiliary class to download information.
 * User: helmedeiros
 * Date: 10/30/13
 * Time: 1:10 PM
 */
public class RemoteDownload {

    public boolean download(final String url) {
        HttpURLConnection con = null;
        StringWriter sw = new StringWriter();
        try {
            PrintWriter pw = new PrintWriter(sw);
            HttpURLConnection.setFollowRedirects(false);
            con = (HttpURLConnection) new URL(url).openConnection();

            con.connect();

            InputStream in = con.getInputStream();

            int lines = 0;
            int size = 0;
            while ((lines = in.read()) != -1) {
                size++;
            }

            pw.println();
            System.out.println("Done for " + Thread.currentThread().getName() + " read " + size);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (con.getResponseCode() == 400) {
                    System.out.println(con.getURL());
                    System.exit(1);
                }
                System.out.print(new StringBuilder().append("fail for \"").append(url).append("\" with status: ").append(con.getResponseCode()).toString());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (con != null)
                con.disconnect();
        }
        return false;
    }

}
