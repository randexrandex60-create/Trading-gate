package com.tradinggate.app;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.widget.*;

public class MainActivity extends android.app.Activity {
    LinearLayout root, checks;
    Button unlock, mt5;
    TextView status;
    CountDownTimer timer;
    final long UNLOCK_MS = 15 * 60 * 1000L;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        buildUi();
    }

    TextView tv(String s, int size) {
        TextView t = new TextView(this);
        t.setText(s); t.setTextSize(size); t.setTextColor(0xFFFFFFFF);
        t.setPadding(16,12,16,12);
        return t;
    }

    void buildUi() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24,24,24,24);
        root.setBackgroundColor(0xFF101014);

        TextView title = tv("🔒 TRADING GATE", 28);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        TextView sub = tv("Checklist wajib sebelum membuka MT5", 16);
        sub.setGravity(Gravity.CENTER);
        root.addView(sub);

        checks = new LinearLayout(this);
        checks.setOrientation(LinearLayout.VERTICAL);
        String[] labels = {"Liquidity", "CISD", "POI", "SL / TP / RR"};
        for (String x : labels) {
            CheckBox c = new CheckBox(this);
            c.setText(x);
            c.setTextColor(0xFFFFFFFF);
            c.setTextSize(18);
            checks.addView(c);
        }
        root.addView(checks);

        status = tv("STATUS: TERKUNCI", 20);
        status.setGravity(Gravity.CENTER);
        root.addView(status);

        unlock = new Button(this);
        unlock.setText("UNLOCK MT5 — 15 MENIT");
        unlock.setOnClickListener(v -> tryUnlock());
        root.addView(unlock);

        mt5 = new Button(this);
        mt5.setText("BUKA MT5");
        mt5.setEnabled(false);
        mt5.setOnClickListener(v -> openMt5());
        root.addView(mt5);

        setContentView(root);
    }

    boolean allChecked() {
        for (int i=0; i<checks.getChildCount(); i++)
            if (!((CheckBox)checks.getChildAt(i)).isChecked()) return false;
        return true;
    }

    void tryUnlock() {
        if (!allChecked()) {
            Toast.makeText(this, "Lengkapi semua checklist dulu.", Toast.LENGTH_SHORT).show();
            return;
        }
        mt5.setEnabled(true);
        unlock.setEnabled(false);
        status.setText("STATUS: TERBUKA • 15:00");
        timer = new CountDownTimer(UNLOCK_MS, 1000) {
            public void onTick(long ms) {
                long sec = ms / 1000;
                status.setText(String.format("STATUS: TERBUKA • %02d:%02d", sec/60, sec%60));
            }
            public void onFinish() { lockGate(); }
        }.start();
    }

    void lockGate() {
        if (timer != null) timer.cancel();
        mt5.setEnabled(false);
        unlock.setEnabled(true);
        status.setText("STATUS: TERKUNCI");
        for (int i=0; i<checks.getChildCount(); i++)
            ((CheckBox)checks.getChildAt(i)).setChecked(false);
        Toast.makeText(this, "Trading Gate terkunci kembali.", Toast.LENGTH_LONG).show();
    }

    void openMt5() {
        try {
            Intent i = getPackageManager().getLaunchIntentForPackage("net.metaquotes.metatrader5");
            if (i != null) startActivity(i);
            else Toast.makeText(this, "MT5 belum terpasang di HP.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "MT5 tidak dapat dibuka.", Toast.LENGTH_LONG).show();
        }
    }

    @Override protected void onDestroy() {
        if (timer != null) timer.cancel();
        super.onDestroy();
    }
}
