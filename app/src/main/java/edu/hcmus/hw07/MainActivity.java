package edu.hcmus.hw07;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private ProgressBar progressBar;
    private TextView tvProgress;
    private EditText edtNumber;
    private Button btnDoItAgain;
    private Float progress = 0f;
    private Integer number;
    private Float step = 0f;
    private Float stepOf100;

    private boolean isRunning = false;
    private Handler handler = new Handler();
    private final String PROGRESS = "Progress: %d%%";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvProgress = (TextView) findViewById(R.id.tvProgress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        edtNumber = (EditText) findViewById(R.id.edtNumber);

        tvProgress.setText(String.format(PROGRESS, progress.intValue()));
        btnDoItAgain = (Button) findViewById(R.id.btnDoItAgain);
        btnDoItAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    number = Integer.parseInt(edtNumber.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Invalid number", Toast.LENGTH_LONG).show();
                    return;
                }
                progressBar.setProgress(0);
                progressBar.setMax(number);
                isRunning = true;
                btnDoItAgain.setEnabled(false);
                step = number * 1f / 100f;

                Thread thread = new Thread(backgroundTask, "bg");
                thread.start();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private Runnable foregroundRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                progress += step;
                stepOf100 = progress / step;
                System.out.println("progress: " + progress);
                System.out.println("step: " + stepOf100.intValue());
                tvProgress.setText(String.format(PROGRESS, stepOf100.intValue()));
                progressBar.setProgress(progress.intValue());
                if (progress.intValue() >= progressBar.getMax()) {
                    btnDoItAgain.setEnabled(true);
                    progress = 0f;
                    isRunning = false;
                }
            }
        }
    };
    private Runnable backgroundTask = new Runnable() {
        @Override
        public void run() {
            for (; progress < progressBar.getMax() && isRunning;) {
                handler.post(foregroundRunnable);
            }
        }
    };
}
