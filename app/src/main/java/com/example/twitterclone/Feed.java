package com.example.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class Feed extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> tUsers;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        setTitle("Tweet");

        listView = findViewById(R.id.listView);
        tUsers = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked,tUsers);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this::onItemClick);

        TextView txtLoadingUsers = findViewById(R.id.txtLoadingUsers);

        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) {
                    if(e==null && users.size() > 0){
                        for(ParseUser user: users){
                            tUsers.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                        txtLoadingUsers.animate().alpha(0).setDuration(700);
                        txtLoadingUsers.setVisibility(View.VISIBLE);

                        for(String user: tUsers){
                            if(ParseUser.getCurrentUser().getList("Following")!=null){
                                if(ParseUser.getCurrentUser().getList("Following").contains(user)){
                                    listView.setItemChecked(tUsers.indexOf(user),true);
                                }

                            }
                        }
                    }
                }
            });
        }
        catch (Exception e){
            Toast.makeText(Feed.this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                if (item.getItemId() == R.id.logout){
                    ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                        @Override
                        public void done(ParseException e) {
                            Intent intent = new Intent(Feed.this, LogIn.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
                break;

            case R.id.tweet:
                Intent intent = new Intent(Feed.this,Tweet.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        CheckedTextView checkedTextView = (CheckedTextView) view;

        if(checkedTextView.isChecked()){
            Toast.makeText(Feed.this,tUsers.get(position) +" is now Followed", Toast.LENGTH_SHORT).show();
            ParseUser.getCurrentUser().add("Following",tUsers.get(position));
        }
        else{
            Toast.makeText(Feed.this,tUsers.get(position) +" is now Unfollowed", Toast.LENGTH_SHORT).show();
            ParseUser.getCurrentUser().getList("Following").remove(tUsers.get(position));
            List currentUserFollowing = ParseUser.getCurrentUser().getList("Following");
            ParseUser.getCurrentUser().remove("Following");
            ParseUser.getCurrentUser().put("Following",currentUserFollowing);



        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(Feed.this,"Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}