package com.example.twitter_clone;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Signup extends AppCompatActivity implements View.OnClickListener{

      EditText  edtusername , edtpassword , edtemail ;
    ImageView imginsta ;
     Button btnsignup , btnlogin ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup_activity);

        edtusername = findViewById(R.id.edtusername);
        edtemail = findViewById(R.id.edtemail);
        imginsta = findViewById(R.id.imginsta);
        btnsignup = findViewById(R.id.btnsignup);
        btnlogin = findViewById(R.id.btnlogin);
        edtpassword = findViewById(R.id.edtpassword);

        btnsignup.setOnClickListener(this);
        btnlogin.setOnClickListener(this);

        if(ParseUser.getCurrentUser() != null){

//            ParseUser.getCurrentUser().logOut();
            transactiontothenextactivity();
        }


        edtpassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keycode, KeyEvent event) {

                if(keycode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    onClick(btnsignup);
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.btnsignup :

                if(edtusername.getText().toString().equals("") || edtemail.getText().toString().equals("") ||
                        edtpassword.getText().toString().equals("")){

                    FancyToast.makeText(Signup.this,"username , password , email required !",FancyToast.LENGTH_LONG,
                            FancyToast.WARNING,true).show();
                }else {
                    final ParseUser parseUser = new ParseUser();
                    parseUser.setUsername(edtusername.getText().toString());
                    parseUser.setPassword(edtpassword.getText().toString());
                    parseUser.setEmail(edtemail.getText().toString());

                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {

                                FancyToast.makeText(Signup.this, parseUser.get("username") + " is signed up successfully",
                                        FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                                transactiontothenextactivity();


                            } else {

                                FancyToast.makeText(Signup.this, e.getMessage(),
                                        FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                            }

                        }
                    });


                }
                break;

            case R.id.btnlogin :

                Intent intent = new Intent(Signup.this,Login.class);
                startActivity(intent);


        }
    }

    public void rootlayouttapped(View v){

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch(Exception e){
            e.printStackTrace();
        }


    }

    private void transactiontothenextactivity(){

        Intent intent= new Intent(Signup.this , SocialMedia.class);
        startActivity(intent);
        finish();
    }
}
