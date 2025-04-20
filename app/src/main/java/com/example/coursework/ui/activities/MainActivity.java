package com.example.coursework.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.coursework.R;
import com.example.coursework.databinding.ActivityMainBinding;
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavController controller = Navigation.findNavController(this, R.id.nav_host_fragment_container);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(controller.getGraph()).build();
        NavigationUI.setupWithNavController(binding.bottomBar, controller);
        NavigationUI.setupWithNavController(binding.toolbar, controller, appBarConfiguration);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

