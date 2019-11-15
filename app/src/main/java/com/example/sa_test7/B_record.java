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

public class B_record extends AppCompatActivity {
    String B_id="";
    int act_page;
    int buy_page;
    TextView[] act=new TextView[5];
    TextView[] buy=new TextView[5];
    int x=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_record);

        SharedPreferences settings = getSharedPreferences("user", 0);
        B_id= settings.getString("B_id","尚未註冊，請先註冊");

        act[0]=(TextView) findViewById(R.id.textView15);
        act[1]=(TextView) findViewById(R.id.textView16);
        act[2]=(TextView) findViewById(R.id.textView17);
        act[3]=(TextView) findViewById(R.id.textView18);
        act[4]=(TextView) findViewById(R.id.textView19);
        buy[0]=(TextView) findViewById(R.id.textView9);
        buy[1]=(TextView) findViewById(R.id.textView11);
        buy[2]=(TextView) findViewById(R.id.textView12);
        buy[3]=(TextView) findViewById(R.id.textView13);
        buy[4]=(TextView) findViewById(R.id.textView14);

        Intent intent =getIntent();
        act_page=intent.getIntExtra("act_p",0);
        buy_page=intent.getIntExtra("buy_p",0);
        db_getrecord.start();

    }
    public void act_before(View v){
        Intent intent=new Intent(this,B_record.class);
        intent.putExtra("act_p", act_page-5);
        startActivity(intent);
        finish();
    }
    public void act_next(View v){
        Intent intent=new Intent(this,B_record.class);
        intent.putExtra("act_p", act_page+5);
        startActivity(intent);
        finish();

    }
    public void buy_before(View v){
        Intent intent=new Intent(this,B_record.class);
        intent.putExtra("buy_p", buy_page-5);
        startActivity(intent);
        finish();
    }
    public void buy_next(View v){
        Intent intent=new Intent(this,B_record.class);
        intent.putExtra("buy_p", buy_page+5);
        startActivity(intent);
        finish();

    }


    final  Thread db_getrecord = new Thread(new Runnable() {
        @Override
        public void run() {

            // 3.連接JDBC
            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://140.135.113.188:5270/kmbmteam?serverTimezone=UTC", "kilin", "5270");

                if (conn != null) {

                    java.sql.Statement statement = conn.createStatement();

                    ResultSet rSet = statement.executeQuery("SELECT M_name,AR_money,AR_date FROM kmbmteam.member,kmbmteam.activity,kmbmteam.activity_record,kmbmteam.business where activity.A_business=activity_record.AR_activity and activity.A_business=business.B_id and activity_record.AR_member=member.M_id and A_business='" + B_id + "' limit " + act_page + ",5 ;");
                    Log.v("ha", "遠程連接成功!");
                    x = 0;
                    while (rSet.next()) {

                        final String gettext = rSet.getString("M_name") + "\t" + rSet.getString("AR_money") + "\t" + rSet.getString("AR_date");
                        act[x].post(new Runnable() {
                            @Override
                            public void run() {
                                act[x].setText(gettext);
                                Log.v("ha", x + "");
                                x++;
                            }
                        });


                    }
                    rSet = statement.executeQuery("SELECT M_name,P_name,SR_price,SR_date FROM kmbmteam.member,kmbmteam.sale_record,kmbmteam.product where SR_product=p_id and M_id=SR_member and p_business='" + B_id + "' limit " + buy_page + ",5;");
                    Log.v("ha", "遠程連接成功!");
                    x = 0;

                    while (rSet.next()) {

                        final String gettext = rSet.getString("M_name") + "\t" + rSet.getString("P_name") + "\t" + rSet.getString("SR_price") + "\t" + rSet.getString("SR_date");
                        buy[x].post(new Runnable() {
                            @Override
                            public void run() {
                                buy[x].setText(gettext);
                                Log.v("ha", x + "");
                                x++;
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

    public void goback(View v){
        Intent it = new Intent(this,MainActivity.class);
        startActivity(it);
        finish();
    }
}