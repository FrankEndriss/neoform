package net.neobp.neoform.value;


public interface ValueChangeListener<V>
{
    public void valueChanged(ValueHolder<V> src, V newValue);
}
