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

public class ResetActivity extends AppCompatActivity {

    EditText reset_password,reset_retype_password;
    Button reset_Button;
    TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset);
        DatabaseHelper dbHelper = new DatabaseHelper(this);


        username =findViewById(R.id.username_reset_text);
        reset_password =findViewById(R.id.reset_password);
        reset_retype_password =findViewById(R.id.reset_retype_password);
        reset_Button =findViewById(R.id.reset_Button);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        if (email != null && !email.isEmpty()) {
            username.setText(email);  // Afficher l'email dans le TextView 'username'
        } else {
            // Si l'email est null ou vide, affichez un message de débogage
            Toast.makeText(ResetActivity.this, "Email non valide", Toast.LENGTH_SHORT).show();
        }

        reset_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String password = reset_password.getText().toString().trim();
                String repass = reset_retype_password.getText().toString().trim();

                if (password.equals(repass))
                {
                    Boolean check_pass_update =dbHelper.updatePassword(user, password);
                    if(check_pass_update==true){
                        Intent intent1 = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent1);
                        Toast.makeText(ResetActivity.this, "password updated successfully", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        Toast.makeText(ResetActivity.this,"password not updated", Toast.LENGTH_SHORT);
                    }
                }
                else {
                    Toast.makeText(ResetActivity.this,"password not matched", Toast.LENGTH_SHORT);

                }


            }
        });


    }
}