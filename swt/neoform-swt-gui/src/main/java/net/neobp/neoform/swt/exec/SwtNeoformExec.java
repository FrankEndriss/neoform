package net.neobp.neoform.swt.exec;

import java.util.concurrent.ExecutorService;

import net.neobp.neoform.exec.AwaitableRunnable;
import net.neobp.neoform.exec.NeoformExec;
import net.neobp.neoform.exec.WrongThreadException;

import org.eclipse.swt.widgets.Display;

public class SwtNeoformExec implements NeoformExec {
    
    private final Display display;
    private final ExecutorService executorService;
    
    /** Counter of "still not executed" Runnables */
    private volatile int asyncCounter=0;
    /** Lock for the above counter */
    private Object asyncCounterLock=new Object();


    public SwtNeoformExec(final Display display, final ExecutorService executorService) {
        this.display=display;
        this.executorService=executorService;
    }

    @Override
    public boolean isPlatformGuiThread() {
        return display.getThread() == Thread.currentThread();
    }

    @Override
    public void runOnPlatformGuiThread(final Runnable r, final boolean waitForCompletition)
    {
        final CountedRunnable countedR=new CountedRunnable(r);
        
        if(!waitForCompletition)
            display.asyncExec(countedR);
        else {
            if(isPlatformGuiThread()) {
                countedR.run();
            } else {
                AwaitableRunnable aR=new AwaitableRunnable(countedR);
                display.asyncExec(aR);
                aR.await();
            }
        }
    }

    @Override
    public ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public void waitForPlatformGuiThread() throws WrongThreadException {
        if(isPlatformGuiThread())
            throw new WrongThreadException("cannot wait for platforms gui thread in platforms gui thread :/");

        synchronized(asyncCounterLock) {
            while(asyncCounter>0) {
                try {
                    asyncCounterLock.wait();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** Helper class to count "still not executed" Runnables.
     */
    private class CountedRunnable implements Runnable {
        private final Runnable delegate;

        public CountedRunnable(Runnable delegate) {
            this.delegate=delegate;
            synchronized(asyncCounterLock) {
                asyncCounter++;
            }
        }

        @Override
        public void run() {
            delegate.run();
            synchronized(asyncCounterLock) {
                asyncCounter--;
                asyncCounterLock.notifyAll();
            }
        }
    }
}
