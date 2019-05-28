package sardari.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import sardari.utils.UIUtil;

public class Button extends android.support.v7.widget.AppCompatButton {
    private static int defaultClickDelay = 1000; //ms
    private boolean isDisableDoubleClick = true;
    private int clickDelay = defaultClickDelay;
    private long lastClickTime = 0;

    private Drawable foreground;
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
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Button, 0, 0);

        try {
            clickDelay = typedArray.getInteger(R.styleable.Button_clickDelay, defaultClickDelay);
            isDisableDoubleClick = typedArray.getBoolean(R.styleable.Button_disableDoubleClick, true);

            Drawable foreground = typedArray.getDrawable(R.styleable.Button_foreground);

            if (foreground != null) {
                setForeground(foreground);
            } else {
                TypedValue outValue = new TypedValue();
                getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                Drawable foreground1 = ContextCompat.getDrawable(context, outValue.resourceId);
                setForeground(foreground1);
            }
        } finally {
            typedArray.recycle();
        }
    }

    private static GradientDrawable drawRoundRect(Context context, int backgroundColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(context.getResources().getDimensionPixelSize(R.dimen._20sdp));
        shape.setColor(backgroundColor);
//        shape.setAlpha(1);

        shape.setStroke(1, UIUtil.getColor(context, R.color.toast_text));
        return shape;
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
