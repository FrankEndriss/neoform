package net.neobp.neoform.exec;

import java.util.concurrent.ExecutorService;

/** Thread util interface for Neoform.
 * Neoform runs on multiple threads.
 * If you want to switch from platforms gui thread to neoform thread use the getExecutorService().submit()
 * If you want to switch from the neoforms Executors Thread(s) to the platforms gui thread,
 * use runOnPlatformGuiThread().
 */
public interface NeoformExec {
    /**
     * @return true if the calling thread is the platforms GUI thread, false otherwise
     */
    public boolean isPlatformGuiThread();

    /** Runs a job on the platforms GUI thread.
     * @param r the runnable
     * @param waitForCompletition if true, the call to this method does not return before the Runnable r 
     * has finished, else it returns immediately.
     */
    public void runOnPlatformGuiThread(Runnable r, boolean waitForCompletition);
    
    /** This method returns after all per runOnPlatformGuiThread() submitted jobs
     * have finished.
     * Do not call this on the platforms gui thread, because such a call would deadlock.
     * @throws WrongThreadException if called on the platforms gui thread
     */
    public void waitForPlatformGuiThread() throws WrongThreadException;

    /** Neoform has not "Neoform thread", but an ExecutorService. Use this if you want to run
     * a job within the Neoform context.
     * The executor service could back only one thread, better do not block it.
     * @return the Neoform ExecutorService
     */
    public ExecutorService getExecutorService();
}
