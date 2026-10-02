package com.example.uvslifecalc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ColorSet extends AppCompatActivity {

    Button submit, test;
    EditText primary_color, secondary_color, primary_background_color, secondary_background_color;

    String primary_color_string, secondary_color_string, primary_background_color_string,
            secondary_background_color_string;
    @SuppressLint("MissingInflatedId")
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
        submit = findViewById(R.id.submit_color_button);
        test = findViewById(R.id.color_test_button);
        primary_color = findViewById(R.id.primary_hint);
        secondary_color = findViewById(R.id.secondary_hint);
        primary_background_color = findViewById(R.id.primary_gradient_hint);
        secondary_background_color = findViewById(R.id.secondary_gradient_hint);

        primary_color_string = primary_color.getText().toString().trim();
        secondary_color_string = secondary_color.getText().toString().trim();
        primary_background_color_string = primary_background_color.getText().toString().trim();
        secondary_background_color_string = secondary_background_color.getText().toString().trim();

        test.setOnClickListener(v ->{
            if(primary_color_string.isEmpty() || secondary_color_string.isEmpty()
                    || primary_background_color_string.isEmpty()
                    || secondary_background_color_string.isEmpty()){
                Toast.makeText(this, "All Fields must be filled in order to test",
                        Toast.LENGTH_SHORT);
            }
            else{

            }

        });


    }
}
