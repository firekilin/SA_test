package com.example.sa_test7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class M_output extends AppCompatActivity {
    String M_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_output);
        SharedPreferences settings = getSharedPreferences("user", 0);
        M_id= settings.getString("M_id","尚未註冊，請先註冊");
        ImageView ivCode = (ImageView) findViewById(R.id.ivCode);
        TextView gogoing=(TextView) findViewById(R.id.textView5);
        BarcodeEncoder encoder = new BarcodeEncoder();
        try {
            Bitmap bit = encoder.encodeBitmap("SAtest;"+M_id, BarcodeFormat.QR_CODE,
                    1000, 1000);
            ivCode.setImageBitmap(bit);
            gogoing.setText("會員編號："+M_id);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void goback(View v){
        Intent it = new Intent(M_output.this,MainActivity.class);
        startActivity(it);
        finish();
    }
}
