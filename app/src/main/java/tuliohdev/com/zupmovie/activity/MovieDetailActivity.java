package tuliohdev.com.zupmovie.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import tuliohdev.com.zupmovie.R;
import tuliohdev.com.zupmovie.helper.MoviePosterHelper;
import tuliohdev.com.zupmovie.model.Movie;

@EActivity(R.layout.activity_movie_detail)
public class MovieDetailActivity extends AppCompatActivity {
    private MoviePosterHelper moviePosterHelper;

    @ViewById
    ImageView moviePoster;

    @ViewById
    TextView movieTitle;

    @ViewById
    TextView movieGenre;

    @ViewById
    TextView movieRuntime;

    @ViewById
    TextView movieDirector;

    @ViewById
    TextView movieWriter;

    @ViewById
    TextView moviePlot;

    @ViewById
    TextView movieActors;

    @ViewById
    TextView movieLanguage;

    @AfterViews
    void afterViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        moviePosterHelper = new MoviePosterHelper(getApplicationContext());

        preencherObjetos();
    }

    private void preencherObjetos() {
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");
        moviePoster.setImageBitmap(moviePosterHelper.getMoviePoster(movie));
        movieTitle.setText(movie.getTitle() + " (" + movie.getYear() + ")");
        movieGenre.setText(movie.getGenre());
        movieRuntime.setText(movie.getRuntime());
        movieDirector.setText(movie.getDirector());
        movieWriter.setText(movie.getWriter());
        moviePlot.setText(movie.getPlot());
        movieActors.setText(movie.getActors());
        movieLanguage.setText(movie.getLanguage());
    }
}
