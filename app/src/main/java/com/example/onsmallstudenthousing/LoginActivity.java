package com.example.onsmallstudenthousing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Button loginButton = findViewById(R.id.login_Button);
        EditText etLoginEmail = findViewById(R.id.login_email);
        EditText etLoginPassword = findViewById(R.id.login_password);

        // Obtient les icônes de gauche et droite
        Drawable[] drawables = etLoginPassword.getCompoundDrawables();
        final Drawable visibilityIcon = drawables[2];  // Icône à droite

        // Gérer le clic sur l'icône de visibilité
        etLoginPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Vérifie si l'icône de droite a été cliquée
                    if (event.getRawX() >= (etLoginPassword.getRight() - visibilityIcon.getBounds().width())) {
                        // Vérifie si le mot de passe est actuellement caché
                        if (etLoginPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                            etLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            visibilityIcon.setLevel(1); // Change l'icône en "visible"
                        } else {
                            // Masquer le mot de passe
                            etLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            visibilityIcon.setLevel(0); // Change l'icône en "invisible"
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        loginButton.setOnClickListener(v -> {
            String email = etLoginEmail.getText().toString().trim();
            String password = etLoginPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.validateUser(email, password)) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                User user = dbHelper.getUserByEmail(email);
                editor.putInt("userId", user.getId());
                editor.putString("userEmail", email);
                editor.putString("userName", user.getUsername());
                editor.putString("userPhone", user.getPhone());
                editor.putString("userPassword", user.getPassword());
                editor.apply();

                // Redirection vers l'activité TypeUser
                Intent intent = new Intent(LoginActivity.this, TypeUserActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });

        TextView signUpText = findViewById(R.id.login_signupText);
        signUpText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
