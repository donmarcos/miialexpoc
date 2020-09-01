package com.amazonaws.sample.lex;

import android.content.Context;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.List;

public class DatabaseAccess {

        private static final String COGNITO_POOL_ID = "us-west-2:9ec376f0-0f80-48c5-8c03-ad8fa5f0b99c";
        private static final Regions MY_REGION = Regions.US_WEST_2;
        private AmazonDynamoDBClient dbClient;
        private Table dbTable;
        private Context context;
        private final String DYNAMODB_TABLE = "users";
        CognitoCachingCredentialsProvider credentialsProvider;


        private static volatile DatabaseAccess instance;
        private DatabaseAccess (Context context) {
            this.context =context;
            credentialsProvider = new CognitoCachingCredentialsProvider (context, COGNITO_POOL_ID, MY_REGION);
            dbClient = new AmazonDynamoDBClient(credentialsProvider);
            dbClient.setRegion(Region.getRegion(Regions.US_WEST_2));
            dbTable = Table.loadTable(dbClient, DYNAMODB_TABLE);
        }
        public static synchronized DatabaseAccess getInstance(Context context) {
            if (instance == null) {
                instance = new DatabaseAccess(context);
            }
            return instance;
        }
        public Document getItem (String user_id){
            Document result = dbTable.getItem(new Primitive(credentialsProvider.getCachedIdentityId()), new Primitive(user_id));
            return result;
        }
        public List<Document> getAllItems() {
            return dbTable.query(new Primitive("457845")).getAllResults();
        }

    }

