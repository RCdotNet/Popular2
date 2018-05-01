package se.rcdotnet.udacity.pop1;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Laszlo_HP_Notebook on 2018-04-22.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    VideoList videoList = null;
    Context mContext;
    final private VideoAdapterClickHandler mClickHandler;

    public interface VideoAdapterClickHandler {
        void onClick(Object data);
    }

    public VideoAdapter(Context context, VideoList list, VideoAdapterClickHandler listener) {
        videoList = list;
        mContext = context;
        mClickHandler = listener;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View retView = inflater.inflate(R.layout.video_item_view, parent, false);
        return new VideoHolder(retView);
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        if (videoList.videos.size()==0) return;
        VideoListItem item = videoList.videos.get((position));
        Picasso.get()
                .load("https://img.youtube.com/vi/"+item.getKey()+"/0.jpg")
                .noFade()
                .into(holder.mPreview);
        holder.itemView.setTag(item);
    }

    public void SwapData(VideoList l) {
        videoList = l;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        if (videoList == null) return 0;
        else return videoList.videos.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mPreview;

        public VideoHolder(View itemView) {
            super(itemView);
            mPreview = (ImageView) itemView.findViewById(R.id.videoPreviewView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(v.getTag());
        }
    }

}
