package net.neobp.neoform.android.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import net.neobp.neoform.android.gui.AbstractNeoformComponent;
import net.neobp.neoform.exec.NeoformExec;

/** Android implementation of NeoformText.
 */
public class NeoformText extends AbstractNeoformComponent<String>
{
    /** The android delegate for this component. */
    private final TextView textDelegate;
    
    /**
     * @param context is usually the surrounding Activity
     * @param parent the parent view group
     */
    public NeoformText(final Context context, final ViewGroup parent, NeoformExec neoformExec) {
        super(neoformExec);
        
        textDelegate=new TextView(context);
        parent.addView(textDelegate);
        
        textDelegate.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable arg0) {
                fireValueChanged(textDelegate.getText().toString());
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
        });
    }

    @Override
    protected void value2widget(final String value) {
        textDelegate.setText(value);
    }
    
    public void layout2widget(final LayoutParams layoutData) {
        textDelegate.setLayoutParams(layoutData);
    }

    public View getControl() {
        return textDelegate;
    }
}
