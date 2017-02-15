package io.github.abcghy.countdown;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.utils.ConstUtils;
import com.blankj.utilcode.utils.ConvertUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 2017/2/15.
 */

public class CountDownView extends View {

    private Context mContext;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.CountDownView);

//        defalutSize = a.getDimensionPixelSize(R.styleable.MyView_default_size, 100);
        mTextSize = a.getDimensionPixelSize(R.styleable.CountDownView_textSize, dp2px(12));

        a.recycle();

        initPaint();
    }

    private Paint mPaint;

    private int mTextSize;

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(mTextSize);
        mPaint.setStrokeWidth(dp2px(2));
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void start() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
//                        Log.d("test", "onCompleted");

                    }

                    @Override
                    public void onError(Throwable e) {
//                        Log.d("test", "onError");
                    }

                    @Override
                    public void onNext(Long aLong) {
//                        Log.d("test", "onNext");
                        calculateTargetTime();
                        invalidate();
                    }
                });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);

//        mPaint.setColor(Color.BLACK);
//        canvas.drawLine(dp2px(-100), 0, dp2px(100), 0, mPaint);

        drawRect(canvas, -31);
        drawRect(canvas, 0);
        drawRect(canvas, 31);

        mPaint.setColor(Color.WHITE);

        String strHour = hour < 10 ? "0" + hour : hour + "";
        canvas.drawText(strHour, dp2px(-31), dp2px(4), mPaint);

        String strMinute = min < 10 ? "0" + min : min + "";
        canvas.drawText(strMinute, 0, dp2px(4), mPaint);
//        drawCenter(canvas, mPaint, strMinute);

        String strSec = sec < 10 ? "0" + sec : sec + "";
        canvas.drawText(strSec, dp2px(31), dp2px(4), mPaint);

        mPaint.setColor(Color.BLACK);
        canvas.drawText(":", dp2px(-16), dp2px(4), mPaint);
        canvas.drawText(":", dp2px(16), dp2px(4), mPaint);
    }

//    Rect r = new Rect();
//
//    private void drawCenter(Canvas canvas, Paint paint, String text) {
//        canvas.getClipBounds(r);
//        int cHeight = r.height();
//        int cWidth = r.width();
//        paint.setTextAlign(Paint.Align.LEFT);
//        paint.getTextBounds(text, 0, text.length(), r);
//        float x = cWidth / 2f - r.width() / 2f - r.left;
//        float y = cHeight / 2f + r.height() / 2f - r.bottom;
//        canvas.drawText(text, x, y, paint);
//    }

    private void drawRect(Canvas canvas, float x) {
        mPaint.setColor(Color.BLACK);
        RectF rect = new RectF(dp2px(x - 10), dp2px(-6.5f), dp2px(x + 10), dp2px(6.5f));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(rect, dp2px(4), dp2px(4), mPaint);
        } else {
            canvas.drawRect(rect, mPaint);
        }
    }

    private void calculateTargetTime() {
        if (mTargetTime != 0) {
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            long countDownTime = mTargetTime - System.currentTimeMillis();
            if (countDownTime >= 0) {
                hour = ConvertUtils.millis2TimeSpan(countDownTime, ConstUtils.TimeUnit.HOUR);
                min  = ConvertUtils.millis2TimeSpan(countDownTime, ConstUtils.TimeUnit.MIN) % 60;
                sec  = ConvertUtils.millis2TimeSpan(countDownTime, ConstUtils.TimeUnit.SEC) % 60;
            }
        }
    }

    private long hour;
    private long min;
    private long sec;

    private long mTargetTime;

    public void setCountDownTime(long targetTime) {
        this.mTargetTime = targetTime;
        calculateTargetTime();
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public int dp2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
