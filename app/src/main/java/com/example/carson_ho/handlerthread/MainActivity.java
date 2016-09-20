package com.example.carson_ho.handlerthread;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    HandlerThread mHandlerThread;
    Handler mainHandler,workHandler;
    TextView text;
    Button button1,button2,button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //显示文本
        text = (TextView) findViewById(R.id.text1);

        //创建与主线程关联的Handler
        mainHandler = new Handler();

        //创建后台线程
        initBackground();

        //点击Button1-通过SendMessage方法将0x121消息发送至MessageQueue
        //在工作线程中,当消息循环时取出0x121消息并在工作线程执行相关操作
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                workHandler.sendEmptyMessage(0x121);
            }
        });

        //点击Button2-通过SendMessage方法将0x122消息发送至MessageQueue
        //在工作线程中,当消息循环时取出0x122消息并在工作线程执行相关操作
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                workHandler.sendEmptyMessage(0x122);
            }
        });


        //点击Button3-退出消息循环
        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mHandlerThread.quit();
            }
        });

    }

    private void initBackground(){


        //通过实例化mHandlerThread从而创建新线程
        mHandlerThread = new HandlerThread("handlerThread");
        //启动新线程
        mHandlerThread.start();

        //创建与工作线程相关联的Handler,并与mHandlerThread所创建的Looper相关联
        //实现了Handler与工作线程相关联
        //下面HandlerMessage的方法体均会在mHandlerThread所创建的工作线程中执行
        workHandler = new Handler(mHandlerThread.getLooper()){
            @Override
            //消息处理的操作
            public void handleMessage(Message msg)
            {
                //设置了两种消息处理操作,通过msg来进行识别
                switch(msg.what){
                    //标识1:0x121
                    case 0x121:
                        try {
                            //延时操作
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //通过主线程Handler.post方法进行在主线程的UI更新操作
                        //可能有人看到new了一个Runnable就以为是又开了一个新线程
                        //事实上并没有开启任何新线程，只是使run()方法体的代码抛到与mHandler相关联的线程中执行，我们知道mainHandler是与主线程关联的，所以更新TextView组件依然发生在主线程
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run () {
                                text.setText("我爱学习");
                            }
                        });
                        break;

                    //标识2:0x122
                    case 0x122:
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run () {
                                text.setText("我不喜欢学习");
                            }
                        });
                        break;
                    default:
                        break;

                }
            }
        };


    }


    }




