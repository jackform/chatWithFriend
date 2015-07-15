package org.jackform.innocent.widget;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import org.jackform.customwidget.superedittext.MaterialEditText;

//import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by jackform on 15-6-11.
 */
public class AgeEditText extends MaterialEditText {
    public AgeEditText(Context context) {
        super(context);
        init();
    }

    public AgeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AgeEditText(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init();
    }

    private void init() {
        setShowClearButton(true);
        setFloatingLabel(FLOATING_LABEL_NORMAL);
        setSingleLineEllipsis(true);
        setInputType(InputType.TYPE_CLASS_NUMBER);
    }
}
