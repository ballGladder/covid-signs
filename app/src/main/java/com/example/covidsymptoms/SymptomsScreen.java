package com.example.covidsymptoms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Date;

public class SymptomsScreen extends AppCompatActivity {

    private Spinner spinner;
    RatingBar symptomRatingBar;
    private AppDatabase db;
    private UserInfo data = new UserInfo();
    float[] cachedRatings = new float[10];
    //Local array to cache symptom ratings

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms_screen);

        symptomRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        Button updateButton = (Button) findViewById(R.id.button2);

        spinner = (Spinner) findViewById(R.id.symptoms_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.symptoms_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Gets app database
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    db = AppDatabase.getInstance(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        symptomRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int i = spinner.getSelectedItemPosition();
                cachedRatings[i] = v;
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                data.fever = cachedRatings[0];
                data.cough = cachedRatings[1];
                data.tiredness = cachedRatings[2];
                data.shortnessOfBreath = cachedRatings[3];
                data.muscleAches = cachedRatings[4];
                data.chills = cachedRatings[5];
                data.soreThroat = cachedRatings[6];
                data.runnyNose = cachedRatings[7];
                data.headache = cachedRatings[8];
                data.chestPain = cachedRatings[9];
                data.timestamp = new Date(System.currentTimeMillis());

                boolean uploadSignsClicked = getIntent().getExtras().getBoolean("uploadSignsClicked");

                //If new row created by Upload Signs button then update that row
                // else create a new row with empty signs and new symptom ratings
                if(uploadSignsClicked == true) {
                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            UserInfo latestData = db.userInfoDao().getLatestData();
                            data.heartRate = latestData.heartRate;
                            data.breathingRate = latestData.breathingRate;
                            data.id = latestData.id;
                            db.userInfoDao().update(data);
                        }
                    });
                    thread.start();

                } else {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            db.userInfoDao().insert(data);
                        }
                    });
                    thread.start();
                }
                Toast.makeText(SymptomsScreen.this, "Symptoms updated!", Toast.LENGTH_SHORT).show();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                symptomRatingBar.setRating(cachedRatings[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

}