package com.john.mykline;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.john.mykline.databinding.ActivityCombinationBinding;

import androidx.appcompat.app.AppCompatActivity;

public class CombinationActivity extends AppCompatActivity {

    private ActivityCombinationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCombinationBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

    }
}
