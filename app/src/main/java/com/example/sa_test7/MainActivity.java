package com.example.sa_test7;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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


        final Thread yoyo = new Thread(new Runnable() {
            @Override
            public void run() {

                //加載驅動
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Log.v("ha", "加載JDBC驅動成功");
                } catch (ClassNotFoundException e) {
                    Log.e("ha", "加載JDBC驅動失敗");
                    return;
                }

                //連接資料庫
                String url="jdbc:mysql://140.135.113.197:5222/sa?serverTimezone=UTC";
                try {

                    Connection conn = DriverManager.getConnection(url, "jing", "1234");
                    Log.v("ha", "遠程連接成功!");
                    if (conn != null) {
                        String sql = "SELECT * FROM sa.member";
                        java.sql.Statement statement = conn.createStatement();
                        ResultSet rSet = statement.executeQuery(sql);

                        conn.close();
                        return;
                    }
                } catch (SQLException e) {
                    Log.e("ha", "遠程連接失敗!"+e);
                }

            }

        });
        yoyo.start();
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

        //加載驅動
          try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("ha", "加載JDBC驅動成功");
        } catch (ClassNotFoundException e) {
            Log.e("ha", "加載JDBC驅動失敗");
            return;
        }

        //連接資料庫
            String url="jdbc:mysql://db4free.net:3306/kmbmteam?serverTimezone=UTC";
            try {
                Connection conn;
                conn= DriverManager.getConnection(url,"（帳號）","（密碼）");
                Log.v("ha", "遠程連接成功!");
                if (conn != null) {
                    String sql = "SELECT * FROM kmbmteam.member";
                    java.sql.Statement statement = conn.createStatement();
                    ResultSet rSet = statement.executeQuery(sql);
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
