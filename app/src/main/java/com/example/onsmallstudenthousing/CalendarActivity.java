package com.example.onsmallstudenthousing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private long selectedDateInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendar);

        CalendarView calendarView = findViewById(R.id.calendarView);
        Button buttonAddEvent = findViewById(R.id.buttonAddEvent);

        selectedDateInMillis = calendarView.getDate();

        // Écouter les changements de date
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth, 0, 0, 0); // Heure à minuit
            selectedDateInMillis = calendar.getTimeInMillis();
        });

        // Vérifiez si une application de calendrier est disponible
        if (!isCalendarAppAvailable()) {
            // Désactiver le bouton pour ajouter un événement
            buttonAddEvent.setText("Installer une application de calendrier");
            buttonAddEvent.setOnClickListener(view -> {
                // Rediriger l'utilisateur vers le Play Store
                Intent playStoreIntent = new Intent(Intent.ACTION_VIEW);
                playStoreIntent.setData(Uri.parse("market://search?q=calendar app"));
                if (playStoreIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(playStoreIntent);
                } else {
                    Toast.makeText(this, "Impossible d'ouvrir le Play Store.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Ajouter un événement au calendrier
            buttonAddEvent.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);

                intent.putExtra(CalendarContract.Events.TITLE, "Nouvel événement");
                intent.putExtra(CalendarContract.Events.ALL_DAY, true); // Utiliser un booléen
                intent.putExtra(CalendarContract.Events.DESCRIPTION, "Description de l'événement");
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Lieu par défaut");

                // Définir la date de début et de fin de l'événement
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, selectedDateInMillis);
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, selectedDateInMillis + 60 * 60 * 1000); // +1h

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(CalendarActivity.this, "Aucune application ne peut gérer cet événement.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Gérer les insets pour les appareils récents
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean isCalendarAppAvailable() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        return intent.resolveActivity(getPackageManager()) != null;
    }
}