import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit test of {@link RemoteDownload}
 * User: helmedeiros
 * Date: 10/30/13
 * Time: 1:17 PM
 */
public class RemoteDownloadTest {

    @Test public void shouldDownloadFromANExistentURL() throws Exception {
        RemoteDownload remoteDownload = new RemoteDownload();
        assertTrue(remoteDownload.download("http://static.zoom.com.br/PARTNER_MEDIA_PORTAL/click_rbs_horizontal_4_32.xml"));
    }
}
