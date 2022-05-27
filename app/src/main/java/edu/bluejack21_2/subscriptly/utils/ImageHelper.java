package edu.bluejack21_2.subscriptly.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.UUID;

import edu.bluejack21_2.subscriptly.AddSubscriptionActivity;
import edu.bluejack21_2.subscriptly.interfaces.QueryFinishListener;
import edu.bluejack21_2.subscriptly.repositories.ImageRepository;

public class ImageHelper {
    public static String getImageFileName(Context context, Uri imageUri) {
        String uuid = UUID.randomUUID().toString();
        String extension = getMimeType(context, imageUri);
        return uuid + "." + extension;
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }

        return extension;
    }

    public static void LoadImage(Context ctx, String url, ImageView image) {
        Glide.with(ctx).load(url).into(image);
    }

    public static ActivityResultLauncher<Intent> chooseImageAndUpload(AppCompatActivity activity, ImageView targetView, String folderName, QueryFinishListener<String> listener) {

        return activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
//                    if(result.getResultCode() == 3) {
//
//                    }
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        if (data != null) {
                            Uri selectedImage = data.getData();
                            if(targetView != null) {
                                targetView.setImageURI(selectedImage);
                            }
                            String fileName = getImageFileName(activity.getApplicationContext(), selectedImage);
                            ImageRepository.InsertImage(activity, folderName, fileName, selectedImage, imageURL -> {
                                listener.onFinish(imageURL);
//                                Toast.makeText(activity.getApplicationContext(), "Upload Image Failed!", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
    }

}
