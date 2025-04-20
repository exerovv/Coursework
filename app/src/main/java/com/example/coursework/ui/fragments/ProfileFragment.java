package com.example.coursework.ui.fragments;

import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.coursework.ui.viewModels.AuthViewModel;
import com.example.coursework.R;
import com.example.coursework.databinding.FragmentProfileBinding;
import com.example.coursework.ui.viewModels.LanguageViewModel;


public class ProfileFragment extends Fragment {
    private AuthViewModel authViewModel;
    private LanguageViewModel languageViewModel;
    private FragmentProfileBinding binding = null;
    public ProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        if (!authViewModel.checkUser()) findNavController(requireActivity(), R.id.nav_host_fragment_container)
                .navigate(ProfileFragmentDirections.toLoginFromProfile());
        languageViewModel = new ViewModelProvider(requireActivity()).get(LanguageViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner spinner = binding.languageChangeSpinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.languages_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        languageViewModel.getLangLiveData().observe(getViewLifecycleOwner(), integer -> {
            spinner.setSelection(integer);
            if (integer == 0)
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"));
            else
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("ru"));
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                languageViewModel.saveSpinnerPos(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

//        binding.passwordChange.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                authViewModel.
//            }
//        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}