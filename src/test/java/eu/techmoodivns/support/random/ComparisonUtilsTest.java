package eu.techmoodivns.support.random;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ComparisonUtilsTest {

    @Test
    public void testNullable() {
        assertTrue(ComparisonUtils.same(null, null));
        assertFalse(ComparisonUtils.same(0, null));
        assertFalse(ComparisonUtils.same(0.0, null));
        assertFalse(ComparisonUtils.same(false, null));
        assertFalse(ComparisonUtils.same("", null));
        assertFalse(ComparisonUtils.same(LocalDateTime.of(1970, 1, 1, 0, 0, 0, 0), null));

        assertTrue(ComparisonUtils.greater(null, null, true));
        assertFalse(ComparisonUtils.greater(null, null, false));

        assertTrue(ComparisonUtils.less(null, null, true));
        assertFalse(ComparisonUtils.less(null, null, false));

        assertTrue(ComparisonUtils.greater(10, null, false));
        assertFalse(ComparisonUtils.greater(null, 10, false));

        assertTrue(ComparisonUtils.less(null, 10, false));
        assertFalse(ComparisonUtils.less(10, null, false));
    }

    @Test
    public void testSame() {
        assertTrue(ComparisonUtils.same("a", "a"));
        assertFalse(ComparisonUtils.same("a", "b"));

        assertTrue(ComparisonUtils.same(10, 10));
        assertFalse(ComparisonUtils.same(5, 10));

        assertTrue(ComparisonUtils.same(4.2, 4.2));
        assertFalse(ComparisonUtils.same(4.0, 4.2));

        assertTrue(ComparisonUtils.same(true, true));
        assertTrue(ComparisonUtils.same(false, false));
        assertFalse(ComparisonUtils.same(true, false));

        assertTrue(ComparisonUtils.same(
                LocalDateTime.of(2019, 2, 12, 12, 30, 3, 0),
                LocalDateTime.of(2019, 2, 12, 12, 30, 3, 0)));

        assertFalse(ComparisonUtils.same(
                LocalDateTime.of(2019, 2, 12, 12, 30, 3, 0),
                LocalDateTime.of(2019, 2, 12, 12, 30, 4, 0)));
    }

    @Test
    public void testGreater() {
        assertTrue(ComparisonUtils.greater(20, 10, false));
        assertTrue(ComparisonUtils.greater(20, 20, true));
        assertFalse(ComparisonUtils.greater(20, 20, false));
        assertFalse(ComparisonUtils.greater(10, 20, false));

        //

        assertTrue(ComparisonUtils.greater(4.2, 4.1, false));
        assertTrue(ComparisonUtils.greater(5.42, 5.42, true));
        assertFalse(ComparisonUtils.greater(5.42, 5.42, false));
        assertFalse(ComparisonUtils.greater(4.1, 4.2, false));

        //

        assertTrue(ComparisonUtils.greater(
                LocalDateTime.of(2019, 2, 12, 12, 30, 4, 0),
                LocalDateTime.of(2019, 2, 12, 12, 30, 3, 0), false));

        assertTrue(ComparisonUtils.greater(
                LocalDateTime.of(2019, 2, 12, 12, 30, 3, 0),
                LocalDateTime.of(2019, 2, 12, 12, 30, 3, 0), true));

        assertFalse(ComparisonUtils.greater(
                LocalDateTime.of(2019, 2, 12, 12, 30, 3, 0),
                LocalDateTime.of(2019, 2, 12, 12, 30, 3, 0), false));

        assertFalse(ComparisonUtils.greater(
                LocalDateTime.of(2019, 2, 12, 12, 30, 3, 0),
                LocalDateTime.of(2019, 2, 12, 12, 30, 4, 0), true));

        try {
            ComparisonUtils.greater("a", "b", false);
            fail();
        } catch (IllegalStateException ex) {
            assertEquals(String.class.getName() + " is not supported", ex.getMessage());
        }

        try {
            ComparisonUtils.greater(true, false, false);
            fail();
        } catch (IllegalStateException ex) {
            assertEquals(Boolean.class.getName() + " is not supported", ex.getMessage());
        }
    }

    @Test
    public void testLess() {
        assertFalse(ComparisonUtils.less(20, 10, false));
        assertTrue(ComparisonUtils.less(20, 20, true));
        assertFalse(ComparisonUtils.less(20, 20, false));
        assertTrue(ComparisonUtils.less(10, 20, false));

        //

        assertFalse(ComparisonUtils.less(4.2, 4.1, false));
        assertTrue(ComparisonUtils.less(5.42, 5.42, true));
        assertFalse(ComparisonUtils.less(5.42, 5.42, false));
        assertTrue(ComparisonUtils.less(4.1, 4.2, false));

        //

        assertFalse(ComparisonUtils.less(
                LocalDateTime.of(2019, 2, 12, 12, 30, 4, 0),
                LocalDateTime.of(2019, 2, 12, 12, 30, 3, 0), false));

        assertTrue(ComparisonUtils.less(
                LocalDateTime.of(2019, 2, 12, 12, 30, 3, 0),
                LocalDateTime.of(2019, 2, 12, 12, 30, 3, 0), true));

        assertFalse(ComparisonUtils.less(
                LocalDateTime.of(2019, 2, 12, 12, 30, 3, 0),
                LocalDateTime.of(2019, 2, 12, 12, 30, 3, 0), false));

        assertTrue(ComparisonUtils.less(
                LocalDateTime.of(2019, 2, 12, 12, 30, 3, 0),
                LocalDateTime.of(2019, 2, 12, 12, 30, 4, 0), true));

        //

        try {
            ComparisonUtils.less("a", "b", false);
            fail();
        } catch (IllegalStateException ex) {
            assertEquals(String.class.getName() + " is not supported", ex.getMessage());
        }

        try {
            ComparisonUtils.less(true, false, false);
            fail();
        } catch (IllegalStateException ex) {
            assertEquals(Boolean.class.getName() + " is not supported", ex.getMessage());
        }
    }

    @Test
    public void testSameType() {
        assertTrue(ComparisonUtils.sameType(null, "a"));
        assertTrue(ComparisonUtils.sameType("a", "a"));
        assertTrue(ComparisonUtils.sameType("a", null));

        assertFalse(ComparisonUtils.sameType(1, "1"));
        assertFalse(ComparisonUtils.sameType(false, 0));
    }
}
