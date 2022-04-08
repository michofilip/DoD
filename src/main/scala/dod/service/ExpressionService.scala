package dod.service

import dod.game.expression.{BooleanExpr, DecimalExpr, Expr, GameObjectExpr, IntegerExpr, StringExpr}
import dod.game.gameobject.GameObjectRepository

import scala.math
import scala.math.Ordered

class ExpressionService {

    def resolve[T](expr: Expr[T])(using gor: GameObjectRepository): Option[T] = expr match {
        case BooleanExpr.Constant(value) => Some(value)
        case BooleanExpr.Not(expr) => res1(expr) { x => Some(!x) }
        case BooleanExpr.And(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x && y) }
        case BooleanExpr.Or(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x || y) }
        case BooleanExpr.Equals(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x == y) }
        case BooleanExpr.UnEquals(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x != y) }
        case BooleanExpr.Less(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(expr1.lt(x, y)) }
        case BooleanExpr.LessEquals(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(expr1.lteq(x, y)) }
        case BooleanExpr.Greater(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(expr1.gt(x, y)) }
        case BooleanExpr.GreaterEquals(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(expr1.gteq(x, y)) }

        case IntegerExpr.Constant(value) => Some(value)
        case IntegerExpr.Negation(expr) => res1(expr) { x => Some(-x) }
        case IntegerExpr.Addition(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x + y) }
        case IntegerExpr.Subtraction(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x - y) }
        case IntegerExpr.Multiplication(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x * y) }
        case IntegerExpr.Division(expr1, expr2) => res2(expr1, expr2) { (x, y) => if y != 0 then Some(x / y) else None }
        case IntegerExpr.Reminder(expr1, expr2) => res2(expr1, expr2) { (x, y) => if y != 0 then Some(x % y) else None }
        case IntegerExpr.DecimalToInteger(expr) => res1(expr) { x => Some(x.toInt) }

        case DecimalExpr.Constant(value) => Some(value)
        case DecimalExpr.Negation(expr) => res1(expr) { x => Some(-x) }
        case DecimalExpr.Addition(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x + y) }
        case DecimalExpr.Subtraction(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x - y) }
        case DecimalExpr.Multiplication(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x * y) }
        case DecimalExpr.Division(expr1, expr2) => res2(expr1, expr2) { (x, y) => if y != 0 then Some(x / y) else None }
        case DecimalExpr.IntegerToDecimal(expr) => res1(expr) { x => Some(x.toDouble) }

        case StringExpr.Constant(value) => Some(value)
        case StringExpr.Concatenate(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x + y) }
        case StringExpr.IntegerToString(expr) => res1(expr) { x => Some(x.toString) }
        case StringExpr.DecimalToString(expr) => res1(expr) { x => Some(x.toString) }

        case GameObjectExpr.GetName(id) => gor.findById(id).map(_.name)

        case _ => None
    }


    private def res1[V, R](e: Expr[V])(f: V => Option[R])(using gor: GameObjectRepository): Option[R] =
        for (x <- resolve(e); r <- f(x)) yield r


    private def res2[V1, V2, R](e1: Expr[V1], e2: Expr[V2])(f: (V1, V2) => Option[R])(using gor: GameObjectRepository): Option[R] =
        for (x <- resolve(e1); y <- resolve(e2); r <- f(x, y)) yield r

}
