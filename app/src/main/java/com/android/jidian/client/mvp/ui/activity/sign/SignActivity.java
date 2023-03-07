package com.android.jidian.client.mvp.ui.activity.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.widgets.PaintView;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;

public class SignActivity extends AppCompatActivity {

    private PaintView paintView;
    private TextView tvTest;
    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        paintView = findViewById(R.id.paintView);
        ivImage = findViewById(R.id.ivImage);
        tvTest = findViewById(R.id.tvTest);
        tvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath;
                if (Build.VERSION.SDK_INT >= 29) {
                    filePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "img111.jpeg" ;
                }else {
                    filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "img111.jpeg";
                }
                Log.d("xiaoming0307", "onClick: " + filePath);
                try {
                    paintView.save(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ivImage.setImageURI(Uri.fromFile(new File(filePath)));
            }
        });
    }
}