package com.example.sa_test7;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class M_register extends AppCompatActivity {
    Button buttons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_register);
       buttons = (Button) findViewById(R.id.button);
    }
    String getname2="";

    public void go_member(View v){
        EditText getname = (EditText) findViewById(R.id.edit_getname);
        getname2=String.valueOf(getname.getText());

        if(getname2.equals("")){
            new AlertDialog.Builder(M_register.this)
                    .setTitle("注意")//設定視窗標題
                    .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                    .setMessage("請輸入暱稱")//設定顯示的文字
                    .setPositiveButton("關閉視窗",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ;
                        }
                    })//設定結束的子視窗
                    .show();//呈現對話視窗
        }else{
            getjdbc();

            dbsetname(getname2);
            new AlertDialog.Builder(M_register.this)
                    .setTitle("對話視窗")//設定視窗標題
                    .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                    .setMessage("請稍候自動跳轉")//設定顯示的文字
                    .show();//呈現對話視窗
            buttons.setVisibility(View.INVISIBLE);
        }
        /*

         new AlertDialog.Builder(M_register.this)
                .setTitle("對話視窗")//設定視窗標題
                .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                .setMessage("這是一個對話視窗")//設定顯示的文字
                .setPositiveButton("關閉視窗",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish;
                    }
                })//設定結束的子視窗
                .show();//呈現對話視窗




        SharedPreferences settings = getSharedPreferences("username", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("myStr", "kkkkkkkkkkkkkk");
        editor.commit();
        Intent it = new Intent(M_register.this,member.class);
        startActivity(it);
        finish();

         */
    }
    public  String sql="";
    public ResultSet rSet;
    public  String name="";
    public String url = "jdbc:mysql://db4free.net:3306/kmbmteam?serverTimezone=UTC";
    public  void getjdbc(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("ha", "加載JDBC驅動成功");
        } catch (ClassNotFoundException e) {
            Log.e("ha", "加載JDBC驅動失敗");

            return;
        }
    }


    //執行
    public void dbexe(String setsql){
        sql=setsql;
        Thread db_exe = new Thread(new Runnable() {
            @Override
            public void run() {

                // 3.連接JDBC
                try {
                    Connection conn = DriverManager.getConnection(url, "mkbmyo", "13145270");
                    Log.v("ha", "遠程連接成功!");
                    if (conn != null) {

                        java.sql.Statement statement = conn.createStatement();
                        statement.execute(sql);
                        conn.close();
                        Log.v("ha", "執行完畢");
                        return ;
                    }
                } catch (SQLException e) {
                    Log.e("ha", "遠程連接失敗!" + e);

                }
            }
        });
        db_exe.start();
    }

    //M_register_禁止重複暱稱並增加資料
    public  void dbsetname(String setname){
        name=setname;

      final  Thread db_setname = new Thread(new Runnable() {
            @Override
            public void run() {

                // 3.連接JDBC
                try {
                    Connection conn = DriverManager.getConnection(url, "mkbmyo", "13145270");
                    Log.v("ha", "遠程連接成功!");
                    if (conn != null) {

                        java.sql.Statement statement = conn.createStatement();
                        rSet = statement.executeQuery("SELECT M_id FROM kmbmteam.member where M_name='"+name+"'");
                        if(rSet.next()){

                            //重複
                            SharedPreferences settings = getSharedPreferences("user", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("M_id", "重複");
                            editor.commit();
                            Intent it = new Intent(M_register.this,MainActivity.class);
                            startActivity(it);
                            finish();
                        }else
                        {
                            statement.execute("INSERT INTO `kmbmteam`.`member` (`M_name`) VALUES ('"+name+"');");
                            rSet = statement.executeQuery("SELECT M_id,M_money FROM kmbmteam.member where M_name='"+name+"'");
                            if(rSet.next()){
                                SharedPreferences settings = getSharedPreferences("user", 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("M_id", rSet.getString("M_id"));
                                editor.putString("M_money", rSet.getString("M_money"));
                                editor.commit();
                            }
                            Intent it = new Intent(M_register.this,MainActivity.class);
                            startActivity(it);
                            finish();

                        }
                        conn.close();

                        return;

                    }
                } catch (Exception e) {
                    Log.e("ha", "遠程連接失敗!" + e);

                }
            }
        });
        db_setname.start();
    }


    public void goback(View v){
        Intent it = new Intent(M_register.this,MainActivity.class);
        startActivity(it);
        finish();
    }

}
