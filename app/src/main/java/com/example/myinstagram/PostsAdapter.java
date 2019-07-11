package com.example.myinstagram;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myinstagram.model.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;


public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvHandle;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvTimeStamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHandle = itemView.findViewById(R.id.tvHandle);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            //tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //gets item position
                    int position = getAdapterPosition();
                    //make sure the position is valid, i.e actually exists in the view
                    if (position != RecyclerView.NO_POSITION){
                        //get the movie at the position, this won't work if the class is static
                        Post post = posts.get(position);
                        //create intent for the new activity
                        Intent intent = new Intent(context, PostDetailsActivity.class);
                        //serialize the movie using parceler, use its short name as a key
                        intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post)); //parcels is library thing not Android thing, wraps and unwraps non primitive data
                        //show the activity
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void bind(Post post) {
            tvHandle.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            //tvTimeStamp.setText(getRelativeTimeAgo(String.valueOf(post.getCreatedAt().getTime())));
            if (image != null){
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
            tvDescription.setText(post.getDescription());
        }

    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

}
