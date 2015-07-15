package org.jackform.innocent.widget;
import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import org.jackform.customwidget.superedittext.MaterialEditText;
//import com.rengwuxian.materialedittext.MaterialEditText;
/**
 * Created by jackform on 15-6-11.
 */
public class PasswordEditText extends MaterialEditText {
    public final static int MAX_CHARACTER = 20;
    public PasswordEditText(Context context) {
        super(context);
        init();
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PasswordEditText(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init();
    }

    public void init() {
        setShowClearButton(true);
//        setFloatingLabel(FLOATING_LABEL_NORMAL);
        setSingleLineEllipsis(true);
        setMaxCharacters(MAX_CHARACTER);
        setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

}
