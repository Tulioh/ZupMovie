package tuliohdev.com.zupmovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tuliohdev.com.zupmovie.R;
import tuliohdev.com.zupmovie.helper.MoviePosterHelper;
import tuliohdev.com.zupmovie.model.Movie;

/**
 * Created by tulio on 1/26/16.
 */
public class MovieListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Movie> movies;
    private Context context;
    private MoviePosterHelper moviePosterHelper;

    public MovieListAdapter(Context context, List<Movie> movies) {
        this.movies = movies;
        this.context = context;
        moviePosterHelper = new MoviePosterHelper(context);
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return movies.size();
    }
    public Movie getItem(int position) {
        return movies.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ItemSuporte itemHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.list_movies, null);

            itemHolder = new ItemSuporte();
            itemHolder.txtTitle = ((TextView) view.findViewById(R.id.movieName));
            itemHolder.imgIcon = ((ImageView) view.findViewById(R.id.moviePoster));

            view.setTag(itemHolder);
        } else {
            itemHolder = (ItemSuporte) view.getTag();
        }

        Movie item = movies.get(position);
        itemHolder.txtTitle.setText(item.getTitle());
        itemHolder.imgIcon.setImageBitmap(
                moviePosterHelper.getMoviePoster(item));

        return view;
    }

    public void updateMovieList(List<Movie> movies) {
        this.movies = movies;
    }

    private class ItemSuporte {

        ImageView imgIcon;
        TextView txtTitle;
    }
}
