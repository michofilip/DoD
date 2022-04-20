package dod.game.statement

import dod.game.event.Event
import dod.game.expression2.Expr2
import dod.game.statement.Statement.{Block, Choose, Execute, Loop, Variant, When}

object Statements {

    def execute(event: Event, events: Event*): Statement =
        Execute(event +: events)

    def block(statement: Statement, statements: Statement*): Statement =
        Block(statement +: statements)

    def when(condition: Expr2[Boolean]): When =
        When(condition)

    def loop(condition: Expr2[Boolean]): Loop =
        Loop(condition)

    def variant[T](expression: Expr2[T], expressions: Expr2[T]*): Variant[T] =
        Variant(expression +: expressions)

    def choose[T](expression: Expr2[T]): Choose[T] =
        Choose(expression)

}
