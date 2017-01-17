package com.example.vitcarazvan.lab5android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;

import com.example.vitcarazvan.lab5android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

//import static com.example.vitcarazvan.lab5android.R.id.tagNameInput;
//import static com.example.vitcarazvan.lab5android.R.id.cityInput;
//import static com.example.vitcarazvan.lab5android.R.id.sprayCanInput;



public class MainActivity extends AppCompatActivity {

    private Button save;
    private Button refresh;

    private EditText tagNameText;
    private EditText cityText;
    private EditText sprayCanText;

    public static ListView showList;

    //array that holds the requests
    public static ArrayList<Request> requestArray = new ArrayList<Request>();

    public void sendEmail(View view) {

        String requestString = "\n__________________________";
        for (int i = 0; i < requestArray.size(); i++) {
            requestString += requestArray.get(i).toString();
            requestString += "\n__________________________";
        }

        Intent intent = null, chooser = null;

        intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        String to = "vatca_razvan@yahoo.com";
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Send from your App");
        intent.putExtra(Intent.EXTRA_TEXT, "Graffers list: \n" + requestString);
        intent.setType("message/rfc822");

        chooser = Intent.createChooser(intent, "Send the email");
        startActivity(chooser);
    }

    // checks if a request is contained by a list of requests comparing each field
    public static boolean contains(ArrayList<Request> list, Request req) {
        for (Iterator<Request> i = list.iterator(); i.hasNext(); ) {
            Request rec = i.next();
            if (rec.getTagName().equals(req.getTagName()) && rec.getCity().equals(req.getCity())
                    && rec.getSprayCan().equals(req.getSprayCan())) {
                return true;
            }
        }
        return false;
    }

    // persistes data from the array of requests into a JSON file on disk
    public static void createFile(View v, Context ctx) throws IOException, JSONException {
        JSONArray data = new JSONArray();
        JSONObject request;

        for (int i = 0; i < requestArray.size(); i++) {
            request = new JSONObject();
            request.put("tagName", requestArray.get(i).getTagName());
            request.put("city", requestArray.get(i).getCity());
            request.put("sprayCan", requestArray.get(i).getSprayCan());
            data.put(request); // il adaug in arrayul de requesturi ce vor fi puse in json
        }
        String text = data.toString();

        FileOutputStream fileOutStr = ctx.openFileOutput("requestsFile", MODE_PRIVATE);
        fileOutStr.write(text.getBytes());
        fileOutStr.close();
    }

    //take data from the json file
    public static void readFromFile(View v, Context ctx) throws IOException, JSONException {
        FileInputStream fileInpStr = ctx.openFileInput("requestsFile");
        BufferedInputStream buffInpStr = new BufferedInputStream(fileInpStr);
        StringBuffer strBuff = new StringBuffer();
        while (buffInpStr.available() != 0) {
            char c = (char) buffInpStr.read();
            strBuff.append(c);
        }

        buffInpStr.close();
        fileInpStr.close();

        JSONArray data = new JSONArray(strBuff.toString());

        for (int i = 0; i < data.length(); i++) {
            String tagName = data.getJSONObject(i).getString("tagName");
            String city = data.getJSONObject(i).getString("city");
            String sprayCan = data.getJSONObject(i).getString("sprayCan");

            Request req = new Request(tagName, city, sprayCan);
            requestArray.add(req);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize UI components
        tagNameText = (EditText) findViewById(R.id.tagNameInput);
        cityText = (EditText) findViewById(R.id.cityInput);
        sprayCanText = (EditText) findViewById(R.id.sprayCanInput);

        showList = (ListView) findViewById(R.id.myListView);
        save = (Button) findViewById(R.id.addButton);
        refresh = (Button) findViewById(R.id.refreshButton);

        //refresh button
        refresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    readFromFile(v, MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException j) {
                    j.printStackTrace();
                }

                if (requestArray.isEmpty()) {
                    Toast.makeText(getBaseContext(), "The List is Empty", Toast.LENGTH_LONG).show();
                } else {
                    ArrayAdapter<Request> adapter = new ArrayAdapter<Request>(MainActivity.this, android.R.layout.simple_list_item_1, requestArray);
                    showList.setAdapter(adapter);
                }
            }
        });

        //save button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getTagName = tagNameText.getText().toString();
                String getCity = cityText.getText().toString();
                String getSprayCan = sprayCanText.getText().toString();

                Request req = new Request(getTagName, getCity, getSprayCan);

                if (contains(requestArray, req)) {
                    Toast.makeText(getBaseContext(), "The item is already in the list", Toast.LENGTH_LONG).show();
                } else if (getTagName == null || getTagName.trim().equals("") || getCity == null || getCity.trim().equals("")
                        || getSprayCan == null || getSprayCan.trim().equals("")) {

                    Toast.makeText(getBaseContext(), "Input is empty", Toast.LENGTH_LONG).show();
                } else {
                    requestArray.add(req);

                    ArrayAdapter<Request> adapter = new ArrayAdapter<Request>(MainActivity.this, android.R.layout.simple_list_item_1,requestArray);
                    showList.setAdapter(adapter);

                    ((EditText) findViewById(R.id.tagNameInput)).setText("");
                    ((EditText) findViewById(R.id.cityInput)).setText("");
                    ((EditText) findViewById(R.id.sprayCanInput)).setText("");

                    try {
                        createFile(v,MainActivity.this);
                    }catch (IOException e){
                        e.printStackTrace();
                    }catch (JSONException j){
                        j.printStackTrace();
                    }
                }
            }

        });

        // event for clicking on a list view item
        // opens a new activity
        showList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request selected = (Request) (showList.getItemAtPosition(position));

                Intent myNewIntent = new Intent(view.getContext(), ListItemActivity.class);
                myNewIntent.putExtra("itemTagName",selected.getTagName());
                myNewIntent.putExtra("itemCity",selected.getCity());
                myNewIntent.putExtra("itemSprayCan",selected.getSprayCan());

                ArrayList<String> sprayCanArray = new ArrayList<String>();
                for (int i =0;i<requestArray.size();i++){
                    sprayCanArray.add(requestArray.get(i).getSprayCan());
                }

                myNewIntent.putExtra("sprayCanArray",sprayCanArray);
                myNewIntent.putExtra("itemPosition",position);

                startActivityForResult(myNewIntent, 0);
            }
        });
    }
}
