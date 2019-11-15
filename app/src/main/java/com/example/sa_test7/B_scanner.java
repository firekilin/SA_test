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
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
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

public class B_scanner extends AppCompatActivity {
    SurfaceView surfaceView;
    TextView textView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    String memberid;
    int can =1;
    String B_id;
    String B_money;
    Integer money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_scanner);
        SharedPreferences settings = getSharedPreferences("user", 0);
       B_id= settings.getString("B_id","尚未註冊，請先註冊");
       B_money=settings.getString("B_money","0");


        surfaceView=(SurfaceView)findViewById(R.id.surfaceView);
        textView=(TextView)findViewById(R.id.textView);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource=new CameraSource.Builder(this,barcodeDetector)
                .setRequestedPreviewSize(5000,5000).build();
        cameraFocus(cameraSource, Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
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

                                if (can == 1) {
                                    if(money!=null) {
                                    try {
                                        textView.setText(qrCodes.valueAt(0).displayValue);
                                        String[] getlist = (qrCodes.valueAt(0).displayValue.split(";"));
                                        memberid = getlist[1];


                                        can = 0;

                                        if (getlist[0].equals("SAtest")) {
                                            if ((Integer.valueOf(B_money) - money) > 0) {
                                                new AlertDialog.Builder(B_scanner.this)
                                                        .setTitle("注意")//設定視窗標題
                                                        .setMessage("掃描成功將自動跳轉")//設定顯示的文字
                                                        .show();//呈現對話視窗

                                                givegift();
                                            } else {
                                                new AlertDialog.Builder(B_scanner.this)
                                                        .setTitle("注意")//設定視窗標題
                                                        .setMessage("沒錢回家去吧")//設定顯示的文字
                                                        .setPositiveButton("關閉視窗", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent it = new Intent(B_scanner.this, MainActivity.class);
                                                                startActivity(it);
                                                                finish();
                                                            }
                                                        })//設定結束的子視窗
                                                        .show();//呈現對話視窗
                                            }


                                        } else {
                                            can=0;
                                            new AlertDialog.Builder(B_scanner.this)
                                                    .setTitle("注意")//設定視窗標題
                                                    .setMessage("失敗，請掃描正確QRCODE")//設定顯示的文字
                                                    .setPositiveButton("關閉視窗", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            can = 1;
                                                        }
                                                    })//設定結束的子視窗
                                                    .show();//呈現對話視窗
                                        }
                                    } catch (Exception e) {
                                        can = 0;
                                        new AlertDialog.Builder(B_scanner.this)
                                                .setTitle("注意")//設定視窗標題
                                                .setMessage("失敗，請掃描正確QRCODE")//設定顯示的文字
                                                .setPositiveButton("關閉視窗", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        can = 1;
                                                    }
                                                })//設定結束的子視窗
                                                .show();//呈現對話視窗

                                    }

                                    } else{
                                        can=0;
                                        new AlertDialog.Builder(B_scanner.this)
                                                .setTitle("注意")//設定視窗標題
                                                .setMessage("失敗，請輸入交易金額")//設定顯示的文字
                                                .setPositiveButton("關閉視窗", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        can = 1;
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
    public void outputing(View v){
        EditText getingmoney=(EditText)findViewById(R.id.editText2);
        money=Integer.valueOf(getingmoney.getText().toString());

    }
    public void goback(View v){
        Intent it = new Intent(B_scanner.this,MainActivity.class);
        startActivity(it);
        finish();
    }
    public  void focusg(View v){
        cameraFocus(cameraSource,Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
    }
    public void givegift(){
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
                String url = "jdbc:mysql://140.135.113.188:5270/kmbmteam?serverTimezone=UTC";
                // 3.連接JDBC
                try {
                    Connection conn;
                    conn = DriverManager.getConnection(url, "kilin", "5270");
                    Log.v("ha", "遠程連接成功!");
                    if (conn != null) {


                        java.sql.Statement statement = conn.createStatement();


                            String sql = "INSERT INTO `kmbmteam`.`activity_record` (`AR_activity`, `AR_member`,`AR_money`) VALUES ('"+B_id+"', '"+memberid+"','"+money+"');";
                            statement.execute(sql);
                            sql=" UPDATE `kmbmteam`.`member` SET `M_money` = `M_money`+"+money+" WHERE (`M_id` = '"+memberid+"');";
                            statement.execute(sql);
                            sql=" UPDATE `kmbmteam`.`business` SET `B_money` = `B_money`-"+money+" WHERE (`B_id` = '"+B_id+"');";
                            statement.execute(sql);
                            Intent it = new Intent(B_scanner.this, MainActivity.class);
                            startActivity(it);
                            finish();
                            conn.close();
                            return;

                    }
                } catch (SQLException e) {
                    Log.e("ha", "遠程連接失敗!" + e);

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
    public static boolean cameraFocus( CameraSource cameraSource, @B_scanner.FocusMode String focusMode) {
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
