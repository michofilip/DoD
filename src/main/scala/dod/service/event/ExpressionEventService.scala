package dod.service.event

import dod.game.GameStage
import dod.game.event.ExpressionEvent
import dod.game.expression.*
import dod.game.gameobject.GameObjectRepository
import dod.game.gameobject.expressions.ExpressionsTransformer
import dod.game.model.{Coordinates, Direction, Duration, Shift, State, Timestamp}
import dod.service.event.EventService.*
import javafx.beans.binding.DoubleExpression

import scala.collection.immutable.Queue

private[event] final class ExpressionEventService {

    private[event] def processExpressionEvent(expressionEvent: ExpressionEvent)(using gameStage: GameStage): EventResponse = expressionEvent match {
        case ExpressionEvent.SetExpression(gameObjectId, exprName, expr) => (gameObjectId, exprName) ~> {
            (gameObjectId, exprName) => handleExpressionsUpdate(gameObjectId, ExpressionsTransformer.setExpr(exprName, expr))
        }

        case ExpressionEvent.SetValue(gameObjectId, exprName, expr) => (gameObjectId, exprName) ~> {
            (gameObjectId, exprName) => {
                val expressionsTransformer = expr match {
                    case expr: BooleanExpr => expr.get.fold(ExpressionsTransformer.removeExpr(exprName))(value => ExpressionsTransformer.setExpr(exprName, Expr(value)))
                    case expr: IntegerExpr => expr.get.fold(ExpressionsTransformer.removeExpr(exprName))(value => ExpressionsTransformer.setExpr(exprName, Expr(value)))
                    case expr: DecimalExpr => expr.get.fold(ExpressionsTransformer.removeExpr(exprName))(value => ExpressionsTransformer.setExpr(exprName, Expr(value)))
                    case expr: StringExpr => expr.get.fold(ExpressionsTransformer.removeExpr(exprName))(value => ExpressionsTransformer.setExpr(exprName, Expr(value)))
                    case expr: TimestampExpr => expr.get.fold(ExpressionsTransformer.removeExpr(exprName))(value => ExpressionsTransformer.setExpr(exprName, Expr(value)))
                    case expr: DurationExpr => expr.get.fold(ExpressionsTransformer.removeExpr(exprName))(value => ExpressionsTransformer.setExpr(exprName, Expr(value)))
                    case expr: CoordinatesExpr => expr.get.fold(ExpressionsTransformer.removeExpr(exprName))(value => ExpressionsTransformer.setExpr(exprName, Expr(value)))
                    case expr: ShiftExpr => expr.get.fold(ExpressionsTransformer.removeExpr(exprName))(value => ExpressionsTransformer.setExpr(exprName, Expr(value)))
                    case expr: DirectionExpr => expr.get.fold(ExpressionsTransformer.removeExpr(exprName))(value => ExpressionsTransformer.setExpr(exprName, Expr(value)))
                    case expr: StateExpr => expr.get.fold(ExpressionsTransformer.removeExpr(exprName))(value => ExpressionsTransformer.setExpr(exprName, Expr(value)))
                }

                handleExpressionsUpdate(gameObjectId, expressionsTransformer)
            }
        }

        case ExpressionEvent.RemoveExpression(gameObjectId, exprName) => (gameObjectId, exprName) ~> {
            (gameObjectId, exprName) => handleExpressionsUpdate(gameObjectId, ExpressionsTransformer.removeExpr(exprName))
        }

        case ExpressionEvent.RemoveAllExpressions(gameObjectId) => gameObjectId ~> {
            gameObjectId => handleExpressionsUpdate(gameObjectId, ExpressionsTransformer.removeAllExpr)
        }
    }

    private inline def handleExpressionsUpdate(gameObjectId: String, expressionsTransformer: ExpressionsTransformer)(using gameStage: GameStage): EventResponse =
        gameStage.gameObjects.findById(gameObjectId).fold(defaultResponse) { gameObject =>
            (gameStage.updateGameObjects(_ - gameObject + gameObject.updateExpressions(expressionsTransformer)), Queue.empty)
        }

}
