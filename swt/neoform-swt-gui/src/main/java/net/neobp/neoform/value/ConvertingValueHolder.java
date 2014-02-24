package net.neobp.neoform.value;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Control;

/** This NeoformComponent acts as a type converter, implemented as a wrapper. Useful for editing an Integer
 * whithin a NeoformText or the like.
 * @param V the type of value used
 * @param T the type of the wrapped NeoformComponent<T>
 */
public class ConvertingValueHolder<V, T> implements ValueHolder<V>
{
    private final ValueHolder<T> delegate;
    private final ValueConverter<V, T> converter;
    private Map<ValueChangeListener<V>, ValueChangeListener<T>> vclMap=new HashMap<ValueChangeListener<V>, ValueChangeListener<T>>();
    
    public ConvertingValueHolder(ValueHolder<T> delegate, ValueConverter<V, T> converter) {
        this.delegate=delegate;
        this.converter=converter;
    }

    public void setValue(V value)
    {
        delegate.setValue(converter.convertToScreen(value));
    }

    public void addValueChangeListener(final ValueChangeListener<V> valueChangeListener)
    {
        ValueChangeListener<T> vlc=new ValueChangeListener<T>() {
            public void valueChanged(ValueHolder<T> src, T newValue)
            {
                valueChangeListener.valueChanged(ConvertingValueHolder.this, converter.convertToModel(newValue));
            }
        };
        vclMap.put(valueChangeListener, vlc);
        delegate.addValueChangeListener(vlc);
    }

    public void removeValueChangeListener(final ValueChangeListener<V> valueChangeListener)
    {
        delegate.removeValueChangeListener(vclMap.get(valueChangeListener));
        vclMap.remove(valueChangeListener);
    }
    
    public Control getControl() {
        return delegate.getControl();
    }
}
