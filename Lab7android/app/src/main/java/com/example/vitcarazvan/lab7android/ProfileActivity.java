package com.example.vitcarazvan.lab7android;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Vitca Razvan on 1/17/2017.
 */

public class ProfileActivity extends Activity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private TextView tvEmail;
    private Button buttonLogout;
    private Button buttonSendRequest;
    private Button buttonShowChart;
    private EditText tagNameTxt;
    private EditText cityTxt;
    private EditText sprayCanTxt;

    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // ma asigur ca esti logat pentru a putea vedea pagina asta
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        tvEmail = (TextView) findViewById(R.id.tvEmailProfile);
        tvEmail.setText(getIntent().getExtras().getString("Email"));

        buttonSendRequest = (Button) findViewById(R.id.btnSendRequest);
        buttonLogout = (Button) findViewById(R.id.btnUserLogout);
        buttonShowChart = (Button) findViewById(R.id.btnStatistics);
        tagNameTxt = (EditText) findViewById(R.id.tagNameInput);
        cityTxt = (EditText) findViewById(R.id.cityInput);
        sprayCanTxt = (EditText) findViewById(R.id.sprayCanInput);


        databaseReference = FirebaseDatabase.getInstance().getReference();

        buttonLogout.setOnClickListener(this);
        buttonSendRequest.setOnClickListener(this);
        buttonShowChart.setOnClickListener(this);
    }

    public void addRequest(){
        String tagName = tagNameTxt.getText().toString().trim();
        String city = cityTxt.getText().toString().trim();
        String sprayCan = sprayCanTxt.getText().toString().trim();

        Request request = new Request(tagName,city,sprayCan);
        request.setEmail(tvEmail.getText().toString().trim());

        databaseReference.child("requests").push().setValue(request);
        Toast.makeText(ProfileActivity.this, "Request sent", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSendRequest:
                addRequest();
                break;
            case R.id.btnUserLogout:
                firebaseAuth.signOut();
                Toast.makeText(ProfileActivity.this, "Logged out", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.btnStatistics:
                finish();
                startActivity(new Intent(this,ChartActivity.class));
                break;

        }
    }
}


