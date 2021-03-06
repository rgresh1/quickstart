package hello.server.transformation;

import java.util.HashSet;
import java.util.Set;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

/**
 * A {@link ClassPool} that supports adding classpath's through {@link ClassLoader} objects.<br>
 * <br>
 *
 * @author <a href="mailto:moelholm@gmail.com">Nicky Moelholm</a>
 */
public class HelloClassPool extends ClassPool {

    // ------------------------------------------------------------------------
    // Static fields
    // ------------------------------------------------------------------------

    private static final Set<ClassLoader> loaders = new HashSet<ClassLoader>();

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------

    public HelloClassPool() {
        super(null);
        appendSystemPath();
    }

    // ------------------------------------------------------------------------
    // Public API
    // ------------------------------------------------------------------------

    public void addClassLoaderClassPath(ClassLoader cl) {
        synchronized (loaders) {
            if (!loaders.contains(cl)) {
                insertClassPath(new LoaderClassPath(cl));
                loaders.add(cl);
            }
        }
    }

    // ------------------------------------------------------------------------
    // Specialization of the normal ClassPool behavior
    // ------------------------------------------------------------------------

    /**
     * This method can cause deadlocks in the parent pool. Hence it is overwritten here without the <code>synchronized</code>
     * modifier.
     */
    @Override
    protected CtClass get0(String classname, boolean useCache) throws NotFoundException {

        CtClass clazz = null;

        if (useCache) {
            clazz = getCached(classname);
            if (clazz != null) {
                return clazz;
            }
        }

        clazz = createCtClass(classname, useCache);

        if (clazz != null) {

            if (useCache) {
                cacheCtClass(clazz.getName(), clazz, false);
            }

            return clazz;
        }

        return clazz;
    }
}