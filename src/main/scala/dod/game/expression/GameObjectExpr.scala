package dod.game.expression


import dod.game.expression.ExprContext
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Durations.Duration
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Direction, Shift, State}

object GameObjectExpr {

    final case class GetName(id: String) extends StringExpr :
        override def get(using ExprContext): Option[String] =
            summon[ExprContext].gameStage.gameObjects.findById(id).map(_.name)

    final case class GetCreationTimestamp(id: String) extends TimestampExpr :
        override def get(using ExprContext): Option[Timestamp] =
            summon[ExprContext].gameStage.gameObjects.findById(id).map(_.creationTimestamp)


    final case class GetCoordinates(id: String) extends CoordinatesExpr :
        override def get(using ExprContext): Option[Coordinates] =
            summon[ExprContext].gameStage.gameObjects.findById(id).flatMap(_.position.coordinates)

    final case class GetDirection(id: String) extends DirectionExpr :
        override def get(using ExprContext): Option[Direction] =
            summon[ExprContext].gameStage.gameObjects.findById(id).flatMap(_.position.direction)

    final case class GetPositionTimestamp(id: String) extends TimestampExpr :
        override def get(using ExprContext): Option[Timestamp] =
            summon[ExprContext].gameStage.gameObjects.findById(id).flatMap(_.position.positionTimestamp)


    final case class GetState(id: String) extends StateExpr :
        override def get(using ExprContext): Option[State] =
            summon[ExprContext].gameStage.gameObjects.findById(id).flatMap(_.states.state)

    final case class GetStateTimestamp(id: String) extends TimestampExpr :
        override def get(using ExprContext): Option[Timestamp] =
            summon[ExprContext].gameStage.gameObjects.findById(id).flatMap(_.states.stateTimestamp)


    final case class GetSolid(id: String) extends BooleanExpr :
        override def get(using ExprContext): Option[Boolean] =
            summon[ExprContext].gameStage.gameObjects.findById(id).flatMap(_.physics).map(_.solid)


    final case class GetBooleanExpr(id: String, exprName: String) extends BooleanExpr :
        override def get(using ExprContext): Option[Boolean] =
            summon[ExprContext].gameStage.gameObjects.findById(id).flatMap(_.expressions.booleanExpr(exprName)).flatMap(_.get)

    final case class GetIntegerExpr(id: String, exprName: String) extends IntegerExpr :
        override def get(using ExprContext): Option[Int] =
            summon[ExprContext].gameStage.gameObjects.findById(id).flatMap(_.expressions.integerExpr(exprName)).flatMap(_.get)

    final case class GetDecimalExpr(id: String, exprName: String) extends DecimalExpr :
        override def get(using ExprContext): Option[Double] =
            summon[ExprContext].gameStage.gameObjects.findById(id).flatMap(_.expressions.decimalExpr(exprName)).flatMap(_.get)

    final case class GetStringExpr(id: String, exprName: String) extends StringExpr :
        override def get(using ExprContext): Option[String] =
            summon[ExprContext].gameStage.gameObjects.findById(id).flatMap(_.expressions.stringExpr(exprName)).flatMap(_.get)

    final case class GetTimestampExpr(id: String, exprName: String) extends TimestampExpr :
        override def get(using ExprContext): Option[Timestamp] =
            summon[ExprContext].gameStage.gameObjects.findById(id).flatMap(_.expressions.timestampExpr(exprName)).flatMap(_.get)

    final case class GetDurationExpr(id: String, exprName: String) extends DurationExpr :
        override def get(using ExprContext): Option[Duration] =
            summon[ExprContext].gameStage.gameObjects.findById(id).flatMap(_.expressions.durationExpr(exprName)).flatMap(_.get)

    final case class GetCoordinatesExpr(id: String, exprName: String) extends CoordinatesExpr :
        override def get(using ExprContext): Option[Coordinates] =
            summon[ExprContext].gameStage.gameObjects.findById(id).flatMap(_.expressions.coordinatesExpr(exprName)).flatMap(_.get)

    final case class GetShiftExpr(id: String, exprName: String) extends ShiftExpr :
        override def get(using ExprContext): Option[Shift] =
            summon[ExprContext].gameStage.gameObjects.findById(id).flatMap(_.expressions.shiftExpr(exprName)).flatMap(_.get)

    final case class GetDirectionExpr(id: String, exprName: String) extends DirectionExpr :
        override def get(using ExprContext): Option[Direction] =
            summon[ExprContext].gameStage.gameObjects.findById(id).flatMap(_.expressions.directionExpr(exprName)).flatMap(_.get)

    final case class GetStateExpr(id: String, exprName: String) extends StateExpr :
        override def get(using ExprContext): Option[State] =
            summon[ExprContext].gameStage.gameObjects.findById(id).flatMap(_.expressions.stateExpr(exprName)).flatMap(_.get)

}
