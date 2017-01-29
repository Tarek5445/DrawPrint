package mcp.drawprint;

import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.*;

import java.util.Arrays;


public class FingerLine extends View {
    private final Paint mPaint;
    int[][] sX=new int[200][4];  //Each row save the stating and the ending points of one line
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


    public String tostring() {
        String myString = Arrays.deepToString(sX);
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
                sX[i][0]=(int)startX;
                sX[i][1]=(int)startY;
                sX[i][2]=(int)endX;
                sX[i][3]=(int)endY;
                i++;
                mCanvas.drawLine(startX, startY, endX, endY, mPaint);
                invalidate();


                break;

        }

        return true;
    }

}