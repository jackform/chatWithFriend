package org.jackform.innocent.widget;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import org.jackform.customwidget.superedittext.MaterialEditText;

/**
 * Created by jackform on 15-8-27.
 */
public class SearchEdiText extends MaterialEditText {
    public SearchEdiText(Context context) {
        super(context);
        init();
    }

    public SearchEdiText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchEdiText(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init();
    }

    private void init() {
        setShowClearButton(true);
//        setFloatingLabel(FLOATING_LABEL_NORMAL);
        setSingleLineEllipsis(true);
        setInputType(InputType.TYPE_CLASS_TEXT);
    }


}

