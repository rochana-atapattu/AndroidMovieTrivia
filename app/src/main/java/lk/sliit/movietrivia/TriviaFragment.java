package lk.sliit.movietrivia;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
    FragmentManager fm;
    FragmentTransaction ft;
    ScoreFragment sf;

    public TriviaFragment() {
        // Required empty public constructor
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public void setFm(FragmentManager fm) {
        this.fm = fm;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getActivity(), " button pressed " + item.getItemId(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {

            case android.R.id.home:


                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    getActivity().finish();
                } else {
                    getFragmentManager().popBackStack();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_trivia, container, false);


        // If moveToFirst() returns false then cursor is empty
        if (!cursor.moveToFirst()) {
            return null;
        }




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
        prepForNextQuestion();
        return v;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSubmit:

                if(cursor.isLast())
                    btnNext.setText("Finish");

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

                        if(cursor.moveToNext())
                            prepForNextQuestion();
                        else {
                            cursor.close();
                            sf = new ScoreFragment();
                            sf.setScore(Score);
                            ft = fm.beginTransaction();
                            ft.replace(R.id.trivia,sf);
                            ft.commit();
                        }

                    }
                }else {
                    Toast.makeText(getActivity(), "Please submit the answer.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void prepForNextQuestion(){


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

        rowNumber = cursor.getPosition() + 1;


        questionNumber.setText("" + rowNumber);
        questionText.setText(cursor.getString(indexQuestion));


        /*option1.setText(cursor.getString(indexAnswer1));
        option2.setText(cursor.getString(indexAnswer2));
        option3.setText(cursor.getString(indexAnswer3));
        option4.setText(cursor.getString(indexAnswer4));*/

        Random rand = new Random();
        List<RadioButton> radio = new ArrayList();
        radio.add(option1);
        radio.add(option2);
        radio.add(option3);
        radio.add(option4);

        List<Integer> indexes =new ArrayList<>();
        indexes.add(indexAnswer1);
        indexes.add(indexAnswer2);
        indexes.add(indexAnswer3);
        indexes.add(indexAnswer4);


        for (int i = 0; i < indexes.size(); i++) {
            int randomIndex = rand.nextInt(radio.size());
            RadioButton option = radio.get(randomIndex);
            option.setText(cursor.getString(indexes.get(i)));
            radio.remove(randomIndex);
        }
        answer = cursor.getString(indexAnswer1);
    }



}
