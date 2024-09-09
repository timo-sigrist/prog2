package ch.zhaw.prog2.functional.stepik;

import ch.zhaw.prog2.functional.stepik.ComposingPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Do not modify this test class
 */
class ComposingPredicateTest {
    private static final IntPredicate isEven = x -> x % 2 == 0;
    private static final IntPredicate isDividableBy3 = x -> x % 3 == 0;
    private static final List<IntPredicate> predicateList = List.of(isEven, isDividableBy3);
    private List<Integer> expected;
    private IntStream testIntegers;

    /*
     * This tests your solution
     */
    @Test
    @Disabled("This exercise is not mandatory. Enable it, if you solve this exercise.")
    void disjunctAll() {
        assertDoesNotThrow(
            () -> ComposingPredicate.disjunctAll(List.of(x -> true)),
            "You have to implement ComposingPredicate.disjunctAll"
        );
        IntPredicate alwaysTrue = ComposingPredicate.disjunctAll(List.of(x -> true));
        assertTrue(alwaysTrue.test(1), "Test with one predicate only");

        IntPredicate dividableBy2Or3 = ComposingPredicate.disjunctAll(predicateList);
        assertArrayEquals(expected.toArray(), testIntegers.filter(dividableBy2Or3).boxed().toArray());
    }

    /*
     * This tests the given classical solution without streams
     */
    @Test
    void disjunctAllNoStream() {
        IntPredicate alwaysTrue = ComposingPredicate.disjunctAllNoStream(List.of(x -> true));
        assertTrue(alwaysTrue.test(1), "Test with one predicate only");

        IntPredicate dividableBy2Or3 = ComposingPredicate.disjunctAllNoStream(predicateList);
        assertArrayEquals(expected.toArray(), testIntegers.filter(dividableBy2Or3).boxed().toArray());
    }

    @BeforeEach
    void setUp() {
        testIntegers = IntStream.range(1, 10);
        expected = List.of(2, 3, 4, 6, 8, 9);
    }
}
