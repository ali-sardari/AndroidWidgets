package sardari.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Button extends AppCompatButton {
    private Context context;
    private static int defaultClickDelay = 1000; //ms
    private boolean isDisableDoubleClick = true;
    private int clickDelay = defaultClickDelay;
    private long lastClickTime = 0;

    private int backgroundColor;
    private int borderColor;
    private float radius;

    private Drawable foreground;
    private Drawable background;

    //==================================================================================

    public Button(Context context) {
        super(context);
        initViews(context, null);
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public Button(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Button, 0, 0);

        try {
            clickDelay = typedArray.getInteger(R.styleable.Button_clickDelay, defaultClickDelay);
            isDisableDoubleClick = typedArray.getBoolean(R.styleable.Button_disableDoubleClick, true);
            radius = typedArray.getDimension(R.styleable.Button_radius, 0);

            Log.w("MyTag", "getBackground1= " + getBackground());
            //region Background & Border
            //---Background--------------------------------------------------------------------------
            if (getBackground() instanceof GradientDrawable || getBackground() instanceof ShapeDrawable) {

            } else if (getBackground() instanceof ColorDrawable) {
                if (typedArray.hasValue(R.styleable.Button_backgroundColor)) {
                    backgroundColor = typedArray.getColor(R.styleable.Button_backgroundColor, -1);
                } else {
                    backgroundColor = ((ColorDrawable) getBackground()).getColor();
                }

                //---Border--------------------------------------------------------------------------
                if (typedArray.hasValue(R.styleable.Button_borderColor)) {
                    borderColor = typedArray.getColor(R.styleable.Button_borderColor, backgroundColor);
                } else {
                    borderColor = backgroundColor;
                }

                background = drawRoundRect(backgroundColor, borderColor);
                setBackground(background);
            }
            //endregion

            //region Foreground
//            foreground = typedArray.getDrawable(R.styleable.Button_foreground);
//            if (foreground != null) {
//                setForeground(foreground);
//            } else {
//                TypedValue outValue = new TypedValue();
//                getContext().getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless, outValue, true);
//                Drawable _foreground = ContextCompat.getDrawable(context, outValue.resourceId);
//                setForeground(_foreground);
//            }
            //endregion
        } finally {
            typedArray.recycle();
        }
    }

    private GradientDrawable drawRoundRect(int backgroundColor, int borderColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(backgroundColor);
        shape.setStroke(1, borderColor);
        shape.setCornerRadius(getRadius());
//        shape.setAlpha(1);

        return shape;
    }

    protected float getDimension(int id) {
        return getResources().getDimension(id);
    }

//    private Bitmap createMask(int width, int height) {
//        Bitmap mask = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
//        Canvas canvas = new Canvas(mask);
//
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(Color.WHITE);
//
//        canvas.drawRect(0, 0, width, height, paint);
//
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        canvas.drawRoundRect(new RectF(0, 0, width, height), cornerRadius, cornerRadius, paint);
//
//        return mask;
//    }

    //==================================================================================

    @Override
    public boolean performClick() {
        if (isDisableDoubleClick) {
            //region Preventing multiple clicks, using threshold of 1 second
            if (SystemClock.elapsedRealtime() - lastClickTime < clickDelay) {
                return false;
            }

            lastClickTime = SystemClock.elapsedRealtime();
            //endregion
            return super.performClick();
        } else {
            return super.performClick();
        }
    }

    @Override
    public void setOnClickListener(@Nullable View.OnClickListener l) {
        super.setOnClickListener(l);
    }
    //==================================================================================

    //region isDisableDoubleClick | Getter/Setter
    public boolean isDisableDoubleClick() {
        return isDisableDoubleClick;
    }

    public void setDisableDoubleClick(boolean disableDoubleClick) {
        isDisableDoubleClick = disableDoubleClick;
    }
    //endregion

    //region clickDelay | Getter/Setter
    public int getClickDelay() {
        return clickDelay;
    }

    public void setClickDelay(int clickDelay) {
        this.clickDelay = clickDelay;
    }

    //endregion


    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    //region Foreground
    public void setForegroundResource(int drawableResId) {
        setForeground(ContextCompat.getDrawable(getContext(), drawableResId));
    }

    public void setForeground(Drawable drawable) {
        if (foreground == drawable) {
            return;
        }

        if (foreground != null) {
            foreground.setCallback(null);
            unscheduleDrawable(foreground);
        }

        foreground = drawable;

        if (drawable != null) {
            drawable.setCallback(this);
            if (drawable.isStateful()) {
                drawable.setState(getDrawableState());
            }
        }

        requestLayout();
        invalidate();
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == foreground;
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (foreground != null) foreground.jumpToCurrentState();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (foreground != null && foreground.isStateful()) {
            foreground.setState(getDrawableState());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (foreground != null) {
            foreground.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            invalidate();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (foreground != null) {
            foreground.setBounds(0, 0, w, h);
            invalidate();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (foreground != null) {
            foreground.draw(canvas);
        }
    }
    //endregion
}
