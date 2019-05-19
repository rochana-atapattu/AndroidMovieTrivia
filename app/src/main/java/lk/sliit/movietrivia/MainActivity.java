package lk.sliit.movietrivia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import lk.sliit.movietrivia.data.TriviaContract.TriviaEntry;

import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Trivia>> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int TRIVIA_LOADER_ID = 1;
    private static final String REQUEST_URL =
            "https://opentdb.com/api.php?amount=10&category=11&difficulty=medium&type=multiple";
    FragmentManager fm;
    FragmentTransaction ft;
    TriviaFragment tf;
    Cursor cursor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(LOG_TAG, "onCreate.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // checks if there is less than 100 questions
        cursor = getContentResolver().query(TriviaEntry.CONTENT_URI,null,null,null);
        //Toast.makeText(this, "have data in db. " + cursor.getCount(), Toast.LENGTH_LONG).show();
        if(cursor.getCount() < 100) {
            // Get a reference to the LoaderManager, in order to interact with loaders.


            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            getSupportLoaderManager().initLoader(TRIVIA_LOADER_ID, null, this).forceLoad();
        }

        //get 10 random questions from db
        String[] projections = {TriviaEntry.COLUMN_QUESTION,TriviaEntry.COLUMN_CORRECT_ANSWER,TriviaEntry.COLUMN_INCORRECT_ANSWER_1,TriviaEntry.COLUMN_INCORRECT_ANSWER_2,TriviaEntry.COLUMN_INCORRECT_ANSWER_3};
        cursor = getContentResolver().query(TriviaEntry.CONTENT_URI,projections,null,null,"RANDOM() limit 5");

        tf = new TriviaFragment();
        //passing the data set from db to fragment
        tf.setCursor(cursor);
        fm= getSupportFragmentManager();
        tf.setFm(fm);
        ft = fm.beginTransaction();
        ft.replace(R.id.trivia,tf);
        ft.commit();
    }

    @NonNull
    @Override
    public Loader<List<Trivia>> onCreateLoader(int i, @Nullable Bundle bundle) {

        Log.e(LOG_TAG, "onCreateLoader.");
        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        /*uriBuilder.appendQueryParameter("amount", "10");
        uriBuilder.appendQueryParameter("category", "11");
        uriBuilder.appendQueryParameter("difficulty", "medium");
        uriBuilder.appendQueryParameter("type", "multiple");*/
        return new TriviaLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Trivia>> loader, List<Trivia> trivias) {

       // Toast.makeText(this, "load finish " , Toast.LENGTH_SHORT).show();
        if (trivias != null && !trivias.isEmpty()) {
            //Toast.makeText(this, "have data. " + trivias.size(), Toast.LENGTH_SHORT).show();
            save(trivias);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Trivia>> loader) {

    }

    private void save(List<Trivia> trivia){

        ContentValues values = new ContentValues();
        for(Trivia trivium:trivia){
            values.put(TriviaEntry.COLUMN_QUESTION,trivium.getmQuestion());
            values.put(TriviaEntry.COLUMN_CATEGORY,trivium.getmCategory());
            values.put(TriviaEntry.COLUMN_CORRECT_ANSWER,trivium.getmCorrectAnswer());
            values.put(TriviaEntry.COLUMN_DIFFICULTY,trivium.getmDifficulty());
            values.put(TriviaEntry.COLUMN_INCORRECT_ANSWER_1,trivium.getmIncorrectAnswer1());
            values.put(TriviaEntry.COLUMN_INCORRECT_ANSWER_2,trivium.getmIncorrectAnswer2());
            values.put(TriviaEntry.COLUMN_INCORRECT_ANSWER_3,trivium.getmIncorrectAnswer3());
            values.put(TriviaEntry.COLUMN_TYPE,trivium.getmType());

            Uri uri = getContentResolver().insert(TriviaEntry.CONTENT_URI,values);
        }



    }
}
