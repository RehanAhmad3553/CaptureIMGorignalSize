package com.quizapplications.orignalresultpiccamera;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.google.firebase.storage.FirebaseStorage;
import com.quizapplications.orignalresultpiccamera.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    private String currentPhotoPath;

    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


       binding.button.setOnClickListener(v -> {


           String fileName ="photo";
           File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

           try {
               File imageFile = File.createTempFile(fileName,".jpg",storageDirectory);
               currentPhotoPath = imageFile.getAbsolutePath();
               Uri imageUri =   FileProvider.getUriForFile(MainActivity.this,"com.quizapplications.orignalresultpiccamera.fileprovider",imageFile);
               Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
               startActivityForResult(intent,1);

           } catch (IOException e) {
               throw new RuntimeException(e);
           }

       });


       binding.btnUpload.setOnClickListener(v -> {

           FirebaseStorage.getInstance().getReference().child("FolderName").putFile(uri);



       });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode== RESULT_OK)
        {
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
             uri = getImageUri(getApplicationContext(),bitmap);
            //binding.imageView.setImageBitmap(bitmap);
            binding.imageView.setImageURI(uri);
        }

    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



}