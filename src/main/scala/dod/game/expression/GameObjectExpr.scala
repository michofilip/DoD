package dod.game.expression


import java.util.UUID

object GameObjectExpr {

    final case class GetName(id: UUID) extends StringExpr

    final case class GetCreationTimestamp(id: UUID) extends TimestampExpr


    final case class GetCoordinates(id: UUID) extends CoordinatesExpr

    final case class GetDirection(id: UUID) extends DirectionExpr

    final case class GetPositionTimestamp(id: UUID) extends TimestampExpr


    final case class GetState(id: UUID) extends StateExpr

    final case class GetStateTimestamp(id: UUID) extends TimestampExpr

    
    final case class GetSolid(id: UUID) extends BooleanExpr

}
