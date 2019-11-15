package com.example.sa_test7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class b_output extends AppCompatActivity {
    String B_id="";
    ImageView ivCode;
    TextView gogoing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_output);
        ivCode = (ImageView) findViewById(R.id.ivCode);
        gogoing=(TextView) findViewById(R.id.textView10);
        SharedPreferences settings = getSharedPreferences("user", 0);
        B_id= settings.getString("B_id","尚未註冊，請先註冊");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("ha", "加載JDBC驅動成功");
        } catch (ClassNotFoundException e) {
            Log.e("ha", "加載JDBC驅動失敗");
            return;
        }
        db_sqlout.start();

    }

    public void goback(View v){
        Intent it = new Intent(b_output.this,MainActivity.class);
        startActivity(it);
        finish();
    }



    public ResultSet rSet;
    public String url = "jdbc:mysql://140.135.113.188:5270/kmbmteam?serverTimezone=UTC";
        final  Thread db_sqlout = new Thread(new Runnable() {
            @Override
            public void run() {

                // 3.連接JDBC
                try {
                    Connection conn = DriverManager.getConnection(url, "kilin", "5270");

                    if (conn != null) {

                        java.sql.Statement statement = conn.createStatement();

                        rSet = statement.executeQuery("  SELECT * FROM kmbmteam.product where `P_business`="+B_id+";");
                        Log.v("ha", "遠程連接成功!");
                        if(rSet.next()) {

                            BarcodeEncoder encoder = new BarcodeEncoder();
                            Log.v("ha", "遠程連接成功!1");
                            final  Bitmap bit = encoder.encodeBitmap(rSet.getString("P_id")+";"+rSet.getString("P_price"), BarcodeFormat.QR_CODE,
                                    1000, 1000);

                            ivCode.post(new Runnable() {
                                public void run() {
                                    ivCode.setImageBitmap(bit);

                                }

                            });
                            final String name_price="商品名稱："+rSet.getString("P_name")+"\n價格："+rSet.getString("P_price");
                           gogoing.post(new Runnable() {
                               @Override
                               public void run() {
                                   gogoing.setText(name_price);
                               }
                           });

                        }

                        conn.close();

                        return;

                    }
                } catch (Exception e) {
                    Log.e("ha", "遠程連接失敗!" + e);

                }
            }
        });



}
