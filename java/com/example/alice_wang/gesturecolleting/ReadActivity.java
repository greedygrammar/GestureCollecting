package com.example.alice_wang.gesturecolleting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.gesture.GestureOverlayView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by alice_wang on 16/10/28.
 */
public class ReadActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;//Acceleration
    private SensorManager sensorManager2;//Gyroscope

    public static TextView tv;//阅读文字显示区 show reading
    public static TextView position;//显示位置信息show finger position
    TextView username;//用户名
    TextView usercount;//用户输入次数
    ScrollView mScrollView;
    Button readfinish;//阅读完成键

    int count = 0;
    String reader = null;
    BufferedReader br = null;
    GestureDetector mGestureDetector ;

    //两个手指触控时分别采集的位置信息，N1为第一个手指first finger，N2为第二个手指second finger
    float tempN1SX = 0;
    float tempN1SY = 0;
    float tempN2SX = 0;
    float tempN2SY = 0;


    static String directory = Environment.getExternalStorageDirectory().toString();

    long oldtime = 0;
    long newtime = 0;
    boolean flag = true;

    int ondown_count = 0;
    int onfling_count = 0;
    int onlongpress_count = 0;
    int onshowpress_count = 0;
    int onscroll_count = 0;
    int onsingletapup_count = 0;
    int ondoubletapup_count = 0;
    int ondoubletapevent_count = 0;
    int onsingletapconfirm_count = 0;
    ArrayList ac = new ArrayList();// to formally store every gyroscope acceleration
    ArrayList gy = new ArrayList();// to formally store every gyroscope
    ArrayList accc = new ArrayList();// to store every value of acceleration
    ArrayList gyyy = new ArrayList();// to store every value of gyroscope
    String everyac;
    String maxac ;//to store the max acceleration
    String minac ;//to store the min acceleration
    String averac ;//to store the average acceleration
    String stdac ;//to store the standard acceleration
    String everygy;
    String maxgy ;//to store the max gyroscope
    String mingy ;//to store the min gyroscope
    String avergy ;//to store the average gyroscope
    String stdgy ;//to store the standard gyroscope
    ArrayList vx = new ArrayList();//to get velocity_x
    ArrayList vy = new ArrayList();//to get velocity_y
    String everyvx;
    String maxvx ;//to store the max velocity_x
    String minvx ;//to store the min velocity_x
    String avervx ;//to store the average velocity_x
    String stdvx ;//to store the standard velocity_x
    String everyvy;
    String maxvy ;//to store the max velocity_y
    String minvy ;//to store the min velocity_y
    String avervy ;//to store the average velocity_y
    String stdvy ;//to store the standard velocity_y
    ArrayList pres = new ArrayList();//to get the pressure
    ArrayList size = new ArrayList();//to get the size
    String everypres;
    String maxpres ;//to store the max pressure
    String minpres ;//to store the min pressure
    String averpres ;//to store the average pressure
    String stdpres ;//to store the standard pressure
    String everysize;
    String maxsize ;//to store the max size
    String minsize ;//to store the min size
    String aversize ;//to store the average size
    String stdsize ;//to store the standard size
    ArrayList x_start = new ArrayList();//to get the x_start
    ArrayList x_stop = new ArrayList();//to get the x_stop
    ArrayList y_start = new ArrayList();//to get the y_start
    ArrayList y_stop = new ArrayList();//to get the y_stop
    String everyxa;
    String maxxa ;//to store the max x_start
    String minxa ;//to store the min x_start
    String averxa ;//to store the average x_start
    String stdxa ;//to store the standard x_start
    String everyxo;
    String maxxo ;//to store the max x_stop
    String minxo ;//to store the min x_stop
    String averxo ;//to store the average x_stop
    String stdxo ;//to store the standard x_stop
    String everyya;
    String maxya ;//to store the max y_start
    String minya ;//to store the min y_start
    String averya ;//to store the average y_start
    String stdya ;//to store the standard y_start
    String everyyo;
    String maxyo ;//to store the max y_stop
    String minyo;//to store the min y_stop
    String averyo ;//to store the average y_stop
    String stdyo ;//to store the standard y_stop
    ArrayList dis_x = new ArrayList();//to get the dis_x
    ArrayList dis_y = new ArrayList();//to get the dis_y
    String everydx;
    String maxdx ;//to store the max dis_x
    String mindx ;//to store the min dis_x
    String averdx ;//to store the average dis_x
    String stddx ;//to store the standard dis_x
    String everydy;
    String maxdy ;//to store the max dis_y
    String mindy;//to store the min dis_y
    String averdy ;//to store the average dis_y
    String stddy ;//to store the standard dis_y

    SQLiteDatabase db;



    //to get gyroscope data in three axis
    public String[] getAcceleration(SensorEvent event){
        String[] acc = new String[2];
        double value = Math.sqrt((event.values[0])*(event.values[0]) +
                (event.values[1])*(event.values[1]) +
                (event.values[2])*(event.values[2]));
        acc[0] ="Acceleration X axis" + event.values[0] +
                "   Y axis" + event.values[1] +
                "   Z axis" + event.values[2] + "  value" +value;
        acc[1] = String.valueOf(value);

        return acc;
    }

    //to get gyroscope data in three axis
    public String[] getGyroscope(SensorEvent event){
        String[] gyr = new String[2];
        double value = Math.sqrt((event.values[0])*(event.values[0]) +
                (event.values[1])*(event.values[1]) +
                (event.values[2])*(event.values[2]));
               gyr[0] ="Gyroscope X axis" + event.values[0] +
                "   Y axis" + event.values[1] +
                "   Z axis" + event.values[2]+ "  value" +value;
               gyr[1] = String.valueOf(value);

        return gyr;
    }

    //record every sample
    public String arrayEvery(ArrayList sample){
        String everyone = "";
        for(int i=0; i<sample.size();i++){
            everyone = everyone + "Sample " +i +"  "+sample.get(i)+"    ";
        }
        return everyone;
    }

    //to get the max valve
    public double arrayMax(ArrayList sample){
        if(!sample.isEmpty()){
            double maxvalue =Double.valueOf((String) sample.get(0));
            for(int i=0; i<sample.size();i++){
                if(Double.valueOf((String) sample.get(i)) > maxvalue)
                    maxvalue = Double.valueOf((String) sample.get(i));
            }
            return maxvalue;}
        else {
            return 0;
        }
    }

    //to get the min valve
    public double arrayMin(ArrayList sample){
        if(!sample.isEmpty()){
            double minvalue = Double.valueOf((String) sample.get(0));
            for(int i=0; i<sample.size();i++){
                if(Double.valueOf((String) sample.get(i)) < minvalue)
                    minvalue = Double.valueOf((String) sample.get(i));
            }
            return minvalue;
        }
        else {
            return 0;
        }
    }

    //to get the average value
    public double averageValue(ArrayList sample){
        double sum=0;
        if(!sample.isEmpty()){
            for(int i=0; i<sample.size();i++){
                sum = sum + Double.valueOf((String) sample.get(i));
            }
            return sum/sample.size();
        }
        else {
            return 0;
        }
    }

    //to get the standard value
    public double stdValue(ArrayList sample){
        double sum=0;
        double aver;
        if(!sample.isEmpty()){
            for(int i=0; i<sample.size();i++){
                sum = sum + Double.valueOf((String) sample.get(i));
            }
            aver = sum/sample.size();
            sum = 0;
            for(int i=0; i<sample.size();i++){
                sum = sum + Math.pow((Double.valueOf((String) sample.get(i)) - aver), 2);
            }
            return sum/sample.size();
        }
        else {
            return 0;
        }
    }
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager2 = (SensorManager) getSystemService(SENSOR_SERVICE);

        tv = (TextView)findViewById(R.id.textv);
        mScrollView = (ScrollView) findViewById(R.id.SCROLLER_ID);
        readfinish  = (Button)findViewById(R.id.btnfinish);
        username  = (TextView)findViewById(R.id.username);
        usercount = (TextView)findViewById(R.id.usercount);
        position  = (TextView)findViewById(R.id.position);

        mGestureDetector = new GestureDetector(new MySimpleGesture());

        username.setText("username: " + MainActivity.DataName);
        usercount.setText("usercount: " + count);
        position.setMovementMethod(ScrollingMovementMethod.getInstance());


        File f = new File(directory,"/wyh/gesturecollect/quanshijie.txt");
        if(f.exists()){
            try {
                br = new BufferedReader(new FileReader(f));
                Log.i("wyhbefore",f.getPath());
                while ((reader = br.readLine())!=null){
                    tv.append(reader);
                    tv.append("\n");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            /*因为没有了去掉finally
            finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/

        }else{
            Log.i("wyh", ReadActivity.this.getFilesDir().toString());
            Log.i("wyh",f.getPath());
            new AlertDialog.Builder(ReadActivity.this).setTitle("错误")
                    .setMessage("文件找不到可能出现路径问题").setPositiveButton("确定", null)
                    .show();
        }

        tv.setFocusable(true);
        tv.setClickable(true);
        tv.setLongClickable(true);


        tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                newtime = System.currentTimeMillis();
                if ((newtime - oldtime) > 500) {
                    flag = true;
                    if (newtime - oldtime > 600000) {
                        Log.i("wyh","the first time");
                        flag = false;
                    }
                    oldtime = newtime;
                }
                if(flag)
                {

                    //deal with the data collected
                    everyac = arrayEvery(accc);
                    maxac = String.valueOf(arrayMax(accc));
                    minac = String.valueOf(arrayMin(accc));
                    averac = String.valueOf(averageValue(accc));
                    stdac = String.valueOf(stdValue(accc));
                    everygy = arrayEvery(gyyy);
                    maxgy = String.valueOf(arrayMax(gyyy));
                    mingy = String.valueOf(arrayMin(gyyy));
                    avergy = String.valueOf(averageValue(gyyy));
                    stdgy = String.valueOf(stdValue(gyyy));
                    everyvx = arrayEvery(vx);
                    maxvx = String.valueOf(arrayMax(vx));
                    minvx = String.valueOf(arrayMin(vx));
                    avervx = String.valueOf(averageValue(vx));
                    stdvx = String.valueOf(stdValue(vx));
                    everyvy = arrayEvery(vy);
                    maxvy = String.valueOf(arrayMax(vy));
                    minvy = String.valueOf(arrayMin(vy));
                    avervy = String.valueOf(averageValue(vy));
                    stdvy = String.valueOf(stdValue(vy));
                    everypres = arrayEvery(pres);
                    maxpres = String.valueOf(arrayMax(pres));
                    minpres = String.valueOf(arrayMin(pres));
                    averpres = String.valueOf(averageValue(pres));
                    stdpres = String.valueOf(stdValue(pres));
                    everysize = arrayEvery(size);
                    maxsize = String.valueOf(arrayMax(size));
                    minsize = String.valueOf(arrayMin(size));
                    aversize = String.valueOf(averageValue(size));
                    stdsize = String.valueOf(stdValue(size));
                    everyxa = arrayEvery(x_start);
                    maxxa = String.valueOf(arrayMax(x_start));
                    minxa = String.valueOf(arrayMin(x_start));
                    averxa = String.valueOf(averageValue(x_start));
                    stdxa = String.valueOf(stdValue(x_start));
                    everyxo = arrayEvery(x_stop);
                    maxxo = String.valueOf(arrayMax(x_stop));
                    minxo = String.valueOf(arrayMin(x_stop));
                    averxo = String.valueOf(averageValue(x_stop));
                    stdxo = String.valueOf(stdValue(x_stop));
                    everyya = arrayEvery(y_start);
                    maxya = String.valueOf(arrayMax(y_start));
                    minya = String.valueOf(arrayMin(y_start));
                    averya = String.valueOf(averageValue(y_start));
                    stdya = String.valueOf(stdValue(y_start));
                    everyyo = arrayEvery(y_stop);
                    maxyo = String.valueOf(arrayMax(y_stop));
                    minyo = String.valueOf(arrayMin(y_stop));
                    averyo = String.valueOf(averageValue(y_stop));
                    stdyo = String.valueOf(stdValue(y_stop));
                    everydx = arrayEvery(dis_x);
                    maxdx = String.valueOf(arrayMax(dis_x));
                    mindx = String.valueOf(arrayMin(dis_x));
                    averdx = String.valueOf(averageValue(dis_x));
                    stddx = String.valueOf(stdValue(dis_x));
                    everydy = arrayEvery(dis_y);
                    maxdy = String.valueOf(arrayMax(dis_y));
                    mindy = String.valueOf(arrayMin(dis_y));
                    averdy = String.valueOf(averageValue(dis_y));
                    stddy = String.valueOf(stdValue(dis_y));

                    //effient scroll
                    if(!maxdx.equals("0.0")) {
                        Log.i("wyh", arrayEvery(accc));
                        Log.i("wyh", "accmax" + maxac);
                        Log.i("wyh", "accmin" + minac);
                        Log.i("wyh", "accaver" + averac);
                        Log.i("wyh", "accstd" + stdac);
                        Log.i("wyh", "vxmax" + maxvx);
                        Log.i("wyh", "vxmin" + minvx);
                        Log.i("wyh", "vxaver" + avervx);
                        Log.i("wyh", "vxstd" + stdvx);
                        Log.i("wyh", "presmax" + maxpres);
                        Log.i("wyh", "presmin" + minpres);
                        Log.i("wyh", "presaver" + averpres);
                        Log.i("wyh", "presstd" + stdpres);
                        Log.i("wyh", "sizemax" + maxsize);
                        Log.i("wyh", "sizemin" + minsize);
                        Log.i("wyh", "sizeaver" + aversize);
                        Log.i("wyh", "sizestd" + stdsize);
                        Log.i("wyh", "xamax" + maxxa);
                        Log.i("wyh", "xamin" + minxa);
                        Log.i("wyh", "xaaver" + averxa);
                        Log.i("wyh", "xastd" + stdxa);
                        Log.i("wyh", "xomax" + maxxo);
                        Log.i("wyh", "xomin" + minxo);
                        Log.i("wyh", "xoaver" + averxo);
                        Log.i("wyh", "xostd" + stdxo);
                        Log.i("wyh", "yamax" + maxya);
                        Log.i("wyh", "yamin" + minya);
                        Log.i("wyh", "yaaver" + averya);
                        Log.i("wyh", "yastd" + stdya);
                        Log.i("wyh", "yomax" + maxyo);
                        Log.i("wyh", "yomin" + minyo);
                        Log.i("wyh", "yoaver" + averyo);
                        Log.i("wyh", "yostd" + stdyo);
                        Log.i("wyh", "dxmax" + maxdx);
                        Log.i("wyh", "dxmin" + mindx);
                        Log.i("wyh", "dxaver" + averdx);
                        Log.i("wyh", "dxstd" + stddx);
                        Log.i("wyh", "dymax" + maxdy);
                        Log.i("wyh", "dymin" + mindy);
                        Log.i("wyh", "dyaver" + averdy);
                        Log.i("wyh", "dystd" + stddy);
                        count++;
                        usercount.setText("usercount: " + count);

                        db = openOrCreateDatabase("readgc.db", Context.MODE_PRIVATE, null);
                        db.execSQL("CREATE TABLE IF NOT EXISTS person (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR," +
                                "password VARCHAR, sex VARCHAR, age VARCHAR, count VARCHAR, ondown_count VARCHAR, onfling_count VARCHAR,  onlongpress_count VARCHAR," +
                                "                         onshowpress_count VARCHAR, onscroll_count VARCHAR,  onsingletapup_count VARCHAR,  ondoubletapup_count VARCHAR," +
                                "                         ondoubletapevent_count VARCHAR,  onsingletapconfirm_count VARCHAR,everyac TEXT, maxac TEXT, minac TEXT," +
                                "                                 averac TEXT ,  stdac TEXT ,  everygy TEXT,  maxgy TEXT ,  mingy TEXT ,  avergy TEXT ," +
                                "                                 stdgy TEXT ,  everyvx TEXT,  maxvx TEXT ,  minvx TEXT ,  avervx TEXT ,  stdvx TEXT ," +
                                "                                 everyvy TEXT,  maxvy TEXT ,  minvy TEXT ,  avervy TEXT , stdvy TEXT , everypres TEXT," +
                                "                                 maxpres TEXT ,  minpres TEXT ,  averpres TEXT ,  stdpres TEXT ,  everysize TEXT,  maxsize TEXT ," +
                                "                                 minsize TEXT ,  aversize TEXT ,  stdsize TEXT ,  everyxa TEXT,  maxxa TEXT ,  minxa TEXT ," +
                                "                                 averxa TEXT ,  stdxa TEXT ,  everyxo TEXT,  maxxo TEXT ,  minxo TEXT ,  averxo TEXT ," +
                                "                                 stdxo TEXT ,  everyya TEXT,  maxya TEXT ,  minya TEXT ,  averya TEXT ,  stdya TEXT ," +
                                "                                 everyyo TEXT,  maxyo TEXT ,  minyo TEXT,  averyo TEXT ,  stdyo TEXT ,  everydx TEXT," +
                                "                                 maxdx TEXT ,  mindx TEXT ,  averdx TEXT ,  stddx TEXT ,  everydy TEXT,  maxdy TEXT ," +
                                "                                 mindy TEXT ,  averdy TEXT ,  stddy TEXT)");
                        //Data person = new Data(MainActivity.DataName, MainActivity.DataPsw);
                        /* person.putInData(MainActivity.sex, MainActivity.age, count, ondown_count, onfling_count,  onlongpress_count,
                         onshowpress_count, onscroll_count,  onsingletapup_count,  ondoubletapup_count,
                         ondoubletapevent_count,  onsingletapconfirm_count,  everyac, maxac, minac,
                                 averac ,  stdac ,  everygy,  maxgy ,  mingy ,  avergy ,
                                 stdgy ,  everyvx,  maxvx ,  minvx ,  avervx ,  stdvx ,
                                 everyvy,  maxvy ,  minvy ,  avervy , stdvy , everypres,
                                 maxpres ,  minpres ,  averpres ,  stdpres ,  everysize,  maxsize ,
                                 minsize ,  aversize ,  stdsize ,  everyxa,  maxxa ,  minxa ,
                                 averxa ,  stdxa ,  everyxo,  maxxo ,  minxo ,  averxo ,
                                 stdxo ,  everyya,  maxya ,  minya ,  averya ,  stdya ,
                                 everyyo,  maxyo ,  minyo,  averyo ,  stdyo ,  everydx,
                                 maxdx ,  mindx ,  averdx ,  stddx ,  everydy,  maxdy ,
                                 mindy ,  averdy ,  stddy );

                    }*/
                        ContentValues cv = new ContentValues();
                        cv.put("name", MainActivity.DataName);
                        cv.put("password", MainActivity.DataPsw);
                        cv.put("sex", MainActivity.sex);
                        cv.put("age", MainActivity.age);
                        cv.put("count", count);
                        cv.put("ondown_count", ondown_count);
                        cv.put("onfling_count", onfling_count);
                        cv.put("onlongpress_count", onlongpress_count);
                        cv.put("onshowpress_count", onshowpress_count);
                        cv.put("onscroll_count", onscroll_count);
                        cv.put("onsingletapup_count", onsingletapup_count);
                        cv.put("ondoubletapup_count", ondoubletapup_count);
                        cv.put("ondoubletapevent_count", ondoubletapevent_count);
                        cv.put("onsingletapconfirm_count", onsingletapconfirm_count);
                        cv.put("everyac", everyac);
                        cv.put("maxac", maxac);
                        cv.put("minac", minac);
                        cv.put("averac", averac);
                        cv.put("stdac", stdac);
                        cv.put("everygy", everygy);
                        cv.put("maxgy", maxgy);
                        cv.put("mingy", mingy);
                        cv.put("avergy", avergy);
                        cv.put("stdgy", stdgy);
                        cv.put("everyvx", everyvx);
                        cv.put("maxvx", maxvx);
                        cv.put("minvx", minvx);
                        cv.put("avervx", avervx);
                        cv.put("stdvx", stdvx);
                        cv.put("everyvy", everyvy);
                        cv.put("maxvy", maxvy);
                        cv.put("minvy", minvy);
                        cv.put("avervy", avervy);
                        cv.put("stdvy", stdvy);
                        cv.put("everypres", everypres);
                        cv.put("maxpres", maxpres);
                        cv.put("minpres", minpres);
                        cv.put("averpres", averpres);
                        cv.put("stdpres", stdpres);
                        cv.put("everysize", everysize);
                        cv.put("maxsize", maxsize);
                        cv.put("minsize", minsize);
                        cv.put("aversize", aversize);
                        cv.put("stdsize", stdsize);
                        cv.put("everyxa", everyxa);
                        cv.put("maxxa", maxxa);
                        cv.put("minxa", minxa);
                        cv.put("averxa", averxa);
                        cv.put("stdxa", stdxa);
                        cv.put("everyxo", everyxo);
                        cv.put("maxxo", maxxo);
                        cv.put("minxo", minxo);
                        cv.put("averxo", averxo);
                        cv.put("stdxo", stdxo);
                        cv.put("everyya", everyya);
                        cv.put("maxya", maxya);
                        cv.put("minya", minya);
                        cv.put("averya", averya);
                        cv.put("stdya", stdya);
                        cv.put("everyyo", everyyo);
                        cv.put("maxyo", maxyo);
                        cv.put("minyo", minyo);
                        cv.put("averyo", averyo);
                        cv.put("stdyo", stdyo);
                        cv.put("everydx", everydx);
                        cv.put("maxdx", maxdx);
                        cv.put("mindx", mindx);
                        cv.put("averdx", averdx);
                        cv.put("stddx", stddx);
                        cv.put("everydy", everydy);
                        cv.put("maxdy", maxdy);
                        cv.put("mindy", mindy);
                        cv.put("averdy", averdy);
                        cv.put("stddy", stddy);

                        db.insert("person", null, cv);
                    }
                        // clear the space for new data to enter
                        position.setText("");
                        ondown_count = 0;
                        onfling_count = 0;
                        onlongpress_count = 0;
                        onshowpress_count = 0;
                        onscroll_count = 0;
                        onsingletapup_count = 0;
                        ondoubletapup_count = 0;
                        ondoubletapevent_count = 0;
                        onsingletapconfirm_count = 0;
                        ac.clear();
                        accc.clear();
                        gy.clear();
                        gyyy.clear();
                        vx.clear();
                        vy.clear();
                        pres.clear();
                        size.clear();
                        x_start.clear();
                        x_stop.clear();
                        y_start.clear();
                        y_stop.clear();
                        dis_x.clear();
                        dis_y.clear();
                        flag = false;

                }
                pres.add(String.valueOf(event.getPressure()));
                size.add(String.valueOf(event.getSize()));

                if (event.getPointerCount() < 2) {
                    mGestureDetector.onTouchEvent(event);
                } else {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {

                        case MotionEvent.ACTION_DOWN:
                            tempN1SX = event.getX();
                            tempN1SY = event.getY();
                            position.setText("N1S" + " (" + tempN1SX + " , " + tempN1SY + ")");
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            tempN2SX = event.getX();
                            tempN2SY = event.getY();
                            position.append("N2S" + " (" + tempN2SX + " , " + tempN2SY + ")");
                            break;
                        case MotionEvent.ACTION_MOVE:
                            position.append("TM" + " (" + event.getX(0) + " , " + event.getY(0) + ")"
                                    + " (" + event.getX(1) + " , " + event.getY(1) + ")");
                    }
                }


                return true;
            }

        });
        readfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position.setText(" " );
                Intent intent = new Intent();
                intent.setClass(ReadActivity.this, FinDifCoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        scrollDown();

    }

    public void scrollDown()
    {
        mScrollView.post(new Runnable() {
            public void run() {
                //mScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    // SimpleOnGestureListener implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener
    private class MySimpleGesture extends GestureDetector.SimpleOnGestureListener {
        // 双击的第二下Touch down时触发 doubletap
        public boolean onDoubleTap(MotionEvent event) {
            Log.i("MyGesture", "onDoubleTap");
            ondoubletapup_count++;
            if(event.getPointerCount()>1){
            position.append("双击"+" ("+event.getX(0)+" , "+event.getY(0)+")"+"  ("+event.getX(1)+" , "+event.getY(1)+")");
            }else{
                position.append("双击"+" ("+event.getX()+" , "+event.getY()+")");
            }
            return super.onDoubleTap(event);
        }

        // 双击的第二下Touch down和up都会触发，可用e.getAction()区分
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.i("MyGesture", "onDoubleTapEvent");
            ondoubletapevent_count++;
            position.append("doubletapevent " + " (" + e.getX() + " , " + e.getY() + ")");
            return super.onDoubleTapEvent(e);
        }

        // Touch down时触发
        public boolean onDown(MotionEvent event) {
            Log.i("MyGesture", "onDown");
            ondown_count++;
            position.append("down" + " (" + event.getX() + " , " + event.getY() + ")");
            return super.onDown(event);
        }

        // Touch了滑动一点距离后，up时触发
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i("MyGesture", "onFling");
            onfling_count++;
            position.append("抛丢位置" + " (" + e1.getX() + " , " + e1.getY() + ")" + " (" + e2.getX() + " , " + e2.getY() + ")"
                    + "velocityX " +velocityX+ " velocityY "+velocityY);
            vx.add(String.valueOf(velocityX));
            vy.add(String.valueOf(velocityY));
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        // Touch了不移动一直Touch down时触发，特别难触发，可以不用写
        public void onLongPress(MotionEvent e) {
            Log.i("MyGesture", "onLongPress");
            onlongpress_count++;
            position.append("longpress "+ " (" + e.getX() + " , " + e.getY() + ")");
            super.onLongPress(e);
        }

        // Touch了滑动时触发；很好触发
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i("MyGesture", "onScroll");
            onscroll_count++;
                position.append("scroll" + " (" + e1.getX() + " , " + e1.getY() + ")" + " (" + e2.getX() + " , " + e2.getY() + ")"
                        + "distancex " + distanceX+ " distancey "+distanceY);
            long timetemp1 = e1.getDownTime();
            long timetemp2 = e2.getEventTime();
            //Log.i("wyh","timetemp1"+timetemp1);
            //Log.i("wyh","timetemp2"+timetemp2);
            x_start.add(String.valueOf(e1.getX()));
            y_start.add(String.valueOf(e1.getY()));
            x_stop.add(String.valueOf(e2.getX()));
            y_stop.add(String.valueOf(e2.getY()));
            dis_x.add(String.valueOf(distanceX));
            dis_y.add(String.valueOf(distanceY));
            vx.add(String.valueOf(distanceX / (timetemp2 - timetemp1)));
            vy.add(String.valueOf(distanceY / (timetemp2 - timetemp1)));
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        /*
         * Touch了还没有滑动时触发
         * (1)onDown只要Touch Down一定立刻触发
         * (2)Touch Down后过一会没有滑动先触发onShowPress再触发onLongPress
         * So: Touch Down后一直不滑动，onDown -> onShowPress -> onLongPress这个顺序触发。
         */
        public void onShowPress(MotionEvent e) {
            Log.i("MyGesture", "onShowPress");
            onshowpress_count++;
            position.append("showpress " + " (" + e.getX() + " , " + e.getY() + ")");
            super.onShowPress(e);
        }

        /*
         * 两个函数都是在Touch Down后又没有滑动(onScroll)，又没有长按(onLongPress)，然后Touch Up时触发
         * 点击一下非常快的(不滑动)Touch Up: onDown->onSingleTapUp->onSingleTapConfirmed
         * 点击一下稍微慢点的(不滑动)Touch Up: onDown->onShowPress->onSingleTapUp->onSingleTapConfirmed
         */
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("MyGesture", "onSingleTapConfirmed");
            onsingletapconfirm_count++;
            position.append("singletapconfirm " + " (" + e.getX() + " , " + e.getY() + ")");
            return super.onSingleTapConfirmed(e);
        }
        public boolean onSingleTapUp(MotionEvent e) {
            Log.i("MyGesture", "onSingleTapUp");
            onsingletapup_count++;
            position.append("singletapup "+ " (" + e.getX() + " , " + e.getY() + ")");
            return super.onSingleTapUp(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager2.registerListener(this,
                sensorManager2.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        sensorManager2.unregisterListener(this);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:

                        ac.add(getAcceleration(event)[0]);
                        accc.add(getGyroscope(event)[1]);

                break;
            case Sensor.TYPE_GYROSCOPE:
                        gy.add(getGyroscope(event)[0]);
                        gyyy.add(getGyroscope(event)[1]);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                flag = false;
                break;
            default:
                break;
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

