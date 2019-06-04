package sardari.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Arrays;

public class Button extends AppCompatButton {
    private static int defaultClickDelay = 1000; //ms
    private boolean isDisableDoubleClick = true;
    private int clickDelay = defaultClickDelay;
    private long lastClickTime = 0;

    private Integer backgroundColor;
    private Integer borderColor;
    private Integer rippleColor;
    private float cornerRadius;

    private Drawable foreground;
    private Drawable background;

    private boolean hasRipple = false;

    //------------------------------------------------------------------------------------
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
            rippleColor = typedArray.getColor(R.styleable.Button_rippleColor, 0);
            cornerRadius = typedArray.getDimension(R.styleable.Button_cornerRadius, context.getResources().getColor(R.color.defaultRippleColor));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (getBackground() instanceof RippleDrawable) {
                    hasRipple = true;
                }
            }

            //region Background
            if (typedArray.hasValue(R.styleable.Button_backgroundColor)) {
                backgroundColor = typedArray.getColor(R.styleable.Button_backgroundColor, -1);
            } else if (!hasRipple) {
                if (getBackground() instanceof ColorDrawable) {
                    backgroundColor = ((ColorDrawable) getBackground()).getColor();
                }
            }
            //endregion

            //region Border
            if (typedArray.hasValue(R.styleable.Button_borderColor)) {
                borderColor = typedArray.getColor(R.styleable.Button_borderColor, backgroundColor);
            } else if (!hasRipple) {
                borderColor = backgroundColor;
            }
            //endregion

            if (backgroundColor != null) {
                background = drawRoundRect(backgroundColor, borderColor);
                setBackground(background);
            }

            //region Foreground
            if (!hasRipple || backgroundColor != null) {
                foreground = typedArray.getDrawable(R.styleable.Button_foreground);
                if (foreground != null) {
                    setForeground(foreground);
                } else {
                    TypedValue outValue = new TypedValue();
                    getContext().getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless, outValue, true);
                    Drawable _foreground = ContextCompat.getDrawable(context, outValue.resourceId);
                    setForeground(_foreground);
                }
            }
            //endregion
        } finally {
            typedArray.recycle();
        }
    }

    //------------------------------------------------------------------------------------
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

    //------------------------------------------------------------------------------------
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

    //region CornerRadius
    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }
    //endregion

    //region BackgroundColor
    public Integer getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Integer backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    //endregion

    //region BorderColor
    public Integer getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Integer borderColor) {
        this.borderColor = borderColor;
    }

    //endregion

    //region RippleColor
    public Integer getRippleColor() {
        return rippleColor;
    }

    public void setRippleColor(Integer rippleColor) {
        this.rippleColor = rippleColor;
    }
    //    //endregion

    //------------------------------------------------------------------------------------
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

        drawable = getAdaptiveRippleDrawable(getRippleColor());
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

    //------------------------------------------------------------------------------------
    private GradientDrawable drawRoundRect(Integer backgroundColor, Integer borderColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);

        if (backgroundColor != null) {
            shape.setColor(backgroundColor);
        }

        if (borderColor != null) {
            shape.setStroke(1, borderColor);
        }

        shape.setCornerRadius(getCornerRadius());

        return shape;
    }

    public Drawable getAdaptiveRippleDrawable(int color) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        int rippleColor;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rippleColor = Color.argb(100, red, green, blue);
            return new RippleDrawable(ColorStateList.valueOf(rippleColor), null, getRippleMask());
//            return new RippleDrawable(ColorStateList.valueOf(rippleColor), null, getRippleMask(rippleColor));
        } else {
            rippleColor = Color.argb(80, red, green, blue);
            return getStateListDrawable(rippleColor);
        }
    }

    private Drawable getRippleMask() {
        float[] outerRadii = new float[8];
        Arrays.fill(outerRadii, getCornerRadius());

        RoundRectShape r = new RoundRectShape(outerRadii, null, null);
        return new ShapeDrawable(r);
    }

    private Drawable getRippleMask(int color) {
        float[] outerRadii = new float[8];
        Arrays.fill(outerRadii, getCornerRadius());

        RoundRectShape r = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(r);
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    public StateListDrawable getStateListDrawable(int pressedColor) {
        StateListDrawable states = new StateListDrawable();
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(getCornerRadius());
        gradientDrawable.setColor(pressedColor);

        states.addState(new int[]{android.R.attr.state_pressed}, gradientDrawable);
        states.addState(new int[]{android.R.attr.state_focused}, gradientDrawable);
        states.addState(new int[]{android.R.attr.state_activated}, gradientDrawable);
        return states;
    }
    //------------------------------------------------------------------------------------
}
