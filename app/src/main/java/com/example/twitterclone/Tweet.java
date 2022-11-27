package com.example.twitterclone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tweet extends AppCompatActivity implements View.OnClickListener {

    private EditText edtTweet;
    private Button btnTweet, btnPosts;
    private ListView listView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        edtTweet = findViewById(R.id.edtTweet);
        btnTweet = findViewById(R.id.btnTweet);
        btnPosts = findViewById(R.id.btnPosts);
        listView2 = findViewById(R.id.listView2);

        btnTweet.setOnClickListener(this::sendTweets);
        btnPosts.setOnClickListener(this::onClick);
    }


    public void sendTweets(View btnTweet){
        ParseObject newObject = new ParseObject("MyTweets");
        newObject.put("Tweet",edtTweet.getText().toString());
        newObject.put("User", ParseUser.getCurrentUser().getUsername());

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Sending tweet");
        dialog.show();

        newObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(Tweet.this,"Done", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Tweet.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View btnPosts) {
        final ArrayList<HashMap<String,String>> tweetList = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(Tweet.this, tweetList, android.R.layout.simple_list_item_2,new String[]{"tweetUser","tweetValue"},new int[]{android.R.id.text1,android.R.id.text2});
        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweets");
            parseQuery.whereContainedIn("User",ParseUser.getCurrentUser().getList("Following"));
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(objects.size()>0 && e==null){
                        for(ParseObject tweetObject:objects){
                            HashMap<String,String> userTweet = new HashMap<>();
                            userTweet.put("tweetUser",tweetObject.getString("User"));
                            userTweet.put("tweetValue",tweetObject.getString("Tweet"));
                            tweetList.add(userTweet);
                        }
                        listView2.setAdapter(adapter);
                    }
                }
            });
        }
        catch(Exception e){
            e.getStackTrace();
        }
    }
}