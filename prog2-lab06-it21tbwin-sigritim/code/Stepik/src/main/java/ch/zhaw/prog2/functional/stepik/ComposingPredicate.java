package ch.zhaw.prog2.functional.stepik;

import java.util.List;
import java.util.function.IntPredicate;

public class ComposingPredicate {

    /**
     * Write a solution which is using streams.
     *
     * @see #disjunctAllNoStream(List)
     */
    public static IntPredicate disjunctAll(List<IntPredicate> predicates) {
        throw new UnsupportedOperationException();  // TODO: remove this line and implement your solution
    }

    /**
     * Classical implementation provided by lecturer to help you solve this exercise.
     * <p>
     * This solution works, but you have to search a solution using streams which will lead you
     * to a solution with less lines of code.
     */
    public static IntPredicate disjunctAllNoStream(List<IntPredicate> predicates) {
        IntPredicate disjunct = x -> false;
        for (IntPredicate currentPredicate : predicates) {
            disjunct = disjunct.or(currentPredicate);
        }
        return disjunct;
    }
}
