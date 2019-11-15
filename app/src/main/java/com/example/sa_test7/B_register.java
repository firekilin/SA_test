package com.example.sa_test7;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class B_register extends AppCompatActivity {
    String name;
    String p_name;
    String p_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_register);
    }
    //M_register_禁止重複暱稱並增加資料
    public  void B_register_click(View v){
        EditText getname = (EditText) findViewById(R.id.editText);
        EditText get_name = (EditText) findViewById(R.id.editText4);
        EditText get_price = (EditText) findViewById(R.id.editText3);
        Button buttons=(Button) findViewById(R.id.button4) ;
        name=getname.getText().toString();
        p_name=get_name.getText().toString();
        p_price=get_price.getText().toString();

        register();
        new AlertDialog.Builder(B_register.this)
                .setTitle("對話視窗")//設定視窗標題
                .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                .setMessage("請稍候自動跳轉")//設定顯示的文字
                .show();//呈現對話視窗
        buttons.setVisibility(View.INVISIBLE);
    }

    public void register(){
        final  Thread db_setname = new Thread(new Runnable() {
            @Override
            public void run() {

                // 3.連接JDBC
                try {
                    String url = "jdbc:mysql://140.135.113.188:5270/kmbmteam?serverTimezone=UTC";
                    Connection conn = DriverManager.getConnection(url, "kilin", "5270");
                    Log.v("ha", "遠程連接成功!");
                    if (conn != null) {
                        java.sql.Statement statement = conn.createStatement();
                        ResultSet rSet = statement.executeQuery("SELECT B_id FROM kmbmteam.business where B_name='"+name+"'");
                        if(rSet.next()){

                            //重複
                            SharedPreferences settings = getSharedPreferences("user", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("B_id", "重複");
                            editor.commit();
                            Intent it = new Intent(B_register.this,MainActivity.class);
                            startActivity(it);
                            finish();
                        }else
                        {
                            statement.execute("INSERT INTO `kmbmteam`.`business` (`B_name`) VALUES ('"+name+"');");
                            Log.v("ha", "遠程連接成功2!");
                            rSet = statement.executeQuery("SELECT B_id,B_money FROM kmbmteam.business where B_name='"+name+"'");
                            Log.v("ha", "遠程連接成功3!");
                            if(rSet.next()){
                                String get_id=rSet.getString("B_id");
                                SharedPreferences settings = getSharedPreferences("user", 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("B_id", rSet.getString("B_id"));
                                editor.putString("B_money", rSet.getString("B_money"));
                                editor.commit();
                                statement.execute("  INSERT INTO `kmbmteam`.`activity` (`A_business`) VALUES ('"+get_id+"');");
                                Log.v("ha", "遠程連接成功4!");
                                statement.execute("INSERT INTO `kmbmteam`.`product` (`P_business`, `P_name`, `P_price`) VALUES ('"+get_id+"', '"+p_name+"', '"+p_price+"');");
                                Log.v("ha", "遠程連接成功5!");
                            }

                            Intent it = new Intent(B_register.this,MainActivity.class);
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
        Intent it = new Intent(B_register.this,MainActivity.class);
        startActivity(it);
        finish();
    }
}
