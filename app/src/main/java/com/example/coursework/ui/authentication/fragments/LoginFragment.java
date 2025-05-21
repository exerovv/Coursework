package com.example.coursework.ui.authentication.fragments;

import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coursework.ui.authentication.viewmodels.AuthViewModel;
import com.example.coursework.R;
import com.example.coursework.databinding.FragmentLoginBinding;
import com.example.coursework.ui.authentication.viewmodels.states.AuthState;
import com.example.coursework.ui.favorites.viewmodels.FavoriteViewModel;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding = null;

    private AuthViewModel authViewModel;
    private FavoriteViewModel favoriteViewModel;

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        favoriteViewModel = new ViewModelProvider(requireActivity()).get(FavoriteViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObservers();
        binding.loginBtn.setOnClickListener(view1 -> {
            String email = binding.emailLogin.getText().toString();
            String password = binding.passwordLogin.getText().toString();
            authViewModel.login(email, password);
        });
        binding.signUp.setOnClickListener(view2 -> findNavController(requireActivity(), R.id.nav_host_fragment_container)
                .navigate(LoginFragmentDirections.toRegFragment()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupObservers(){
        authViewModel.getAuthState().observe(getViewLifecycleOwner(), state -> {
            if (state instanceof AuthState.Success){
                favoriteViewModel.setExit(false);
                findNavController(requireActivity(), R.id.nav_host_fragment_container)
                        .navigate(LoginFragmentDirections.toProfileFragment());

            }
            if (state instanceof AuthState.Loading){
                binding.passwordLogin.setEnabled(false);
                binding.emailLogin.setEnabled(false);
                binding.loginBtn.setEnabled(false);
                binding.signUp.setEnabled(false);
            }
            if (state instanceof AuthState.Default){
                binding.passwordLogin.setEnabled(true);
                binding.emailLogin.setEnabled(true);
                binding.loginBtn.setEnabled(true);
                binding.signUp.setEnabled(true);
            }
            if (state instanceof AuthState.Error){
                Toast.makeText(requireContext(), ((AuthState.Error) state).error, Toast.LENGTH_SHORT).show();
                binding.passwordLogin.setEnabled(true);
                binding.emailLogin.setEnabled(true);
                binding.loginBtn.setEnabled(true);
                binding.signUp.setEnabled(true);
            }
        });
    }
}