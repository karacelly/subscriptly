package edu.bluejack21_2.subscriptly.repositories;

import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import edu.bluejack21_2.subscriptly.interfaces.QueryFinishListener;

public class ImageRepository {
    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    public static StorageReference storageRef = storage.getReference();

    public static void InsertImage(String folder, String fileName, Uri image, QueryFinishListener<Boolean> listener) {
        StorageReference folderStorageRef = storageRef.child(folder + "/" + fileName);
        UploadTask uploadTask = folderStorageRef.putFile(image);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            listener.onFinish(true);
        }).addOnFailureListener(e -> {
            listener.onFinish(false);
        });
    }
}
