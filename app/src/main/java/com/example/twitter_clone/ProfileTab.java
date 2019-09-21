package com.example.twitter_clone;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {

    private EditText edtprofilename , edtprofession , edthobbies , edtfavsport , edtbio ;
    private Button btnupdateinfo ;


    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile_tab, container, false);

        edtprofilename = view.findViewById(R.id.edtprofilename);
        edtbio = view.findViewById(R.id.edtbio);
        edtprofession = view.findViewById(R.id.edtprofession);
        edthobbies = view.findViewById(R.id.edthobbies);
        edtfavsport = view.findViewById(R.id.edtfavsport);
        btnupdateinfo = view.findViewById(R.id.btnupdateinfo);

        final ParseUser parseUser = ParseUser.getCurrentUser();

        if(parseUser.get("profileName") == null){

            edtprofilename.setText("");
        }else{
            edtprofilename.setText(parseUser.get("profileName").toString());
        }
        if(parseUser.get("bio") == null){

            edtprofilename.setText("");
        }else{
            edtprofilename.setText(parseUser.get("bio").toString());
        }
        if(parseUser.get("hobbies") == null){

            edtprofilename.setText("");
        }else{
            edtprofilename.setText(parseUser.get("hobbies").toString());
        }
        if(parseUser.get("profession") == null){

            edtprofilename.setText("");
        }else{
            edtprofilename.setText(parseUser.get("profession").toString());
        }
        if(parseUser.get("favsport") == null){

            edtprofilename.setText("");
        }else{
            edtprofilename.setText(parseUser.get("favsport").toString());
        }


        btnupdateinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                parseUser.put("profileName" , edtprofilename.getText().toString());
                parseUser.put("bio" , edtbio.getText().toString());
                parseUser.put("profession" , edtprofession.getText().toString());
                parseUser.put("hobbies" , edthobbies.getText().toString());
                parseUser.put("favsport" , edtfavsport.getText().toString());

                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if(e == null){

                            FancyToast.makeText(getContext(), "Info Updated!",
                                    FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                        }else{

                            FancyToast.makeText(getContext(), e.getMessage(),
                                    FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                        }
                    }
                });
            }

        });

        return view;
    }

}
