package com.example.android.gucdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Response;
import com.cloudant.client.org.lightcouch.NoDocumentException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText sensorData;
    private Button addToDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorData = (EditText) findViewById(R.id.sensor_data);
        addToDatabase = (Button) findViewById(R.id.enter_data);

        addToDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sensorData.length() == 0) {
                    Toast.makeText(MainActivity.this, "Enter Temp", Toast.LENGTH_LONG).show();
                }
                else {
                    String tempData = sensorData.getText().toString();
                    new DatabaseConnection().execute(tempData);
                }
            }
        });
    }

    public class DatabaseConnection extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(final String... strings) {
            // Create a new CloudantClient instance for account endpoint example.cloudant.com
            CloudantClient client = ClientBuilder.account("hostname")
                    .username("username from credintials")
                    .password("password from credintials")
                    .build();

            // Show the server version
            System.out.println("Server Version: " + client.serverVersion());

            // Get a List of all the databases this Cloudant account
            List<String> databases = client.getAllDbs();
            System.out.println("All my databases : ");
            for ( String db : databases ) {
                System.out.println(db);
            }

            // Working with data

            // Delete a database we created previously.
//            client.deleteDB("iot_db");

            // Create a new database.

//            client.createDB("iot_db");

            // Get a Database instance to interact with, but don't create it if it doesn't already exist
            Database db = client.database("iot_db", false);

            JSONObject tempObject = new JSONObject();
            try {
                tempObject.put("_id", "Temperature-Sensor");
                tempObject.put("Temp", strings[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            db.save(tempObject);

//            BufferedReader br = null;
//            StringBuilder sb = new StringBuilder();
//
//            String line;
//            try {
//
//                br = new BufferedReader(new InputStreamReader(db.find("Temperature-Sensor")));
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (NoDocumentException e) {
//                e.printStackTrace();
//            } finally {
//                if (br != null) {
//                    try {
//                        br.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            System.out.println(sb.toString());


            return null;
        }
    }
}
