package tuliohdev.com.zupmovie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tulio on 1/26/16.
 */
class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "FDSgram";

    DatabaseHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createQuery = "CREATE TABLE Movie (" +
                " title TEXT, " +
                " year TEXT, " +
                " rated TEXT, " +
                " released TEXT, " +
                " runtime TEXT, " +
                " genre TEXT, " +
                " director TEXT, " +
                " writer TEXT, " +
                " actors TEXT, " +
                " plot TEXT, " +
                " language TEXT, " +
                " country TEXT, " +
                " awards TEXT, " +
                " poster TEXT, " +
                " metascore TEXT, " +
                " imdbRating TEXT, " +
                " imdbVotes TEXT, " +
                " imdbID TEXT, " +
                " type TEXT )";

        db.execSQL( createQuery );
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int i, int j ) {}
}
