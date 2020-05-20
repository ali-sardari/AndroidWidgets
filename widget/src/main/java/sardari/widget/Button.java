package sardari.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Button extends AppCompatButton {
    private Handler handler;
    private static int defaultClickDelay = 1000; //ms
    private boolean isDisableDoubleClick = true;
    private int clickDelay = defaultClickDelay;
    private long lastClickTime = 0;

    private Integer backgroundColor;
    private Integer disableColor;
    private Integer borderColor;
    private Integer rippleColor;
    private float cornerRadius;

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
        handler = new Handler(context.getMainLooper());

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Button, 0, 0);

        try {
            initDrawable(context, typedArray);

            clickDelay = typedArray.getInteger(R.styleable.Button_clickDelay, defaultClickDelay);
            isDisableDoubleClick = typedArray.getBoolean(R.styleable.Button_disableDoubleClick, true);
            rippleColor = typedArray.getColor(R.styleable.Button_rippleColor, context.getResources().getColor(R.color.defaultRippleColor));
            cornerRadius = typedArray.getDimension(R.styleable.Button_cornerRadius, context.getResources().getDimension(R.dimen.corner_radius));
            if (typedArray.hasValue(R.styleable.Button_disableColor)) {
                disableColor = typedArray.getColor(R.styleable.Button_disableColor, context.getResources().getColor(R.color.disableColor));
            }

            if (getBackground() instanceof StateListDrawable) {
                hasRipple = true;
            }

            Log.w("MyTag", " Background-> " + getBackground());
            Log.w("MyTag", " hasRipple-> " + hasRipple);

            //region Background
            if (typedArray.hasValue(R.styleable.Button_defaultColor)) {
                backgroundColor = typedArray.getColor(R.styleable.Button_defaultColor, -1);
            } else {
                if (getBackground() instanceof ColorDrawable) {
                    backgroundColor = ((ColorDrawable) getBackground()).getColor();
                }
            }
            //endregion

            //region Border
            if (typedArray.hasValue(R.styleable.Button_borderColor)) {
                borderColor = typedArray.getColor(R.styleable.Button_borderColor, context.getResources().getColor(android.R.color.transparent));
            }
            //endregion

            if (!hasRipple) {
                if (backgroundColor != null) {
                    setBackground(generateRippleDrawable());
                }
            }
        } finally {
            typedArray.recycle();
        }
    }

    private void initDrawable(Context context, TypedArray typedArray) {
        Drawable drawableLeft = null;
        Drawable drawableRight = null;
        Drawable drawableBottom = null;
        Drawable drawableTop = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawableLeft = typedArray.getDrawable(R.styleable.Button_drawableLeftCompat);
            drawableRight = typedArray.getDrawable(R.styleable.Button_drawableRightCompat);
            drawableBottom = typedArray.getDrawable(R.styleable.Button_drawableBottomCompat);
            drawableTop = typedArray.getDrawable(R.styleable.Button_drawableTopCompat);
        } else {
            final int drawableLeftId = typedArray.getResourceId(R.styleable.Button_drawableLeftCompat, -1);
            final int drawableRightId = typedArray.getResourceId(R.styleable.Button_drawableRightCompat, -1);
            final int drawableBottomId = typedArray.getResourceId(R.styleable.Button_drawableBottomCompat, -1);
            final int drawableTopId = typedArray.getResourceId(R.styleable.Button_drawableTopCompat, -1);

            if (drawableLeftId != -1)
                drawableLeft = AppCompatResources.getDrawable(context, drawableLeftId);
            if (drawableRightId != -1)
                drawableRight = AppCompatResources.getDrawable(context, drawableRightId);
            if (drawableBottomId != -1)
                drawableBottom = AppCompatResources.getDrawable(context, drawableBottomId);
            if (drawableTopId != -1)
                drawableTop = AppCompatResources.getDrawable(context, drawableTopId);
        }

        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
//        setCompoundDrawablePadding(3);
//        setPadding(10, 2, 10, 2);
//        setCompoundDrawablePadding(10);
    }

    //------------------------------------------------------------------------------------
    @Override
    public boolean performClick() {
        if (isDisableDoubleClick) {
            setEnabled(false);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setEnabled(true);
                }
            }, clickDelay);

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

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        setAlpha(enabled ? 1f : 0.5f);
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
    public Integer getRippleAlphaColor() {
        if (rippleColor != null) {
            int red = Color.red(rippleColor);
            int green = Color.green(rippleColor);
            int blue = Color.blue(rippleColor);

            return Color.argb(80, red, green, blue);
        } else {
            return null;
        }
    }

    public Integer getRippleColor() {
        return rippleColor;
    }

    public void setRippleColor(Integer rippleColor) {
        this.rippleColor = rippleColor;
    }
    //endregion

    //region DisableColor
    public Integer getDisableColor() {
        return disableColor;
    }

    public void setDisableColor(Integer disableColor) {
        this.disableColor = disableColor;
    }
    //endregion

    //------------------------------------------------------------------------------------

    private Drawable generateRippleDrawable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (getRippleColor() != null) {
                return new RippleDrawable(ColorStateList.valueOf(getRippleAlphaColor()), generateGradientDrawable(), null);
            } else {
                return null;
            }
        } else {
            return generateStateListDrawable();
        }
    }

    private StateListDrawable generateStateListDrawable() {
        StateListDrawable states = new StateListDrawable();

        //region defaultGradientDrawable
        GradientDrawable defaultGradientDrawable = new GradientDrawable();
        defaultGradientDrawable.setCornerRadius(getCornerRadius());
        if (getBackgroundColor() != null) {
            defaultGradientDrawable.setColor(getBackgroundColor());
        }
        if (getBorderColor() != null) {
            defaultGradientDrawable.setStroke(1, getBorderColor());
        }
        //endregion

        //region rippleGradientDrawable
        GradientDrawable rippleGradientDrawable = new GradientDrawable();
        rippleGradientDrawable.setCornerRadius(getCornerRadius());
        if (getRippleColor() != null) {
            rippleGradientDrawable.setColor(getRippleColor());
        }
        if (getBorderColor() != null) {
            rippleGradientDrawable.setStroke(1, getBorderColor());
        }
        //endregion

        states.addState(new int[]{android.R.attr.state_pressed}, rippleGradientDrawable);
        states.addState(new int[]{android.R.attr.state_focused}, rippleGradientDrawable);
        states.addState(new int[]{android.R.attr.state_activated}, rippleGradientDrawable);
        states.addState(new int[]{}, defaultGradientDrawable);
        return states;
    }

    private GradientDrawable generateGradientDrawable() {
        GradientDrawable defaultGradientDrawable = new GradientDrawable();
        defaultGradientDrawable.setCornerRadius(getCornerRadius());
        if (getBackgroundColor() != null) {
            defaultGradientDrawable.setColor(getBackgroundColor());
        }
        if (getBorderColor() != null) {
            defaultGradientDrawable.setStroke(1, getBorderColor());
        }

        return defaultGradientDrawable;
    }
}
