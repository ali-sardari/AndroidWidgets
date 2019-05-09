package sardari.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;

public class ImageView extends android.support.v7.widget.AppCompatImageView {
    private Context context;
    private final static int defaultClickDelay = 1000; //ms
    private boolean isDisableDoubleClick = true;
    private int clickDelay = defaultClickDelay;
    private long lastClickTime = 0;
    private boolean isSquare = false;
    private boolean isRipple = false;

    private Drawable foreground;
    //==================================================================================

    public ImageView(Context context) {
        super(context);
        initViews(context, null);
    }

    public ImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public ImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageView, 0, 0);

        try {
            clickDelay = typedArray.getInteger(R.styleable.ImageView_clickDelay, defaultClickDelay);
            isDisableDoubleClick = typedArray.getBoolean(R.styleable.ImageView_disableDoubleClick, true);
            isSquare = typedArray.getBoolean(R.styleable.ImageView_square, false);
            isRipple = typedArray.getBoolean(R.styleable.ImageView_ripple, false);

            Drawable foreground = typedArray.getDrawable(R.styleable.ImageView_foreground);
            if (isRipple && foreground == null && this.hasOnClickListeners()) {
                TypedValue outValue = new TypedValue();
                getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                setForeground(ContextCompat.getDrawable(context, outValue.resourceId));
            } else if (foreground != null) {
                setForeground(foreground);
            }
        } finally {
            typedArray.recycle();
        }
    }
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
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);

        if (isRipple && foreground == null && this.hasOnClickListeners()) {
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            setForeground(ContextCompat.getDrawable(context, outValue.resourceId));
        }
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

        if (isSquare) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int size = Math.min(width, height);
            size = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
            setMeasuredDimension(size, size);
        }

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


    public boolean isRipple() {
        return isRipple;
    }

    public void setRipple(boolean ripple) {
        isRipple = ripple;
    }

    public boolean isSquare() {
        return isSquare;
    }

    public void setSquare(boolean square) {
        isSquare = square;
    }
}
