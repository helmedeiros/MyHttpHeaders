import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Auxiliary class to download information.
 * User: helmedeiros
 * Date: 10/30/13
 * Time: 1:10 PM
 */
public class RemoteDownload {

    public static final int BAD_REQUEST = 400;

    public boolean download(final String url) {
        HttpURLConnection con = null;
        StringWriter sw = new StringWriter();
        try {
            HttpURLConnection.setFollowRedirects(false);

            con = (HttpURLConnection) new URL(url).openConnection();
            con.connect();

            int size = checkNumberOfLines(con.getInputStream());
            getAndPrintHeaders(con.getHeaderFields());

            PrintWriter pw = new PrintWriter(sw);
            pw.println();

            printSuccess(size);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            dealWithErrors(url, con);
        } finally {
            if (con != null)
                con.disconnect();
        }
        return false;
    }

    private int checkNumberOfLines(InputStream in) throws IOException {
        int lines = 0;
        int size = 0;
        while ((lines = in.read()) != -1) {
            size++;
        }
        return size;
    }

    private void getAndPrintHeaders(Map<String, List<String>> headerFields) {
        for (Map.Entry entry : headerFields.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
    }

    private void printSuccess(int size) {
        System.out.println("Done for " + Thread.currentThread().getName() + " read " + size);
    }

    private void dealWithErrors(String url, HttpURLConnection con) {
        try {
            stopTryingWhenBadRequestIn(con);
            printFail(url, con);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void printFail(String url, HttpURLConnection con) throws IOException {
        System.out.print(new StringBuilder().append("fail for \"").append(url).append("\" with status: ").append(con.getResponseCode()).toString());
    }

    private void stopTryingWhenBadRequestIn(HttpURLConnection con) throws IOException {
        if (respondBadRequest(con)) {
            System.out.println(con.getURL());
            System.exit(1);
        }
    }

    private boolean respondBadRequest(HttpURLConnection con) throws IOException {
        return con.getResponseCode() == BAD_REQUEST;
    }

}
