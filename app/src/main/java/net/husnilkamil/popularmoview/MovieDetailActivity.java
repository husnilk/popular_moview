package net.husnilkamil.popularmoview;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.husnilkamil.popularmoview.data.AppPreferences;
import net.husnilkamil.popularmoview.data.Movie;
import net.husnilkamil.popularmoview.utility.MovieDbJsonUtils;
import net.husnilkamil.popularmoview.utility.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity {

    private int movieId;

    private TextView mMovieTitle;
    private TextView mMovieRating;
    private TextView mMovieSynopsis;
    private TextView mMovieReleaseDate;
    private ImageView mPosterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mMovieTitle = (TextView) findViewById(R.id.tv_movie_title);
        mMovieRating = (TextView) findViewById(R.id.tv_movie_rating);
        mMovieSynopsis = (TextView) findViewById(R.id.tv_movie_synopsis);
        mMovieReleaseDate = (TextView) findViewById(R.id.tv_movie_release_date);
        mPosterImage = (ImageView) findViewById(R.id.iv_movie_poster);

        Intent startedIntent = getIntent();
        if(startedIntent.hasExtra("MOVIE_ID")){
            movieId = startedIntent.getIntExtra("MOVIE_ID", 0);
        }
        loadMovieData();
    }

    public void loadMovieData()
    {
        URL url = NetworkUtils.buildMovieDetailUrl(String.valueOf(movieId), AppPreferences.API_KEY);
        new FetchMovieDetailTask().execute(url);
    }

    private void updateUIMovieDetail(Movie movie) {
        Picasso.with(this)
                .load(movie.getPoster())
                .into(mPosterImage);
        String rating = "Rating " + String.valueOf(movie.getRate());
        String releaseDate = "Release Date " + movie.getReleaseDate();
        mMovieTitle.setText(movie.getTitle());
        mMovieRating.setText(rating);
        mMovieSynopsis.setText(movie.getSynopsis());
        mMovieReleaseDate.setText(releaseDate);
    }

    private class FetchMovieDetailTask extends AsyncTask<URL, Void, Movie>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Movie doInBackground(URL... params) {
            if(params.length <= 0)
            {
                return null;
            }
            URL url = params[0];

            String queryResult;
            Movie movie = null;
            try {
                queryResult = NetworkUtils.fetchUrlQueryResult(url);
                movie = MovieDbJsonUtils.getMovieDetailFromJsonString(queryResult);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return movie;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            updateUIMovieDetail(movie);
        }
    }
}
