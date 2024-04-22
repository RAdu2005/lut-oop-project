package com.example.oopandroidapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuizActivity extends AppCompatActivity {
    private String municipalityName;
    private TextView questionNumber, questionText, textHome;
    private RadioGroup radioGroup;
    private RadioButton q1, q2, q3, q4;
    private ImageView imageHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            municipalityName = extras.getString("cityName");
        }


        imageHome = (ImageView) findViewById(R.id.imageHome);
        textHome = (TextView) findViewById(R.id.textHome);
        textHome.setClickable(true);

        imageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        textHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        questionNumber = (TextView) findViewById(R.id.textCount);
        questionText = (TextView) findViewById(R.id.textQuestion);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        Quiz quiz = new Quiz(municipalityName, )
        for(int i = 0; i < 4; i++){

        }
    }

    private void populationQuestion(){
        questionNumber.setText("1/5");

    }
}