package com.ardi.kamus.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.ardi.kamus.R;
import com.ardi.kamus.db.KamusHelper;
import com.ardi.kamus.entity.Kamus;
import com.ardi.kamus.preference.KamusPreference;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreLoadActivity extends AppCompatActivity {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_load);
        ButterKnife.bind(this);
        new LoadData().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadData extends AsyncTask<Void, Integer, Void>{
        KamusHelper kamusHelper;
        KamusPreference kamusPreference;
        double progress;
        double maxprogress = 100;

        @Override
        protected void onPreExecute(){
            kamusHelper = new KamusHelper(PreLoadActivity.this);
            kamusPreference = new KamusPreference(PreLoadActivity.this);
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            Boolean firstRun = kamusPreference.getFirstRun();
            if (firstRun){
                ArrayList<Kamus> kamusEnglish = preLoadRaw(R.raw.english_indonesia);
                ArrayList<Kamus> kamusIndonesia = preLoadRaw(R.raw.indonesia_english);

                publishProgress((int) progress);

                try {
                    kamusHelper.open();
                }catch (SQLException e){
                    e.printStackTrace();
                }

                Double progressMaxInsert = 100.0;
                Double progressDiff = (progressMaxInsert - progress) / (kamusEnglish.size() + kamusIndonesia.size());

                kamusHelper.insertTransaction(kamusEnglish, true);
                progress += progressDiff;
                publishProgress((int) progress);

                kamusHelper.insertTransaction(kamusIndonesia, false);
                progress += progressDiff;
                publishProgress((int) progress);

                kamusHelper.close();
                kamusPreference.setFirstRun(false);

                publishProgress((int) maxprogress);
            }else {
                try {
                    synchronized (this){
                        this.wait(1000);
                        publishProgress(50);

                        this.wait(300);
                        publishProgress((int) maxprogress);
                    }
                }catch (Exception ignored){
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result){
            Intent i = new Intent(PreLoadActivity.this, MainActivity.class);
            startActivity(i);

            finish();
        }
    }

    public ArrayList<Kamus> preLoadRaw(int selection){
        ArrayList<Kamus> kamuses = new ArrayList<>();
        BufferedReader reader;
        try {
            Resources res = getResources();
            InputStream raw_dict = res.openRawResource(selection);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            String line;
                do {
                line = reader.readLine();
                String[] splits = line.split("\t");
                Kamus kamus;
                kamus = new Kamus(splits[0], splits[1]);
                kamuses.add(kamus);
            }while (true);
        }catch (Exception e){
            e.printStackTrace();
        }

        return kamuses;
    }
}
