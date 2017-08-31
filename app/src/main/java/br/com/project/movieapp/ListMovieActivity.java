package br.com.project.movieapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

import br.com.project.movieapp.WebService.WebServiceUtils;
import br.com.project.movieapp.adapter.ListMovieAdapter;
import br.com.project.movieapp.model.MovieEntity;
import br.com.project.movieapp.utils.JSONUtils;

public class ListMovieActivity extends AppCompatActivity implements ListMovieAdapter.ItemClickListener{

    ListMovieAdapter movieAdapter;
    RecyclerView rvListMovies;

    private int refreshStatus;

    private ProgressBar mLoadingIndicator;
    private TextView tvNoMovies;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movie);

        context = this;

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        rvListMovies = (RecyclerView) findViewById(R.id.idRVListMovies);
        tvNoMovies = (TextView) findViewById(R.id.idTVNoMovies);

        int columns = 2;

        rvListMovies.setLayoutManager(new GridLayoutManager(this, columns));

        if (isInternetConnected()) {
            tvNoMovies.setVisibility(TextView.INVISIBLE);
            rvListMovies.setVisibility(RecyclerView.VISIBLE);
            refreshStatus = WebServiceUtils.getPOPULAR();
            URL movieSearchUrl = WebServiceUtils.buildUrl(WebServiceUtils.getPOPULAR());
            new MovieSearchTask().execute(movieSearchUrl);
        } else {
            tvNoMovies.setVisibility(TextView.VISIBLE);
            rvListMovies.setVisibility(RecyclerView.INVISIBLE);
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        MovieEntity movie = movieAdapter.getItem(position);
        Intent intent = new Intent(this, MovieInfoActivity.class);
        Bundle extras = new Bundle();

        extras.putString(getString(R.string.original_title), movie.getOriginalTitle());
        String posterPath = WebServiceUtils.getMoviePosterBaseUrl() +
                WebServiceUtils.getMoviePosterSize() +
                movie.getPosterPath();
        extras.putString(getString(R.string.poster), posterPath);
        extras.putString(getString(R.string.overview), movie.getOverview());
        extras.putDouble(getString(R.string.vote_average), movie.getVoteAverage());
        extras.putString(getString(R.string.release_date), movie.getReleaseDate());

        intent.putExtras(extras);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();

        if(isInternetConnected()) {
            if (itemThatWasClickedId == R.id.idMenuTopRated) {
                tvNoMovies.setVisibility(TextView.INVISIBLE);
                rvListMovies.setVisibility(RecyclerView.VISIBLE);
                refreshStatus = WebServiceUtils.getTopRated();
                URL movieSearchUrl = WebServiceUtils.buildUrl(WebServiceUtils.getTopRated());
                new MovieSearchTask().execute(movieSearchUrl);
                return true;
            } else if (itemThatWasClickedId == R.id.idMenuPopularity) {
                tvNoMovies.setVisibility(TextView.INVISIBLE);
                rvListMovies.setVisibility(RecyclerView.VISIBLE);
                refreshStatus = WebServiceUtils.getPOPULAR();
                URL movieSearchUrl = WebServiceUtils.buildUrl(WebServiceUtils.getPOPULAR());
                new MovieSearchTask().execute(movieSearchUrl);
                return true;
            } else if (itemThatWasClickedId == R.id.idMenuRefresh){
                URL movieSearchUrl = WebServiceUtils.buildUrl(refreshStatus);
                new MovieSearchTask().execute(movieSearchUrl);
            }
        } else {
            showAlertDialog(getCurrentFocus());
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isInternetConnected(){

        boolean connected;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            connected = true;
        } else {
            connected = false;
        }
        return connected;

    }

    public void showAlertDialog(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.something_wrong));
        builder.setMessage(getString(R.string.check_connection));

        builder.setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class MovieSearchTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String movieSearchResults = null;
            try {
                movieSearchResults = WebServiceUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieSearchResults;
        }

        @Override
        protected void onPostExecute(String movieSearchResults) {
            JSONUtils listMovies = new JSONUtils();

            movieAdapter = new ListMovieAdapter(getBaseContext(), listMovies.getMovies(movieSearchResults));
            movieAdapter.setClickListener((ListMovieActivity.this));
            rvListMovies.setAdapter(movieAdapter);

            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

}
