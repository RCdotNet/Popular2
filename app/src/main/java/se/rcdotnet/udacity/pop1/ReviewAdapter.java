package se.rcdotnet.udacity.pop1;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Laszlo_HP_Notebook on 2018-04-22.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    ReviewList mReviewList;
    Context mContext;
    final private ReviewAdapterClickHandler mClickHandler;

    public interface ReviewAdapterClickHandler {
        void onClick(Object data);
    }

    public ReviewAdapter(Context context, ReviewList r, ReviewAdapterClickHandler listener) {
        mReviewList = r;
        mContext = context;
        mClickHandler = listener;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View retView = inflater.inflate(R.layout.review_item_view, parent, false);
        return new ReviewHolder(retView);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        if (mReviewList.reviews.size() == 0) return;
        ReviewItem item = mReviewList.reviews.get(position);
        holder.mReview.setText(item.content);
        holder.mAuthor.setText(item.author);
        holder.itemView.setTag(item);
    }

    public void SwapData( ReviewList r) {
        mReviewList = r;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mReviewList == null) return 0;
        else return mReviewList.reviews.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mAuthor;
        TextView mReview;

        public ReviewHolder(View itemView) {
            super(itemView);
            mAuthor = itemView.findViewById(R.id.authorTextView);
            mReview = itemView.findViewById(R.id.reviewTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(v.getTag());
        }
    }

}
