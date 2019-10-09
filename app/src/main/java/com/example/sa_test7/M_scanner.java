package com.example.sa_test7;

import androidx.annotation.StringDef;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class M_scanner extends AppCompatActivity {

    SurfaceView surfaceView;
    TextView textView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    int productid=1;
    int memberid=1;
    int money=1;
    int can=1;
    String M_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_scanner);
        SharedPreferences settings = getSharedPreferences("user", 0);
        M_id= settings.getString("M_id","尚未註冊，請先註冊");
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
        }

        surfaceView=(SurfaceView)findViewById(R.id.surfaceView);
        textView=(TextView)findViewById(R.id.textView);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource=new CameraSource.Builder(this,barcodeDetector)
                .setRequestedPreviewSize(5000,5000).build();
        cameraFocus(cameraSource,Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED)
                    return;
                try{
                    cameraSource.start(holder);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>(){
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes=detections.getDetectedItems();
                if(qrCodes.size()!=0){
                    textView.post(new Runnable() {
                        @Override
                        public void run() {

                            if (can==1){
                                try{
                                    textView.setText(qrCodes.valueAt(0).displayValue);
                                    String[] tokens = qrCodes.valueAt(0).displayValue.split(";");
                                    productid=Integer.valueOf(tokens[0]);
                                    memberid=Integer.valueOf(M_id);
                                    money=Integer.valueOf(tokens[1]);
                                    SharedPreferences settings = getSharedPreferences("user", 0);
                                    String M_money= settings.getString("M_money","0");
                                    can=0;
                                    if (  (Integer.valueOf(M_money)-money)>0) {
                                        new AlertDialog.Builder(M_scanner.this)
                                                .setTitle("注意")//設定視窗標題
                                                .setMessage("掃描成功將自動跳轉")//設定顯示的文字
                                                .show();//呈現對話視窗

                                        buysometing();
                                    }else{
                                        new AlertDialog.Builder(M_scanner.this)
                                                .setTitle("注意")//設定視窗標題
                                                .setMessage("沒錢回家去吧")//設定顯示的文字
                                                .setPositiveButton("關閉視窗",new DialogInterface.OnClickListener(){
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent it = new Intent(M_scanner.this,MainActivity.class);
                                                        startActivity(it);
                                                        finish();
                                                    }
                                                })//設定結束的子視窗
                                                .show();//呈現對話視窗
                                    }
                                }catch (Exception e){
                                    can=0;
                                    new AlertDialog.Builder(M_scanner.this)
                                            .setTitle("注意")//設定視窗標題
                                            .setMessage("失敗，請掃描正確QRCODE")//設定顯示的文字
                                            .setPositiveButton("關閉視窗",new DialogInterface.OnClickListener(){
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    can=1;
                                                }
                                            })//設定結束的子視窗
                                            .show();//呈現對話視窗

                                }


                            }


                        }
                    });
                }
            }


        });
    }
    public void goback(View v){
        Intent it = new Intent(M_scanner.this,MainActivity.class);
        startActivity(it);
        finish();
    }
    public  void focusg(View v){
        cameraFocus(cameraSource,Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
    }

    public void buysometing(){
        final Thread kilin = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Log.v("ha", "加載JDBC驅動成功");
                } catch (ClassNotFoundException e) {
                    Log.e("ha", "加載JDBC驅動失敗");

                    return;
                }
                String url = "jdbc:mysql://db4free.net:3306/kmbmteam?serverTimezone=UTC";
                // 3.連接JDBC
                try {
                    Connection conn;
                    conn = DriverManager.getConnection(url, "mkbmyo", "13145270");
                    Log.v("ha", "遠程連接成功!");
                    if (conn != null) {


                         java.sql.Statement statement = conn.createStatement();
                        ResultSet  rSet = statement.executeQuery("SELECT M_money FROM kmbmteam.member where M_id='"+M_id+"'");
                        rSet.next();
                        if(  (Integer.valueOf(rSet.getString("M_money"))-money)>0) {
                            String sql = "INSERT INTO `kmbmteam`.`sale_record` (`SR_product`, `SR_member`, `SR_price`) VALUES ('" + productid + "', '" + memberid + "', '" + money + "');";
                            statement.execute(sql);
                            sql = "UPDATE `kmbmteam`.`member` SET `M_money` = `M_money`-" + money + " WHERE (`M_id` = '" + M_id + "');";
                            statement.execute(sql);
                            ResultSet rSet2 = statement.executeQuery("SELECT * FROM kmbmteam.product where P_id="+productid+";");
                            rSet2.next();

                            sql = "UPDATE `kmbmteam`.`business` SET `B_money` = `B_money`+" + money + " WHERE (`B_id` = '" + rSet2.getString("P_business") + "');";
                            statement.execute(sql);
                            SharedPreferences settings = getSharedPreferences("user", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("M_money", String.valueOf(Integer.valueOf(settings.getString("M_money", "0")) - money));
                            editor.commit();
                            Intent it = new Intent(M_scanner.this, MainActivity.class);
                            startActivity(it);
                            finish();
                            conn.close();
                            return;
                        }else
                        {
                            return;
                        }
                    }
                } catch (SQLException e) {
                    Log.e("ha", "遠程連接失敗!" + e);
                    TextView etContent = (TextView) findViewById(R.id.textView);
                    etContent.setText("失敗"+e);
                }


            }
        });
        kilin.start();
    }

    @StringDef({
            Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE,
            Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO,
            Camera.Parameters.FOCUS_MODE_AUTO,
            Camera.Parameters.FOCUS_MODE_EDOF,
            Camera.Parameters.FOCUS_MODE_FIXED,
            Camera.Parameters.FOCUS_MODE_INFINITY,
            Camera.Parameters.FOCUS_MODE_MACRO
    })
    @Retention(RetentionPolicy.SOURCE)
    private @interface FocusMode {}
    public static boolean cameraFocus( CameraSource cameraSource, @FocusMode  String focusMode) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        Camera.Parameters params = camera.getParameters();

                        if (!params.getSupportedFocusModes().contains(focusMode)) {
                            return false;
                        }

                        params.setFocusMode(focusMode);
                        camera.setParameters(params);
                        return true;
                    }

                    return false;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                break;
            }
        }

        return false;
    }
}
