package lk.sliit.movietrivia;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lk.sliit.movietrivia.data.TriviaContract.TriviaEntry;


import lk.sliit.movietrivia.data.TriviaContract.TriviaEntry;

/**
 * A simple {@link Fragment} subclass.
 */
public class TriviaFragment extends Fragment implements View.OnClickListener{

    TextView questionText, questionNumber;

    Context applicationContext = MainActivity.getContextOfApplication();
    boolean notLast;

    int indexQuestion,indexAnswer1,indexAnswer2,indexAnswer3,indexAnswer4;

    RadioGroup radioGroup;
    RadioButton option1, option2, option3,option4;
    Button btnSubmit, btnNext;
    TextView textViewScore;
    int Score = 0;
    boolean submitted;
    static final String SCOREKEY = "key1";
    View v;
    Cursor cursor;
    int rowNumber = 1;
    String answer;

    public TriviaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_trivia, container, false);
        fetchQuestions();

        // If moveToFirst() returns false then cursor is empty
        if (!cursor.moveToFirst()) {
            return null;
        }
        Toast.makeText(getActivity(), "Number of questions " +cursor.getCount(), Toast.LENGTH_SHORT).show();

        notLast =true;
        indexQuestion = cursor.getColumnIndex(TriviaEntry.COLUMN_QUESTION);
        indexAnswer1 = cursor.getColumnIndex(TriviaEntry.COLUMN_CORRECT_ANSWER);
        indexAnswer2 =cursor.getColumnIndex(TriviaEntry.COLUMN_INCORRECT_ANSWER_1);
        indexAnswer3 = cursor.getColumnIndex(TriviaEntry.COLUMN_INCORRECT_ANSWER_2);
        indexAnswer4 = cursor.getColumnIndex(TriviaEntry.COLUMN_INCORRECT_ANSWER_3);


        questionNumber = (TextView) v.findViewById(R.id.qNo);
        questionText = (TextView) v.findViewById(R.id.tvQuestion);

        option1 = (RadioButton) v.findViewById(R.id.rbOption1);
        option2 = (RadioButton) v.findViewById(R.id.rbOption2);
        option3 = (RadioButton) v.findViewById(R.id.rbOption3);
        option4 = (RadioButton) v.findViewById(R.id.rbOption4);

        radioGroup = (RadioGroup) v.findViewById(R.id.rg);

        btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
        btnNext = (Button) v.findViewById(R.id.btnNext);

        btnSubmit.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        Score = 0;

        textViewScore = (TextView) v.findViewById(R.id.tvScore);

        btnSubmit.setClickable(true);
        return v;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSubmit:

                if(radioGroup.getCheckedRadioButtonId()== -1){
                    Toast.makeText(getActivity(), "Please select an answer.", Toast.LENGTH_SHORT).show();
                }else {
                    submitted = true;
                    if ((((RadioButton) v.findViewById((radioGroup.getCheckedRadioButtonId()))).getText().toString()).equalsIgnoreCase(answer)) {
                        Toast.makeText(getActivity(), "Correct Answer", Toast.LENGTH_SHORT).show();
                        Score++;
                        textViewScore.setText(""+Score);

                        for (int i = 0; i < radioGroup.getChildCount(); i++) {
                            radioGroup.getChildAt(i).setEnabled(false);
                        }
                        btnSubmit.setClickable(false);

                    } else {
                        Toast.makeText(getActivity(), "Incorrect", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < radioGroup.getChildCount(); i++) {
                            radioGroup.getChildAt(i).setEnabled(false);
                        }
                        btnSubmit.setClickable(false);
                    }
                }

                if (rowNumber == 11) {
                    Toast.makeText(getActivity(), ""+Score, Toast.LENGTH_SHORT).show();

                }

                break;
            case R.id.btnNext:
                if (submitted) {
                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getActivity(), "You can't skip a question.", Toast.LENGTH_SHORT).show();
                    } else {
                        nextQuestion();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please submit the answer.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void fetchQuestions(){
    //get 10 random questions from db
        String[] projections = {TriviaEntry.COLUMN_QUESTION,TriviaEntry.COLUMN_CORRECT_ANSWER,TriviaEntry.COLUMN_INCORRECT_ANSWER_1,TriviaEntry.COLUMN_INCORRECT_ANSWER_2,TriviaEntry.COLUMN_INCORRECT_ANSWER_3};
        cursor = applicationContext.getContentResolver().query(TriviaEntry.CONTENT_URI,projections,null,null,"RANDOM() limit 5");

    }

    private void nextQuestion(){

        notLast=cursor.moveToNext();
        submitted = false;
        btnSubmit.setClickable(true);

        option1.setVisibility(View.VISIBLE);
        option2.setVisibility(View.VISIBLE);
        option3.setVisibility(View.VISIBLE);
        option4.setVisibility(View.VISIBLE);
        radioGroup.clearCheck();

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(true);
        }

        rowNumber = cursor.getPosition();


        questionNumber.setText("" + rowNumber);
        questionText.setText(cursor.getString(indexQuestion));


            option1.setText(cursor.getString(indexAnswer1));

            option2.setText(cursor.getString(indexAnswer2));

            option3.setText(cursor.getString(indexAnswer3));

            option4.setText(cursor.getString(indexAnswer4));
        answer = cursor.getString(indexAnswer1);
    }

    /*public RadioButton randomOp() {
        Random rand = new Random();
        List<RadioButton> givenList = new ArrayList();
        givenList.add(option1);
        givenList.add(option2);
        givenList.add(option3);
        givenList.add(option4);

        int numberOfElements = 4;

        for (int i = 0; i < numberOfElements; i++) {
            int randomIndex = rand.nextInt(givenList.size());
            String randomElement = givenList.get(randomIndex);
            givenList.remove(randomIndex);
        }
    }*/

}
