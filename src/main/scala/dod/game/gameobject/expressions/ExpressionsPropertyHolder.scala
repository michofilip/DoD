package dod.game.gameobject.expressions

import dod.game.expression.{BooleanExpr, CoordinatesExpr, DecimalExpr, DirectionExpr, DurationExpr, IntegerExpr, ShiftExpr, StateExpr, StringExpr, TimestampExpr}
import dod.game.gameobject.GameObject

trait ExpressionsPropertyHolder {
    self: GameObject =>

    protected val expressionsProperty: Option[ExpressionsProperty]

    final val expressions = new ExpressionsAccessor {
        override def booleanExpr(exprName: String): Option[BooleanExpr] = self.expressionsProperty.flatMap(_.expressions.get(exprName)).collect { case expr: BooleanExpr => expr }

        override def integerExpr(exprName: String): Option[IntegerExpr] = self.expressionsProperty.flatMap(_.expressions.get(exprName)).collect { case expr: IntegerExpr => expr }

        override def decimalExpr(exprName: String): Option[DecimalExpr] = self.expressionsProperty.flatMap(_.expressions.get(exprName)).collect { case expr: DecimalExpr => expr }

        override def stringExpr(exprName: String): Option[StringExpr] = self.expressionsProperty.flatMap(_.expressions.get(exprName)).collect { case expr: StringExpr => expr }

        override def timestampExpr(exprName: String): Option[TimestampExpr] = self.expressionsProperty.flatMap(_.expressions.get(exprName)).collect { case expr: TimestampExpr => expr }

        override def durationExpr(exprName: String): Option[DurationExpr] = self.expressionsProperty.flatMap(_.expressions.get(exprName)).collect { case expr: DurationExpr => expr }

        override def coordinatesExpr(exprName: String): Option[CoordinatesExpr] = self.expressionsProperty.flatMap(_.expressions.get(exprName)).collect { case expr: CoordinatesExpr => expr }

        override def shiftExpr(exprName: String): Option[ShiftExpr] = self.expressionsProperty.flatMap(_.expressions.get(exprName)).collect { case expr: ShiftExpr => expr }

        override def directionExpr(exprName: String): Option[DirectionExpr] = self.expressionsProperty.flatMap(_.expressions.get(exprName)).collect { case expr: DirectionExpr => expr }

        override def stateExpr(exprName: String): Option[StateExpr] = self.expressionsProperty.flatMap(_.expressions.get(exprName)).collect { case expr: StateExpr => expr }
    }

    final def updateExpressions(expressionsTransformer: ExpressionsTransformer): GameObject =
        update(expressionsProperty = self.expressionsProperty.map(_.updateExpressions(expressionsTransformer)))

    final def withExpressionsProperty(): GameObject =
        if (self.expressionsProperty.isEmpty)
            update(expressionsProperty = Some(ExpressionsProperty()))
        else this

}
