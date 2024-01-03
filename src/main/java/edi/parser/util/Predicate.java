package edi.parser.util;

/**
 * Predicate is a function of object which return true if object satisfy the
 * object and false otherwise.
 * <p>
 * Usually predicate implement some condition on object. This interface used in
 * CollectionUtil and during parsing of BSP Hot file.
 *
 * @param <T> type of argument
 */
public interface Predicate<T> {
    public boolean evaluate(T object);

    /**
     * Predicate which return always true
     *
     * @param <T>
     */
    public class TRUE<T> implements Predicate<T> {
        public boolean evaluate(T object) {
            return true;
        }
    }
}
