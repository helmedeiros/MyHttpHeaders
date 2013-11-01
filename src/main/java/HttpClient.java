import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Auxiliary class to execute information.
 * User: helmedeiros
 * Date: 10/30/13
 * Time: 1:10 PM
 */
public class HttpClient {

    public static final int BAD_REQUEST = 400;

    private final String url;
    private Map<String, List<String>> headerFields;

    public HttpClient(String url) {
        this.url = url;
    }

    public boolean execute() {
        HttpURLConnection con = null;
        StringWriter sw = new StringWriter();
        try {
            HttpURLConnection.setFollowRedirects(false);

            con = (HttpURLConnection) new URL(this.url).openConnection();
            con.connect();

            int size = checkNumberOfLines(con.getInputStream());
            this.headerFields = con.getHeaderFields();
            printHeaders();

            PrintWriter pw = new PrintWriter(sw);
            pw.println();

            printSuccess(size);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            dealWithErrors(this.url, con);
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

    private void printHeaders() {
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
        System.out.print("fail for \"" + url + "\" with status: " + con.getResponseCode());
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
