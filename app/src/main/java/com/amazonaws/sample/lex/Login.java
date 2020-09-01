package com.amazonaws.sample.lex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoauth.tokens.IdToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.tokens.CognitoIdToken;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoServiceConstants;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.google.android.material.textfield.TextInputEditText;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
public class Login extends AppCompatActivity {

    private TextInputEditText login_id, login_password;
    private CheckBox remember_me;
    private Button login_button;
    private CognitoIdToken token;

    final AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
            Log.i("Cognito", "Login successful!");
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }


        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
            Log.i("Cognito", "Getting user authentication details.");
            AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, String.valueOf(login_password.getText()), null);

            // Pass creds to continuation
            authenticationContinuation.setAuthenticationDetails(authenticationDetails);

            // allow sign in to continue
            authenticationContinuation.continueTask();
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
            Log.i("Cognito", "Getting MFA code.");
        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            Log.i("Cognito", "In the authentication challenge code.");
            //continuation.setChallengeResponse(CognitoServiceConstants.CHLG_RESP_ANSWER, "5");
            //continuation.continueTask();
        }

        @Override
        public void onFailure(Exception exception) {
            Toast.makeText(Login.this, "INVALID CREDENTIALS", Toast.LENGTH_SHORT).show();
            Log.i("Cognito", "Login Failed! Sorry mucho!" + exception.getLocalizedMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Hiding the application action bar when activity is created
        Objects.requireNonNull(getSupportActionBar()).hide();
        login_id = findViewById(R.id.login_id);
        login_password = findViewById(R.id.login_password);
        // remember_me = findViewById(R.id.Remember_checkbox);
        login_button = findViewById(R.id.login_button);
    }

    @Override
    protected void onStop() {
        super.onStop();
        login_id.setText(null);
        login_password.setText(null);
    }
    public void loginFunction(View view){
        CognitoSettings cognitoSettings = new CognitoSettings(Login.this);
        CognitoUser cognitoUser = cognitoSettings.getUserPool().getUser(String.valueOf(login_id.getText()));

        cognitoUser.getSessionInBackground(authenticationHandler);
    }
    public void goToSignup(View view) {
        startActivity(new Intent(Login.this, SignUp.class));
    }
}