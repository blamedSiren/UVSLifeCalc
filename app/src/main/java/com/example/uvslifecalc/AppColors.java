package com.example.uvslifecalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.EnumMap;
import java.util.Map;

public final class AppColors {
    public enum Role {
        PRIMARY("color_primary", R.color.primary),
        SECONDARY("color_secondary", R.color.secondary),
        PRIMARYGRADIENTCOLOR("color_gradient_color_one", R.color.gradient_color_one),
        SECONDARYGRADIENTCOLOR("color_gradient_color_two", R.color.gradient_color_two);

        final String key;
        final int defaultRes;

        Role(String key,int defaultRes) {
            this.key = key;
            this.defaultRes = defaultRes;
        }
    }

    private static SharedPreferences prefs;
    private static Context appContext;
    private static final Map<Role, MutableLiveData<Integer>> data = new EnumMap<>(Role.class);

    private AppColors(){}
        public static void init(Context context){
            appContext = appContext.getApplicationContext();
            prefs = appContext.getSharedPreferences("theme", Context.MODE_PRIVATE);
            for (Role role : Role.values()){
                int def = ContextCompat.getColor(appContext, role.defaultRes);
                data.put(role, new MutableLiveData<>(prefs.getInt(role.key, def)));
        }
    }

    public static LiveData<Integer> get(Role role){
        return data.get(role);
    }

    @ColorInt
    public static int getValue(Role role){
        return data.get(role).getValue();
    }

    //Return false if text isn't a valid color code :)
    public static boolean setFromHex(Role role, String input){
        Integer color = parse(input);
        if(color == null) return false;
        prefs.edit().putInt(role.key, color).apply();
        data.get(role).setValue(color);
        return true;
    }

    public static void reset(Role role){
        prefs.edit().remove(role.key).apply();
        data.get(role).setValue(ContextCompat.getColor(appContext, role.defaultRes));
    }

    @Nullable
    public static Integer parse(String input){
        if(input == null) return null;
        String s = input.trim();
        if (!s.startsWith("#")) s = "#" + s;
        if(s.length() != 7 && s.length() != 9) return null; //blocks named colors like 'red'
        try{
            return Color.parseColor(s);
        } catch (IllegalArgumentException e){
            return null;
        }
    }
}
