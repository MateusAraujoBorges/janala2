package tests.casestudies.math3.exception.util;


public interface ExceptionContextProvider {
    /**
     * Gets a reference to the "rich context" data structure that allows to
     * customize error messages and store key, value pairs in exceptions.
     *
     * @return a reference to the exception context.
     */
    ExceptionContext getContext();

}