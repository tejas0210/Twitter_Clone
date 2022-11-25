package com.example.twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {

    private EditText edtName, edtSUsername, edtEmail, edtBirthDate, edtSPassword;
    private TextView txtView4;
    private Button btnSCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setTitle("Create an account");

        edtName = findViewById(R.id.edtName);
        edtSUsername = findViewById(R.id.edtSUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtBirthDate = findViewById(R.id.edtBirthDate);
        edtSPassword = findViewById(R.id.edtSPassword);
        txtView4 = findViewById(R.id.txtView4);
        btnSCreateAccount = findViewById(R.id.btnSCreateAccount);

        btnSCreateAccount.setOnClickListener(this::onClick);

        if(ParseUser.getCurrentUser()!=null){
            transitionToFeed();
        }

    }

    @Override
    public void onClick(View pressed) {

        switch (pressed.getId()){
            case R.id.btnSCreateAccount:
                try {
                    final  ParseUser newUser = new ParseUser();
                    newUser.setEmail(edtEmail.getText().toString());
                    newUser.setUsername(edtSUsername.getText().toString());
                    newUser.setPassword(edtSPassword.getText().toString());
//                    newUser.setObjectId(edtBirthDate.getText().toString());
//                    newUser.setObjectId(edtName.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(CreateAccount.this);
                    progressDialog.setMessage("Signing Up in " +newUser.getUsername());
                    progressDialog.show();

                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            try {
                                if(e==null){
                                    transitionToFeed();
                                }
                                else{
                                    Toast.makeText(CreateAccount.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }catch(Exception e1){
                                Toast.makeText(CreateAccount.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });

                }
                catch (Exception e){
                    Toast.makeText(CreateAccount.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtView4:
                Intent intent = new Intent(CreateAccount.this,LogIn.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    void transitionToFeed(){
        Intent intent = new Intent(CreateAccount.this,Feed.class);
        startActivity(intent);
        finish();
    }

}