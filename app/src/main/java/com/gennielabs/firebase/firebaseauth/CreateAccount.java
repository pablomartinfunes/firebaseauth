package com.gennielabs.firebase.firebaseauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccount extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final static String TAG = "CreateAccount";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        final EditText email = (EditText) findViewById(R.id.etEmailUser);
        final EditText password = (EditText) findViewById(R.id.etPasswordUser);
        final EditText passwordAgain = (EditText) findViewById(R.id.etPasswordUserAgain);
        final Button createAccount = (Button) findViewById(R.id.btCreateAccount);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createNewAccount(email.getText().toString(), password.getText().toString(), passwordAgain.getText().toString());

            }
        });

    }

    public void createNewAccount(String email, String password, String passwordAgain){


        if(password.equals(passwordAgain)){

            if ( !isValidPassword(password)){
                Toast.makeText(CreateAccount.this, "Password have to be al least 9 lenth character, with numbers anc characters. Example: JonnaS2018", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Log.d(TAG, "Create Account successful");
                                FirebaseUser fUser = mAuth.getCurrentUser();
                                Log.d(TAG, "Current User: " + fUser.getEmail());
                                Toast.makeText(CreateAccount.this, "Account Success", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(CreateAccount.this, AccessAccount.class));
                            }else{
                                Log.w(TAG, "Create Account Failed");
                                Toast.makeText(CreateAccount.this, "Auth failed", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

        }else{
            Toast.makeText(CreateAccount.this, "Different Passwords", Toast.LENGTH_LONG).show();
        }

    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

}
