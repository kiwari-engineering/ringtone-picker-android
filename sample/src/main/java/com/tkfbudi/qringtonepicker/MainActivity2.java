package com.tkfbudi.qringtonepicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tkfbudi.ringtone.QRingtone;
import com.tkfbudi.ringtone.QRingtoneListener;
import com.tkfbudi.ringtone.Ringtone;

import org.jetbrains.annotations.NotNull;

/**
 * Created on : 07/10/18
 * Author     : Taufik Budi S
 * GitHub     : https://github.com/tfkbudi
 */
class MainActivity2 extends AppCompatActivity {

    String currentRingtone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new QRingtone(this, getSupportFragmentManager())
                .setTitle("Ringtone Picker")
                .isPlayable(true)
                .setNegativeButton("Cancel")
                .setPositiveButton("Ok")
                .setCurrentRingtone(currentRingtone)
                .addDefaultRingtone(false)
                .addSilentRingtone(false)
                .setRingtoneListener(new QRingtoneListener() {
                    @Override
                    public void onItemRingtoneSelected(@NotNull Ringtone ringtone) {
                        currentRingtone = ringtone.getUri().toString();

                    }
                })
                .show();
    }
}
