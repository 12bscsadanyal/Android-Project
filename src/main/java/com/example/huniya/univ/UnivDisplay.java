package com.example.huniya.univ;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huniya on 5/15/2015.
 */
public class UnivDisplay extends Activity {

    private RatingBar ratingBar;
    private TextView txtAvRatingValue;
    private String jsonResult;
    private String url = AppConfig.URL_Display;
    private String Rurl = AppConfig.URL_Ratings;
    private ListView listView;
    private String uname=null;
    int ratings=0;
    int no=0;
    float finalratings=0;
    float r=0;



    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.university);

        listView = (ListView) findViewById(R.id.list);
        Intent it = getIntent();
        uname=it.getStringExtra("Univ");
        TextView name=(TextView) findViewById(R.id.textView5);
        name.setText(uname);


        accessWebService2();

        ratingBar= (RatingBar) findViewById(R.id.ratingBar);

        txtAvRatingValue = (TextView) findViewById(R.id.textView7);

        accessWebService();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                r = rating;

                finalratings = (ratings + r) / (no + 1);
                txtAvRatingValue.setText(String.valueOf(finalratings) + " avg ratings");


            }
        });


        Button n = (Button) findViewById(R.id.button2);
        n.setOnClickListener(

                new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                            sendIntent.putExtra("sms_body", "Hello. Notifications turned on.");
                            sendIntent.setType("vnd.android-dir/mms-sms");
                            startActivity(sendIntent);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),
                                    "SMS failed, please try again later!",
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                });

        Button image = (Button) findViewById(R.id.im);
        image.setOnClickListener(

                new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(UnivDisplay.this, UnivImage.class);
                        i.putExtra("uname", uname);

                        startActivity(i);


                    }

                });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }    // Async Task to access the web

    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {     // e.printStackTrace();

                Toast.makeText(getApplicationContext(),
                        "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            ListDrwaer();
        }
    }// end async task

    private class JsonReadRev extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {     // e.printStackTrace();

                Toast.makeText(getApplicationContext(),
                        "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            ListRatings();
        }
    }// end async task

    public void accessWebService() {
        JsonReadTask task = new JsonReadTask();   // passes values for the urls string array
        task.execute(new String[]{url});
    }    // build hash set for list view

    public void accessWebService2() {
        JsonReadRev task = new JsonReadRev();   // passes values for the urls string array
        task.execute(new String[]{Rurl});
    }
    public void ListDrwaer() {
        List<Map<String, String>> univList = new ArrayList<Map<String, String>>();
        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("univ_details");



            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("univ_name");
                String deadline = jsonChildNode.optString("deadline");
                String details = jsonChildNode.optString("details");
                String outPut = details+"\n\n"+deadline;

                if(uname.equals(name)) {
                    univList.add(createUniv("univ", outPut));
                }

            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, univList, android.R.layout.simple_list_item_1, new String[]{"univ"}, new int[]{android.R.id.text1});
        listView.setAdapter(simpleAdapter);
/*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String sText = ((TextView) view).getText().toString();
                Intent newActivity = new Intent(ShowUniv.this, UnivDisplay.class);
                newActivity.putExtra("Univ", sText);
                startActivity(newActivity);


            }

            @SuppressWarnings("unused")
            public void onClick(View v) {
            }

            ;
        });
*/


    }

    public void ListRatings() {
        List<Map<String, String>> univList = new ArrayList<Map<String, String>>();
        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("univ_reviews");



            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String uniname = jsonChildNode.optString("univ_name");
                String reviews = jsonChildNode.optString("reviews");
                String Number = jsonChildNode.optString("Number");
                String outPut = "";

                if(uname.equals(uniname)) {
                    ratings=Integer.parseInt(reviews);
                    no=Integer.parseInt(Number);

                }
            }
            finalratings=(ratings)/(no);
            txtAvRatingValue.setText(String.valueOf(finalratings)+" avg ratings");

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }


/*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String sText = ((TextView) view).getText().toString();
                Intent newActivity = new Intent(ShowUniv.this, UnivDisplay.class);
                newActivity.putExtra("Univ", sText);
                startActivity(newActivity);


            }

            @SuppressWarnings("unused")
            public void onClick(View v) {
            }

            ;
        });
*/


    }



    private HashMap<String, String> createUniv(String name, String number) {
        HashMap<String, String> univInfo = new HashMap<String, String>();
        univInfo.put(name, number);
        return univInfo;
    }





}


