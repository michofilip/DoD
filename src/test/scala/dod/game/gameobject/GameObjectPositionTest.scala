package dod.game.gameobject

import dod.game.gameobject.GameObject
import dod.game.gameobject.commons.CommonsProperty
import dod.game.gameobject.position.{Coordinates, Direction, Position, PositionProperty, PositionTransformer, Shift}
import dod.game.temporal.Timestamps.Timestamp
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectPositionTest extends AnyFunSuite {

    private val commonsProperty = CommonsProperty(id = UUID.randomUUID(), name = "TestGameObject", creationTimestamp = Timestamp.zero)
    private val positionProperty = PositionProperty(Position(coordinates = Coordinates(0, 0), direction = Direction.North), positionTimestamp = Timestamp.zero)
    private val gameObject = GameObject(commonsProperty = commonsProperty, positionProperty = Some(positionProperty))

    test("GameObject::positionAccessor no PositionProperty test") {
        val gameObject = GameObject(commonsProperty = commonsProperty)

        assertResult(None)(gameObject.position.coordinates)
        assertResult(None)(gameObject.position.direction)
        assertResult(None)(gameObject.position.positionTimestamp)
    }

    test("GameObject::positionAccessor test") {
        assertResult(Some(Coordinates(0, 0)))(gameObject.position.coordinates)
        assertResult(Some(Direction.North))(gameObject.position.direction)
        assertResult(Some(Timestamp.zero))(gameObject.position.positionTimestamp)
    }

    test("GameObject::updatePosition moveTo test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.moveTo(Coordinates(10, 20)), Timestamp(1000))

        assertResult(Some(Coordinates(10, 20)))(gameObject.position.coordinates)
        assertResult(Some(Direction.North))(gameObject.position.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.position.positionTimestamp)
    }

    test("GameObject::updatePosition no changes test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.moveTo(Coordinates(0, 0)), Timestamp(1000))

        assertResult(Some(Coordinates(0, 0)))(gameObject.position.coordinates)
        assertResult(Some(Direction.North))(gameObject.position.direction)
        assertResult(Some(Timestamp.zero))(gameObject.position.positionTimestamp)
    }

    test("GameObject::updatePosition moveBy test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.moveBy(Shift(10, 20)), Timestamp(1000))

        assertResult(Some(Coordinates(10, 20)))(gameObject.position.coordinates)
        assertResult(Some(Direction.North))(gameObject.position.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.position.positionTimestamp)
    }

    test("GameObject::updatePosition turnTo test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.turnTo(Direction.East), Timestamp(1000))

        assertResult(Some(Coordinates(0, 0)))(gameObject.position.coordinates)
        assertResult(Some(Direction.East))(gameObject.position.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.position.positionTimestamp)
    }

    test("GameObject::updatePosition turnClockwise test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.turnClockwise, Timestamp(1000))

        assertResult(Some(Coordinates(0, 0)))(gameObject.position.coordinates)
        assertResult(Some(Direction.East))(gameObject.position.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.position.positionTimestamp)
    }

    test("GameObject::updatePosition turnCounterClockwise test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.turnCounterClockwise, Timestamp(1000))

        assertResult(Some(Coordinates(0, 0)))(gameObject.position.coordinates)
        assertResult(Some(Direction.West))(gameObject.position.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.position.positionTimestamp)
    }

    test("GameObject::updatePosition turnBack test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.turnBack, Timestamp(1000))

        assertResult(Some(Coordinates(0, 0)))(gameObject.position.coordinates)
        assertResult(Some(Direction.South))(gameObject.position.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.position.positionTimestamp)
    }

    test("GameObject::updatePosition step test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.step(Direction.East), Timestamp(1000))

        assertResult(Some(Coordinates(1, 0)))(gameObject.position.coordinates)
        assertResult(Some(Direction.North))(gameObject.position.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.position.positionTimestamp)
    }

    test("GameObject::updatePosition stepForward test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepForward, Timestamp(1000))

        assertResult(Some(Coordinates(0, -1)))(gameObject.position.coordinates)
        assertResult(Some(Direction.North))(gameObject.position.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.position.positionTimestamp)
    }

    test("GameObject::updatePosition stepRight test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepRight, Timestamp(1000))

        assertResult(Some(Coordinates(1, 0)))(gameObject.position.coordinates)
        assertResult(Some(Direction.North))(gameObject.position.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.position.positionTimestamp)
    }

    test("GameObject::updatePosition stepLeft test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepLeft, Timestamp(1000))

        assertResult(Some(Coordinates(-1, 0)))(gameObject.position.coordinates)
        assertResult(Some(Direction.North))(gameObject.position.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.position.positionTimestamp)
    }

    test("GameObject::updatePosition stepBack test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepBack, Timestamp(1000))

        assertResult(Some(Coordinates(0, 1)))(gameObject.position.coordinates)
        assertResult(Some(Direction.North))(gameObject.position.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.position.positionTimestamp)
    }

    test("GameObject::updatePosition stepAndFace test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepAndFace(Direction.East), Timestamp(1000))

        assertResult(Some(Coordinates(1, 0)))(gameObject.position.coordinates)
        assertResult(Some(Direction.East))(gameObject.position.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.position.positionTimestamp)
    }

    test("GameObject::updatePosition stepRightAndFace test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepRightAndFace, Timestamp(1000))

        assertResult(Some(Coordinates(1, 0)))(gameObject.position.coordinates)
        assertResult(Some(Direction.East))(gameObject.position.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.position.positionTimestamp)
    }

    test("GameObject::stepLeftAndFace stepLeftAndFace test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepLeftAndFace, Timestamp(1000))

        assertResult(Some(Coordinates(-1, 0)))(gameObject.position.coordinates)
        assertResult(Some(Direction.West))(gameObject.position.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.position.positionTimestamp)
    }

    test("GameObject::updatePosition stepBackAndFace test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepBackAndFace, Timestamp(1000))

        assertResult(Some(Coordinates(0, 1)))(gameObject.position.coordinates)
        assertResult(Some(Direction.South))(gameObject.position.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.position.positionTimestamp)
    }
}
