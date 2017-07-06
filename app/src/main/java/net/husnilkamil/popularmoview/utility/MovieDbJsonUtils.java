package net.husnilkamil.popularmoview.utility;

import net.husnilkamil.popularmoview.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDbJsonUtils {


    public static ArrayList<Movie> getMovieListFromJsonString(String queryResult) throws JSONException {
        if(queryResult != null && !queryResult.equals(""))
        {
            ArrayList<Movie> movieList = new ArrayList<Movie>();

            JSONObject jsonMovies = new JSONObject(queryResult);
            JSONArray jsonMoviesResult = jsonMovies.getJSONArray("results");
            for (int i = 0; i < jsonMoviesResult.length(); i++)
            {
                JSONObject jsonMovie = jsonMoviesResult.getJSONObject(i);
                int id = jsonMovie.getInt("id");
                String title = jsonMovie.getString("title");
                String poster = jsonMovie.getString("poster_path");

                movieList.add(new Movie(
                        id,
                        title,
                        NetworkUtils.buildPosterRequestUrl(poster).toString()));
            }

            return movieList;
        }
        return null;
    }

    public static Movie getMovieDetailFromJsonString(String jsonString) throws JSONException
    {
        Movie movie = null;
        if(jsonString != null && !jsonString.equals(""))
        {
            JSONObject movieJson = new JSONObject(jsonString);
            int id = movieJson.getInt("id");
            String title = movieJson.getString("title");
            String poster = movieJson.getString("poster_path");
            String synopsis = movieJson.getString("overview");
            String releaseDate = movieJson.getString("release_date");
            double rate = movieJson.getDouble("vote_average");

            movie = new Movie(
                    id,
                    title,
                    NetworkUtils.buildPosterRequestUrl(poster).toString(),
                    synopsis,
                    rate,
                    releaseDate);
        }
        return movie;
    }
}
