package net.husnilkamil.popularmoview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.husnilkamil.popularmoview.data.Movie;

import java.util.ArrayList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder> {

    private ArrayList<Movie> mMovieList = new ArrayList<>();
    private OnWeatherItemClickListener mClickListener;

    public MovieListAdapter(OnWeatherItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setMovieDataSource(ArrayList<Movie> movieList){
        mMovieList.clear();
        mMovieList.addAll(movieList);
        notifyDataSetChanged();
    }

    @Override
    public MovieListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_movie_item, parent, false);
        return new MovieListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieListViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        if(mMovieList != null){
            return mMovieList.size();
        }
        return 0;
    }

    public interface OnWeatherItemClickListener
    {
        void weatherItemClick(Movie movie);
    }


    public class MovieListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final String TAG = MovieListViewHolder.class.getName();

        ImageView mMovieImageView;
        Context context;

        public MovieListViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mMovieImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie){
            Picasso.with(context)
                    .load(movie.getPoster())
                    .into(mMovieImageView);
            Log.d(TAG, movie.getPoster());
        }

        @Override
        public void onClick(View view) {
            Movie movie = mMovieList.get(getAdapterPosition());
            mClickListener.weatherItemClick(movie);
        }
    }
}
