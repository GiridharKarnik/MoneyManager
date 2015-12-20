package com.giridharkarnik.moneymanager.customViewClasses;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

import com.giridharkarnik.moneymanager.R;

public class NextButton extends Button {

    int aw,bw,cw,dw,ew,fw;
    int ah,bh,ch,dh,eh,fh;
    Paint paint;
    Context localContext;
    float w,h;

    public NextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        localContext = context;
        //paint object for drawing in onDraw
        //get the attributes specified in attrs.xml using the name we included
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NextButton, 0, 0);
        try {
            w = typedArray.getFloat(R.styleable.NextButton_buttonWidth,0);
            h = typedArray.getFloat(R.styleable.NextButton_buttonHeight,0);
            aw = (int)(0.3 * w);
            bw = (int) (0.45 * w);
            cw = (int) (0.7 * w);
            dw = (int) (0.45 * w);
            ew = (int) (0.3 * w);
            fw = (int) (0.55 * w);

            ah = (int) (0.1 *h);
            bh = (int) (0.1 *h);
            ch = (int) (0.5 *h);
            dh = (int) (0.9 *h);
            eh = (int) (0.9 *h);
            fh = (int) (0.5 *h);

            //assign points
          /*  a = new Point(30,10);
            b = new Point(45,10);
            c = new Point(70,50);
            d = new Point(45,90);
            e = new Point(30,90);
            f = new Point(55,50);*/
        }
        finally {
            typedArray.recycle();
        }
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int viewWidth = this.getMeasuredWidth();
        int viewHeight = this.getMeasuredHeight();
        Log.i("dev", "The size of the canvas's width is " + viewWidth);
        Log.i("dev", " The size of the canvas's height is " + viewHeight);

        canvas.drawColor(Color.argb(50, 255, 255, 255));
        paint.setStrokeWidth(1);

        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        paint.setAntiAlias(true);
        paint.setColor(Color.argb(255, 0, 0, 0));

           /*  a = new Point(30,10);
            b = new Point(45,10);
            c = new Point(70,50);
            d = new Point(45,90);
            e = new Point(30,90);
            f = new Point(55,50);*/

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(0.3f * viewWidth, 0.1f * viewHeight);
        path.lineTo(0.45f * viewWidth,  0.1f * viewHeight);
        path.lineTo(0.7f * viewWidth, 0.5f * viewHeight);
        path.lineTo(0.45f * viewWidth, 0.9f * viewHeight);
        path.lineTo(0.3f * viewWidth, 0.9f * viewHeight);
        path.lineTo(0.55f * viewWidth, 0.5f * viewHeight);
        path.lineTo(0.3f * viewWidth, 0.1f * viewHeight);
        path.close();

        canvas.drawPath(path, paint);
    }
}
