package com.amazonaws.sample.lex;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

public class CognitoSettings {
    /* AKhilesh setup
    private String userPoolID = "us-east-1_S4OJX7kYw";
    private String clientID = "a2c8jonqqnpghmkh4ikktbn6m";
    private String clientSecret = "13kk2985v1a96tqikg89fbr8qnolvumnq139ii1v9dttg482d26s";
    private Regions cognitoRegion = Regions.US_EAST_1;
    */
     // marcos user pool
    private String userPoolID = "us-west-2_QvaXKaYbA";
    private String clientID = "6oifns009clursiom63nticstp";
    private String clientSecret = "";
    private Regions cognitoRegion = Regions.US_WEST_2;

    private Context context;

    public CognitoSettings(Context context){
        this.context = context;
    }

    public String getUserPoolID(){ return userPoolID; }

    public String getClientID(){ return clientID; }

    public String getClientSecret(){ return clientSecret; }

    public Regions getCognitoRegion(){ return cognitoRegion; }

    // entry point for interactions with cognito user pool
    public CognitoUserPool getUserPool(){
        return new CognitoUserPool(context, userPoolID, clientID, clientSecret, cognitoRegion);
    }
}
