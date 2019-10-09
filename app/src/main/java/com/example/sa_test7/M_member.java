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

public class M_member extends AppCompatActivity {
    String M_id="";
    String M_money="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_member);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("ha", "加載JDBC驅動成功");
        } catch (ClassNotFoundException e) {
            Log.e("ha", "加載JDBC驅動失敗");
            return;
        }
        db_nowmoney.start();
        SharedPreferences settings = getSharedPreferences("user", 0);
        M_id= settings.getString("M_id","尚未註冊，請先註冊");
        M_money= settings.getString("M_money","若無金額請重新啟動");
        TextView etContent = (TextView) findViewById(R.id.nowmoney);
        etContent.setText("目前金額："+M_money);

    }

    public void buy_click(View v){
        Intent it = new Intent(M_member.this,M_scanner.class);
        startActivity(it);
        finish();
    }
    public void getmoney_click(View v){
        Intent it = new Intent(M_member.this,M_output.class);
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

                        rSet = statement.executeQuery("SELECT M_money FROM kmbmteam.member where M_id='"+M_id+"'");
                        Log.v("ha", "遠程連接成功!");
                        if(rSet.next()) {
                            SharedPreferences settings = getSharedPreferences("user", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("M_money", rSet.getString("M_money"));
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
