package dod.service.event

import dod.game.GameStage
import dod.game.event.ExpressionEvent
import dod.game.expression.*
import dod.game.gameobject.GameObjectRepository
import dod.game.gameobject.expressions.ExpressionsTransformer
import dod.game.model.Durations.Duration
import dod.game.model.Timestamps.Timestamp
import dod.game.model.{Coordinates, Direction, Shift, State}
import dod.service.event.EventService.*
import javafx.beans.binding.DoubleExpression

import scala.collection.immutable.Queue

private[event] final class ExpressionEventService {

    private[event] def processExpressionEvent(expressionEvent: ExpressionEvent)(using gameStage: GameStage): EventResponse = expressionEvent match {
        case ExpressionEvent.SetExpr(gameObjectId, exprName, expr) => (gameObjectId, exprName) ~> {
            (gameObjectId, exprName) => handleExpressionsUpdate(gameObjectId, ExpressionsTransformer.setExpr(exprName, expr))
        }

        case ExpressionEvent.SetCalculatedExpr(gameObjectId, exprName, expr) => (gameObjectId, exprName) ~> {
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

        case ExpressionEvent.RemoveExpr(gameObjectId, exprName) => (gameObjectId, exprName) ~> {
            (gameObjectId, exprName) => handleExpressionsUpdate(gameObjectId, ExpressionsTransformer.removeExpr(exprName))
        }

        case ExpressionEvent.RemoveAllExpr(gameObjectId) => gameObjectId ~> {
            gameObjectId => handleExpressionsUpdate(gameObjectId, ExpressionsTransformer.removeAllExpr)
        }
    }

    private inline def handleExpressionsUpdate(gameObjectId: String, expressionsTransformer: ExpressionsTransformer)(using gameStage: GameStage): EventResponse =
        gameStage.gameObjectRepository.findById(gameObjectId).fold(defaultResponse) { gameObject =>
            (gameStage.updateGameObjectRepository(gameStage.gameObjectRepository - gameObject + gameObject.updateExpressions(expressionsTransformer)), Queue.empty)
        }

}
