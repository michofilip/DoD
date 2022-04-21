package dod.game.statement

import dod.game.event.Event
import dod.game.expression.{BooleanExpr, Expr}
import dod.game.statement.Statement.{Block, Choose, Execute, Loop, Variant, When}

object Statements {

    def execute(event: Event, events: Event*): Statement =
        Execute(event +: events)

    def block(statement: Statement, statements: Statement*): Statement =
        Block(statement +: statements)

    def when(condition: BooleanExpr): When =
        When(condition)

    def loop(condition: BooleanExpr): Loop =
        Loop(condition)

    def variant[T](expression: Expr[T], expressions: Expr[T]*): Variant[T] =
        Variant(expression +: expressions)

    def choose[T](expression: Expr[T]): Choose[T] =
        Choose(expression)

}
