package lk.sliit.movietrivia.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import lk.sliit.movietrivia.data.TriviaContract.TriviaEntry;

public class TriviaDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "trivia.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "+ TriviaEntry.TABLE_NAME +" ("
            +TriviaEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +TriviaEntry.COLUMN_QUESTION + " TEXT NOT NULL, "
            +TriviaEntry.COLUMN_CATEGORY+ " TEXT, "
            +TriviaEntry.COLUMN_TYPE+ " TEXT, "
            +TriviaEntry.COLUMN_DIFFICULTY + " TEXT, "
            +TriviaEntry.COLUMN_CORRECT_ANSWER+ " TEXT, "
            +TriviaEntry.COLUMN_INCORRECT_ANSWER_1+ " TEXT, "
            +TriviaEntry.COLUMN_INCORRECT_ANSWER_2 + " TEXT, "
            +TriviaEntry.COLUMN_INCORRECT_ANSWER_3+ " TEXT ); ";
    public TriviaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
