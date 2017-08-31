package br.com.project.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.project.movieapp.R;
import br.com.project.movieapp.WebService.WebServiceUtils;
import br.com.project.movieapp.model.MovieEntity;

/**
 * Created by lsitec211.arruda on 28/08/17.
 */

public class ListMovieAdapter extends RecyclerView.Adapter<ListMovieAdapter.ViewHolder> {

    private List<MovieEntity> movieList = new ArrayList<MovieEntity>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    public ListMovieAdapter(Context context, List<MovieEntity> movieList) {
        this.mInflater = LayoutInflater.from(context);
        this.movieList = movieList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MovieEntity movieEntity = movieList.get(position);

        holder.tvMovieTitle.setText(movieEntity.getTitle());
        String posterPath = WebServiceUtils.getMoviePosterBaseUrl() +
                WebServiceUtils.getMoviePosterSize() +
                movieEntity.getPosterPath();
        Picasso.with(context).load(posterPath).into(holder.ivMoviePoster);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvMovieTitle;
        public ImageView ivMoviePoster;

        public ViewHolder(View itemView) {
            super(itemView);
            tvMovieTitle = (TextView) itemView.findViewById(R.id.idTVTitleMovie);
            ivMoviePoster = (ImageView) itemView.findViewById(R.id.idIVPosterMovie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public MovieEntity getItem(int id) {
        return movieList.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
