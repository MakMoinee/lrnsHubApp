package com.project.lrnshub.ui.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.lrnshub.databinding.FragmentAccountBinding;
import com.project.lrnshub.models.Users;
import com.project.lrnshub.preference.UserPreference;
import com.project.lrnshub.ui.aboutus.AboutUs;
import com.project.lrnshub.ui.activitylog.ActivityLog;
import com.project.lrnshub.util.SplashScreen;

public class AccountFragment extends Fragment {
    FirebaseAuth fAuth;
    private FragmentAccountBinding binding;
    Uri mImageUri;
    FirebaseFirestore fStore;
    CountDownTimer timer;
    private StorageReference storageReference;

    private static final int PICK_IMAGE_REQUEST = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("imageFolder");
        fStore = FirebaseFirestore.getInstance();

        binding.editProfileButton.setOnClickListener(v -> {
            openFileChooser();
        });

        binding.logoutButton.setOnClickListener(v -> {
            fAuth.signOut();
            new UserPreference(requireContext()).saveLogin(new Users());
            requireContext().startActivity(new Intent(requireContext(), SplashScreen.class));
            requireActivity().finishAffinity();
            requireActivity().finish();

        });

        binding.activitylogButton.setOnClickListener(v -> {
            requireContext().startActivity(new Intent(requireContext(), ActivityLog.class));
        });

        binding.aboutusButton.setOnClickListener(v ->
                requireContext().startActivity(new Intent(requireContext(), AboutUs.class))
        );


        updateImage();
        return root;
    }

    private void updateImage() {
        String userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("CheckResult")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = (String) documentSnapshot.getString("name");
                String id = (String) documentSnapshot.getString("id");
                Glide.with(requireContext()).asBitmap().load(id).centerCrop().into(binding.imageView);
                binding.name.setText(name);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (timer != null) {
            timer.cancel();
        }
    }


//    private void showNotifs() {
//        String channelId = "myNotificationChannel"; // Store channel ID as String or String resource
//
//        NotificationChannel notificationChannel = new NotificationChannel(channelId , "Notify", NotificationManager.IMPORTANCE_HIGH);
//        Intent intent = new Intent(requireContext(), MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
//
//        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.createNotificationChannel(notificationChannel);
//
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notification = new NotificationCompat.Builder(requireContext(), channelId)
//                .setSmallIcon(R.drawable.logo)
//                .setContentTitle("sample")
//                .setContentText("sample")
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//        notificationManager.notify(1, notification.build());
//    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == -1
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Glide.with(this).asBitmap().load(mImageUri).centerCrop().into(binding.imageView);
            StorageReference Imagename = storageReference.child("image" + mImageUri.getLastPathSegment());
            Imagename.putFile(mImageUri).addOnSuccessListener(taskSnapshot ->
                    Imagename.getDownloadUrl().addOnSuccessListener(this::addData));
        }
    }

    private void addData(Uri uri) {
        String userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.update("id", uri.toString()).addOnCompleteListener(task3 -> {
            Toast.makeText(requireContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
        });
    }

}