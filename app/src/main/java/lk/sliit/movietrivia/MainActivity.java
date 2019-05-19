package lk.sliit.movietrivia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import lk.sliit.movietrivia.data.TriviaContract.TriviaEntry;
import android.support.design.widget.FloatingActionButton;

import java.util.List;


public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int TRIVIA_LOADER_ID = 1;
    private static final int TRIVIA_QUESTION_LOADER_ID = 2;
    private static final String REQUEST_URL =
            "https://opentdb.com/api.php?amount=10&category=11&difficulty=medium&type=multiple";
    FragmentManager fm;
    FragmentTransaction ft;
    TriviaFragment tf;
    Cursor cursor,cursor1;
    FloatingActionButton fab;
    CoordinatorLayout cl;
    private RecyclerView recyclerView;
    private QuestionsCursorAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(LOG_TAG, "onCreate.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        cl =  findViewById(R.id.recycler_view_container);

        // checks if there is less than 100 questions

        //Toast.makeText(this, "have data in db. " + cursor.getCount(), Toast.LENGTH_LONG).show();
        if(getQuestionCount() < 1000) {
            // Get a reference to the LoaderManager, in order to interact with loaders.


            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            getSupportLoaderManager().initLoader(TRIVIA_LOADER_ID, null, this).forceLoad();
        }


        //get 10 random questions from db
        String[] projections = {TriviaEntry.COLUMN_QUESTION,TriviaEntry.COLUMN_CORRECT_ANSWER,TriviaEntry.COLUMN_INCORRECT_ANSWER_1,TriviaEntry.COLUMN_INCORRECT_ANSWER_2,TriviaEntry.COLUMN_INCORRECT_ANSWER_3};
         cursor1 = getContentResolver().query(TriviaEntry.CONTENT_URI,projections,null,null,"RANDOM() limit 5");


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new QuestionsCursorAdapter();
        recyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);
        cl.setVisibility(View.VISIBLE);
        fab =  findViewById(R.id.fab);
        LoaderManager.getInstance(MainActivity.this).initLoader(TRIVIA_QUESTION_LOADER_ID,null,MainActivity.this).forceLoad();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cursor1.getCount() > 0) {
                    cl.setVisibility(View.GONE);


                    tf = new TriviaFragment();
                    //passing the data set from db to fragment
                    tf.setCursor(cursor1);
                    fm = getSupportFragmentManager();
                    tf.setFm(fm);
                    ft = fm.beginTransaction();
                    ft.replace(R.id.trivia, tf);
                    Toast.makeText(MainActivity.this, "Number of questions " +cursor1.getCount() + "out of " + getQuestionCount(), Toast.LENGTH_SHORT).show();
                    ft.commit();
                }
            }
        });

    }

    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable Bundle bundle) {

        int id = i;

        Log.e(LOG_TAG, "onCreateLoader. id " + id);
        if(id == 1){
        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        /*uriBuilder.appendQueryParameter("amount", "10");
        uriBuilder.appendQueryParameter("category", "11");
        uriBuilder.appendQueryParameter("difficulty", "medium");
        uriBuilder.appendQueryParameter("type", "multiple");*/
        return new TriviaLoader(this, uriBuilder.toString());
        }
        else if(id == 2) {
            return questionLoader();
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object o) {
        int id = loader.getId();
        Log.e(LOG_TAG, "onLoadFinished. id " + id);
        if(id == 1){
            List<Trivia> trivias = (List<Trivia>) o;
            // Toast.makeText(this, "load finish " , Toast.LENGTH_SHORT).show();
            if (trivias != null && !trivias.isEmpty()) {
                //Toast.makeText(this, "have data. " + trivias.size(), Toast.LENGTH_SHORT).show();
                save(trivias);
            }
        }
        else if(id == 2){
            Cursor cursor = (Cursor) o;
            mAdapter.swapCursor(cursor);
        }
    }

    /*@Override
    public void onLoadFinished(@NonNull Loader loader, List trivias) {


    }*/

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

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
    private void removeItem(long id){
        int uri = getContentResolver().delete(TriviaEntry.CONTENT_URI,TriviaEntry._ID + "=" + id,null);

        if (uri == -1){
            Toast.makeText(this,"failed to delete : " + id,Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"deleted : " + uri,Toast.LENGTH_LONG).show();
        }

    }
    private Loader<Cursor> questionLoader(){
        Uri questionUri =  TriviaEntry.CONTENT_URI;
        String[] projection = {TriviaEntry.COLUMN_QUESTION,TriviaEntry._ID};

        return new CursorLoader(getApplicationContext(),questionUri,projection,null,null,null);
    }
    private int getQuestionCount(){
        int count;
        Cursor cursor2 = getContentResolver().query(TriviaEntry.CONTENT_URI,null,null,null);
        count = cursor2.getCount();
        cursor2.close();
        return count;
    }
}
