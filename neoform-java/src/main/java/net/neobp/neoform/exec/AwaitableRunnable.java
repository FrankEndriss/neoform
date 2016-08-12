package net.neobp.neoform.exec;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/** Helper class to make a Runnable awaitable.
 * Use the call to await() to wait until the delegate has been run.
 */
public class AwaitableRunnable implements Runnable {
    private final Runnable delegate;
    private final CountDownLatch latch;
    private final AtomicInteger runCounter;

    /** Calls the other constructor with a runcount of 1
     * @param delegate
     */
    public AwaitableRunnable(final Runnable delegate) {
        this(delegate, 1);
    }

    /**
     * @param delegate the delegate to run at most runcount times.
     * @param runcount the purpose of this parameter is to control the call(s) to await(). As a consequence
     * of this, run() throws a RuntimeException if called more than runcount times.
     */
    public AwaitableRunnable(final Runnable delegate, int runcount) {
        if(delegate==null)
            throw new IllegalArgumentException("Runnable must not be null");
        if(runcount<1)
            throw new IllegalArgumentException("runcount must not be less than 1");

        this.delegate=delegate;
        latch=new CountDownLatch(runcount);
        runCounter=new AtomicInteger(runcount);
    }

    /** Executes the runnable given in the constructor by calling its run() method.
     * @see java.lang.Runnable#run()
     */
    @Override
    public synchronized void run() {
        if(runCounter.decrementAndGet()<0)
            throw new RuntimeException("You must not call run() more times than you specified with the runcount constructor argument");
        delegate.run();
        latch.countDown();
    }
    
    /** Waits until all calls to delegate.run() have finshed. 
     * "All calls" means run() was called as much times as you specified in the constructor argument runcount. 
     **/
    public void await() {
        try {
            latch.await();
        } catch(InterruptedException e) {
            // ignore
            e.printStackTrace();
        }
    }

}
