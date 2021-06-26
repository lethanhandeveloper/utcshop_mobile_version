package com.vku.lethanhan.utcshop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.data_local.AccessTokenManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new SplashAsyncTask(this).execute();
    }


    public class SplashAsyncTask extends AsyncTask<Void, Integer, Void> {
        SplashActivity splashActivity;

        public SplashAsyncTask(SplashActivity splashActivity) {
            this.splashActivity = splashActivity;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            new AccessTokenManager(SplashActivity.this.getApplicationContext());

            for (int i=1; i<=5; i++){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                publishProgress(i*20);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startActivity(new Intent(splashActivity, MainActivity.class));
        }
    }
}