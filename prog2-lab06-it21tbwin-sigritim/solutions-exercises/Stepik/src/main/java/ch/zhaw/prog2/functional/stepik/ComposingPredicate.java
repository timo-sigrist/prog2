package ch.zhaw.prog2.functional.stepik;

import java.util.List;
import java.util.function.IntPredicate;

public class ComposingPredicate {

    /**
     * The method represents a disjunct operator for a list of predicates.
     * For an empty list it returns the always false predicate.
     */
    public static IntPredicate disjunctAll(List<IntPredicate> predicates) {
        return predicates.stream().reduce(x -> false, (a, b) -> a.or(b));
    }

    /**
     * Using anyMatch to reduce compute time if possible
     */
    public static IntPredicate disjunctAllFaster(List<IntPredicate> predicates) {
        return i -> predicates.stream().anyMatch(p -> p.test(i));
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
