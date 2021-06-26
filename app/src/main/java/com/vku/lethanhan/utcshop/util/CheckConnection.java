package com.vku.lethanhan.utcshop.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.vku.lethanhan.utcshop.activity.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class CheckConnection extends AsyncTask<Void, Void, Void> {
    MainActivity mainActivity;
    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    Timer timer;

    public CheckConnection(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        connMgr = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        timer = new Timer();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (networkInfo != null && networkInfo.isConnected()) {

                } else {
                    publishProgress();
                }
            }
        }, 1000, 10000);

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        mainActivity.showToast("Vui lòng kiểm tra kết nối mạng");
    }

}
