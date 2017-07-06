package net.husnilkamil.popularmoview.utility;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String BASE_URL_TOP_RATED_MOVIE = "https://api.themoviedb.org/3/movie/top_rated";
    private static final String BASE_URL_POPULAR_MOVIE = "https://api.themoviedb.org/3/movie/popular";
    private static final String BASE_URL_MOVIE_DETAIL = "https://api.themoviedb.org/3/movie/";
    private static final String BASE_URL_POSTER_IMAGE = "http://image.tmdb.org/t/p/";

    private static final String PARAM_API_KEY = "api_key";

    private static final String DEFAULT_POSTER_SIZE = "w300";

    public static URL buildPopularMovieDbUrl(String apiKey)
    {
        Uri uri = Uri.parse(BASE_URL_POPULAR_MOVIE)
                .buildUpon()
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return url;
        }
    }

    public static URL buildTopRatedMovieDbUrl(String apiKey)
    {
        Uri uri = Uri.parse(BASE_URL_TOP_RATED_MOVIE)
                .buildUpon()
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return url;
        }
    }

    public static URL buildMovieDetailUrl(String movieId, String apiKey)
    {
        Uri uri = Uri.parse(BASE_URL_MOVIE_DETAIL)
                .buildUpon()
                .appendPath(movieId)
                .appendQueryParameter(PARAM_API_KEY, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return url;
        }
    }

    public static String fetchUrlQueryResult(URL url) throws IOException
    {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput)
            {
                return scanner.next();
            }else{
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }

    public static URL buildPosterRequestUrl(String filename)
    {
        Uri uri = Uri.parse(BASE_URL_POSTER_IMAGE)
                .buildUpon()
                .appendPath(DEFAULT_POSTER_SIZE)
                .appendEncodedPath(filename)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return url;
        }
    }

}
