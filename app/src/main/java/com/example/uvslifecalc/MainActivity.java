package com.example.uvslifecalc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button opp_up, opp_down, me_up, me_down, reset, max_set, attack, defend;
    TextView opp, me;

    public static String opp_max = "25";
    public static String me_max = "21";

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
        opp_up = findViewById(R.id.opp_button_up);
        opp_down = findViewById(R.id.opp_button_down);
        opp = findViewById(R.id.opp_health);
        me_up = findViewById(R.id.me_button_up);
        me_down = findViewById(R.id.me_button_down);
        me = findViewById(R.id.my_health);
        reset = findViewById(R.id.reset_button);
        max_set = findViewById(R.id.set_max_button);
        attack = findViewById(R.id.attack_button);
        defend = findViewById(R.id.defend_button);

        attack.setOnClickListener(v -> {
            Intent i = new Intent(this, Attack.class);
            i.putExtra("meHealth", me.getText().toString());
            i.putExtra("opHealth", opp.getText().toString());
            attackLauncher.launch(i);
        });
        defend.setOnClickListener(v -> {
            Intent i = new Intent(this, Defend.class);
            i.putExtra("meHealth", me.getText().toString());
            i.putExtra("opHealth", opp.getText().toString());
            defendLauncher.launch(i);
        });


        opp_up.setOnClickListener(v -> {
            increase(opp, opp_max);

        });
        opp_down.setOnClickListener(v -> {
            decrease(opp);
            String o = opp.getText().toString().trim();
            int oi = Integer.parseInt(o);
            if (oi <= 0) {
                opp.setText("DEAD");
            }
        });

        me_up.setOnClickListener(v -> {
            increase(me, me_max);
        });
        me_down.setOnClickListener(v -> {
            decrease(me);
            String o = me.getText().toString().trim();
            int oi = Integer.parseInt(o);
            if (oi <= 0) {
                me.setText("DEAD");
            }
        });

        reset.setOnLongClickListener(v -> {
            opp.setText(opp_max);
            me.setText(me_max);
            return true;
        });

        max_set.setOnClickListener(v -> {
            Intent i = new Intent(this, SetMax.class);
            maxLauncher.launch(i);
        });

    }

    public void increase(TextView t, String max) {
        String ts = t.getText().toString().trim();
        int ti = Integer.parseInt(ts);
        ti += 1;
        if(ti > Integer.parseInt(max)){
            ti = Integer.parseInt(max);
        }
        String tn = String.valueOf(ti);
        t.setText(tn);
    }

    public void decrease(TextView t) {
        String ts = t.getText().toString().trim();
        int ti = Integer.parseInt(ts);
        ti -= 1;
        String tn = String.valueOf(ti);
        t.setText(tn);
    }


    private final ActivityResultLauncher<Intent> maxLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null &&
                                result.getData().getSerializableExtra(SetMax.ME_KEY) != null &&
                                result.getData().getSerializableExtra(SetMax.OPP_KEY) != null) {

                            String mes = (String) result.getData().getSerializableExtra(SetMax.ME_KEY);
                            String opps = (String) result.getData().getSerializableExtra(SetMax.OPP_KEY);
                            if (me != null && opp != null) {
                                me.setText(mes);
                                me_max = mes;
                                opp.setText(opps);
                                opp_max = opps;
                            }
                        }
                    });
    private final ActivityResultLauncher<Intent> attackLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null &&
                                result.getData().getSerializableExtra(Attack.ME_KEY) != null &&
                                result.getData().getSerializableExtra(Attack.OPP_KEY) != null) {
                            String mes = (String) result.getData().getSerializableExtra(Attack.ME_KEY);
                            String opps = (String) result.getData().getSerializableExtra(Attack.OPP_KEY);
                            if (me != null && opp != null) {
                                me.setText(mes);
                                opp.setText(opps);
                            }
                        }

                    });

    private final ActivityResultLauncher<Intent> defendLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null &&
                                result.getData().getSerializableExtra(Attack.ME_KEY) != null &&
                                result.getData().getSerializableExtra(Attack.OPP_KEY) != null) {
                            String mes = (String) result.getData().getSerializableExtra(Attack.ME_KEY);
                            String opps = (String) result.getData().getSerializableExtra(Attack.OPP_KEY);
                            if (me != null && opp != null) {
                                me.setText(mes);
                                opp.setText(opps);
                            }
                        }

                    });


}
