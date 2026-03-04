package com.example.uvslifecalc;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Attack extends AppCompatActivity {

    Button speed, speedUp, speedDown, damage, damageUp, damageDown, uB, hB, fB, oUp, oDown, mUp,
            mDown;
    TextView mH, oH;

    static public final String ME_KEY = "me";
    static public final String OPP_KEY = "opp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attack);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.attack),
                (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        speed = findViewById(R.id.speed_button);
        speedUp = findViewById(R.id.attack_speed_up);
        speedDown = findViewById(R.id.attack_speed_down);
        damage = findViewById(R.id.attack_damage);
        damageUp =findViewById(R.id.attack_damage_up);
        damageDown = findViewById(R.id.attack_damage_down);
        uB = findViewById(R.id.unblocked_button);
        hB = findViewById(R.id.half_block_button);
        fB = findViewById(R.id.full_block_button);
        oUp = findViewById(R.id.o_p);
        oDown = findViewById(R.id.o_m);
        mUp = findViewById(R.id.m_p);
        mDown = findViewById(R.id.m_m);
        mH = findViewById(R.id.my_health_3);
        mH.setText(getIntent().getStringExtra("meHealth"));
        oH = findViewById(R.id.opp_health_3);
        oH.setText(getIntent().getStringExtra("opHealth"));

        final int[] colors = {
                Color.parseColor("#f06f24"), // orange
                Color.parseColor("#f1c835"), // yellow
                Color.parseColor("#cd1720")  // red
        };
        final int[] index = {0};

        speed.setBackgroundTintList(ColorStateList.valueOf(colors[0])); // set initial color

        speed.setOnClickListener(v -> {
            index[0] = (index[0] + 1) % colors.length;
            speed.setBackgroundTintList(ColorStateList.valueOf(colors[index[0]]));
        });

        speedUp.setOnClickListener(v ->{
            int f = Integer.parseInt(speed.getText().toString());
            f += 1;
            speed.setText(String.valueOf(f));
        });

        speedDown.setOnClickListener(v ->{
            int f = Integer.parseInt(speed.getText().toString());
            f -= 1;
            speed.setText(String.valueOf(f));
        });

        damageDown.setOnClickListener(v ->{
            int f = Integer.parseInt(damage.getText().toString());
            f -= 1;
            damage.setText(String.valueOf(f));
        });

        damageUp.setOnClickListener(v ->{
            int f = Integer.parseInt(damage.getText().toString());
            f += 1;
            damage.setText(String.valueOf(f));
        });

        oUp.setOnClickListener(v ->{
            int f = Integer.parseInt(oH.getText().toString());
            f += 1;
            oH.setText(String.valueOf(f));
        });

        oDown.setOnClickListener(v ->{
            int f = Integer.parseInt(oH.getText().toString());
            f -= 1;
            oH.setText(String.valueOf(f));
        });

        mUp.setOnClickListener(v ->{
            int f = Integer.parseInt(mH.getText().toString());
            f += 1;
            mH.setText(String.valueOf(f));
        });

        mDown.setOnClickListener(v ->{
            int f = Integer.parseInt(mH.getText().toString());
            f -= 1;
            mH.setText(String.valueOf(f));
        });

        fB.setOnClickListener(v ->{

            String m = mH.getText().toString();
            String o = oH.getText().toString();
            Intent result = new Intent();
            result.putExtra(OPP_KEY, o);
            result.putExtra(ME_KEY, m);
            setResult(RESULT_OK, result);
            finish();
        });

        hB.setOnClickListener(v ->{
            String m = mH.getText().toString();
            String o = oH.getText().toString();
            int oi = Integer.parseInt(o);
            oi -= Math.round((Double.parseDouble(damage.getText().toString()) / 2 ));
            o = String.valueOf(oi);
            Intent result = new Intent();
            result.putExtra(OPP_KEY, o);
            result.putExtra(ME_KEY, m);
            setResult(RESULT_OK, result);
            finish();
        });

        uB.setOnClickListener(v ->{
            String m = mH.getText().toString();
            String o = oH.getText().toString();
            int oi = Integer.parseInt(o);
            oi -= Math.round((Double.parseDouble(damage.getText().toString())));
            o = String.valueOf(oi);
            Intent result = new Intent();
            result.putExtra(OPP_KEY, o);
            result.putExtra(ME_KEY, m);
            setResult(RESULT_OK, result);
            finish();
        });


    }
}
