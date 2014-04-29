package net.neobp.neoform.gui;

import java.util.HashSet;
import java.util.Set;

import net.neobp.neoform.exec.NeoformExec;
import net.neobp.neoform.value.ValueChangeListener;
import net.neobp.neoform.value.ValueHolder;

/** Abstract implementation of ValueHolder, to easy implementation of components in neoform.
 * <pre>
 * To subclass this class you must implement two things:
 * -the method value2widget(V value) which transports the value into the widget delegate.
 * -a Listener on the delegate widget which should call fireValueChanged(newValue) after
 * any change to the value in the widget.
 * -layout2widget(L LayoutData) which transports layout constraint data to the 
 * widget.
 * </pre>
 * Some platform widgets do not fire change events on setValue(...) which you call in value2widget(). Dont
 * care about this problem, it is handled in this class in a way that this class fires an event
 * independent of the widget after value2widget().
 * 
 * @param V the class of the held value
 * @param C the platform dependent base class of the Controls
 * @param LD the platform dependent base class of LayoutData
 * @param L the class of the ValueChangeListener
 */
public abstract class AbstractNeoformComponent<V, C, LD, L extends ValueChangeListener<V>> implements ValueHolder<V, C, L>
{
    private Set<L> valueChangeListeners=new HashSet<L>();
    private volatile boolean ignoreValueChanged=false;
    private final NeoformExec neoformExec;
    
    public AbstractNeoformComponent(final NeoformExec neoformExec) {
        if(neoformExec==null)
            throw new IllegalArgumentException("NeoformExec must not be null");
        this.neoformExec=neoformExec;
    }

    @Override
    public void setValue(final V value) {
        if(neoformExec.isPlatformGuiThread())
            setValue_sync(value);
        else {
            neoformExec.runOnPlatformGuiThread(new Runnable() {
                @Override
                public void run() {
                    setValue_sync(value);
                }
            }, false);
        }
    }

    private void setValue_sync(final V value)
    {
        assert(neoformExec.isPlatformGuiThread());

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
     * This method is called on the platform GUIs gui-thread.
     * @param value the value to show
     */
    protected abstract void value2widget(V value);
    
    /** Sets the layoutData for the widget, can be called on any thread.
     * It makes that layout2widget(layoutData) is called on the platforms
     * gui-thread.
     * It returns immediately.
     * @param layoutData
     */
    public void setLayoutData(final LD layoutData) {
        if(neoformExec.isPlatformGuiThread())
            layout2widget(layoutData);
        else {
            neoformExec.runOnPlatformGuiThread(new Runnable() {
                @Override
                public void run() {
                    layout2widget(layoutData);
                }
            }, false);
        }
    }
    
    public void setLayoutData_sync(LD layoutData) {
        assert(neoformExec.isPlatformGuiThread());
        layout2widget(layoutData);
    }

    /** This method is called when the platform dependent layout constraints are set on the component.
     * (ie. in SWT someControl.setLayoutData(Object o))
     * It should simply do that call on the platform widget.
     * This method is called on the platforms GUIs gui-thread
     * @param layoutData
     */
    protected abstract void layout2widget(LD layoutData);
    
    public void addValueChangeListener(L valueChangeListener) {
        valueChangeListeners.add(valueChangeListener);
    }

    public void removeValueChangeListener(L valueChangeListener) {
        valueChangeListeners.remove(valueChangeListener);
    }

    /** Call this method in your subclass whenever the widget changes the value while editing.
     * It will trigger the valueChangeListeners.
     * The call to the valueChangeListeners will be performed on the neoformExecutor in parallel
     * while the call to this method returns immediately.
     * @param newValue
     */
    protected void fireValueChanged(final V newValue)
    {
        if(ignoreValueChanged)
            return;
        
        for(final ValueChangeListener<V> l : valueChangeListeners) {
            neoformExec.getExecutorService().submit(new Runnable() {
                @Override
                public void run() {
                    l.valueChanged(newValue);
                }
            });
        }
    }
    
}
