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

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentChangePasswordBinding;
import com.example.coursework.ui.authentication.viewmodels.AuthViewModel;
import com.example.coursework.ui.authentication.viewmodels.states.AuthState;

public class PasswordChangeFragment extends Fragment {
    private FragmentChangePasswordBinding binding = null;
    private AuthViewModel authViewModel;

    public PasswordChangeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObservers();
        binding.passwordChangeBtn.setOnClickListener(view1 -> {
            String email = binding.changePasswordEmail.getText().toString();
            String oldPassword = binding.oldPassword.getText().toString();
            String newPassword = binding.newPassword.getText().toString();
            authViewModel.changePassword(email, oldPassword, newPassword);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupObservers(){
        authViewModel.getAuthState().observe(getViewLifecycleOwner(), state -> {
            if (state instanceof AuthState.Success){
                findNavController(requireActivity(), R.id.nav_host_fragment_container)
                        .navigate(PasswordChangeFragmentDirections.toLoginFragment());
            }

            if (state instanceof AuthState.Loading){
                binding.oldPassword.setEnabled(false);
                binding.changePasswordEmail.setEnabled(false);
                binding.newPassword.setEnabled(false);
            }
            if (state instanceof AuthState.Default){
                binding.oldPassword.setEnabled(true);
                binding.changePasswordEmail.setEnabled(true);
                binding.newPassword.setEnabled(true);
            }
            if (state instanceof AuthState.Error){
                Toast.makeText(requireContext(), ((AuthState.Error) state).error, Toast.LENGTH_SHORT).show();
                binding.oldPassword.setEnabled(true);
                binding.changePasswordEmail.setEnabled(true);
                binding.newPassword.setEnabled(true);
            }
        });
    }
}