package com.example.a24a10357exe2.Utilities;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

public class SignalManager {
    private static SignalManager instance = null;
    private Context context;
    private static Vibrator vibrator;

    private SignalManager(Context context) {
        this.context = context;
    }

    public static SignalManager getInstance() {
        return instance;
    }

    public static void init(Context context) {
        synchronized (SignalManager.class) {
            if (instance == null) {
                instance = new SignalManager(context);
                vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            }
        }
    }

    public void vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(500);
        }
    }

    public void toast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
