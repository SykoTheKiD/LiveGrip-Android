package com.jaysyko.wrestlechat.customViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

/**
 * @author Jay Syko on 2016-07-26.
 */

public class TriangleLayoutView extends FrameLayout {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Xfermode pdMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private Path path = new Path();

    public TriangleLayoutView(Context context) {
        super(context);
    }

    public TriangleLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TriangleLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TriangleLayoutView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);

        paint.setXfermode(pdMode);
        path.reset();
        path.moveTo(getWidth(), 0);
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getHeight(), getWidth() - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
        path.lineTo(getWidth(), 0);
        path.close();
        canvas.drawPath(path, paint);

        canvas.restoreToCount(saveCount);
        paint.setXfermode(null);
    }
}
