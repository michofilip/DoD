package dod.service

import dod.game.expression.{BooleanExpr, Expr}
import dod.game.gameobject.GameObjectRepository
import org.scalatest.funsuite.AnyFunSuite

class ExpressionServiceBooleanExprTest extends AnyFunSuite {

    given GameObjectRepository = GameObjectRepository()
    private val expressionService = new ExpressionService

    test("BooleanExpr::Constant test") {
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(true)
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(false)
            }
        }
    }

    test("BooleanExpr::Not test") {
        assertResult(Some(false)) {
            expressionService.resolve {
                !Expr(true)
            }
        }
        assertResult(Some(true)) {
            expressionService.resolve {
                !Expr(false)
            }
        }
    }

    test("BooleanExpr::And test") {
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(true) && Expr(true)
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(true) && Expr(false)
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(false) && Expr(true)
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(false) && Expr(false)
            }
        }
    }

    test("BooleanExpr::Or test") {
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(true) || Expr(true)
            }
        }
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(true) || Expr(false)
            }
        }
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(false) || Expr(true)
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(false) || Expr(false)
            }
        }
    }

    test("BooleanExpr::Equals test") {
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(true) === Expr(true)
            }
        }
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(1) === Expr(1)
            }
        }
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(1.5) === Expr(1.5)
            }
        }
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr("str") === Expr("str")
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(false) === Expr(true)
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(0) === Expr(1)
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(0.5) === Expr(1.5)
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr("rts") === Expr("str")
            }
        }
    }

    test("BooleanExpr::UnEquals test") {
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(true) !== Expr(true)
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(1) !== Expr(1)
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(1.5) !== Expr(1.5)
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr("str") !== Expr("str")
            }
        }
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(false) !== Expr(true)
            }
        }
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(0) !== Expr(1)
            }
        }
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(0.5) !== Expr(1.5)
            }
        }
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr("rts") !== Expr("str")
            }
        }
    }

    test("BooleanExpr::Less test") {
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(0) < Expr(0)
            }
        }
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(0) < Expr(1)
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(1) < Expr(0)
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(1) < Expr(1)
            }
        }
    }

    test("LessEquals::Less test") {
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(0) <= Expr(0)
            }
        }
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(0) <= Expr(1)
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(1) <= Expr(0)
            }
        }
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(1) <= Expr(1)
            }
        }
    }

    test("BooleanExpr::Greater test") {
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(0) > Expr(0)
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(0) > Expr(1)
            }
        }
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(1) > Expr(0)
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(1) > Expr(1)
            }
        }
    }

    test("BooleanExpr::GreaterEquals test") {
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(0) >= Expr(0)
            }
        }
        assertResult(Some(false)) {
            expressionService.resolve {
                Expr(0) >= Expr(1)
            }
        }
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(1) >= Expr(0)
            }
        }
        assertResult(Some(true)) {
            expressionService.resolve {
                Expr(1) >= Expr(1)
            }
        }
    }

}
