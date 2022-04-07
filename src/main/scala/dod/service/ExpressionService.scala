package dod.service

import dod.game.expression.{BooleanExpr, DecimalExpr, Expr, IntegerExpr}

import scala.math
import scala.math.Ordered

class ExpressionService {

    def resolve[T](expr: Expr[T]): Option[T] = expr match {
        case expr: BooleanExpr => expr match {
            case BooleanExpr.Constant(expr) => Some(expr)
            case BooleanExpr.Not(expr) => res1(expr) { x => Some(!x) }
            case BooleanExpr.And(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x && y) }
            case BooleanExpr.Or(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x || y) }
            case BooleanExpr.Equals(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x == y) }
            case BooleanExpr.UnEquals(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x != y) }
            case BooleanExpr.Less(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(expr1.lt(x, y)) }
            case BooleanExpr.LessEquals(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(expr1.lteq(x, y)) }
            case BooleanExpr.Greater(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(expr1.gt(x, y)) }
            case BooleanExpr.GreaterEquals(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(expr1.gteq(x, y)) }
        }
        case expr: IntegerExpr => expr match {
            case IntegerExpr.Constant(expr) => Some(expr)
            case IntegerExpr.Negation(expr) => res1(expr) { x => Some(-x) }
            case IntegerExpr.Addition(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x + y) }
            case IntegerExpr.Subtraction(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x - y) }
            case IntegerExpr.Multiplication(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x * y) }
            case IntegerExpr.Division(expr1, expr2) => res2(expr1, expr2) { (x, y) => if y != 0 then Some(x / y) else None }
            case IntegerExpr.Reminder(expr1, expr2) => res2(expr1, expr2) { (x, y) => if y != 0 then Some(x % y) else None }
            case IntegerExpr.DecimalToInteger(expr) => res1(expr) { x => Some(x.toInt) }
        }
        case expr: DecimalExpr => expr match {
            case DecimalExpr.Constant(expr) => Some(expr)
            case DecimalExpr.Negation(expr) => res1(expr) { x => Some(-x) }
            case DecimalExpr.Addition(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x + y) }
            case DecimalExpr.Subtraction(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x - y) }
            case DecimalExpr.Multiplication(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x * y) }
            case DecimalExpr.Division(expr1, expr2) => res2(expr1, expr2) { (x, y) => if y != 0 then Some(x / y) else None }
            case DecimalExpr.IntegerToDecimal(expr) => res1(expr) { x => Some(x.toDouble) }
        }
    }

    private def res1[V, R](e: Expr[V])(f: V => Option[R]): Option[R] =
        for (x <- resolve(e); r <- f(x)) yield r

    private def res2[V1, V2, R](e1: Expr[V1], e2: Expr[V2])(f: (V1, V2) => Option[R]): Option[R] =
        for (x <- resolve(e1); y <- resolve(e2); r <- f(x, y)) yield r

}