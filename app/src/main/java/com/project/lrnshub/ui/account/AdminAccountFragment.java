package com.project.lrnshub.ui.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.lrnshub.constants.Constants;
import com.project.lrnshub.databinding.FragmentAccountBinding;
import com.project.lrnshub.models.Users;
import com.project.lrnshub.preference.UserPreference;
import com.project.lrnshub.service.LocalWorkManager;
import com.project.lrnshub.service.LocalWorkerService;
import com.project.lrnshub.ui.aboutus.AboutUs;
import com.project.lrnshub.util.SplashScreen;

public class AdminAccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.name.setText("Administrator");
        binding.activitylogButton.setVisibility(View.GONE);
        binding.fView.setVisibility(View.GONE);
        binding.editProfileButton.setVisibility(View.GONE);

        binding.aboutusButton.setOnClickListener(v ->
                requireContext().startActivity(new Intent(requireContext(), AboutUs.class))
        );

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(requireContext());
                DialogInterface.OnClickListener dListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_NEGATIVE:
                                new UserPreference(requireContext()).saveLogin(new Users());
                                Constants.UserID = "";
                                LocalWorkerService.shouldStop = true;
                                requireContext().startActivity(new Intent(requireContext(), SplashScreen.class));
                                requireActivity().finishAffinity();
                                requireActivity().finish();
                                break;
                            default:
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                mBuilder.setMessage("Are You Sure You Want To Logout?")
                        .setNegativeButton("Yes", dListener)
                        .setPositiveButton("No", dListener)
                        .setCancelable(false)
                        .show();


            }
        });

        return root;
    }
}
