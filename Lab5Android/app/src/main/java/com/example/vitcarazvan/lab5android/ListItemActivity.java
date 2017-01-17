package com.example.vitcarazvan.lab5android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Vitca Razvan on 1/16/2017.
 */
import org.json.JSONException;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.vitcarazvan.lab5android.MainActivity.contains;
import static com.example.vitcarazvan.lab5android.MainActivity.createFile;
import static com.example.vitcarazvan.lab5android.MainActivity.showList;
import static com.example.vitcarazvan.lab5android.MainActivity.requestArray;

public class ListItemActivity extends AppCompatActivity {

    private EditText recievedTagNameText;
    private EditText recievedCityText;
    private EditText recievedSprayCanText;

    private Button saveChanges;
    private Button deleteRequest;
    private Button openChart;

    public ArrayList<String> cansArray = new ArrayList<String>();

    public void clickChartButton(){
        openChart = (Button) findViewById(R.id.showChartButton);

        openChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.vitcarazvan.lab5android.ChartActivity");
                intent.putExtra("arraySprayCansFromListItemActivity",cansArray);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_activity);

        clickChartButton();
        saveChanges = (Button) findViewById(R.id.saveChangesButton);
        deleteRequest = (Button) findViewById(R.id.deleteItemButton);

        String getTagName = getIntent().getStringExtra("itemTagName");
        String getCity= getIntent().getStringExtra("itemCity");
        String getSprayCan = getIntent().getStringExtra("itemSprayCan");
        final int pos = getIntent().getIntExtra("itemPosition",-1);

        cansArray = getIntent().getStringArrayListExtra("sprayCanArray");

        recievedTagNameText = (EditText) findViewById(R.id.recievedTagName);
        recievedTagNameText.setText(getTagName);

        recievedCityText = (EditText) findViewById(R.id.recievedCity);
        recievedCityText.setText(getCity);

        recievedSprayCanText = (EditText) findViewById(R.id.recievedSprayCan);
        recievedSprayCanText.setText(getSprayCan);

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getNewTagName = recievedTagNameText.getText().toString();
                String getNewCity = recievedCityText.getText().toString();
                String getNewSprayCan = recievedSprayCanText.getText().toString();

                Request req = new Request(getNewTagName,getNewCity,getNewSprayCan);

                if(contains(requestArray,req)){
                    finish();
                }else if( getNewTagName==null || getNewTagName.trim().equals("") ||
                        getNewCity==null || getNewCity.trim().equals("") || getNewSprayCan==null
                        || getNewSprayCan.trim().equals("")){
                    Toast.makeText(getBaseContext(), "Input is empty", Toast.LENGTH_LONG).show();
                } else {
                    requestArray.set(pos,req);
                    Toast.makeText(getBaseContext(), "Item Saved", Toast.LENGTH_LONG).show();
                    showList.invalidateViews();

                    try {
                        createFile(v, ListItemActivity.this);
                    } catch (IOException e){
                        e.printStackTrace();
                    } catch (JSONException j){
                        j.printStackTrace();
                    }
                    finish();
                }
            }
        });

        deleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListItemActivity.this);
                builder.setMessage("Delete?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String getNewTagName = recievedTagNameText.getText().toString();
                                String getNewCity = recievedCityText.getText().toString();
                                String getNewSprayCan = recievedSprayCanText.getText().toString();

                                Request req = new Request(getNewTagName,getNewCity,getNewSprayCan);

                                if(contains(requestArray, req)){
                                    requestArray.remove(pos);
                                    Toast.makeText(getBaseContext(),"Deleted!", Toast.LENGTH_LONG).show();
                                    showList.invalidateViews();

                                    try {
                                        createFile(v, ListItemActivity.this);
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }catch (JSONException j){
                                        j.printStackTrace();
                                    }
                                    finish();
                                }else {
                                    Toast.makeText(getBaseContext(),"No matching request",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id){

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
            });
    }

}
