package dod.gameobject.position

import dod.gameobject.GameObject
import dod.gameobject.commons.CommonsProperty
import dod.gameobject.position.{Coordinates, Direction, PositionProperty, PositionTransformer, Shift}
import dod.temporal.Timestamps.Timestamp
import org.scalatest.funsuite.AnyFunSuite

import java.util.UUID

class GameObjectPositionTest extends AnyFunSuite {

    private val commonsProperty = new CommonsProperty(name = "TestGameObject", creationTimestamp = Timestamp(0))
    private val positionProperty = new PositionProperty(coordinates = Coordinates(0, 0), direction = Direction.North, positionTimestamp = Timestamp(0))
    private val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty, positionProperty = Some(positionProperty))

    test("GameObject::positionData no PositionProperty test") {
        val gameObject = new GameObject(id = UUID.randomUUID(), commonsProperty = commonsProperty)

        assertResult(None)(gameObject.positionData.coordinates)
        assertResult(None)(gameObject.positionData.direction)
        assertResult(None)(gameObject.positionData.positionTimestamp)
    }

    test("GameObject::positionData test") {
        assertResult(Some(Coordinates(0, 0)))(gameObject.positionData.coordinates)
        assertResult(Some(Direction.North))(gameObject.positionData.direction)
        assertResult(Some(Timestamp(0)))(gameObject.positionData.positionTimestamp)
    }

    test("GameObject::updatePosition moveTo test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.moveTo(Coordinates(10, 20)), Timestamp(1000))

        assertResult(Some(Coordinates(10, 20)))(gameObject.positionData.coordinates)
        assertResult(Some(Direction.North))(gameObject.positionData.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionData.positionTimestamp)
    }

    test("GameObject::updatePosition no changes test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.moveTo(Coordinates(0, 0)), Timestamp(1000))

        assertResult(Some(Coordinates(0, 0)))(gameObject.positionData.coordinates)
        assertResult(Some(Direction.North))(gameObject.positionData.direction)
        assertResult(Some(Timestamp(0)))(gameObject.positionData.positionTimestamp)
    }

    test("GameObject::updatePosition moveBy test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.moveBy(Shift(10, 20)), Timestamp(1000))

        assertResult(Some(Coordinates(10, 20)))(gameObject.positionData.coordinates)
        assertResult(Some(Direction.North))(gameObject.positionData.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionData.positionTimestamp)
    }

    test("GameObject::updatePosition turnTo test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.turnTo(Direction.East), Timestamp(1000))

        assertResult(Some(Coordinates(0, 0)))(gameObject.positionData.coordinates)
        assertResult(Some(Direction.East))(gameObject.positionData.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionData.positionTimestamp)
    }

    test("GameObject::updatePosition turnClockwise test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.turnClockwise, Timestamp(1000))

        assertResult(Some(Coordinates(0, 0)))(gameObject.positionData.coordinates)
        assertResult(Some(Direction.East))(gameObject.positionData.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionData.positionTimestamp)
    }

    test("GameObject::updatePosition turnCounterClockwise test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.turnCounterClockwise, Timestamp(1000))

        assertResult(Some(Coordinates(0, 0)))(gameObject.positionData.coordinates)
        assertResult(Some(Direction.West))(gameObject.positionData.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionData.positionTimestamp)
    }

    test("GameObject::updatePosition turnBack test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.turnBack, Timestamp(1000))

        assertResult(Some(Coordinates(0, 0)))(gameObject.positionData.coordinates)
        assertResult(Some(Direction.South))(gameObject.positionData.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionData.positionTimestamp)
    }

    test("GameObject::updatePosition stepForward test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepForward, Timestamp(1000))

        assertResult(Some(Coordinates(0, 1)))(gameObject.positionData.coordinates)
        assertResult(Some(Direction.North))(gameObject.positionData.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionData.positionTimestamp)
    }

    test("GameObject::updatePosition stepRight test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepRight, Timestamp(1000))

        assertResult(Some(Coordinates(1, 0)))(gameObject.positionData.coordinates)
        assertResult(Some(Direction.North))(gameObject.positionData.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionData.positionTimestamp)
    }

    test("GameObject::updatePosition stepLeft test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepLeft, Timestamp(1000))

        assertResult(Some(Coordinates(-1, 0)))(gameObject.positionData.coordinates)
        assertResult(Some(Direction.North))(gameObject.positionData.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionData.positionTimestamp)
    }

    test("GameObject::updatePosition stepBack test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepBack, Timestamp(1000))

        assertResult(Some(Coordinates(0, -1)))(gameObject.positionData.coordinates)
        assertResult(Some(Direction.North))(gameObject.positionData.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionData.positionTimestamp)
    }

    test("GameObject::updatePosition stepRightAndFace test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepRightAndFace, Timestamp(1000))

        assertResult(Some(Coordinates(1, 0)))(gameObject.positionData.coordinates)
        assertResult(Some(Direction.East))(gameObject.positionData.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionData.positionTimestamp)
    }

    test("GameObject::stepLeftAndFace stepLeft test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepLeftAndFace, Timestamp(1000))

        assertResult(Some(Coordinates(-1, 0)))(gameObject.positionData.coordinates)
        assertResult(Some(Direction.West))(gameObject.positionData.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionData.positionTimestamp)
    }

    test("GameObject::updatePosition stepBackAndFace test") {
        val gameObject = this.gameObject.updatePosition(PositionTransformer.stepBackAndFace, Timestamp(1000))

        assertResult(Some(Coordinates(0, -1)))(gameObject.positionData.coordinates)
        assertResult(Some(Direction.South))(gameObject.positionData.direction)
        assertResult(Some(Timestamp(1000)))(gameObject.positionData.positionTimestamp)
    }
}
