package lk.sliit.movietrivia.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import  lk.sliit.movietrivia.data.TriviaContract.TriviaEntry;

public class TriviaProvider extends ContentProvider {

    //constants to match URI
    private static final int TRIVIA = 1;
    private static final int TRIVIUM = 2;

    private TriviaDbHelper mDbHelper;

    //use UriMatcher class to make the matching
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static{
        sUriMatcher.addURI(TriviaContract.CONTENT_AUTHORITY,TriviaContract.PATH_TRIVIA,TRIVIA);
        sUriMatcher.addURI(TriviaContract.CONTENT_AUTHORITY,TriviaContract.PATH_TRIVIA + "/#",TRIVIUM);
    }
    @Override
    public boolean onCreate() {
        mDbHelper = new TriviaDbHelper(getContext());
        return mDbHelper != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        //readable db
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //cursor to hold data from db
        Cursor cursor;

        //match URI
        int match = sUriMatcher.match(uri);
        switch(match){
            case TRIVIA:
                cursor = db.query(TriviaEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case TRIVIUM:
                selection = TriviaEntry._ID + "=?";
                //add the id to selectArgs string array
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(TriviaEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);

                break;
            default:
                throw new IllegalArgumentException("Cannot resolve URI :" + uri);
        }

        //set notification URI on the cursor
        //if the data at this URI changes, then we know we need to update the cursor
        //listener atteched to this resolver will be notified
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {

            case TRIVIA:
                return TriviaEntry.CONTENT_LIST_TYPE;
            case TRIVIUM:
                return TriviaEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Cannot resolve URI :" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int match = sUriMatcher.match(uri);
        switch (match){
            case TRIVIA:
                return insertQuestion(uri, values);
            default:
                throw new IllegalArgumentException("Cannot resolve URI :" + uri);
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        switch (match) {

            case TRIVIA:
                return db.delete(TriviaEntry.TABLE_NAME,selection,selectionArgs);
            case TRIVIUM:
                selection = TriviaEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                int rowsDeleted = db.delete(TriviaEntry.TABLE_NAME,selection,selectionArgs);
                if (rowsDeleted != 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Cannot resolve URI :" + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {


        int match = sUriMatcher.match(uri);
        switch (match) {

            case TRIVIA:
                return updateQuestion(uri,values,selection,selectionArgs);
            case TRIVIUM:
                selection = TriviaEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateQuestion(uri,values,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Cannot resolve URI :" + uri);
        }

    }
    public int getCount(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String count = "SELECT count(*) FROM trivia";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        return mcursor.getInt(0);
    }

    private Uri insertQuestion(Uri uri, ContentValues values){

        //data check
        String name = values.getAsString(TriviaEntry.COLUMN_QUESTION);
        if (name == null){
            throw new IllegalArgumentException("Invalid name");
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //insert new data
        long id = db.insert(TriviaEntry.TABLE_NAME,null,values);

        if (id == -1){
            return null;
        }

        getContext().getContentResolver().notifyChange(uri,null);
        //return uri with the id
        return ContentUris.withAppendedId(uri,id);
    }

    private int updateQuestion(Uri uri,ContentValues values,String selection,String[] selectionArgs){

        //data check
        String name = values.getAsString(TriviaEntry.COLUMN_QUESTION);
        if (name == null){
            throw new IllegalArgumentException("Invalid name");
        }

        //dont update if values is empty
        if (values.size() == 0){
            return 0;
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        getContext().getContentResolver().notifyChange(uri,null);
        return  db.update(TriviaEntry.TABLE_NAME,values,selection,selectionArgs);
    }

}
