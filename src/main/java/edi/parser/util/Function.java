package edi.parser.util;

/**
 * Function is just function. Which return object from class Result by object of class Argument.
 * <p>
 * Example of usage:
 * CollectionUtil use this interface for converting one collection type to another.
 *
 * @param <Result>   result of function
 * @param <Argument> argument of function
 */
public interface Function<Result, Argument> {
    public Result evaluate(Argument arg);
}
