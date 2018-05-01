package se.rcdotnet.udacity.pop1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ReviewActivity extends AppCompatActivity {
    TextView mAuthor;
    TextView mReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        mAuthor = findViewById(R.id.authorText);
        mReview = findViewById(R.id.reviewText);
        ReviewItem item;
        if ((item = getIntent().getParcelableExtra("review")) != null){
            mAuthor.setText(item.getAuthor());
            mReview.setText(item.getContent());
        }
    }
}
