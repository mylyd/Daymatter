package com.mobo.daymatter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.mobo.daymatter.MatterApplication;
import com.mobo.daymatter.R;
import com.mobo.daymatter.utils.BaseUtils;

public class StartUpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MatterApplication.getHadLaunched()) {
            startActivity(new Intent(this, DayMatterActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_start_up);
        String version = "Daily Reminder V " + BaseUtils.getVersionName(this);
        ((TextView) findViewById(R.id.tv_version)).setText(version);

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(StartUpActivity.this, DayMatterActivity.class));
                overridePendingTransition(0, R.anim.zoomout);
                finish();
            }
        }, 2000);
    }
}
