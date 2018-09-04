package com.briatka.pavol.skymoviesproject.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.briatka.pavol.skymoviesproject.R;
import com.briatka.pavol.skymoviesproject.customobjects.MovieObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<MovieObject> data;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_poster_thumbnail)
        ImageView moviePoster;
        @BindView(R.id.movie_genre)
        TextView movieGenre;
        @BindView(R.id.placeholder_name)
        TextView placeholderName;

        private Boolean isLastItem = false;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setLastItem(Boolean lastItem) {
            isLastItem = lastItem;
        }

        public Boolean getLastItem() {
            return isLastItem;
        }
    }

    public MovieAdapter(List<MovieObject> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        final MovieObject currentObject = data.get(position);

        viewHolder.movieGenre.setText(currentObject.getGenre());
        String placeholderText = "(" + currentObject.getTitle() + ")";
        viewHolder.placeholderName.setText(placeholderText);
        Picasso.get()
                .load(currentObject.getPoster())
                .placeholder(R.drawable.ic_image_36dp)
                .error(R.drawable.ic_broken_image)
                .into(viewHolder.moviePoster);

        if (position == data.size() - 1) {
            viewHolder.setLastItem(true);
            viewHolder.itemView.setTag("last_item");
        } else {
            viewHolder.setLastItem(false);
        }

    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    public void setData(List<MovieObject> passedData) {
        data = passedData;
        notifyDataSetChanged();
    }
}
