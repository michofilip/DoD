package dod

import dod.gameobject.GameObject
import dod.gameobject.position.{Coordinates, Direction, PositionData, PositionProperty, PositionTransformer}
import dod.temporal.Timestamps.Timestamp

import java.awt.Graphics
import scala.util.chaining.scalaUtilChainingOps

object Main {
    def main(args: Array[String]): Unit = {

        //        case class User(name: String, age: Int)
        //
        //        extension (maybeUser: Option[User]) {
        //            def name: Option[String] = maybeUser.map(_.name)
        //            def age: Option[Int] = maybeUser.map(_.age)
        //        }
        //
        //        Some(User("Ala", 30)).name.tap(println)
        //        Some(User("Ala", 30)).age.tap(println)
        //
        //        val user = User("Ala", 30)

        trait SuperUser {
            this: User =>

            protected val isSuper: Boolean

            def setSuper(isSuper: Boolean): User = {
                this.copy(isSuper = isSuper)
            }
        }

        case class User(private val name: String, private val age: Int, protected val isSuper: Boolean) extends SuperUser

        User(name = "Ala", age = 30, isSuper = false)
            .tap(println)
            .pipe(_.setSuper(true))
            .tap(println)
    }

}
