package com.sheygam.java_23_07_04_19_hw;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

import static com.sheygam.java_23_07_04_19_hw.DownloadWorker.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Handler.Callback{
    TextView countTxt, resultTxt, statusTxt;
    ProgressBar progressBar, horProgressBar;
    Button startBtn;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler(this);
        countTxt = findViewById(R.id.totalCountTxt);
        resultTxt = findViewById(R.id.resultTxt);
        statusTxt = findViewById(R.id.statusTxt);
        progressBar = findViewById(R.id.progressBar);
        horProgressBar = findViewById(R.id.horProgressBar);
        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.startBtn){
//            new Thread(new DownloadWorker(handler)).start();
            new DownloadTask().execute();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case STATUS_START:
                progressBar.setVisibility(View.VISIBLE);
                countTxt.setText("Count:");
                resultTxt.setVisibility(View.INVISIBLE);
                startBtn.setEnabled(false);
                statusTxt.setVisibility(View.VISIBLE);
                statusTxt.setText("Downloading...");
                horProgressBar.setVisibility(View.VISIBLE);
                horProgressBar.setProgress(0);
                break;
            case STATUS_COUNT:
                countTxt.setText("Count: " + msg.arg1);
                break;
            case STATUS_CURRENT:
                statusTxt.setText("Downloading... " + msg.arg1 + "/" + msg.arg2);
                break;
            case STATUS_PROGRESS:
                horProgressBar.setProgress(msg.arg1);
                break;
            case STATUS_END:
                progressBar.setVisibility(View.INVISIBLE);
                statusTxt.setVisibility(View.INVISIBLE);
                horProgressBar.setVisibility(View.INVISIBLE);
                resultTxt.setVisibility(View.VISIBLE);
                startBtn.setEnabled(true);
                break;
        }
        return true;
    }

    class DownloadTask extends AsyncTask<Void,Integer,Void>{

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            countTxt.setText("Count:");
            resultTxt.setVisibility(View.INVISIBLE);
            startBtn.setEnabled(false);
            statusTxt.setVisibility(View.VISIBLE);
            statusTxt.setText("Downloading...");
            horProgressBar.setVisibility(View.VISIBLE);
            horProgressBar.setProgress(0);
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            switch (values[0]){
                case 1:
                    countTxt.setText("Count: " + values[1]);
                    break;
                case 2:
                    statusTxt.setText("Downloading... " + values[1] + "/" + values[2]);
                    break;
                case 3:
                    horProgressBar.setProgress(values[1]);
                    break;
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Random rnd = new Random();
            int count = rnd.nextInt(15)+1;
            publishProgress(1,count);
            for (int i = 0; i < count; i++) {
                publishProgress(2,i+1,count);
                for (int j = 0; j < 100; j++) {
                    publishProgress(3,j+1);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.INVISIBLE);
            statusTxt.setVisibility(View.INVISIBLE);
            horProgressBar.setVisibility(View.INVISIBLE);
            resultTxt.setVisibility(View.VISIBLE);
            startBtn.setEnabled(true);
        }
    }
}
