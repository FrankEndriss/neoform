package net.neobp.neoform.value;


/** Interface for most likely editable widgets.
 * @param V the class of the held value.
 * @param C the base class of the controls of the implementing platform, ie. org.eclipse.swt.Control or
 * javax.swing.JComponent or the like
 * @param L the type of the ValueChangeListener (should be ValueChangeListener<V, C>)
 */
public interface ValueHolder<V, C, L extends ValueChangeListener<V>>
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
    public void addValueChangeListener(L valueChangeListener);

    /** Removes a previously added ValueChangeListener
     * @param valueChangeListener the ValueChangeListener to remove
     */
    public void removeValueChangeListener(L valueChangeListener);
    
    /** This method is needed for platform dependend stuff
     * @return the wrapped platform dependend control
     */
    public C getControl();
}
