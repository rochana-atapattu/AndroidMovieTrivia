package lk.sliit.movietrivia;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends Fragment {

    private int score;
    TextView tvScore;
    View v;


    public ScoreFragment() {
        // Required empty public constructor
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_score, container, false);
        tvScore = (TextView) v.findViewById(R.id.tvScore);

        tvScore.setText("" + score);
        return v;
    }

}
