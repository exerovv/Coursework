package com.example.coursework;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_bar);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.favorites){
                replaceFragment(new FavoritesFragment());
            }
            else if (item.getItemId() == R.id.search){
                replaceFragment(new SearchFragment());
            }
            else if (item.getItemId() == R.id.profile){
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}