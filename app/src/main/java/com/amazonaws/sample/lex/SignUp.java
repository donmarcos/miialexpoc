package com.amazonaws.sample.lex;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

//import com.amazonaws.auth.CognitoCachingCredentialsProvider;
//import com.amazonaws.regions.Regions;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;
//import com.amplifyframework.AmplifyException;
//import com.amplifyframework.auth.AuthUserAttributeKey;
//import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
//import com.amplifyframework.auth.nav_drawer.AuthSignUpOptions;
//import com.amplifyframework.core.Amplify;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    ScrollView signup_form;
    TextInputEditText name, email, password, phone, dob, address, familyName, preferredName;
    Spinner gender;

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
        setContentView(R.layout.activity_sign_up);

        // Hiding the application action bar when activity is created
        Objects.requireNonNull(getSupportActionBar()).hide();

        setTitle("MIIA");
        signup_form = findViewById(R.id.signup_layout);
        // Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        // signup_form.startAnimation(aniFade);
        name = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone_number);
        gender = findViewById(R.id.gender);
        dob = findViewById(R.id.dob);
        address = findViewById(R.id.address);
        familyName = findViewById(R.id.family_name);
        preferredName = findViewById(R.id.preferred_username);
    }

/*
DON'T NEED THE GENDER SPINNER ANYMORE
        ArrayAdapter<CharSequence> adp3 = ArrayAdapter.createFromResource(this,
                R.array.gender, R.layout.checked_text_view);
        adp3.setDropDownViewResource(R.layout.spinner_dropdown_list);
        gender.setAdapter(adp3);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
//                String ss = gender.getSelectedItem().toString();
//                Toast.makeText(getBaseContext(), ss, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
*/

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onStop() {
        super.onStop();
        name.setText(null);
        familyName.setText(null);
        email.setText(null);
        dob.setText(null);
        address.setText(null);
        phone.setText(null);
        preferredName.setText(null);
        password.setText(null);
        gender.resetPivot();

    }

    public void submit_form(View view){

        String[] parameters = new String[9];
        parameters[0] = String.valueOf(name.getText());
        parameters[1] = String.valueOf(familyName.getText());
        parameters[2] = String.valueOf(preferredName.getText());
        parameters[3] = String.valueOf(email.getText());
        parameters[4] = String.valueOf(password.getText());
//        parameters[5] = String.valueOf(phone.getText());
//        parameters[6] = gender.getSelectedItem().toString();
//        parameters[7] = String.valueOf(dob.getText());
//        parameters[8] = String.valueOf(address.getText());

        Toast toast = new Toast(SignUp.this);
        toast.setText("Sign up parameters: " + Arrays.toString(parameters));
        toast.show();

        validation(parameters, cognitoUserAttributes);
//        startActivity(new Intent(Signup.this, MainActivity.class));
    }

    protected void validation(String[] parameters, CognitoUserAttributes cognitoUserAttributes){
        cognitoUserAttributes.addAttribute("given_name", parameters[0]);
        cognitoUserAttributes.addAttribute("family_name", parameters[1]);
        cognitoUserAttributes.addAttribute("preferred_username", parameters[2]);
        cognitoUserAttributes.addAttribute("email", parameters[3]);
//        cognitoUserAttributes.addAttribute("password", parameters[4]);
//        cognitoUserAttributes.addAttribute("phone_number", parameters[5]);
//        cognitoUserAttributes.addAttribute("gender", parameters[6]);
//        cognitoUserAttributes.addAttribute("birthdate", parameters[7]);
//        cognitoUserAttributes.addAttribute("address", parameters[8]);

        CognitoSettings cognitoSettings = new CognitoSettings(SignUp.this);
        cognitoSettings.getUserPool().signUpInBackground(parameters[2], parameters[4], cognitoUserAttributes, null, signupCallBack);

        startActivity(new Intent(SignUp.this, Login.class));
    }
}

/*
DON'T NEED THIS NOW. THE CONFIRMATION IS DONE BY CLICKING A LINK IN THE RECEIVED EMAIL
    private String confirmationCode = "";

    // Call API to confirm this user using dialog box
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Enter Confirmation Code");

        final EditText input = new EditText(this);
    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alertDialog.setView(input);

        // Set up the buttons
        alertDialog.setPositiveButton("OK", (dialog, which) -> confirmationCode = input.getText().toString());
        alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        alertDialog.show();
        CognitoUser cognitoUser = cognitoSettings.getUserPool().getUser();
        cognitoUser.confirmSignUpInBackground(confirmationCode, forcedAliasCreation, confirmationCallback);

    // Call back handler for confirmSignUp API
    GenericHandler confirmationCallback = new GenericHandler() {

        @Override
        public void onSuccess() {
            // User was successfully confirmed
        }

        @Override
        public void onFailure(Exception exception) {
            // User confirmation failed. Check exception for the cause.
        }
    };
*/
