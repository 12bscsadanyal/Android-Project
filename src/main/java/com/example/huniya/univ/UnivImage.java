package com.example.huniya.univ;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by huniya on 5/16/2015.
 */
public class UnivImage extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        ImageView iv = (ImageView) findViewById(R.id.imageView);

        Intent it = getIntent();

        String u = it.getStringExtra("uname");;

        if (u.equals("NUST")) {
            iv.setImageResource(R.drawable.capture);
        }else if(u.equals("Comsats")){
            iv.setImageResource(R.drawable.comsat);
        }
        else if(u.equals("NCA")){
            iv.setImageResource(R.drawable.ncaa);
        }
        else if(u.equals("PNEC")){
            iv.setImageResource(R.drawable.pnecc);
        }
        else if(u.equals("Lahore School Economics")){
            iv.setImageResource(R.drawable.lsee);
        }
        else {
            iv.setImageResource(R.drawable.amc);
        }
    }
}
