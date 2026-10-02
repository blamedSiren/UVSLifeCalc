package com.example.uvslifecalc;

import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    private static final int ROLE_NONE = 0, ROLE_PRIMARY = 1, ROLE_SECONDARY = 2;

    private interface Binding { void apply(int primary, int secondary); }

    private final List<Binding> bindings = new ArrayList<>();
    private View contentRoot;
    private int defaultPrimary, defaultSecondary;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        defaultPrimary = ContextCompat.getColor(this, R.color.primary);
        defaultSecondary = ContextCompat.getColor(this, R.color.secondary);

        contentRoot = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        scan(contentRoot);

        for (AppColors.Role role : AppColors.Role.values()) {
            AppColors.get(role).observe(this, c -> applySaved());
        }
    }

    private void applySaved() {
        applyTheme(
                AppColors.getValue(AppColors.Role.PRIMARY),
                AppColors.getValue(AppColors.Role.SECONDARY),
                AppColors.getValue(AppColors.Role.PRIMARYGRADIENTCOLOR),
                AppColors.getValue(AppColors.Role.SECONDARYGRADIENTCOLOR));
    }

    protected void applyTheme(int primary, int secondary, int bgPrimary, int bgSecondary) {
        contentRoot.setBackground(new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP, new int[]{bgPrimary, bgSecondary}));
        for (Binding b : bindings) b.apply(primary, secondary);
    }

    private int roleOf(int color) {
        if (color == defaultPrimary) return ROLE_PRIMARY;
        if (color == defaultSecondary) return ROLE_SECONDARY;
        return ROLE_NONE;
    }

    private static int pick(int role, int primary, int secondary) {
        return role == ROLE_PRIMARY ? primary : secondary;
    }

    private void scan(View v) {
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            final int textRole = roleOf(tv.getCurrentTextColor());
            final int hintRole = roleOf(tv.getCurrentHintTextColor());
            if (textRole != ROLE_NONE || hintRole != ROLE_NONE) {
                bindings.add((p, s) -> {
                    if (textRole != ROLE_NONE) tv.setTextColor(pick(textRole, p, s));
                    if (hintRole != ROLE_NONE) tv.setHintTextColor(pick(hintRole, p, s));
                });
            }
        }

        ColorStateList tint = v.getBackgroundTintList();
        if (tint != null) {
            final int tintRole = roleOf(tint.getDefaultColor());
            if (tintRole != ROLE_NONE) {
                bindings.add((p, s) ->
                        v.setBackgroundTintList(ColorStateList.valueOf(pick(tintRole, p, s))));
            }
        }

        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            for (int i = 0; i < group.getChildCount(); i++) scan(group.getChildAt(i));
        }
    }
}