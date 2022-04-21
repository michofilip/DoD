package dod.game.statement

import dod.game.event.Event
import dod.game.expression.{BooleanExpr, Expr}

sealed abstract class Statement

object Statement {

    final case class Execute(events: Seq[Event]) extends Statement

    final case class Block(statements: Seq[Statement]) extends Statement

    final case class When(condition: BooleanExpr) {
        def therefore(statement: Statement, statements: Statement*): MultiWhenTherefore =
            MultiWhenTherefore(Vector(WhenTherefore(condition, Block(statement +: statements))))
    }

    final case class WhenTherefore(condition: BooleanExpr, thereforeStatement: Statement)

    final case class MultiWhenTherefore(whenThereforeSeq: IndexedSeq[WhenTherefore]) extends Statement {
        def otherwiseWhen(condition: BooleanExpr): MultiWhenThereforeWhen =
            MultiWhenThereforeWhen(whenThereforeSeq, condition)

        def otherwise(statement: Statement, statements: Statement*): MultiWhenThereforeOtherwise =
            MultiWhenThereforeOtherwise(whenThereforeSeq, Block(statement +: statements))
    }

    final case class MultiWhenThereforeWhen(whenThereforeSeq: IndexedSeq[WhenTherefore], condition: BooleanExpr) {
        def therefore(statement: Statement, statements: Statement*): MultiWhenTherefore =
            MultiWhenTherefore(whenThereforeSeq :+ WhenTherefore(condition, Block(statement +: statements)))
    }

    final case class MultiWhenThereforeOtherwise(whenThereforeSeq: IndexedSeq[WhenTherefore], otherwiseStatement: Statement) extends Statement

    final case class Loop(condition: BooleanExpr) {
        def body(statement: Statement, statements: Statement*): LoopBody =
            LoopBody(condition, Block(statement +: statements))
    }

    final case class LoopBody(condition: BooleanExpr, body: Statement) extends Statement

    final case class Variant[T](expressions: Seq[Expr[T]]) {
        def when(condition: BooleanExpr): VariantWhen[T] =
            VariantWhen(expressions, Some(condition))

        def therefore(statement: Statement, statements: Statement*): VariantWhenTherefore[T] =
            VariantWhenTherefore(expressions, None, Block(statement +: statements))
    }

    final case class VariantWhen[T](expressions: Seq[Expr[T]], conditionOpt: Option[BooleanExpr]) {
        def therefore(statement: Statement, statements: Statement*): VariantWhenTherefore[T] =
            VariantWhenTherefore(expressions, conditionOpt, Block(statement +: statements))
    }

    final case class VariantWhenTherefore[T](expressions: Seq[Expr[T]], conditionOpt: Option[BooleanExpr], therefore: Statement)

    final case class Choose[T](expression: Expr[T]) {
        def from(variant: VariantWhenTherefore[T], variants: VariantWhenTherefore[T]*): ChooseVariants[T] =
            ChooseVariants(expression, variant +: variants)
    }

    final case class ChooseVariants[T](expression: Expr[T], variants: Seq[VariantWhenTherefore[T]]) extends Statement {
        def otherwise(statement: Statement, statements: Statement*): ChooseVariantsOtherwise[T] =
            ChooseVariantsOtherwise(expression, variants, Block(statement +: statements))
    }

    final case class ChooseVariantsOtherwise[T](expression: Expr[T], variants: Seq[VariantWhenTherefore[T]], otherwise: Statement) extends Statement

}
