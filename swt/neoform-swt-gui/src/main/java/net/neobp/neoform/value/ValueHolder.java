package net.neobp.neoform.value;

import org.eclipse.swt.widgets.Control;


/** Interface for most likely editable widgets.
 * @param V the class of the held value.
 */
public interface ValueHolder<V>
{
    /** Sets the current value of the widget. 
     * Does usually trigger a call to the ValueChangeListeners.
     * @param value the new value to show (most likely for programmatic actions while editing)
     */
    public void setValue(V value);

    /** 
     * NOTE: there is no getValue(); Use a valueChangeListener instead.
     */
    
    /** Adds a ValueChangeListener to this ValueHolder. It will be called whenever the value of this
     * ValueHolder changes. This happens by setValue(value) or some other way.
     * @param valueChangeListener the listener added to the list of listeners.
     */
    public void addValueChangeListener(ValueChangeListener<V> valueChangeListener);

    /** Removes a previously added ValueChangeListener
     * @param valueChangeListener the ValueChangeListener to remove
     */
    public void removeValueChangeListener(ValueChangeListener<V> valueChangeListener);
    
    /** This method is needed for creation of SWT tables, dont use otherwise, please.
     * @return the wrapped control
     */
    public Control getControl();
}
