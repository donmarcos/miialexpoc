package com.amazonaws.sample.lex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

//import com.amazonaws.auth.CognitoCachingCredentialsProvider;
//import com.amazonaws.regions.Regions;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;
//import com.amplifyframework.AmplifyException;
//import com.amplifyframework.auth.AuthUserAttributeKey;
//import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
//import com.amplifyframework.auth.options.AuthSignUpOptions;
//import com.amplifyframework.core.Amplify;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;

public class Signup extends AppCompatActivity {

    LinearLayout signup_form;
    TextInputEditText name, email, password, phone;
    RadioGroup gender;
    final CognitoUserAttributes cognitoUserAttributes = new CognitoUserAttributes();
    final SignUpHandler signupCallBack = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, SignUpResult signUpResult) {
            Log.i("Signup", "Sign-up successful and confirmed! " + signUpResult.getUserConfirmed());

            if (!signUpResult.getUserConfirmed()){
                Log.i("Signup", "Sign-up successful but not confirmed.. \nVerification code sent to " + signUpResult.getCodeDeliveryDetails().getDestination());
            }
            else{
                Log.i("Signup", "Sign-up successful and confirmed now! ");
            }
        }

        @Override
        public void onFailure(Exception exception) {
            Log.i("Signup", "Sign-up failed! \nException: " + exception.getLocalizedMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup_form = findViewById(R.id.signup_layout);
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        signup_form.startAnimation(aniFade);
        name = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone_number);
        gender = findViewById(R.id.gender);
    }
    public void submit_form(View view){

        String[] parameters = new String[5];
        parameters[0] = String.valueOf(name.getText());
        parameters[1] = String.valueOf(email.getText());
        parameters[2] = String.valueOf(password.getText());
        parameters[3] = String.valueOf(phone.getText());
        RadioButton selectedGender = findViewById(gender.getCheckedRadioButtonId());
        parameters[4] = String.valueOf(selectedGender.getText());

        Toast toast = new Toast(Signup.this);
        toast.setText("Sign up parameters: " + Arrays.toString(parameters));
        toast.show();

        validation(parameters, cognitoUserAttributes);
//        startActivity(new Intent(Signup.this, MainActivity.class));
    }

    protected void validation(String[] parameters, CognitoUserAttributes cognitoUserAttributes){
        cognitoUserAttributes.addAttribute("name", parameters[0]);
        cognitoUserAttributes.addAttribute("email", parameters[1]);
//        cognitoUserAttributes.addAttribute("password", parameters[2]);
        cognitoUserAttributes.addAttribute("phone_number", parameters[3]);
        cognitoUserAttributes.addAttribute("gender", parameters[4]);
        cognitoUserAttributes.addAttribute("birthdate", "08/01/1987");
        cognitoUserAttributes.addAttribute("address", "704 Park Point Avenue, Rochester, NY 14623");


        CognitoSettings cognitoSettings = new CognitoSettings(Signup.this);
        cognitoSettings.getUserPool().signUpInBackground(parameters[0], parameters[2], cognitoUserAttributes, null, signupCallBack);
        startActivity(new Intent(Signup.this, Login.class));
    }
}
