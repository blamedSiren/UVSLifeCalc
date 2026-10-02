package com.example.uvslifecalc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import android.graphics.Color;

import java.util.Locale;

public class MainActivity extends BaseActivity {
    private static final long DURATION_MS = 60L * 60L * 1000L;  // 60 minutes
    private static final long TICK_MS = 250;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private TextView timerText;
    private TimerViewModel timerVm;

    private final Runnable tick = new Runnable() {
        @Override public void run() {
            updateTimer();
            handler.postDelayed(this, TICK_MS);
        }
    };

    Button opp_up, opp_down, me_up, me_down, reset, max_set, attack, defend;
    TextView opp, me;

    public static String opp_max = "25";
    public static String me_max = "25";

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
        timerText = findViewById(R.id.timer);
        timerVm = new ViewModelProvider(this).get(TimerViewModel.class);

        timerText.setOnClickListener(v -> {
            if (timerVm.startTime < 0) {          // only start if it isn't already running
                timerVm.startTime = SystemClock.elapsedRealtime();
                updateTimer();
            }
        });

        // Long-press to stop and return to 60:00
        timerText.setOnLongClickListener(v -> {
            timerVm.startTime = -1;
            updateTimer();
            return true;
        });


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
        Button setColors = findViewById(R.id.set_color_button);
        setColors.setOnClickListener(v -> startActivity(new Intent(this,
                ColorSet.class)));

    }
    @Override
    protected void onResume() {
        super.onResume();
        handler.post(tick);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(tick);
    }
    private void updateTimer(){
        if (timerVm.startTime < 0) {
            timerText.setText("60:00");
            timerText.setTextColor(AppColors.getValue(AppColors.Role.PRIMARY));
            return;
        }
        long elapsed = SystemClock.elapsedRealtime() - timerVm.startTime;
        long remaining = DURATION_MS - elapsed;
        boolean overtime = remaining <= 0;
        //Round up while counting down, down while counting up
        long secs = overtime ? (-remaining) / 1000 : (remaining + 999) / 1000;
        String sign = (overtime && secs > 0) ? "-" : "";
        timerText.setText(String.format(Locale.US, "%s%02d:%02d", sign, secs / 60, secs % 60));

        timerText.setTextColor(overtime
                ? Color.RED
                : AppColors.getValue(AppColors.Role.PRIMARY));
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
