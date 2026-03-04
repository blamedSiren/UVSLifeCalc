package com.example.uvslifecalc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SetMax extends AppCompatActivity {
    Button done;
    EditText opp, me;

    static public final String ME_KEY = "me";
    static public final String OPP_KEY = "opp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_max);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.set_max), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        done = findViewById(R.id.done_button);
        opp = findViewById(R.id.opp_health_2);
        me = findViewById(R.id.my_health_2);

        opp.setText(MainActivity.opp_max);
        me.setText(MainActivity.me_max);

        done.setOnClickListener(v ->{
           String opps = opp.getText().toString().trim();
           String mes = me.getText().toString().trim();

           if(opps.isEmpty()){
               Toast.makeText(this, "they can't be dead yet", Toast.LENGTH_SHORT);
           }
           if(mes.isEmpty()){
               Toast.makeText(this, "i am definitely not dead yet", Toast.LENGTH_SHORT);
           }

           Intent result = new Intent();
           result.putExtra(OPP_KEY, opps);
           result.putExtra(ME_KEY, mes);
           setResult(RESULT_OK, result);
           finish();
        });
    }


}
