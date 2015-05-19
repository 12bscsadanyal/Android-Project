package com.example.huniya.univ;

/**
 * Created by huniya on 5/15/2015.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException; import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle; import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ShowUniv extends Activity {
    private String jsonResult;
    private String eng, med, bus, arch, l, opt = null;
    private String url = AppConfig.URL_View;
    private ListView listView;
//    private Button backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listView = (ListView) findViewById(R.id.listView1);
//        backButton = (Button) findViewById(R.id.backButton);
        Intent it = getIntent();
        if (it.hasExtra("eng")) {
            eng = it.getStringExtra("eng");
        }
        if (it.hasExtra("med")) {
            med = it.getStringExtra("med");
        }

        if (it.hasExtra("bus")) {
            bus = it.getStringExtra("bus");
        }

        if (it.hasExtra("arch")) {
            arch = it.getStringExtra("arch");
        }

        opt = it.getStringExtra("opt");
        l = it.getStringExtra("Location");

//        backButton.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View view) {
//                Intent i = new Intent(ShowUniv.this,
//                        UnivFilter.class);
//                startActivity(i);
//                finish();
//            }
//        });

        accessWebService();
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

    public void accessWebService() {
        JsonReadTask task = new JsonReadTask();   // passes values for the urls string array
        task.execute(new String[]{url});
    }    // build hash set for list view

    public void ListDrwaer() {
        List<Map<String, String>> univList = new ArrayList<Map<String, String>>();
        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("univ_info");



            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("univ_name");
                String sector = jsonChildNode.optString("sector");
                String hostels = jsonChildNode.optString("hostels");
                String location = jsonChildNode.optString("location");
                String fields = jsonChildNode.optString("fields");
                String opt_for = jsonChildNode.optString("opt_for");
                String outPut = name;

                if((l.equals(location)||l.equals(""))&&opt.equals(opt_for)) {

                    if(arch!=null){
                        if(arch.equals(fields)){
                            univList.add(createUniv("universities", outPut));
                        }
                    }
                    if(eng!=null){
                        if(eng.equals(fields)){
                            univList.add(createUniv("universities", outPut));
                        }
                    }
                    if(med!=null){
                        if(med.equals(fields)){
                            univList.add(createUniv("universities", outPut));
                        }
                    }
                    if(bus!=null){
                        if(bus.equals(fields)){
                            univList.add(createUniv("universities", outPut));
                        }
                    }


                }


            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, univList, android.R.layout.simple_list_item_1, new String[]{"universities"}, new int[]{android.R.id.text1});
        listView.setAdapter(simpleAdapter);

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



    }
    private HashMap<String, String> createUniv(String name, String number) {
        HashMap<String, String> univInfo = new HashMap<String, String>();
        univInfo.put(name, number);
        return univInfo;
    }


}