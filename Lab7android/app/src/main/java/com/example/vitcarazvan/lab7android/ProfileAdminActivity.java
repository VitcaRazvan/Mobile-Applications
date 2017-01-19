package com.example.vitcarazvan.lab7android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Vitca Razvan on 1/17/2017.
 */

public class ProfileAdminActivity extends Activity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;

    private TextView tvEmail;
    private Button buttonLogout;

    public static ListView listViewRequests;

    private DatabaseReference databaseReference;
    private DatabaseReference requestsReference;

    protected ArrayList<Request> requests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_admin);

        // ma asigur ca esti logat pentru a putea vedea pagina asta
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        requestsReference = databaseReference.child("requests");

        listViewRequests = (ListView) findViewById(R.id.listViewId);
        tvEmail = (TextView) findViewById(R.id.tvEmailProfile);
        tvEmail.setText(getIntent().getExtras().getString("Email"));
        buttonLogout = (Button) findViewById(R.id.btnUserLogout);

        buttonLogout.setOnClickListener(this);

        requestsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requests.clear();

                for (DataSnapshot request : dataSnapshot.getChildren()) {
                    String tagName = request.child("tagName").getValue().toString();
                    String city = request.child("city").getValue().toString();
                    String sprayCan = request.child("sprayCan").getValue().toString();

                    String status = request.child("status").getValue().toString();
                    String email = request.child("email").getValue().toString();

                    Request req = new Request(tagName, city, sprayCan);
                    req.setEmail(email);
                    req.setStatus(status);
                    req.setKey(request.getKey());
                    requests.add(req);
                }
                ArrayAdapter<Request> adapter = new ArrayAdapter<Request>(ProfileAdminActivity.this, android.R.layout.simple_list_item_1, requests);
                listViewRequests.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Toast.makeText(ProfileAdminActivity.this, firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        listViewRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // iau requestul  selectat
                final Request selectedFromList = (Request) (listViewRequests.getItemAtPosition(position));

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileAdminActivity.this);
                builder.setMessage("Chose option");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                requestsReference.child(selectedFromList.getKey()).removeValue();
                                Toast.makeText(ProfileAdminActivity.this, "Request deleted", Toast.LENGTH_LONG).show();

                                Intent intent = null, chooser = null;

                                intent = new Intent(Intent.ACTION_SEND);
                                intent.setData(Uri.parse("mailto:"));
                                String[] to = {selectedFromList.getEmail()};
                                intent.putExtra(Intent.EXTRA_EMAIL, to);
                                intent.putExtra(Intent.EXTRA_SUBJECT,"Request done");
                                intent.putExtra(Intent.EXTRA_TEXT,"Your request is done");
                                intent.setType("message/rfc822");
                                chooser = Intent.createChooser(intent, "Send email");
                                startActivity(chooser);
                            }
                        });
                builder.setNeutralButton(
                        "Put in progress",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                requestsReference.child(selectedFromList.getKey()).child("status").setValue("in progress");
                                Toast.makeText(ProfileAdminActivity.this, "Request in progress", Toast.LENGTH_LONG).show();

                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(ProfileAdminActivity.this)
                                                //.setSmallIcon(R.drawable.notification_icon)
                                                .setContentTitle("Status of request changed !")
                                                .setContentText("It has been set to in progress");

                                mBuilder.setColor(0xff777777);
                                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                //mNotificationManager.notify(001,mBuilder.build());
                            }
                        });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(ProfileAdminActivity.this, "Canceled", Toast.LENGTH_LONG).show();
                    }
                });

                AlertDialog alert11 = builder.create();
                alert11.show();

            }

        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUserLogout:
                firebaseAuth.signOut();
                Toast.makeText(ProfileAdminActivity.this, "Logged out", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }
    }
}