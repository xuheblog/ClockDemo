package com.xuhe.clockdemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private int mWidth = 500;
    private int mHeight = 500;
    private Bitmap.Config mConfig = Bitmap.Config.ARGB_8888;
    private float mDegrees;
    private float mLength;
    private Calendar mCalendar;
    private int mHour;
    private int mMinutes;
    private int mSecond;
    private boolean mRunning;
    private String text = "上午";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image_view);
        imageView.setImageBitmap(drawClock());
    }
    //开启时钟
    public void doStartClock(View view){
        mRunning = true;
        new ClockTask().execute();
    }

    //关闭时钟
    public void doStopClock(View view){
        mRunning = false;
    }

    private class ClockTask extends AsyncTask<Objects,Objects,Objects>{

        @Override
        protected Objects doInBackground(Objects... params) {
            while (mRunning){
                publishProgress();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Objects... values) {
            imageView.setImageBitmap(drawClock());
        }
    }

    private Bitmap drawClock() {

        Bitmap bm = drawClockFace();
        Canvas canvas = new Canvas(bm);
        Paint mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#333333"));
        mPaint.setAntiAlias(true);//消除锯齿
        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR);
        mMinutes = mCalendar.get(Calendar.MINUTE);
        mSecond = mCalendar.get(Calendar.SECOND);

        //画时针
        mPaint.setStrokeWidth(10);
        mDegrees = mHour * 30 + mMinutes/2;
        mLength = (mHeight/2 + 5) * 0.70f;
        canvas.save();
        canvas.rotate(mDegrees,mWidth/2 + 5,mHeight/2 + 5);
        canvas.drawLine(mWidth/2 + 5,mHeight/2 + 5,mWidth/2 + 5,(mHeight/2 + 5) - mLength,mPaint);
        canvas.restore();

        //画分针
        mPaint.setStrokeWidth(6);
        mDegrees = mMinutes * 6 + mSecond/10;
        mLength = (mHeight/2 + 5) * 0.78f;
        canvas.save();
        canvas.rotate(mDegrees,mWidth/2 + 5,mHeight/2 + 5);
        canvas.drawLine(mWidth/2 + 5,mHeight/2 + 5,mWidth/2 + 5,(mHeight/2 + 5) - mLength,mPaint);
        canvas.restore();

        //画秒针
        mPaint.setStrokeWidth(3);
        mDegrees = mSecond * 6;
        mLength = (mHeight/2 + 5) * 0.82f;
        canvas.save();
        canvas.rotate(mDegrees,mWidth/2 + 5,mHeight/2 + 5);
        canvas.drawLine(mWidth/2 + 5,mHeight/2 + 5,mWidth/2 + 5,(mHeight/2 + 5) - mLength,mPaint);
        canvas.restore();

        //画文字;
        Paint textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#333333"));
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);
        if ((mHour = mCalendar.get(Calendar.HOUR_OF_DAY)) < 12){
            text = "上午";
        }else
        text = "下午";
        canvas.save();
        canvas.drawText(text,mWidth/2,mHeight/2 + 100,textPaint);
        canvas.restore();
        return bm;
    }

    private Bitmap drawClockFace() {

        Bitmap mBitmap = Bitmap.createBitmap(mWidth + 10,mHeight + 10,mConfig);
        Canvas canvas = new Canvas(mBitmap);
        Paint mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#333333"));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);

        //外部大圆
        canvas.drawCircle(mWidth/2 + 5,mHeight/2 + 5,mWidth/2,mPaint);
        //中心小圆
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth/2 + 5,mHeight/2 + 5,12,mPaint);
        //画上刻度
        for (int i = 0;i < 12;i++){
            if (i % 3 == 0){
                mPaint.setStrokeWidth(10);
                canvas.drawLine(mWidth/2 + 5,5,mWidth/2 + 5,25,mPaint);
            }else {
                mPaint.setStrokeWidth(4);
                canvas.drawLine(mWidth/2 + 5,5,mWidth/2 + 5,25,mPaint);
            }
            canvas.rotate(30,mWidth/2 + 5,mHeight/2 + 5);
        }

        return mBitmap;
    }

    @Override
    protected void onDestroy() {
        mRunning = false;
        super.onDestroy();
    }
}
