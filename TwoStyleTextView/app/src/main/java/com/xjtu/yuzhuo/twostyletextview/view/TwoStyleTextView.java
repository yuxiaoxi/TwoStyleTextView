package com.xjtu.yuzhuo.twostyletextview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.xjtu.yuzhuo.twostyletextview.R;

/**
 * Created by yuzhuo on 16/9/2.
 */
public class TwoStyleTextView extends View{

    private Context context;


    /**
     * 内容一行包行多少个字符
     */
    private int rowsCount;

    /**
     * 第一行在哪处开始字符换行
     */
    private int enterofIndex;

    /**
     * 单行的高度
     */
    private float textHeight;

    /**
     * 行数
     */
    private int rows;
    /**
     * title文本
     */
    private String mTitleText;
    /**
     * title文本的颜色
     */
    private int mTitleTextColor;
    /**
     * titel文本的大小
     */
    private int mTitleTextSize;

    /**
     * content文本
     */
    private String mContentText;
    /**
     * content文本的颜色
     */
    private int mContentTextColor;
    /**
     * content文本的大小
     */
    private int mContentTextSize;

    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mtitleBound,mcontentBound,testBound;
    private Paint mtitlePaint,mcontentPaint,testPaint;

    public TwoStyleTextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        this.context = context;
    }

    public TwoStyleTextView(Context context)
    {
        this(context, null);
        this.context = context;
    }

    /**
     * 获得我自定义的样式属性
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public TwoStyleTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TwoStyleTextView, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.TwoStyleTextView_titleText:
                    mTitleText = a.getString(attr);
                    break;
                case R.styleable.TwoStyleTextView_titleTextColor:
                    // 默认颜色设置为黑色
                    mTitleTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.TwoStyleTextView_titleTextSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.TwoStyleTextView_contentText:
                    mContentText = a.getString(attr);
                    break;
                case R.styleable.TwoStyleTextView_contentTextColor:
                    // 默认颜色设置为黑色
                    mContentTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.TwoStyleTextView_contentTextSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    mContentTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;

            }

        }
        a.recycle();

        /**
         * 获得绘制文本的宽和高
         */
        mtitlePaint = new Paint();
        mtitlePaint.setTextSize(mTitleTextSize);
        // mPaint.setColor(mTitleTextColor);
        mtitleBound = new Rect();
        mtitlePaint.getTextBounds(mTitleText, 0, mTitleText.length(), mtitleBound);

        mcontentPaint = new Paint();
        mcontentPaint.setTextSize(mContentTextSize);
        mcontentBound = new Rect();
        mcontentPaint.getTextBounds(mContentText, 0, mContentText.length(), mcontentBound);

        testPaint = new Paint();
        testPaint.setTextSize(mContentTextSize);
        testBound = new Rect();
        testPaint.getTextBounds("我",0,"我".length(),testBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height ;
        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
        } else
        {
            mtitlePaint.setTextSize(mTitleTextSize);
            mtitlePaint.getTextBounds(mTitleText, 0, mTitleText.length(), mtitleBound);
            mcontentPaint.setTextSize(mContentTextSize);
            mcontentPaint.getTextBounds(mContentText, 0, mContentText.length(), mcontentBound);
            float wordWidth = (float)testBound.width();
            float textWidth = mTitleText.length()*wordWidth+mContentText.length()*wordWidth;

            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            if(desired%widthSize==0){
                rows = desired/widthSize;
            }else{
                rows = desired/widthSize + 1;
            }
            if(rows>1){
                rowsCount = (int)((widthSize-getPaddingLeft()*2) / wordWidth);
                enterofIndex = (int)((widthSize - mtitleBound.width())/wordWidth)-1;
            }
            width = desired<=widthSize?desired:widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        } else
        {
            mtitlePaint.setTextSize(mTitleTextSize);
            mtitlePaint.getTextBounds(mTitleText, 0, mTitleText.length(), mtitleBound);
            mcontentPaint.setTextSize(mContentTextSize);
            mcontentPaint.getTextBounds(mContentText, 0, mContentText.length(), mcontentBound);
            textHeight = mtitleBound.height()>mcontentBound.height()?mtitleBound.height():mcontentBound.height();

            int desired = (int) (getPaddingTop() + textHeight*rows+ getPaddingTop()*(rows-1) + getPaddingBottom());
            height = desired<=heightSize?desired:heightSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mtitlePaint.setColor(Color.TRANSPARENT);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mtitlePaint);

        mtitlePaint.setColor(mTitleTextColor);
        mcontentPaint.setColor(mContentTextColor);
        canvas.drawText(mTitleText, getPaddingLeft(),textHeight+getPaddingTop(), mtitlePaint);
        canvas.drawText(mContentText.substring(0,enterofIndex), getPaddingLeft()*2+mtitleBound.width(),textHeight+getPaddingTop(), mcontentPaint);
        for (int i = 1;rows>1&&i<rows;i++){
            if(i == rows -1){//最后一行
                canvas.drawText(mContentText.substring(enterofIndex+(i-1)*rowsCount), getPaddingLeft(),(textHeight+getPaddingTop())*(i+1), mcontentPaint);

            }else{
                canvas.drawText(mContentText.substring(enterofIndex+(i-1)*rowsCount,enterofIndex+i*rowsCount), getPaddingLeft(),(textHeight+getPaddingTop())*(i+1), mcontentPaint);

            }
        }
    }
}
