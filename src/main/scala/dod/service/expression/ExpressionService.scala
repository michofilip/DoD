//package dod.service.expression
//
//import dod.game.expression.{BooleanExpr, CoordinatesExpr, DecimalExpr, DirectionExpr, Expr, GameObjectExpr, IntegerExpr, ShiftExpr, StateExpr, StringExpr, TimestampExpr}
//import dod.game.gameobject.GameObjectRepository
//import dod.game.model.Timestamps.Timestamp
//import dod.game.model.{Coordinates, Direction, Shift, State}
//
//import java.util.UUID
//import scala.math
//import scala.math.Ordered
//
//class ExpressionService {
//
//    def resolve[T](expr: Expr[T])(using gor: GameObjectRepository): Option[T] = expr match {
//        case GameObjectExpr.GetName(id) => gor.findById(id).map(_.name)
//        case GameObjectExpr.GetCreationTimestamp(id) => gor.findById(id).map(_.creationTimestamp)
//        case GameObjectExpr.GetCoordinates(id) => gor.findById(id).flatMap(_.position.coordinates)
//        case GameObjectExpr.GetDirection(id) => gor.findById(id).flatMap(_.position.direction)
//        case GameObjectExpr.GetPositionTimestamp(id) => gor.findById(id).flatMap(_.position.positionTimestamp)
//        case GameObjectExpr.GetState(id) => gor.findById(id).flatMap(_.states.state)
//        case GameObjectExpr.GetStateTimestamp(id) => gor.findById(id).flatMap(_.states.stateTimestamp)
//        case GameObjectExpr.GetSolid(id) => gor.findById(id).flatMap(_.physics).map(_.solid)
//
//        case expr: BooleanExpr => resolveBooleanExpr(expr)
//        case expr: IntegerExpr => resolveIntegerExpr(expr)
//        case expr: DecimalExpr => resolveDecimalExpr(expr)
//        case expr: StringExpr => resolveStringExpr(expr)
//        case expr: TimestampExpr => resolveTimestampExpr(expr)
//        case expr: CoordinatesExpr => resolveCoordinatesExpr(expr)
//        case expr: ShiftExpr => resolveShiftExpr(expr)
//        case expr: DirectionExpr => resolveDirectionExpr(expr)
//        case expr: StateExpr => resolveStateExpr(expr)
//
//        case _ => None
//    }
//
//    private inline def resolveBooleanExpr(expr: BooleanExpr)(using gor: GameObjectRepository): Option[Boolean] = expr match {
//        case BooleanExpr.Constant(value) => Some(value)
//        case BooleanExpr.Not(expr) => res1(expr) { x => Some(!x) }
//        case BooleanExpr.And(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x && y) }
//        case BooleanExpr.Or(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x || y) }
//        case BooleanExpr.Equals(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x == y) }
//        case BooleanExpr.UnEquals(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x != y) }
//        case BooleanExpr.Less(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(expr1.lt(x, y)) }
//        case BooleanExpr.LessEquals(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(expr1.lteq(x, y)) }
//        case BooleanExpr.Greater(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(expr1.gt(x, y)) }
//        case BooleanExpr.GreaterEquals(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(expr1.gteq(x, y)) }
//    }
//
//    private inline def resolveIntegerExpr(expr: IntegerExpr)(using gor: GameObjectRepository): Option[Int] = expr match {
//        case IntegerExpr.Constant(value) => Some(value)
//        case IntegerExpr.Negation(expr) => res1(expr) { x => Some(-x) }
//        case IntegerExpr.Addition(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x + y) }
//        case IntegerExpr.Subtraction(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x - y) }
//        case IntegerExpr.Multiplication(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x * y) }
//        case IntegerExpr.Division(expr1, expr2) => res2(expr1, expr2) { (x, y) => if y != 0 then Some(x / y) else None }
//        case IntegerExpr.Reminder(expr1, expr2) => res2(expr1, expr2) { (x, y) => if y != 0 then Some(x % y) else None }
//        case IntegerExpr.DecimalToInteger(expr) => res1(expr) { x => Some(x.toInt) }
//    }
//
//    private inline def resolveDecimalExpr(expr: DecimalExpr)(using gor: GameObjectRepository): Option[Double] = expr match {
//        case DecimalExpr.Constant(value) => Some(value)
//        case DecimalExpr.Negation(expr) => res1(expr) { x => Some(-x) }
//        case DecimalExpr.Addition(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x + y) }
//        case DecimalExpr.Subtraction(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x - y) }
//        case DecimalExpr.Multiplication(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x * y) }
//        case DecimalExpr.Division(expr1, expr2) => res2(expr1, expr2) { (x, y) => if y != 0 then Some(x / y) else None }
//        case DecimalExpr.IntegerToDecimal(expr) => res1(expr) { x => Some(x.toDouble) }
//    }
//
//    private inline def resolveStringExpr(expr: StringExpr)(using gor: GameObjectRepository): Option[String] = expr match {
//        case StringExpr.Constant(value) => Some(value)
//        case StringExpr.Concatenate(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x + y) }
//        case StringExpr.IntegerToString(expr) => res1(expr) { x => Some(x.toString) }
//        case StringExpr.DecimalToString(expr) => res1(expr) { x => Some(x.toString) }
//    }
//
//    private inline def resolveTimestampExpr(expr: TimestampExpr)(using gor: GameObjectRepository): Option[Timestamp] = expr match {
//        case TimestampExpr.Constant(value) => Some(value)
//    }
//
//    private inline def resolveCoordinatesExpr(expr: CoordinatesExpr)(using gor: GameObjectRepository): Option[Coordinates] = expr match {
//        case CoordinatesExpr.Constant(value) => Some(value)
//        case CoordinatesExpr.MoveBy(expr1, expr2) => res2(expr1, expr2) { (x, y) => Some(x.moveBy(y)) }
//    }
//
//    private inline def resolveShiftExpr(expr: ShiftExpr)(using gor: GameObjectRepository): Option[Shift] = expr match {
//        case ShiftExpr.Constant(value) => Some(value)
//    }
//
//    private inline def resolveDirectionExpr(expr: DirectionExpr)(using gor: GameObjectRepository): Option[Direction] = expr match {
//        case DirectionExpr.Constant(value) => Some(value)
//    }
//
//    private inline def resolveStateExpr(expr: StateExpr)(using gor: GameObjectRepository): Option[State] = expr match {
//        case StateExpr.Constant(value) => Some(value)
//    }
//
//
//    private def res1[V, R](e: Expr[V])(f: V => Option[R])(using gor: GameObjectRepository): Option[R] =
//        for (x <- resolve(e); r <- f(x)) yield r
//
//
//    private def res2[V1, V2, R](e1: Expr[V1], e2: Expr[V2])(f: (V1, V2) => Option[R])(using gor: GameObjectRepository): Option[R] =
//        for (x <- resolve(e1); y <- resolve(e2); r <- f(x, y)) yield r
//
//}
