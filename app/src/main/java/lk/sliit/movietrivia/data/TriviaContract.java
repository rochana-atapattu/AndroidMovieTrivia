package lk.sliit.movietrivia.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class TriviaContract {
    public final static String CONTENT_AUTHORITY  = "lk.sliit.movietrivia";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_TRIVIA = "questions";


    public static final class TriviaEntry implements BaseColumns {

        //MIME type
        public static  final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +CONTENT_AUTHORITY + "/" + PATH_TRIVIA;
        public static  final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +CONTENT_AUTHORITY + "/" + PATH_TRIVIA;

        public static final String TABLE_NAME = "trivia";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_CATEGORY= "category";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_CORRECT_ANSWER = "correct_answer";
        public static final String COLUMN_INCORRECT_ANSWER_1 = "incorrect_answer1";
        public static final String COLUMN_INCORRECT_ANSWER_2 = "incorrect_answer2";
        public static final String COLUMN_INCORRECT_ANSWER_3 = "incorrect_answer3";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_TRIVIA);
    }
}
