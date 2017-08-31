package br.com.project.movieapp.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.project.movieapp.model.MovieEntity;

/**
 * Created by lsitec211.arruda on 28/08/17.
 */

public class JSONUtils {

    public List<MovieEntity> getMovies(String response) {

        JSONObject json = null;
        JSONArray movies = null;

        ArrayList<MovieEntity> movieList = new ArrayList<MovieEntity>();

        try {
            json = new JSONObject(response);

            movies = json.getJSONArray("results");

            MovieEntity movie = null;

            for (int i=0 ; i < movies.length() ; i++){

                movie = new MovieEntity();

                JSONObject objJSON = (JSONObject) movies.get(i);

                movie.setId(objJSON.getInt("id"));
                movie.setVoteAverage(objJSON.getDouble("vote_average"));
                movie.setPopularity(objJSON.getDouble("popularity"));
                movie.setTitle(objJSON.getString("title"));
                movie.setOriginalTitle(objJSON.getString("original_title"));
                movie.setOverview(objJSON.getString("overview"));
                movie.setPosterPath(objJSON.getString("poster_path"));
                movie.setReleaseDate(objJSON.getString("release_date"));

                movieList.add(movie);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieList;

    }


}
