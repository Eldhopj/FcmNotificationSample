package com.eldhopj.fcmnotificationsample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eldhopj.fcmnotificationsample.modelClass.UserDetialsModel;
import com.eldhopj.fcmnotificationsample.utils.Commons;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();

        String token = Commons.TOKEN;

        Log.d(TAG, "onCreate: "+token);
        generateFcmToken();
    }

    /**
     * onNewIntent(): needed to get the intent data from FCM if the activity is already running
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // What is the use of this? need help
        getExtras();
    }

    private void getExtras() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            // Get keys from notification to get the bundle values
            String name = bundle.getString("id");
        }
    }

    @Override
    protected void onStart() { // checking the user login or not
        super.onStart();
        if (mAuth.getCurrentUser() == null){
            mainActivityIntent();
        }
    }

    private void generateFcmToken() { //Generating FCM token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()){
                            String token = task.getResult().getToken(); // Getting token in here
                            Commons.TOKEN = token;
                            saveToken(token);
                            Log.d(TAG, "onComplete: " + token);
                        }else {
                            Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void saveToken(String token) { // Saving token into firebase DB
        if (mAuth.getCurrentUser() == null) {
            return;
        }
        String email = "";
        if (mAuth.getCurrentUser().getEmail() == null) {
            email = mAuth.getCurrentUser().getEmail();
        }
        String uId = mAuth.getCurrentUser().getUid();

        UserDetialsModel user = new UserDetialsModel(email, token);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(uId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: Token Saved");
            }
        });
    }

    private void mainActivityIntent() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void logout(View view) { // logout user
        mAuth.signOut();
        if (mAuth.getCurrentUser()==null){
            mainActivityIntent();
        }
    }
}