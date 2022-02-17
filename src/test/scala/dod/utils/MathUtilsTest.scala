package dod.utils

import org.scalatest.funsuite.AnyFunSuite

class MathUtilsTest extends AnyFunSuite {

    test("mod(-1,3) == 2") {
        assertResult(2)(MathUtils.mod(-1, 3))
    }

    test("mod(0,3) == 0") {
        assertResult(0)(MathUtils.mod(0, 3))
    }

    test("mod(1,3) == 1") {
        assertResult(1)(MathUtils.mod(1, 3))
    }

    test("mod(2,3) == 2") {
        assertResult(2)(MathUtils.mod(2, 3))
    }

    test("mod(3,3) == 0") {
        assertResult(0)(MathUtils.mod(3, 3))
    }

    test("mod(4,3) == 1") {
        assertResult(1)(MathUtils.mod(4, 3))
    }

    test("bound(0,1,3) == 1") {
        assertResult(1)(MathUtils.bound(0, 1, 3))
    }

    test("bound(1,1,3) == 1") {
        assertResult(1)(MathUtils.bound(1, 1, 3))
    }

    test("bound(2,1,3) == 2") {
        assertResult(2)(MathUtils.bound(2, 1, 3))
    }

    test("bound(3,1,3) == 3") {
        assertResult(3)(MathUtils.bound(3, 1, 3))
    }

    test("bound(4,1,3) == 3") {
        assertResult(3)(MathUtils.bound(4, 1, 3))
    }

    test("ceil(-0.5) == 0") {
        assertResult(0)(MathUtils.ceil(-0.5))
    }

    test("ceil(0) == 0") {
        assertResult(0)(MathUtils.ceil(0))
    }

    test("ceil(0.5) == 1") {
        assertResult(1)(MathUtils.ceil(0.5))
    }

    test("floor(-0.5) == -1") {
        assertResult(-1)(MathUtils.floor(-0.5))
    }

    test("floor(0) == 0") {
        assertResult(0)(MathUtils.floor(0))
    }

    test("floor(0.5) == 0") {
        assertResult(0)(MathUtils.floor(0.5))
    }

}
