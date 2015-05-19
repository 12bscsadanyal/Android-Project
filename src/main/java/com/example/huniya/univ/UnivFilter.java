package com.example.huniya.univ;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by huniya on 5/15/2015.
 */
public class UnivFilter extends Activity{

    Spinner spinner;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.univform);
        Button button = (Button) findViewById(R.id.button);



        button.setOnClickListener(

                new Button.OnClickListener() {
                    public void onClick(View v) {

                         CheckBox responseCheckbox = (CheckBox) findViewById(R.id.checkBox);
                        CheckBox responseCheckbox2 = (CheckBox) findViewById(R.id.checkBox2);
                        CheckBox responseCheckbox3 = (CheckBox) findViewById(R.id.checkBox3);
                        CheckBox responseCheckbox4 = (CheckBox) findViewById(R.id.checkBox4);


                        RadioButton radio= (RadioButton)findViewById(R.id.radioButton);
                        RadioButton radio2= (RadioButton)findViewById(R.id.radioButton2);

                        boolean check1 = responseCheckbox.isChecked();
                        boolean check2 = responseCheckbox2.isChecked();
                        boolean check3 = responseCheckbox3.isChecked();
                        boolean check4 = responseCheckbox4.isChecked();

                        boolean r1=radio.isChecked();
                        boolean r2=radio2.isChecked();

                        EditText city = (EditText) findViewById(R.id.editText);
                        String l = city.getText().toString();

                        Intent intent = new Intent(UnivFilter.this, ShowUniv.class);

                        if(r1)
                            intent.putExtra("opt", "UG");
                        else if(r2)
                            intent.putExtra("opt", "PG");

                        if(check1)
                            intent.putExtra("arch", "Architecture");
                        if(check2)
                            intent.putExtra("eng", "Engineering or Computer Science");
                        if(check3)
                            intent.putExtra("med", "Medical or Bio Sciences");
                        if(check4)
                            intent.putExtra("bus", "Business");

                        intent.putExtra("Location", l);

                        startActivity(intent);
                        finish();

                    }
                }
        );


    }




}
