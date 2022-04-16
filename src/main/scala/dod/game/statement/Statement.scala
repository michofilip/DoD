package dod.game.statement

import dod.game.event.Event
import dod.game.expression.{BooleanExpr, Expr}

sealed abstract class Statement

object Statement {

    def execute(events: Event*): Statement =
        Execute(events)

    def block(statements: Statement*): Statement =
        Block(statements)

    def when(condition: BooleanExpr): When =
        When(condition)

    def loop(condition: BooleanExpr): Loop =
        Loop(condition)

    def variant(expression: Expr[_], expressions: Expr[_]*): Variant =
        Variant(expression +: expressions)

    def choose(expression: Expr[_]): Choose =
        Choose(expression)


    final case class Execute(events: Seq[Event]) extends Statement

    final case class Block(statements: Seq[Statement]) extends Statement

    final case class When(condition: BooleanExpr) {
        def therefore(statements: Statement*): MultiWhenTherefore =
            MultiWhenTherefore(Vector(WhenTherefore(condition, Block(statements))))
    }

    final case class WhenTherefore(condition: BooleanExpr, thereforeStatement: Statement)

    final case class MultiWhenTherefore(whenThereforeSeq: IndexedSeq[WhenTherefore]) extends Statement {
        def otherwiseWhen(condition: BooleanExpr): MultiWhenThereforeWhen =
            MultiWhenThereforeWhen(whenThereforeSeq, condition)

        def otherwise(statements: Statement*): MultiWhenThereforeOtherwise =
            MultiWhenThereforeOtherwise(whenThereforeSeq, Block(statements))
    }

    final case class MultiWhenThereforeWhen(whenThereforeSeq: IndexedSeq[WhenTherefore], condition: BooleanExpr) {
        def therefore(statements: Statement*): MultiWhenTherefore =
            MultiWhenTherefore(whenThereforeSeq :+ WhenTherefore(condition, Block(statements)))
    }

    final case class MultiWhenThereforeOtherwise(whenThereforeSeq: IndexedSeq[WhenTherefore], otherwiseStatement: Statement) extends Statement

    final case class Loop(condition: BooleanExpr) {
        def body(statements: Statement*): LoopBody =
            LoopBody(condition, Block(statements))
    }

    final case class LoopBody(condition: BooleanExpr, body: Statement) extends Statement

    final case class Variant(expressions: Seq[Expr[_]]) {
        def when(condition: BooleanExpr): VariantWhen =
            VariantWhen(expressions, Some(condition))

        def therefore(statements: Statement*): VariantWhenTherefore =
            VariantWhenTherefore(expressions, None, Block(statements))
    }

    final case class VariantWhen(expressions: Seq[Expr[_]], conditionOpt: Option[BooleanExpr]) {
        def therefore(statements: Statement*): VariantWhenTherefore =
            VariantWhenTherefore(expressions, conditionOpt, Block(statements))
    }

    final case class VariantWhenTherefore(expressions: Seq[Expr[_]], conditionOpt: Option[BooleanExpr], therefore: Statement)

    final case class Choose(expression: Expr[_]) {
        def from(variant: VariantWhenTherefore, variants: VariantWhenTherefore*): ChooseVariants =
            ChooseVariants(expression, variant +: variants)
    }

    final case class ChooseVariants(expression: Expr[_], variants: Seq[VariantWhenTherefore]) extends Statement {
        def otherwise(statements: Statement*): ChooseVariantsOtherwise =
            ChooseVariantsOtherwise(expression, variants, Block(statements))
    }

    final case class ChooseVariantsOtherwise(expression: Expr[_], variants: Seq[VariantWhenTherefore], otherwise: Statement) extends Statement

}