package com.eldhopj.fcmnotificationsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    String emailID,password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() !=null){
            profileActivityIntent();
        }
    }

    public void signUp(View view) {
        createUser();
    }


    private void createUser() {

        if (!(validateEmail() & validatePassword())) {
            return;
        } else {
            progressBar.setVisibility(View.VISIBLE);
             mAuth.createUserWithEmailAndPassword(emailID, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            profileActivityIntent();
                        } else {
                            //If the user is already have an account no need to create the user again instead Login the user
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                userLogin(emailID, password);
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });

        }
    }

    private void userLogin(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            profileActivityIntent();
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void profileActivityIntent() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //---------------------Validations-----------------------------//
    private boolean validateEmail() {
        emailID = editTextEmail.getText().toString().trim();
        if (!(Patterns.EMAIL_ADDRESS.matcher(emailID).matches())) {
            editTextEmail.setError("Email required");
            editTextEmail.requestFocus();
            return false;
        } else {
            editTextEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        password = editTextPassword.getText().toString().trim();

        if (password.isEmpty()){
            editTextPassword.setError("Field Cant be Empty");
            editTextPassword.requestFocus();
            return false;
        }
        else if (password.length() < 6){
            editTextPassword.setError("Minimum 6 characters needed");
            editTextPassword.requestFocus();
            return false;
        }
        else {
            editTextPassword.setError(null);
            editTextPassword.setError(null);
            return true;
        }

    }
//---------------------Validations-----------------------------//

}
