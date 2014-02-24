package net.neobp.dev.codeGenerator;

public interface SpringCallback {
    public Class<?> getType(String beanName);

    /** Usually called right after a spring configuration file was created
     */
    public void refresh();
}