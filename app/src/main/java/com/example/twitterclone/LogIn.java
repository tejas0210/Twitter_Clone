package com.example.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LogIn extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsername, edtPassword;
    private Button btnLogIn, btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);

        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnLogIn = findViewById(R.id.btnLogIn);

        btnLogIn.setOnClickListener(this);
        btnCreateAccount.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null){
            transitionToFeed();
        }

    }

    @Override
    public void onClick(View btnPressed) {
        switch (btnPressed.getId()){
            case R.id.btnLogIn:
                try {
                    ParseUser.logInInBackground(edtUsername.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if(user!=null && e==null){
                                transitionToFeed();
                            }
                            else{
                                Toast.makeText(LogIn.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                catch (Exception e){
                    Toast.makeText(LogIn.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnCreateAccount:
                Intent intent1 = new Intent(LogIn.this,CreateAccount.class);
                startActivity(intent1);
                finish();
                break;

        }
    }

    void transitionToFeed(){
        Intent intent = new Intent(LogIn.this,Feed.class);
        startActivity(intent);
        finish();
    }
}