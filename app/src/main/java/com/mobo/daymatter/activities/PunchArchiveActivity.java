package com.mobo.daymatter.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobo.daymatter.R;
import com.mobo.daymatter.adapter.PunchArchiveAdapter;
import com.mobo.daymatter.adapter.PunchClockAdapter;
import com.mobo.daymatter.beans.PunchClockBean;
import com.mobo.daymatter.manager.PunchClockManager;
import com.mobo.daymatter.utils.MoboLogger;

import java.util.ArrayList;

public class PunchArchiveActivity extends BaseActivity{
    private RecyclerView mRecycleView;
    private PunchArchiveAdapter mAdapter;
    private TextView tvEmpty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecycleView = findViewById(R.id.recycler);
        tvEmpty = findViewById(R.id.tv_isEmpty);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PunchArchiveAdapter(this, new PunchArchiveAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PunchClockBean bean) {
                MoboLogger.debug("ddd", "item");
                Toast.makeText(PunchArchiveActivity.this, R.string.matter_punch_archived_click, Toast.LENGTH_SHORT).show();
            }
        });
        mRecycleView.setAdapter(mAdapter);
        ArrayList<PunchClockBean> beans = PunchClockManager.get().getArchives();
        if (beans != null && !beans.isEmpty()) {
            mAdapter.refresh(beans);
            mRecycleView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }else {
            tvEmpty.setVisibility(View.VISIBLE);
        }
    }
}
