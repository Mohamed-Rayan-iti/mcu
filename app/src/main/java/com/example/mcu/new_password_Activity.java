package com.example.mcu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class new_password_Activity extends AppCompatActivity {
    EditText new_password, confirm_new_password;
    Button sava_new_password;
    ImageView iconBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        new_password=findViewById(R.id.new_password);
        confirm_new_password=findViewById(R.id.confirm_new_password);
        sava_new_password=findViewById(R.id.save_new_password);

        sava_new_password.setOnClickListener(v ->
                validationData());

        iconBack = findViewById(R.id.forget_password_back);
        iconBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, login_Activity.class);
            startActivity(intent);
        });
    }

    private void validationData() {

        String new_pass = new_password.getText().toString().trim();
        String con_new_pass = confirm_new_password.getText().toString().trim();
        String email=  getIntent().getExtras().getString("Email");

        //trust data
        // password
        if (new_pass.isEmpty()) {
            new_password.requestFocus();
            // change to Alert
            showAlert("Password is required");
            return;
        }
        if (new_pass.length() < 8) {
            new_password.requestFocus();
            // change to Alert
            showAlert("Password must be 8 digits");
            return;
        }
        if (con_new_pass.isEmpty()) {
            confirm_new_password.requestFocus();
            // change to Alert
            showAlert("Confirm Password is required");
            return;
        }
        if (!new_pass.equals(con_new_pass)) {
            confirm_new_password.requestFocus();
            // change to Alert
            showAlert("Password not like Confirm Password !");
            return;
        }

        // to sign up from fire base
        updatePasswordWithFirebase(email, con_new_pass);

    }

    private void updatePasswordWithFirebase(String email, String con_new_pass) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider.getCredential(email, con_new_pass);

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(con_new_pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        showAlert("Password updated");
                                    } else {
                                        showAlert("Error password not updated");
                                    }
                                }
                            });
                        } else {
                            showAlert("Error auth failed");

                        }
                    }
                });
        showAlert("you wont to update pass: '"+con_new_pass+"', to this email: '"+email+"' ");
    }

    void showAlert (String msg){
        new AlertDialog.Builder(this)
                .setTitle("attention!")
                .setMessage(msg)
                .setIcon(R.drawable.ic_attention)
                .setPositiveButton("Okay!", null)
                .create().show();
    }
}