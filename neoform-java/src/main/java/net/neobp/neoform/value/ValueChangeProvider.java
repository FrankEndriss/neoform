package net.neobp.neoform.value;

public interface ValueChangeProvider<V, C, L extends ValueChangeListener<V>>
{
    public void addValueChangeListener(L listener);
    public void removeValueChangeListener(L listener);
}
