package edu.hcmus.hw06;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    ProgressBar myBarHorizontal;
    TextView lblTopCaption;
    EditText txtDataBox;
    Button btnDoSomething;
    Button btnReset;
    double progressStep = 1;
    int SoLanLap = 0;
    final int MAX_PROGRESS = 100;
    int globalVar = 0;
    double accum = 0;
    long startingMills = System.currentTimeMillis();
    boolean isRunning = false;
    Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lblTopCaption = (TextView) findViewById(R.id.lblTopCaption);
        myBarHorizontal = (ProgressBar) findViewById(R.id.myBarHor);
        txtDataBox = (EditText) findViewById(R.id.txtBox1);
        txtDataBox.setHint("Nhập số: ");
        lblTopCaption.setText("progress: 0%");
        myBarHorizontal.setProgress(0);
        btnDoSomething = (Button) findViewById(R.id.btnDoItAgain);
        btnDoSomething.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer number = Integer.parseInt(txtDataBox.getText().toString());
                if (number <= 0) {
                    Toast.makeText(MainActivity.this, "Số không hợp lệ!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    btnDoSomething.setEnabled(false);
                    // int ketQua = 100 % number;
                    double ketQua = 100.0 / number * 1.0;
                    SoLanLap = number;
                    progressStep = ketQua;

                    Thread myBackgroundThread = new Thread(backgroundTask, "bg");
                    isRunning = true;
                    myBackgroundThread.start();
                }
            }// onClick
        });// setOnClickListener
    }

    @Override
    protected void onStart() {
        super.onStart();
        txtDataBox.setText("");
        accum = 0;
        myBarHorizontal.setMax(MAX_PROGRESS);
        myBarHorizontal.setProgress(0);
        myBarHorizontal.setVisibility(View.VISIBLE);

    }

    private Runnable foregroundRunnable = new Runnable() {
        @Override
        public void run() {
            try {

                if (isRunning) {
                    lblTopCaption.setText("progress: " + (int) accum
                            + "%");
                    myBarHorizontal.setProgress((int) accum);
                    accum += progressStep;
                    if (accum > myBarHorizontal.getMax()) {
                        lblTopCaption.setText("Time up");
                        myBarHorizontal.setProgress(0);
                        btnDoSomething.setEnabled(true);
                        accum = 0;
                        isRunning = false;
                    }
                }
            } catch (Exception e) {
                Log.e("<<foregroundTask>>", e.getMessage());
            }
        }
    }; // foregroundTask
    private Runnable backgroundTask = new Runnable() {
        @Override
        public void run() {
            for (int n = 0; accum <= myBarHorizontal.getMax(); n++) {
                globalVar++;

                myHandler.post(foregroundRunnable);
            }
        }
    };
}