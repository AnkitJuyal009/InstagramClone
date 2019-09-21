package com.example.twitter_clone;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePicture extends Fragment implements View.OnClickListener {

    private ImageView img;
    private EditText edtdescription;
    private Button btnshareimage;

    Bitmap receivedImageBitmap ;


    public SharePicture() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_picture, container, false);

        img = view.findViewById(R.id.img);
        edtdescription = view.findViewById(R.id.edtdescription);
        btnshareimage = view.findViewById(R.id.btnshareimage);
        img.setOnClickListener(SharePicture.this);

        btnshareimage.setOnClickListener(SharePicture.this);
        return view;

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.img :
                //android api level 23 or above asks for runtime permissions so this if statement is for this to check if the permissions are granted.

                if(Build.VERSION.SDK_INT >= 23 &&
                        ActivityCompat.checkSelfPermission(getContext() ,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

                    requestPermissions(new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE},
                            1000);
                }else{

                    getChosenImage();
                }
                break;
            case R.id.btnshareimage :
                if(receivedImageBitmap != null){//checking if the image is received

                   if(edtdescription.getText().toString() .equals("")){

                       FancyToast.makeText(getContext(),"ERROR: you must specify a description.",
                               Toast.LENGTH_SHORT  , FancyToast.ERROR , true).show();
                   }else{

                       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // usage of this is uploading the image to the server as
                       // image is so large that we cannot convert it into array so byte will work out
                       receivedImageBitmap.compress(Bitmap.CompressFormat.PNG , 100 , byteArrayOutputStream);
                       byte[] bytes = byteArrayOutputStream.toByteArray();
                       ParseFile parseFile = new ParseFile("pic.png" , bytes);
                       ParseObject parseObject = new ParseObject("photo");
                       parseObject.put("picture" , parseFile);
                       parseObject.put("image_desc" , edtdescription.getText().toString());
                       parseObject.put("username" , ParseUser.getCurrentUser().getUsername());
                       final ProgressBar progressBar = new ProgressBar(getContext());
                       progressBar.setVisibility(View.VISIBLE);
                       parseObject.saveInBackground(new SaveCallback() {
                           @Override
                           public void done(ParseException e) {

                               if(e == null){

                                   FancyToast.makeText(getContext(), "Done !!" ,
                                           FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                               }else{

                                   FancyToast.makeText(getContext(), "Unknown Error: " +e.getMessage() ,
                                           FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                               }
                               progressBar.setVisibility(View.GONE);

                           }
                       });



                   }
                }else{

                    FancyToast.makeText(getContext(),"ERROR: you must select an image.",
                            Toast.LENGTH_SHORT  , FancyToast.ERROR , true).show();
                }
                break;
        }

    }

    private void getChosenImage(){

       // FancyToast.makeText(getContext() , "Now we can access the images" ,
               // Toast.LENGTH_SHORT , FancyToast.SUCCESS , true ).show();

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.
                EXTERNAL_CONTENT_URI); //to access the images of gallery..
        startActivityForResult(intent , 2000);

    }
    // we want to see if the above if statement gives us permission result or not

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1000){

            if(grantResults.length > 0 && grantResults[0] ==
            PackageManager.PERMISSION_DENIED){

                getChosenImage();


            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2000) {
            if(resultCode == Activity.RESULT_OK){

                //do something with your captured image
                try{

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor  cursor = getActivity().getContentResolver().query(selectedImage ,
                            filePathColumn , null , null ,null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                     receivedImageBitmap = BitmapFactory.decodeFile(picturePath);

                    img.setImageBitmap(receivedImageBitmap);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
