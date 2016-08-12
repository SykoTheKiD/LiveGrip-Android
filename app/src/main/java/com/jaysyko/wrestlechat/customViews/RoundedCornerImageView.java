package com.jaysyko.wrestlechat.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author Jay Syko on 2016-08-11.
 */
public class RoundedCornerImageView extends ImageView {

    private static final PorterDuffXfermode PORTER_DUFF_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private static final float CORNER_RADIUS = 50.0f;
    private RectF rectF = new RectF();

    public RoundedCornerImageView(Context context) {
        super(context);
    }

    public RoundedCornerImageView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        Drawable myDrawable = getDrawable();

        if (myDrawable != null && myDrawable instanceof BitmapDrawable) {
            Paint paint = ((BitmapDrawable) myDrawable).getPaint();
            final int color = 0xff000000;
            Rect bitmapBounds = myDrawable.getBounds();
            rectF.set(bitmapBounds);
            // Create an off-screen bitmap to the PorterDuff alpha blending to work right
            int saveCount = canvas.saveLayer(rectF, null,
                    Canvas.MATRIX_SAVE_FLAG |
                            Canvas.CLIP_SAVE_FLAG |
                            Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                            Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                            Canvas.CLIP_TO_LAYER_SAVE_FLAG);
            // Resize the rounded rect we'll clip by this view's current bounds
            // (super.onDraw() will do something similar with the drawable to draw)
            getImageMatrix().mapRect(rectF);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, CORNER_RADIUS, CORNER_RADIUS, paint);

            Xfermode oldMode = paint.getXfermode();
            // This is the paint already associated with the BitmapDrawable that super draws
            paint.setXfermode(PORTER_DUFF_XFERMODE);
            super.onDraw(canvas);
            paint.setXfermode(oldMode);
            canvas.restoreToCount(saveCount);
        } else {
            super.onDraw(canvas);
        }
    }


}