package com.example.oopandroidapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {
    private String municipalityName;
    private TextView questionNumber, questionText, textHome, scoreText;
    private RadioGroup radioGroup;
    private RadioButton q1, q2, q3, q4;
    private ImageView imageHome;
    private MunicipalityData municipalityData;
    private Button nextQuestion;
    private Quiz quiz;
    private int pos = -1, score = 0;

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
            municipalityData = (MunicipalityData) extras.get("cityData");
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
        scoreText = (TextView) findViewById(R.id.textScore);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        quiz = new Quiz(municipalityName, municipalityData);
        pos = 0;
        buildQuiz(pos);
        scoreText.setText("Score: " + score + "/5");

        nextQuestion = (Button) findViewById(R.id.buttonSubmit);
        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pressedButton = radioGroup.getCheckedRadioButtonId();
                if(pressedButton == -1){
                    Toast.makeText(getApplicationContext(), "You need to select an option", Toast.LENGTH_SHORT);
                }else{
                    if(((RadioButton) v.findViewById(pressedButton)).getText().equals(quiz.getQuiz().get(questionText.getText()).get(4))){
                        score++;
                    }
                    scoreText.setText("Score: " + score + "/5");
                    pos++;
                    buildQuiz(pos);
                }
            }
        });
    }

    private void buildQuiz(int pos){
        questionNumber.setText((pos + 1) + "/5");

        int count = 0;
        for(Map.Entry<String, ArrayList<String>> entry : quiz.getQuiz().entrySet()){
            if(count == pos){
                questionText.setText(entry.getKey());
            }
        }
        q1 = (RadioButton) findViewById(R.id.radioQ1);
        q1.setText(quiz.getQuiz().get(questionText.getText()).get(0));

        q2 = (RadioButton) findViewById(R.id.radioQ2);
        q2.setText(quiz.getQuiz().get(questionText.getText()).get(1));

        q3 = (RadioButton) findViewById(R.id.radioQ3);
        q3.setText(quiz.getQuiz().get(questionText.getText()).get(2));

        q4 = (RadioButton) findViewById(R.id.radioQ4);
        q4.setText(quiz.getQuiz().get(questionText.getText()).get(3));
    }
}