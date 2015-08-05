package com.moocher.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moocher.squarecameralibrary.CameraActivity;
import com.moocher.squarecameralibrary.ImageStore;

import java.io.File;
import java.lang.annotation.Target;


public class MainActivity extends ActionBarActivity {
    private static final int REQUEST_TAKE_PICTURE_CODE = 0x111;

    private TextView camera;
    private ImageView show;
    private File saveFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show = (ImageView)findViewById(R.id.show_image);

        File dirFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera");
        saveFile = new File(dirFile, "test.jpg");

        camera = (TextView)findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra(ImageStore.SQUARE_LENGTH_KEY, 600);
                //intent.putExtra(ImageStore.SQUARE_EXTRA_OUTPUT_KEY, Uri.fromFile(saveFile));
                startActivityForResult(intent, REQUEST_TAKE_PICTURE_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PICTURE_CODE && resultCode == RESULT_OK){
//            Bitmap bitmap = BitmapFactory.decodeFile(saveFile.getAbsolutePath());
            byte[] buffer = data.getByteArrayExtra("data");
            Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            show.setImageBitmap(bitmap);
        }else if(resultCode == CameraActivity.RESULT_ALBUMS && requestCode == REQUEST_TAKE_PICTURE_CODE){
            String imagePath = data.getStringExtra("imagePath");
            Glide.with(this)
                    .load(imagePath)
                    .override(500, 500)
                    .into(show);
        }
    }
}
