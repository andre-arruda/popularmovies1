package br.com.project.movieapp.WebService;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by lsitec211.arruda on 25/08/17.
 */

public class WebServiceUtils {

    final static String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3";
    final static String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    final static String MOVIE_POSTER_SIZE = "w780";
    final static String SORT_BY_POPULAR = "/movie/popular";
    final static String SORT_BY_TOP_RATED = "/movie/top_rated";
    final static String APIKEY = "?api_key=[INSERT_API_KEY_HERE]";
    final static Integer POPULAR = 0;
    final static Integer TOP_RATED = 1;

    public static String getMoviePosterBaseUrl() {
        return MOVIE_POSTER_BASE_URL;
    }

    public static String getMoviePosterSize() {
        return MOVIE_POSTER_SIZE;
    }

    public static Integer getPOPULAR() {
        return POPULAR;
    }

    public static Integer getTopRated() {
        return TOP_RATED;
    }

    public static URL buildUrl(int sortType){
        Uri builtUri = null;
        if(sortType == POPULAR)
            builtUri = Uri.parse(MOVIEDB_BASE_URL+SORT_BY_POPULAR+APIKEY).buildUpon().build();
        if(sortType == TOP_RATED)
            builtUri = Uri.parse(MOVIEDB_BASE_URL+SORT_BY_TOP_RATED+APIKEY).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
