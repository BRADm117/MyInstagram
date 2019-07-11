package com.example.myinstagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myinstagram.PostsAdapter;
import com.example.myinstagram.R;
import com.example.myinstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {

    private SwipeRefreshLayout swipeContainer;
    public Button logoutButton;


    public static final String TAG = "PostsFragment";

    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> mPosts;

    //onCreateView to inflate the view

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //container.removeView(container.findViewById(R.id.logout_btn));

        return inflater.inflate(R.layout.fragment_posts, container, false); //returns appropriate view
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvPosts = view.findViewById(R.id.rvPosts);
        //create the data source //list of different post objects
        mPosts = new ArrayList<>();
        //create the adapter
        adapter = new PostsAdapter(getContext(), mPosts);
        //set the adapter on the recycler view //tells recycler view how to show the contents
        rvPosts.setAdapter(adapter);
        //set the layout manager on the recycler view //how you lay out contents onto the screen
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPosts();

        logoutButton = view.findViewById(R.id.logout_btn);
        logoutButton.setVisibility(View.GONE);


        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    protected void queryPosts(){ //protected allows us to override this method in Profile
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null){
                    Log.d(TAG, "Error with Query");
                    e.printStackTrace();
                    return;
                }
                mPosts.addAll(posts);
                adapter.notifyDataSetChanged();

                for (int i = 0; i < posts.size(); i++) {
                    Post post = posts.get(i);
                    Log.d(TAG, "Post: "
                            + post.getDescription()
                            + ", username: "
                            + posts.get(i).getUser().getUsername()
                    );
                }
            }
        });
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        // Remember to CLEAR OUT old items before appending in the new ones
        adapter.clear();
        // ...the data has come back, add new items to your adapter...
        queryPosts();// Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);
    }


}
