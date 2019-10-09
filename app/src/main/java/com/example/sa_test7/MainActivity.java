package com.example.sa_test7;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Member;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences settings = getSharedPreferences("user", 0);



        String str = settings.getString("M_id","尚未註冊，請先註冊");
        if (str.equals("重複")){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("注意")//設定視窗標題
                    .setMessage("暱稱重複，請重新註冊")//設定顯示的文字
                    .setPositiveButton("關閉視窗",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })//設定結束的子視窗
                    .show();//呈現對話視窗
        }else if(str.equals("尚未註冊，請先註冊")){
            String str2 = settings.getString("B_id","尚未註冊，請先註冊");
            if (str2.equals("重複")){
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("注意")//設定視窗標題
                        .setMessage("暱稱重複，請重新註冊")//設定顯示的文字
                        .setPositiveButton("關閉視窗",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })//設定結束的子視窗
                        .show();//呈現對話視窗
            }else if(str2.equals("尚未註冊，請先註冊")) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("注意")//設定視窗標題
                        .setMessage(str)//設定顯示的文字
                        .setPositiveButton("關閉視窗", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })//設定結束的子視窗
                        .show();//呈現對話視窗
            }else{
                gobusiness();
            }
        }else{
            gomember();
        }



    }
    public void gobusiness(){
        Intent it = new Intent(MainActivity.this,business.class);
        startActivity(it);
        finish();
    }
    public void gomember(){
        Intent it = new Intent(MainActivity.this,M_member.class);
        startActivity(it);
        finish();
    }
    public void b_register_click(View v){
        Intent it = new Intent(MainActivity.this, B_register.class);
        startActivity(it);
        finish();
    }
    public void m_register_click(View v){
        Intent it = new Intent(MainActivity.this,M_register.class);
        startActivity(it);
        finish();
    }

        /* 儲存註冊資料
        SharedPreferences settings = getSharedPreferences("user", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("M_id", "kkkkkkkkkkkkkk");
        editor.commit();
        String str = settings.getString("M_id","defaultStr");
        Log.v("ha",str);
      */
    /*
          yoyo.start();
    final Thread yoyo = new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("ha", "加載JDBC驅動成功");
        } catch (ClassNotFoundException e) {
            Log.e("ha", "加載JDBC驅動失敗");
            return;
        }

            String url="jdbc:mysql://db4free.net:3306/kmbmteam?serverTimezone=UTC";
            // 3.連接JDBC
            try {
                Connection conn;
                conn= DriverManager.getConnection(url,"mkbmyo","13145270");
                Log.v("ha", "遠程連接成功!");
                if (conn != null) {
                    String sql = "SELECT * FROM kmbmteam.member";
                    java.sql.Statement statement = conn.createStatement();
                    ResultSet rSet = statement.executeQuery(sql);
                    String showa="";
                    while (rSet.next()) {
                        showa=showa+"/n"+rSet.getString(3);
                    }
                    conn.close();
                    return;
                }
            } catch (SQLException e) {
                Log.e("ha", "遠程連接失敗!"+e);
            }

        }

    });
    */

}
