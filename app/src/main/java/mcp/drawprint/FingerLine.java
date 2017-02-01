package mcp.drawprint;

import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.*;

import java.util.ArrayList;
import java.util.Arrays;


public class FingerLine extends View {
    private final Paint mPaint;
    ArrayList<int[]> sX = new ArrayList<>();
    String myString;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    public Canvas mCanvas;
    Bitmap mBitmap;
    int i=0,j=0;

    public FingerLine(Context context) {
        this(context, null);

    }

    public FingerLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.STROKE);
        mPaint.setColor(Color.BLACK);

    }

    public void eraseAll() {
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public String tostring() {
        //String myString = Arrays.deepToString(sX);
        return myString;
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0 , 0, mPaint);
        canvas.drawLine(startX, startY, endX, endY, mPaint);

    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //create canvas of certain device size.
        super.onSizeChanged(w, h, oldw, oldh);

        //create Bitmap of certain w,h
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //apply bitmap to graphic to start drawing.
        mCanvas = new Canvas(mBitmap);
    }
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = event.getX();
                endY = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                endX = event.getX();
                endY = event.getY();
                int[] temp = new int[4];
                temp[0]=(int)startX;
                temp[1]=(int)startY;
                temp[2]=(int)endX;
                temp[3]=(int)endY;
                sX.add(temp);
                mCanvas.drawLine(startX, startY, endX, endY, mPaint);
               // mCanvas.drawRect(startX, startY, endX, endY, mPaint);
                invalidate();


                break;

        }

        return true;
    }

}