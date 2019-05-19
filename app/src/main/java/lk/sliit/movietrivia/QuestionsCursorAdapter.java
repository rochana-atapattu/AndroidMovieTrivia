package lk.sliit.movietrivia;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import lk.sliit.movietrivia.data.TriviaContract.TriviaEntry;

public class QuestionsCursorAdapter extends BaseCursorAdapter<QuestionsCursorAdapter.QuestionsViewHolder>{

    private static final String LOG_TAG = QuestionsCursorAdapter.class.getSimpleName();

    public QuestionsCursorAdapter() {

        super(null);
    }

    @NonNull
    @Override
    public QuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.e(LOG_TAG, "onCreateViewHolder.");
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                viewGroup.getContext());
        View v =
                inflater.inflate(R.layout.row_layout, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        return new QuestionsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(QuestionsViewHolder holder, Cursor cursor) {
        Log.e(LOG_TAG, "onBindViewHolder.  " + cursor.getCount());
        String question = cursor.getString(cursor.getColumnIndex(TriviaEntry.COLUMN_QUESTION));
        long id = cursor.getInt(cursor.getColumnIndex(TriviaEntry._ID));

        holder.nameTextView.setText(question);
        holder.itemView.setTag(id);
    }

    class QuestionsViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView;
        public QuestionsViewHolder(@NonNull View v) {
            super(v);

            nameTextView = (TextView) v.findViewById(R.id.firstLine);
        }
    }
}
