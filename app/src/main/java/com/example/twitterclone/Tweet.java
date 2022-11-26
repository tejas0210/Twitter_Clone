package com.example.twitterclone;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class Tweet extends AppCompatActivity implements View.OnClickListener {

    private EditText edtTweet;
    private Button btnTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        edtTweet = findViewById(R.id.edtTweet);
        btnTweet = findViewById(R.id.btnTweet);

        btnTweet.setOnClickListener(this);
    }

    @Override
    public void onClick(View btnTweet) {
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
}