package com.example.onsmallstudenthousing;

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

public class MotPasseOublie extends AppCompatActivity {

    EditText resetEmailEditText;
    Button resetButton;
    TextView backToLoginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mot_passe_oublie);
        DatabaseHelper dbHelper = new DatabaseHelper(this);


        // Initialisation des vues
        resetEmailEditText = findViewById(R.id.reset_email);
        resetButton = findViewById(R.id.reset_Button);
        backToLoginTextView = findViewById(R.id.back_to_login);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = resetEmailEditText.getText().toString().trim();
               Boolean checkuser = dbHelper.checkuserName(email);
                if(checkuser==true)
                {
                    Intent intent = new Intent(getApplicationContext(),ResetActivity.class);
                    intent.putExtra("email",email);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MotPasseOublie.this, "user does not exists ",Toast.LENGTH_SHORT);
                }
            }
        });

        // Retour à la page de connexion
        backToLoginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MotPasseOublie.this, LoginActivity.class);
            startActivity(intent);
        });
    }


}