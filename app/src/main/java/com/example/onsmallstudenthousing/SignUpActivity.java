package com.example.onsmallstudenthousing;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        EditText etUsername, etEmail, etPassword,etPhone;
        //dbHelper.deleteAllUsers();


        etUsername = findViewById(R.id.signup_username);
        etEmail = findViewById(R.id.signup_email);
        etPassword = findViewById(R.id.signup_password);
        etPhone = findViewById(R.id.signup_phonenumber);

        Button btnSignUp = findViewById(R.id.signup_Button);
        btnSignUp.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()|| phone.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

           if (dbHelper.isEmailExists(email)) {
                showEmailExistsAlert();
            } else {
                boolean isInserted = dbHelper.insertUserphone(username, email, password,phone);
                if (isInserted) {
                    Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Error Creating Account", Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView logRedirect = findViewById(R.id.logRedirect);

        logRedirect.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Facultatif, ferme l'activité SignUp pour empêcher l'utilisateur de revenir en arrière
        });




    }

    private void showEmailExistsAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("This email already exists. Please use a different email.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

}