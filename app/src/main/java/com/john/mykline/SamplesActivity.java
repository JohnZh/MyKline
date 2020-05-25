package com.john.mykline;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.john.mykline.databinding.ActivitySamplesBinding;

import androidx.appcompat.app.AppCompatActivity;

public class SamplesActivity extends AppCompatActivity {

    private ActivitySamplesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySamplesBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.areasAndAppendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SamplesActivity.this, HistoryActivity.class));
            }
        });
        binding.modifyStyles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SamplesActivity.this, ChangeStyleActivity.class));
            }
        });
        binding.modifyIndicatorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
