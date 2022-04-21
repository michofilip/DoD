package dod.game.expression

import dod.game.expression.StringExpr.Concatenate
import dod.game.gameobject.GameObjectRepository

abstract class StringExpr extends Expr[String] {

    final def +(that: StringExpr): StringExpr = Concatenate(this, that)

}

object StringExpr {

    final case class Constant(value: String) extends StringExpr :
        override def get(using GameObjectRepository): Option[String] = Some(value)

    final case class Concatenate(expr1: StringExpr, expr2: StringExpr) extends StringExpr :
        override def get(using GameObjectRepository): Option[String] = (expr1, expr2) ~> ((x, y) => Some(x + y))

    final case class IntegerToString(expr: IntegerExpr) extends StringExpr :
        override def get(using GameObjectRepository): Option[String] = expr ~> (x => Some(x.toString))

    final case class DecimalToString(expr: DecimalExpr) extends StringExpr :
        override def get(using GameObjectRepository): Option[String] = expr ~> (x => Some(x.toString))

}
