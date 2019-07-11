package com.example.myinstagram;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myinstagram.model.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class PostDetailsActivity extends AppCompatActivity {

    //////statics at the top

    //the movie to display
    Post post;

    //the view objects
    TextView tvHandle;
    TextView tvDescription;
    TextView tvTimeStamp;
    ImageView ivImage;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        context = getApplicationContext();

        //resolve the view objects
        tvHandle = (TextView) findViewById(R.id.tvHandle);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        tvTimeStamp = (TextView) findViewById(R.id.tvTimeStamp);

        //unwrap the movie passed in via intent, using its simple name as a key
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()) );
        //Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        //set the title and overview
        tvHandle.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        tvTimeStamp.setText(getRelativeTimeAgo(post));
        ParseFile image = post.getImage();
        if (image != null){
            Glide.with(context).load(image.getUrl()).into(ivImage);
        }
    }

    public String getRelativeTimeAgo(Post post) {
        String twitterFormat = "dd MMM yyyy HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        long dateMillis = post.getCreatedAt().getTime();
        relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        return relativeDate;
    }
}
