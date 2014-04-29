package net.neobp.neoform.value;


/** A simplest ValueChangeListener.
 * @param V the type of the changed value
 */
public interface ValueChangeListener<V> {
    public void valueChanged(V newValue);
}
