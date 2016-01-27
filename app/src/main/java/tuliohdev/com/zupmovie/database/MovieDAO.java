package tuliohdev.com.zupmovie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tuliohdev.com.zupmovie.builder.MovieBuilder;
import tuliohdev.com.zupmovie.model.Movie;

/**
 * Created by tulio on 1/26/16.
 */
public class MovieDAO {
    private SQLiteDatabase sqLite;
    private final String NOME_TABELA;

    public MovieDAO( Context context ) {
        sqLite = new DatabaseHelper( context ).getWritableDatabase();
        NOME_TABELA = "Movie";
    }

    public List<Movie> obterTodosFilmes() {
        return getMoviesFromCursor(
                sqLite.rawQuery("SELECT * FROM " + NOME_TABELA, null));
    }

    public boolean filmeExiste(String imdbID) {
        String[] args = new String[] { imdbID };
        return sqLite.rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE imdbID = ?", args)
                .getCount() > 0;
    }

    private List<Movie> getMoviesFromCursor(Cursor cursor) {
        cursor.moveToFirst();
        List<Movie> movies = new ArrayList<>( cursor.getCount() );

        while( cursor.isAfterLast() == false ) {
            Movie movie = getMovieFromCursor(cursor);
            movies.add(movie);

            cursor.moveToNext();
        }

        return movies;
    }

    private Movie getMovieFromCursor(Cursor cursor) {
        return MovieBuilder.newInstance()
                .setTitle(cursor.getString(0))
                .setYear(cursor.getString(1))
                .setRated(cursor.getString(2))
                .setReleased(cursor.getString(3))
                .setRuntime(cursor.getString(4))
                .setGenre(cursor.getString(5))
                .setDirector(cursor.getString(6))
                .setWriter(cursor.getString(7))
                .setActors(cursor.getString(8))
                .setPlot(cursor.getString(9))
                .setLanguage(cursor.getString(10))
                .setCountry(cursor.getString(11))
                .setAwards(cursor.getString(12))
                .setPoster(cursor.getString(13))
                .setMetascore(cursor.getString(14))
                .setImdbRating(cursor.getString(15))
                .setImdbVotes(cursor.getString(16))
                .setImdbID(cursor.getString(17))
                .setType(cursor.getString(18))
                .build();
    }

    public void inserir( Movie movie ) {
        ContentValues content = new ContentValues();

        content.put( "title", movie.getTitle() );
        content.put( "year", movie.getYear() );
        content.put( "rated", movie.getRated() );
        content.put( "released", movie.getReleased() );
        content.put( "runtime", movie.getRuntime() );
        content.put( "genre", movie.getGenre() );
        content.put( "director", movie.getDirector() );
        content.put( "writer", movie.getWriter() );
        content.put( "actors", movie.getActors() );
        content.put( "plot", movie.getPlot() );
        content.put( "language", movie.getLanguage() );
        content.put( "country", movie.getCountry() );
        content.put( "awards", movie.getAwards() );
        content.put( "poster", movie.getPoster() );
        content.put( "metascore", movie.getMetascore() );
        content.put( "imdbRating", movie.getImdbRating());
        content.put( "imdbVotes", movie.getImdbVotes());
        content.put( "imdbID", movie.getImdbID());
        content.put( "type", movie.getType());

        sqLite.insert(NOME_TABELA, null, content);
    }

    public void deletar( String imdbID ) {
        String where = "imdbID = ? ";
        String[] args = new String[] { imdbID };

        sqLite.delete( NOME_TABELA, where, args);
    }
}
