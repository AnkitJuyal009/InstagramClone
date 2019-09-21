package com.example.twitter_clone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

import static java.security.AccessController.getContext;


public class SocialMedia extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private TabAdapter tabadapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.social_media_activity);
        setTitle("My Social Activity");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewpager = findViewById(R.id.viewpager);
        tabadapter = new TabAdapter(getSupportFragmentManager());
        viewpager.setAdapter(tabadapter);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewpager, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.postImageItem) {

            if (Build.VERSION.SDK_INT >= 23 &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]
                                {Manifest.permission.READ_EXTERNAL_STORAGE},
                        3000);
            } else {

                captureImage();
            }
        }else{

            if(item.getItemId() == R.id.logoutUserItem) {

                ParseUser.getCurrentUser().logOut();
                finish();
                Intent intent = new Intent(this , Signup.class);
                startActivity(intent);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 3000){

            if(grantResults.length > 0 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED){

                captureImage();
            }
        }
    }

    private void captureImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.
                EXTERNAL_CONTENT_URI); //to access the images of gallery..
        startActivityForResult(intent , 4000);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 4000 && resultCode == RESULT_OK && data != null){

            try{

                Uri capturedImage = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver()
                ,capturedImage);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG , 100 , byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();

                ParseFile parseFile = new ParseFile("pic.png" , bytes);
                ParseObject parseObject = new ParseObject("photo");
                parseObject.put("picture" , parseFile);
                parseObject.put("username" , ParseUser.getCurrentUser().getUsername());
                final ProgressBar progressBar = new ProgressBar(this);
                progressBar.setVisibility(View.VISIBLE);
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if(e == null){

                            FancyToast.makeText(SocialMedia.this, "Done !!" ,
                                    FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                        }else{

                            FancyToast.makeText(SocialMedia.this, "Unknown Error: " +e.getMessage() ,
                                    FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                        }
                        progressBar.setVisibility(View.GONE);

                    }
                });




            }catch(Exception e){

                e.printStackTrace();
            }


        }
    }
}
