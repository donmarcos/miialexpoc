package com.amazonaws.sample.lex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    private TextInputEditText login_id, login_password;
    private CheckBox remember_me;
    private Button login_button;

    final AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
            Log.i("Cognito", "Login successful!");
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
        }

        @Override
        public void onFailure(Exception exception) {
            Log.i("Cognito", "Login Failed! Sorry mucho!" + exception.getLocalizedMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_id = findViewById(R.id.login_id);
        login_password = findViewById(R.id.login_password);
        remember_me = findViewById(R.id.Remember_checkbox);
        login_button = findViewById(R.id.login_button);
    }

    public void loginFunction(View view){
        CognitoSettings cognitoSettings = new CognitoSettings(Login.this);
        CognitoUser cognitoUser = cognitoSettings.getUserPool().getUser(String.valueOf(login_id.getText()));

        cognitoUser.getSessionInBackground(authenticationHandler);
    }
}
