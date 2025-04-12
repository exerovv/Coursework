package com.example.coursework.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coursework.R;
import com.example.coursework.databinding.FragmentRegisterBinding;
import com.example.coursework.model.service.impl.UserAuthImpl;
import com.example.coursework.utils.AuthCallback;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private final UserAuthImpl userReg = new UserAuthImpl();

    public RegisterFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        binding.regBtn.setOnClickListener(view1 -> {
            String email = binding.emailReg.getText().toString();
            String password = binding.passwordReg.getText().toString();
            if (userReg.validateEmail(email) && userReg.validatePassword(password)){
                userReg.signUp(email, password, new AuthCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(requireContext(), "Registration success!", Toast.LENGTH_SHORT).show();
                        navigateFragment(new LoginFragment());
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(requireContext(), "Registration error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(requireContext(), "Wrong fields values!", Toast.LENGTH_SHORT).show();
            }
        });
        binding.backArrow.setOnClickListener(view2 -> navigateFragment(new LoginFragment()));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void navigateFragment(Fragment fragment){
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}