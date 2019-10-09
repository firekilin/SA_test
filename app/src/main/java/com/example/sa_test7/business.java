package com.example.sa_test7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class business extends AppCompatActivity {
    String B_id="";
    String B_money="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("ha", "加載JDBC驅動成功");
        } catch (ClassNotFoundException e) {
            Log.e("ha", "加載JDBC驅動失敗");
            return;
        }
        db_nowmoney.start();
        SharedPreferences settings = getSharedPreferences("user", 0);
        B_id= settings.getString("B_id","尚未註冊，請先註冊");
        B_money= settings.getString("B_money","若無金額請重新啟動");
        TextView etContent = (TextView) findViewById(R.id.nowmoney);
        etContent.setText("目前金額："+B_money);

    }

    public void gift_click(View v){
        Intent it = new Intent(business.this,B_scanner.class);
        startActivity(it);
        finish();
    }
    public void sale_click(View v){
        Intent it = new Intent(business.this,b_output.class);
        startActivity(it);
        finish();
    }
    public ResultSet rSet;

    public String url = "jdbc:mysql://db4free.net:3306/kmbmteam?serverTimezone=UTC";



    final  Thread db_nowmoney = new Thread(new Runnable() {
        @Override
        public void run() {

            // 3.連接JDBC
            try {
                Connection conn = DriverManager.getConnection(url, "mkbmyo", "13145270");

                if (conn != null) {

                    java.sql.Statement statement = conn.createStatement();

                    rSet = statement.executeQuery("SELECT B_money FROM kmbmteam.business where B_id='"+B_id+"'");
                    Log.v("ha", "遠程連接成功!");
                    if(rSet.next()) {
                        SharedPreferences settings = getSharedPreferences("user", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("B_money", rSet.getString("B_money"));
                        editor.commit();
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
