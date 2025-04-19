package com.example.coursework.ui;

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

import com.example.coursework.AuthViewModel;
import com.example.coursework.R;
import com.example.coursework.databinding.FragmentRegisterBinding;
import com.example.coursework.utils.AuthState;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private AuthViewModel viewModel;

    public RegisterFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObservers();
        binding.regBtn.setOnClickListener(view1 -> {
            String email = binding.emailReg.getText().toString();
            String password = binding.passwordReg.getText().toString();
            viewModel.register(email, password);
        });

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
                        .navigate(RegisterFragmentDirections.toProfileFromReg());
            if (state instanceof AuthState.Loading){
                binding.passwordReg.setEnabled(false);
                binding.emailReg.setEnabled(false);
                binding.regBtn.setEnabled(false);
            }
            if (state instanceof AuthState.Default){
                binding.passwordReg.setEnabled(true);
                binding.emailReg.setEnabled(true);
                binding.regBtn.setEnabled(true);
            }
            if (state instanceof AuthState.Error){
                Toast.makeText(requireContext(), ((AuthState.Error) state).error, Toast.LENGTH_SHORT).show();
                binding.passwordReg.setEnabled(true);
                binding.emailReg.setEnabled(true);
                binding.regBtn.setEnabled(true);
            }
        });
    }
}