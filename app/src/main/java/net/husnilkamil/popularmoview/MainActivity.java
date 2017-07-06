package net.husnilkamil.popularmoview;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.husnilkamil.popularmoview.data.AppPreferences;
import net.husnilkamil.popularmoview.data.Movie;
import net.husnilkamil.popularmoview.utility.MovieDbJsonUtils;
import net.husnilkamil.popularmoview.utility.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.OnWeatherItemClickListener{

    private static final String TAG = MainActivity.class.getName();

    private static final int SORT_BY_POPULARITY = 21;
    private static final int SORT_BY_RATING = 31;

    private ProgressBar mLoadingProgressBar;
    private TextView mErrorMessageTextView;
    private RecyclerView mMovieGridRecyclerView;
    private MovieListAdapter mMovieListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        mMovieGridRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_grid);

        mMovieListAdapter = new MovieListAdapter(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        mMovieGridRecyclerView.setAdapter(mMovieListAdapter);
        mMovieGridRecyclerView.setHasFixedSize(true);
        mMovieGridRecyclerView.setLayoutManager(layoutManager);

        loadMovieList(SORT_BY_POPULARITY);
        loadMovieList(SORT_BY_RATING);
        loadMovieList(SORT_BY_POPULARITY);
    }


    public void loadMovieList(int selectedOption)
    {

        String apiKey = AppPreferences.API_KEY;

        if(selectedOption == SORT_BY_RATING){
            URL url = NetworkUtils.buildTopRatedMovieDbUrl(apiKey);
            Log.d(TAG, url.toString());
            showUIMovieResult();
            new FetchMovieDataTask().execute(url);
        }else{
            URL url = NetworkUtils.buildPopularMovieDbUrl(apiKey);
            Log.d(TAG, url.toString());
            showUIMovieResult();
            new FetchMovieDataTask().execute(url);
        }
    }


    public void showUIErrorMessage()
    {
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mMovieGridRecyclerView.setVisibility(View.INVISIBLE);
    }

    public void showUIMovieResult()
    {
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mMovieGridRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuID = item.getItemId();
        switch (menuID){
            case R.id.action_sort_popularity :
                loadMovieList(SORT_BY_POPULARITY);
                return true;
            case R.id.action_sort_rating:
                loadMovieList(SORT_BY_RATING);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void weatherItemClick(Movie movie) {
        Intent displayMovieIntent = new Intent(this, MovieDetailActivity.class);
        displayMovieIntent.putExtra("MOVIE_ID", movie.getId());
        startActivity(displayMovieIntent);
    }

    private class FetchMovieDataTask extends AsyncTask<URL, Void, ArrayList<Movie>>
    {
        @Override
        protected void onPreExecute() {
            mLoadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(URL... params) {
            if(params.length <= 0)
            {
                return null;
            }
            URL url = params[0];

            String queryResult;
            ArrayList<Movie> movieList = null;
            try {
                queryResult = NetworkUtils.fetchUrlQueryResult(url);
                movieList = MovieDbJsonUtils.getMovieListFromJsonString(queryResult);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                showUIErrorMessage();
            }

            return movieList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieList)
        {
            mLoadingProgressBar.setVisibility(View.INVISIBLE);
            mMovieListAdapter.setMovieDataSource(movieList);
        }
    }
}
