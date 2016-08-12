package net.neobp.neoform.value;

import java.util.HashMap;
import java.util.Map;


/** This NeoformComponent acts as a type/value converter, implemented as a wrapper. Useful for editing an Integer
 * whithin a NeoformText or the like.
 * @param V the type of value used in this ValueHolder (ie. the Integer)
 * @param T the type of the wrapped NeoformComponent<T> (ie. the String)
 * @param C the type of the underlying Control (ie. Control in SWT)
 * @param L the type of the corresponding ValueChangeListener of this value Holder
 * @param Ld the type of the ValueChangeListener of the delegate
 */
public class ConvertingValueHolder<V, T, C> implements ValueHolder<V, C, ValueChangeListener<V>>
{
    private final ValueHolder<T, C, ValueChangeListener<T>> delegate;
    private final ValueConverter<V, T> converter;
    private Map<ValueChangeListener<V>, ValueChangeListener<T>> vclMap
        =new HashMap<ValueChangeListener<V>, ValueChangeListener<T>>();
    
    public ConvertingValueHolder(ValueHolder<T, C, ValueChangeListener<T>> delegate, ValueConverter<V, T> converter) {
        this.delegate=delegate;
        this.converter=converter;
    }

     /** Converts the value to the type of the delegates value (what is T) and sets it on the delegate. */
    public void setValue(V value)
    {
        delegate.setValue(converter.model2Screen(value));
    }

    public void addValueChangeListener(final ValueChangeListener<V> valueChangeListener)
    {
        ValueChangeListener<T> vlc=new ValueChangeListener<T>() {
            @Override
            public void valueChanged(T newValue)
            {
                valueChangeListener.valueChanged(converter.screen2Model(newValue));
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
    
    public C getControl() {
        return delegate.getControl();
    }
}
