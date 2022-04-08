package dod.game.expression

import java.util.UUID

object GameObjectExpr {

    final case class GetName(id: UUID) extends StringExpr

}
