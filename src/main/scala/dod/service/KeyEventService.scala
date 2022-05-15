package dod.service

import dod.game.GameStage
import dod.game.event.{Event, PositionEvent}
import dod.game.expression.Expr
import dod.game.expression.Implicits.given
import dod.game.gameobject.GameObjectRepository
import dod.game.model.Direction
import scalafx.scene.input.{KeyCode, KeyEvent}

class KeyEventService {
    def processKeyEvent(gameStage: GameStage, keyEvent: KeyEvent): Seq[Event] = {
        gameStage.gameObjects.findById("player") match {
            case Some(player) => keyEvent.code match {
                case KeyCode.Numpad8 | KeyCode.Up => Seq(PositionEvent.StepAndFace(player.id, Direction.North))
                case KeyCode.Numpad6 | KeyCode.Right => Seq(PositionEvent.StepAndFace(player.id, Direction.East))
                case KeyCode.Numpad2 | KeyCode.Down => Seq(PositionEvent.StepAndFace(player.id, Direction.South))
                case KeyCode.Numpad4 | KeyCode.Left => Seq(PositionEvent.StepAndFace(player.id, Direction.West))
                case _ => Seq.empty
            }
            case None => Seq.empty
        }
    }
}
