package net.neobp.neoform.gui;

import java.util.HashSet;
import java.util.Set;

import net.neobp.neoform.value.ValueChangeListener;
import net.neobp.neoform.value.ValueHolder;

/** Abstract implementation of ValueHolder, to easy implementation of components in neoform.
 * To subclass this class you must implement two things:
 * -the method value2widget(V value) which transports the value into the widget delegate.
 * -a Listener on the delegate widget which should call fireValueChanged(newValue) after
 * any change to the value in the widget.
 * -setLayoutData(Object LayoutData)
 * Some platform widgets do not fire change events on setValue(...) which you call in value2widget(). Dont
 * care about this problem, it is handled in this class in a way that this class fires an event
 * independent of the widget after value2widget().
 * 
 * @param V the class of the held value
 */
public abstract class AbstractNeoformComponent<V> implements ValueHolder<V>
{
    private Set<ValueChangeListener<V>> valueChangeListeners=new HashSet<ValueChangeListener<V>>();
    private volatile boolean ignoreValueChanged=false;
    
    public void setValue(final V value)
    {
        try {
            ignoreValueChanged=true;
            value2widget(value);
        }finally{
            ignoreValueChanged=false;
        }

        // now make sure event is fired
        fireValueChanged(value);
    }
    
    /** This method is called when a value was set programmatically on
     * this component (setValue(value) was called).
     * It should simply put the value into the widget.
     * @param value the value to show
     */
    protected abstract void value2widget(V value);
    
    /**
     * @param layoutData
     */
    public abstract void setLayoutData(Object layoutData);
    
    public void addValueChangeListener(final ValueChangeListener<V> valueChangeListener)
    {
        valueChangeListeners.add(valueChangeListener);
    }

    public void removeValueChangeListener(final ValueChangeListener<V> valueChangeListener)
    {
        valueChangeListeners.remove(valueChangeListener);
    }

    /** Call this method in your subclass whenever the widget changes the value while editing.
     * It will trigger the valueChangeListeners.
     * @param newValue
     */
    protected void fireValueChanged(final V newValue)
    {
        if(ignoreValueChanged)
            return;
        
        for(ValueChangeListener<V> l : valueChangeListeners)
            l.valueChanged(this, newValue);
    }
}
