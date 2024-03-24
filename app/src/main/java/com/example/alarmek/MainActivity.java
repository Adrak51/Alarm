package com.example.alarmek;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView godzina, ustawionyAlarm;
    Handler handler, handler2;
    private MediaPlayer mediaPlayer;
    private EditText alarmHour, alarmMinute;
    private Button ustawAlarm, anulujAlarm;
    private Vibrator vibrator;
    private String godzinaAlarmu, minutaAlarmu;
    private Boolean alarmaktywny = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        godzina = findViewById(R.id.godzina);
        ustawionyAlarm = findViewById(R.id.ustawionyAlarm);
        alarmHour = findViewById(R.id.editTextHour);
        alarmMinute = findViewById(R.id.editTextMinute);
        ustawAlarm = findViewById(R.id.button);
        anulujAlarm = findViewById(R.id.buttonAnuluj);

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.alarmsound);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        ustawAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                godzinaAlarmu = String.format("%02d", Integer.parseInt(alarmHour.getText().toString()));
                minutaAlarmu = String.format("%02d", Integer.parseInt(alarmMinute.getText().toString()));
                alarmaktywny = true;
                setAlarm();
            }
        });

        anulujAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmaktywny = false;
                ustawionyAlarm.setText("Anulowano alarm");
                alarmHour.setText("00");
                alarmMinute.setText("00");
            }
        });

        handler = new Handler();

        alarmHour.setFilters(new InputFilter[]{new MinMaxFilter("0", "23")});
        alarmMinute.setFilters(new InputFilter[]{new MinMaxFilter("0", "59")});


        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = dateFormat.format(Calendar.getInstance().getTime());
        godzina.setText(currentTime);


        handler.postDelayed(updateTimer, 1000);
    }

    public void setAlarm() {
        handler2 = new Handler();
        ustawionyAlarm.setText("Ustawiono alarm na: " + godzinaAlarmu + ":" + minutaAlarmu);
        handler2.postDelayed(Alarm, 1000);
    }

    private Runnable Alarm = new Runnable() {
        @Override
        public void run() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String currentTime = dateFormat.format(Calendar.getInstance().getTime());
            String time = godzinaAlarmu + ":" + minutaAlarmu + ":00";
            if(time.equals(currentTime) && alarmaktywny == true) {
                graj();

            }
            handler2.postDelayed(this, 1000);
        }
    };

    private void graj() {
        mediaPlayer.start();
        long[] pattern = {0, 1000, 1000, 1000};
        vibrator.vibrate(pattern, -1);
    }

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String currentTime = dateFormat.format(Calendar.getInstance().getTime());
            godzina.setText(currentTime);

            handler.postDelayed(this, 1000);
        }
    };
}