package com.example.coursework.ui.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.coursework.R;
import com.example.coursework.databinding.ActivityMainBinding;
import com.example.coursework.ui.authentication.viewmodels.UserSessionViewModel;
import com.example.coursework.ui.favorites.viewmodels.FavoriteViewModel;

import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        FavoriteViewModel favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        UserSessionViewModel userSessionViewModel = new ViewModelProvider(this).get(UserSessionViewModel.class);
        favoriteViewModel.setFavoriteId(userSessionViewModel.getCurrentUser());
        setContentView(binding.getRoot());
        //Устанавливем кастомный Aoolbar вместо системного ActionBar
        setSupportActionBar(binding.toolbar);
        //Убираем отображение заголовка фрагмента, так как это реализовано через Navigation Component
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        //Получение NavController
        NavController controller = Navigation.findNavController(this, R.id.nav_host_fragment_container);
        //Добавление конфигурации appbar для корректной работы с навигацией, а именно чтобы кнопка назад
        //не появлялась на указанных экранах
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                Set.of(
                        R.id.homeFragment,
                        R.id.searchFragment,
                        R.id.favoritesFragment,
                        R.id.profileFragment)).build();
        //Обработка повторных нажатий на кнопки меню. Добавлено для того чтобы вернуться с фрагмента деталей
        //на родительский
        binding.bottomBar.setOnItemReselectedListener(item -> {
            if (item.getItemId() == R.id.homeFragment) controller.popBackStack(R.id.homeFragment, false);
            if (item.getItemId() == R.id.searchFragment) controller.popBackStack(R.id.searchFragment, false);
            if (item.getItemId() == R.id.favoritesFragment) controller.popBackStack(R.id.favoritesFragment, false);
            if (item.getItemId() == R.id.profileFragment) controller.popBackStack(R.id.profileFragment, false);
        });

        //Связывание bottomBar с NavController
        NavigationUI.setupWithNavController(binding.bottomBar, controller);
        //Связывание toolbar с NavController с возможностью обработки кнопки back
        NavigationUI.setupWithNavController(binding.toolbar, controller, appBarConfiguration);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

