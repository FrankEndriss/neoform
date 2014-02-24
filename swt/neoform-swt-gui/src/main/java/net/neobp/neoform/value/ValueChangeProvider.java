package net.neobp.neoform.value;


public interface ValueChangeProvider<V>
{
    public void addValueChangeListener(ValueChangeListener<V> listener);
    public void removeValueChangeListener(ValueChangeListener<V> listener);
}
