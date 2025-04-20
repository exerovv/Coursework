package com.example.coursework.ui.fragments;

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

import com.example.coursework.ui.viewModels.AuthViewModel;
import com.example.coursework.R;
import com.example.coursework.databinding.FragmentLoginBinding;
import com.example.coursework.utils.AuthState;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding = null;

    private AuthViewModel viewModel;

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
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
            viewModel.login(email, password);
        });
        binding.signUp.setOnClickListener(view2 -> findNavController(requireActivity(), R.id.nav_host_fragment_container)
                .navigate(LoginFragmentDirections.toRegFromLogin()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupObservers(){
        viewModel.getAuthState().observe(getViewLifecycleOwner(), state -> {
            if (state instanceof AuthState.Success)
                findNavController(requireActivity(), R.id.nav_host_fragment_container)
                        .navigate(LoginFragmentDirections.toProfileFromLogin());
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