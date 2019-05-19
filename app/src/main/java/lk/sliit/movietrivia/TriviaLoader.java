package lk.sliit.movietrivia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class TriviaLoader extends AsyncTaskLoader<List<Trivia>> {

    private static final String LOG_TAG = TriviaLoader.class.getSimpleName();
    /** Query URL */
    private String mUrl;

    public TriviaLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }



    @Nullable
    @Override
    public List<Trivia> loadInBackground() {
        Log.e(LOG_TAG, "loadInBackground.");
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Trivia> trivia = QueryUtils.fetchData(mUrl);
        return trivia;
    }
}
