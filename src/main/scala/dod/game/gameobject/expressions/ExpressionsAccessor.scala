package dod.game.gameobject.expressions

import dod.game.expression.*

trait ExpressionsAccessor {
    def booleanExpr(exprName: String): Option[BooleanExpr]

    def integerExpr(exprName: String): Option[IntegerExpr]

    def decimalExpr(exprName: String): Option[DecimalExpr]

    def stringExpr(exprName: String): Option[StringExpr]

    def timestampExpr(exprName: String): Option[TimestampExpr]

    def durationExpr(exprName: String): Option[DurationExpr]

    def coordinatesExpr(exprName: String): Option[CoordinatesExpr]

    def shiftExpr(exprName: String): Option[ShiftExpr]

    def directionExpr(exprName: String): Option[DirectionExpr]

    def stateExpr(exprName: String): Option[StateExpr]
}
