package org.chai.util.customwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by victor on 03-Apr-15.
 */
public class NoParentPressImageView extends ImageView {

    public NoParentPressImageView(Context context) {
        this(context, null);
    }

    public NoParentPressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setPressed(boolean pressed) {
        // If the parent is pressed, do not set to pressed.
        if (pressed && ((View) getParent()).isPressed()) {
            return;
        }
        super.setPressed(pressed);
    }
}
