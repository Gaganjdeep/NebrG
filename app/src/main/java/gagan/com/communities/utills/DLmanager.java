package gagan.com.communities.utills;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

/**
 * Created by sony on 31-03-2016.
 */
public class DLmanager
{

    public static void useDownloadManager(String url, String name, Context c) {
        DownloadManager dm = (DownloadManager) c
                .getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request dlrequest = new DownloadManager.Request(
                Uri.parse(url));
        dlrequest
                .setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI
                                | DownloadManager.Request.NETWORK_MOBILE)
                .setTitle("Neibr")
                .setDescription("Saving image..")
                .setDestinationInExternalPublicDir("Neibr", name + ".jpg")
                .allowScanningByMediaScanner();

        dm.enqueue(dlrequest);

    }
}

