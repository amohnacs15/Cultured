package com.androidtitan.hotspots.main.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidtitan.hotspots.R;
import com.androidtitan.hotspots.main.model.Item;
import com.androidtitan.hotspots.main.presenter.ImageDownloadPresenter;
import com.androidtitan.hotspots.main.ui.MainActivity;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by amohnacs on 3/3/16.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    @Inject ImageDownloadPresenter imageDownloader;

    private Context context;
    private List<Item> trackList;


    @Inject
    public ImageAdapter(Context context, List<Item> adapterTrackList) {

        this.context = context;
        this.trackList = adapterTrackList;

        ((MainActivity)context).getPresenterComponent().inject(this);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView albumImage;
        public RelativeLayout relativeLayout;
        public TextView trackText;
        public TextView artistText;
        public TextView albumText;

        public ViewHolder(View itemView) {
            super(itemView);

            albumImage = (ImageView) itemView.findViewById(R.id.albumImageView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.infoRelativeLayout);
            trackText = (TextView) itemView.findViewById(R.id.trackTextView);
            artistText = (TextView) itemView.findViewById(R.id.artistTextView);
            albumText = (TextView) itemView.findViewById(R.id.albumTextView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.trackText.setText(trackList.get(position).getName());
        holder.albumText.setText(trackList.get(position).getAlbum().getName());

        String artistString = "";
        for(int i = 0; i < trackList.get(position).getArtists().size(); i++) {
            if(i == 0) {
                artistString += trackList.get(position).getArtists().get(i).getName();
            } else {
                artistString += ", " + trackList.get(position).getArtists().get(i).getName();
            }
        }
        holder.artistText.setText(artistString);

        Log.e(TAG, trackList.get(position).getAlbum().getImages().get(0).getHeight()
                + " x " + trackList.get(position).getAlbum().getImages().get(0).getWidth());

        imageDownloader.imageDownload(trackList.get(position).getAlbum().getImages().get(0).getUrl(),
                holder.albumImage);


    }

    @Override
    public int getItemCount() {
     return trackList.size();
    }


}