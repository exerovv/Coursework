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
import com.example.coursework.databinding.FragmentLoginBinding;
import com.example.coursework.model.service.impl.UserAuthImpl;
import com.example.coursework.utils.AuthCallback;

public class LoginFragment extends Fragment {
    private final UserAuthImpl userLogin = new UserAuthImpl();
    private FragmentLoginBinding binding = null;

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (userLogin.getmAuth().getCurrentUser() != null){
            navigateFragment(new ProfileFragment());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.loginBtn.setOnClickListener(view1 -> {
            String email = binding.emailLogin.getText().toString();
            String password = binding.passwordLogin.getText().toString();
            if (userLogin.validateEmail(email) && userLogin.validatePassword(password)){
                userLogin.signIn(email, password, new AuthCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(requireContext(), "Auth success!", Toast.LENGTH_SHORT).show();
                        navigateFragment(new ProfileFragment());
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(requireContext(), "Auth error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(requireContext(), "Wrong fields values!", Toast.LENGTH_SHORT).show();
            }
        });
        binding.signUp.setOnClickListener(view2 -> navigateFragment(new RegisterFragment()));
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