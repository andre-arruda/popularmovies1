package br.com.project.movieapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieInfoActivity extends AppCompatActivity {

    ImageView ivPosterImage;
    TextView tvTitle;
    TextView tvReleaseDate;
    TextView tvRate;
    TextView tvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        Bundle data = getIntent().getExtras();

        String posterPath = data.getString(getString(R.string.poster));
        String title = data.getString(getString(R.string.original_title));
        String rate = String.valueOf(data.getDouble(getString(R.string.vote_average)));
        String releaseDate = data.getString(getString(R.string.release_date));
        String overview = data.getString(getString(R.string.overview));

        ivPosterImage = (ImageView) findViewById(R.id.idMovieInfoPoster);
        tvTitle = (TextView) findViewById(R.id.idMovieInfoTitle);
        tvRate = (TextView) findViewById(R.id.idMovieInfoRate);
        tvReleaseDate = (TextView) findViewById(R.id.idMovieInfoReleaseDate);
        tvOverview = (TextView) findViewById(R.id.idMovieInfoOverview);


        Picasso.with(this).load(posterPath).into(ivPosterImage);
        tvTitle.setText(title);
        tvRate.setText(rate);
        tvReleaseDate.setText(releaseDate);
        tvOverview.setText(overview);

    }
}
