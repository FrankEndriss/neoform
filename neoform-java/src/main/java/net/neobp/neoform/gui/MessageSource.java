package net.neobp.neoform.gui;

/** Interface for sources of messages.
 */
public interface MessageSource {
    /**
     * @param key the key
     * @return a message for a key
     */
    public String getMessage(String key);
}
