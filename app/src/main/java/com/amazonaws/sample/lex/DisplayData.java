package com.amazonaws.sample.lex;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayData extends AppCompatActivity {

    private static final String COGNITO_POOL_ID = "us-west-2:9ec376f0-0f80-48c5-8c03-ad8fa5f0b99c";
    private static final Regions MY_REGION = Regions.US_WEST_2;
    private AmazonDynamoDBClient dbClient;
    private Table dbTable;
    private final Context context = DisplayData.this;
    private final String DYNAMODB_TABLE = "users";
    CognitoCachingCredentialsProvider credentialsProvider;

    private TextView displayUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data);
        try{
            init();
        }catch (Exception e){
            Log.e("EXCEPTION BAKAA!", Arrays.toString(e.getStackTrace()));
        }
    }


    private void init(){
        Thread thread = new Thread(() -> {
            credentialsProvider = new CognitoCachingCredentialsProvider (context, COGNITO_POOL_ID, MY_REGION);
            dbClient = new AmazonDynamoDBClient(credentialsProvider);
            dbClient.setRegion(Region.getRegion(Regions.US_WEST_2));
            dbTable = Table.loadTable(dbClient, DYNAMODB_TABLE);
        });
        thread.start();
        displayUserData = findViewById(R.id.displayUserData);
        Log.i("DYNAMO-DB-ATTRIBUTES", String.valueOf(dbTable.getAttributes()));
        displayUserData.setText(String.valueOf(getById("457845")));

        Log.i("DYNAMO-DB-DATA", String.valueOf(getById("457845")));
        Log.i("DATABASE QUERY: ", getAll().toString());
    }

        // GET THE TABLE NAME
     /*   Thread thread = new Thread(() -> {
            Map<String, String> logins = new HashMap<>();
            // set the Amazon login token
            logins.put("www.amazon.com", credentialsProvider.getCredentials().getSessionToken());
            credentialsProvider.withLogins(logins);

//            Log.i("DYNAMO-DB-TABLES", String.valueOf(credentialsProvider.getCredentials().getAWSAccessKeyId()));

            userInfo = Table.loadTable(dbClient, "users");
        */

//        });
//        thread.start();

    public Document getById(String id) {
        return dbTable.getItem(new Primitive(id));
    }

    public List<Document> getAll() {
        return dbTable.query(new Primitive("457000")).getAllResults();
    }
}