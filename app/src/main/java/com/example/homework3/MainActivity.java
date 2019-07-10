package com.example.homework3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.homework3.Constants.REQUEST_SELECT_CONTACT;

public class MainActivity extends AppCompatActivity {

    Button btnCamera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCamera = findViewById(R.id.btnCamera);


    }

    public void buttonClick(View view)
    {
        Intent i;
        switch (view.getId())
        {
            case R.id.btnCamera:
                 i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager())!=null)
                {
                    startActivityForResult(i, Constants.REQUEST_IMAGE_CAPTURE);
                }
                break;
            case R.id.btnContact:
                i = new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, Constants.REQUEST_SELECT_PHONE_NUMBER);
                }
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBMP = (Bitmap) extras.get("data");
            ImageView imgV = findViewById(R.id.imageView);
            imgV.setImageBitmap(imageBMP);
        }
        else  if (requestCode == Constants.REQUEST_SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
            Cursor cursor = getContentResolver().query(contactUri, projection,
                    null, null, null);
            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
               int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberIndex);
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name = cursor.getString(nameIndex);

                TextView textView = findViewById(R.id.txtViewContact);
             textView.setText(number.concat("\n" + name));
            }
        }
    }
}

