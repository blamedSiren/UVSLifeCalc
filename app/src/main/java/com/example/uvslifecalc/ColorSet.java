package com.example.uvslifecalc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
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
    private static final AppColors.Role[] ROLES={
            AppColors.Role.PRIMARY,
            AppColors.Role.SECONDARY,
            AppColors.Role.PRIMARYGRADIENTCOLOR,
            AppColors.Role.SECONDARYGRADIENTCOLOR
    };
    Button submit, test;
    EditText primary_color, secondary_color, primary_background_color, secondary_background_color;
    EditText[] inputs;
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
        inputs = new EditText[]{primary_color, secondary_color, primary_background_color,
                secondary_background_color};
        for (int i = 0; i < ROLES.length; i++){
            inputs[i].setText(String.format("#%08X", AppColors.getValue(ROLES[1])));
        }
        test.setOnClickListener(v ->{
            Integer[]colors = readAndValidate();
            if(colors == null) {
                Toast.makeText(this, "Fix the highlighted fields to test",
                        Toast.LENGTH_SHORT);
                return;
            }
            preview(colors);
        });
    }
    private Integer[] readAndValidate(){
        Integer[] result = new Integer[inputs.length];
        boolean allValid = true;
        for(int i = 0; i < inputs.length; i++){
            String text = inputs[i].getText().toString().trim();
            Integer color = text.isEmpty() ? null : AppColors.parse(text);
            if(color == null) {
                inputs[i].setError(text.isEmpty() ? "Required" : "Ues a hex code like #FF5722");
                allValid = false;
            } else{
                inputs[i].setError(null);
                result[i] = color;
            }
        }
        return allValid ? result : null;
    }
    /** Temporarily applies the entered colors to this screen. */
    private void preview(Integer[] c) {
        int primary = c[0], secondary = c[1], bgTop = c[2], bgBottom = c[3];

        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, new int[]{bgTop, bgBottom});
        findViewById(R.id.main).setBackground(gradient);

        for (Button b : new Button[]{submit, test}) {
            b.setBackgroundTintList(ColorStateList.valueOf(primary));
            b.setTextColor(secondary);
        }
    }
}
