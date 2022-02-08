package dod.gameobject.position

import dod.gameobject.GameObject
import dod.gameobject.commons.CommonsProperty
import dod.gameobject.position.{Coordinates, Direction, PositionProperty, PositionTransformer, Shift}
import dod.temporal.Timestamps.Timestamp
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectPositionTest extends AnyFunSuite {

    private val commonsProperty = new CommonsProperty(id = UUID.randomUUID(), name = "TestGameObject", creationTimestamp = Timestamp(0))
    private val positionProperty = new PositionProperty(coordinates = Coordinates(0, 0), direction = Direction.North, positionTimestamp = Timestamp(0))
    private val gameObject = new GameObject(commonsProperty = commonsProperty, positionProperty = Some(positionProperty))

    test("GameObject::positionAccessor no PositionProperty test") {
        val gameObject = new GameObject(commonsProperty = commonsProperty)

        assertResult(None)(gameObject.positionAccessor.coordinates)
        assertResult(None)(gameObject.positionAccessor.direction)
        assertResult(None)(gameObject.positionAccessor.positionTimestamp)
    }

    test("GameObject::positionAccessor test") {
        assertResult(Some(Coordinates(0, 0)))(gameObject.positionAccessor.coordinates)
        assertResult(Some(Direction.North))(gameObject.positionAccessor.direction)
        assertResult(Some(Timestamp(0)))(gameObject.positionAccessor.positionTimestamp)
    }

    test("GameObject::updatePosition moveTo test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.moveTo(Coordinates(10, 20)), Timestamp(1000))

        assertResult(Some(Coordinates(10, 20)))(gameObject.positionAccessor.coordinates)
        assertResult(Some(Direction.North))(gameObject.positionAccessor.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionAccessor.positionTimestamp)
    }

    test("GameObject::updatePosition no changes test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.moveTo(Coordinates(0, 0)), Timestamp(1000))

        assertResult(Some(Coordinates(0, 0)))(gameObject.positionAccessor.coordinates)
        assertResult(Some(Direction.North))(gameObject.positionAccessor.direction)
        assertResult(Some(Timestamp(0)))(gameObject.positionAccessor.positionTimestamp)
    }

    test("GameObject::updatePosition moveBy test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.moveBy(Shift(10, 20)), Timestamp(1000))

        assertResult(Some(Coordinates(10, 20)))(gameObject.positionAccessor.coordinates)
        assertResult(Some(Direction.North))(gameObject.positionAccessor.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionAccessor.positionTimestamp)
    }

    test("GameObject::updatePosition turnTo test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.turnTo(Direction.East), Timestamp(1000))

        assertResult(Some(Coordinates(0, 0)))(gameObject.positionAccessor.coordinates)
        assertResult(Some(Direction.East))(gameObject.positionAccessor.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionAccessor.positionTimestamp)
    }

    test("GameObject::updatePosition turnClockwise test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.turnClockwise, Timestamp(1000))

        assertResult(Some(Coordinates(0, 0)))(gameObject.positionAccessor.coordinates)
        assertResult(Some(Direction.East))(gameObject.positionAccessor.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionAccessor.positionTimestamp)
    }

    test("GameObject::updatePosition turnCounterClockwise test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.turnCounterClockwise, Timestamp(1000))

        assertResult(Some(Coordinates(0, 0)))(gameObject.positionAccessor.coordinates)
        assertResult(Some(Direction.West))(gameObject.positionAccessor.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionAccessor.positionTimestamp)
    }

    test("GameObject::updatePosition turnBack test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.turnBack, Timestamp(1000))

        assertResult(Some(Coordinates(0, 0)))(gameObject.positionAccessor.coordinates)
        assertResult(Some(Direction.South))(gameObject.positionAccessor.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionAccessor.positionTimestamp)
    }

    test("GameObject::updatePosition stepForward test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepForward, Timestamp(1000))

        assertResult(Some(Coordinates(0, 1)))(gameObject.positionAccessor.coordinates)
        assertResult(Some(Direction.North))(gameObject.positionAccessor.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionAccessor.positionTimestamp)
    }

    test("GameObject::updatePosition stepRight test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepRight, Timestamp(1000))

        assertResult(Some(Coordinates(1, 0)))(gameObject.positionAccessor.coordinates)
        assertResult(Some(Direction.North))(gameObject.positionAccessor.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionAccessor.positionTimestamp)
    }

    test("GameObject::updatePosition stepLeft test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepLeft, Timestamp(1000))

        assertResult(Some(Coordinates(-1, 0)))(gameObject.positionAccessor.coordinates)
        assertResult(Some(Direction.North))(gameObject.positionAccessor.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionAccessor.positionTimestamp)
    }

    test("GameObject::updatePosition stepBack test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepBack, Timestamp(1000))

        assertResult(Some(Coordinates(0, -1)))(gameObject.positionAccessor.coordinates)
        assertResult(Some(Direction.North))(gameObject.positionAccessor.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionAccessor.positionTimestamp)
    }

    test("GameObject::updatePosition stepRightAndFace test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepRightAndFace, Timestamp(1000))

        assertResult(Some(Coordinates(1, 0)))(gameObject.positionAccessor.coordinates)
        assertResult(Some(Direction.East))(gameObject.positionAccessor.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionAccessor.positionTimestamp)
    }

    test("GameObject::stepLeftAndFace stepLeft test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepLeftAndFace, Timestamp(1000))

        assertResult(Some(Coordinates(-1, 0)))(gameObject.positionAccessor.coordinates)
        assertResult(Some(Direction.West))(gameObject.positionAccessor.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionAccessor.positionTimestamp)
    }

    test("GameObject::updatePosition stepBackAndFace test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepBackAndFace, Timestamp(1000))

        assertResult(Some(Coordinates(0, -1)))(gameObject.positionAccessor.coordinates)
        assertResult(Some(Direction.South))(gameObject.positionAccessor.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionAccessor.positionTimestamp)
    }
}
