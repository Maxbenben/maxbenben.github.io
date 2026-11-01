package com.example.testlinks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        infoTextView = findViewById(R.id.info_text_view);

        // Проверяем, открылось ли приложение по ссылке
        handleInviteLink(getIntent());
    }

    // Этот метод срабатывает, если приложение уже было открыто и свернуто
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Обновляем intent
        handleInviteLink(intent);
    }

    private void handleInviteLink(Intent intent) {
        if (intent == null) return;

        Uri data = intent.getData();

        if (data != null) {
            // Достаем код из параметра ?code=...
            String inviteCode = data.getQueryParameter("code");

            // Если код есть (независимо от того, какая схема использовалась: https или testlinks)
            if (inviteCode != null && !inviteCode.isEmpty()) {
                String message = "Успех! Код приглашения: " + inviteCode;
                infoTextView.setText(message);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            } else {
                // Если ссылка открылась, но кода в ней нет
                infoTextView.setText("Ссылка открыта, но кода приглашения нет.\nURL: " + data.toString());
            }
        } else {
            // Если приложение открыли обычным способом (не по ссылке)
            infoTextView.setText("Приложение запущено обычным способом.\nОжидание ссылки...");
        }
    }
}