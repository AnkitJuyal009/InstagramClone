package com.example.twitter_clone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Login extends AppCompatActivity {

    ImageView imginsta1;
    EditText edtloginusername , edtloginpassword ;
    Button btnlogin1;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        imginsta1 = findViewById(R.id.imginsta1);
        edtloginusername = findViewById(R.id.edtloginusername);
        edtloginpassword = findViewById(R.id.edtloginpassword);
        btnlogin1 = findViewById(R.id.btnlogin1);

        btnlogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ParseUser.logInInBackground(edtloginusername.getText().toString(), edtloginpassword.getText().toString(),
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {

                                if(user != null && e==null ){

                                    FancyToast.makeText(Login.this, user.getUsername() + " is logged in successfully" ,
                                            FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                                    transactiontothenextactivity();


                                }else{

                                    FancyToast.makeText(Login.this, e.getMessage(),
                                            FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();


                                }


                            }
                        });


            }
        });



    }
    private void transactiontothenextactivity(){

        Intent intent= new Intent(Login.this , SocialMedia.class);
        startActivity(intent);
        finish();
    }
}
