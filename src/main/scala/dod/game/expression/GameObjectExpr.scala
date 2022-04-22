package dod.game.expression


import dod.game.gameobject.GameObjectRepository
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Direction, State}
import dod.game.expression.Expr.ExprContext

import java.util.UUID

object GameObjectExpr {

    final case class GetName(id: UUID) extends StringExpr :
        override def get(using ExprContext): Option[String] =
            summon[ExprContext].asInstanceOf[GameObjectRepository].findById(id).map(_.name)

    final case class GetCreationTimestamp(id: UUID) extends TimestampExpr :
        override def get(using ExprContext): Option[Timestamp] =
            summon[ExprContext].asInstanceOf[GameObjectRepository].findById(id).map(_.creationTimestamp)


    final case class GetCoordinates(id: UUID) extends CoordinatesExpr :
        override def get(using ExprContext): Option[Coordinates] =
            summon[ExprContext].asInstanceOf[GameObjectRepository].findById(id).flatMap(_.position.coordinates)

    final case class GetDirection(id: UUID) extends DirectionExpr :
        override def get(using ExprContext): Option[Direction] =
            summon[ExprContext].asInstanceOf[GameObjectRepository].findById(id).flatMap(_.position.direction)

    final case class GetPositionTimestamp(id: UUID) extends TimestampExpr :
        override def get(using ExprContext): Option[Timestamp] =
            summon[ExprContext].asInstanceOf[GameObjectRepository].findById(id).flatMap(_.position.positionTimestamp)


    final case class GetState(id: UUID) extends StateExpr :
        override def get(using ExprContext): Option[State] =
            summon[ExprContext].asInstanceOf[GameObjectRepository].findById(id).flatMap(_.states.state)

    final case class GetStateTimestamp(id: UUID) extends TimestampExpr :
        override def get(using ExprContext): Option[Timestamp] =
            summon[ExprContext].asInstanceOf[GameObjectRepository].findById(id).flatMap(_.states.stateTimestamp)


    final case class GetSolid(id: UUID) extends BooleanExpr :
        override def get(using ExprContext): Option[Boolean] =
            summon[ExprContext].asInstanceOf[GameObjectRepository].findById(id).flatMap(_.physics).map(_.solid)

}
