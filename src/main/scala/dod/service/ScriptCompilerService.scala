package dod.service

import dod.game.event.Event
import dod.game.expression.{BooleanExpr, Expr}
import dod.game.model.Instruction.*
import dod.game.model.{Instruction, Script}
import dod.game.statement.Statement
import dod.game.statement.Statement.*

object ScriptCompilerService {

    private given[T]: Conversion[T, IndexedSeq[T]] = IndexedSeq(_)

    private type CompilerResponse = (IndexedSeq[Instruction], Int)

    def compile(statement: Statement): Script = {
        val (instructions, _) = compile(statement, IndexedSeq.empty, 0)
        Script(instructions :+ EXIT(0))
    }

    private def compile(statement: Statement, instructions: IndexedSeq[Instruction], labelId: Int): CompilerResponse = statement match {
        case Execute(events) =>
            compileExecute(events, instructions, labelId)

        case Block(statements) =>
            compileBlock(statements, instructions, labelId)

        case MultiWhenTherefore(whenThereforeSeq) =>
            compileWhen(whenThereforeSeq, Block(Seq.empty), instructions, labelId)

        case MultiWhenThereforeOtherwise(whenThereforeSeq, otherwiseStatement) =>
            compileWhen(whenThereforeSeq, otherwiseStatement, instructions, labelId)

        case LoopBody(condition, body) =>
            compileLoop(condition, body, instructions, labelId)

        case ChooseVariants(expression, variants) =>
            compileChoose(expression, variants, Block(Seq.empty), instructions, labelId)

        case ChooseVariantsOtherwise(expression, variants, otherwise) =>
            compileChoose(expression, variants, otherwise, instructions, labelId)
    }

    private inline def compileExecute(events: Seq[Event], instructions: IndexedSeq[Instruction], labelId: Int): CompilerResponse = {
        (instructions :+ EXECUTE(events), labelId)
    }

    private inline def compileBlock(statements: Seq[Statement], instructions: IndexedSeq[Instruction], labelId: Int): CompilerResponse = {
        val (newInstructions, afterBlockLabelId) = statements.foldLeft((instructions, labelId)) {
            case ((instructions, labelId), statement) => compile(statement, instructions, labelId)
        }

        (instructions ++ newInstructions, afterBlockLabelId)
    }

    private inline def compileWhen(whenThereforeSeq: IndexedSeq[WhenTherefore], otherwiseStatement: Statement, instructions: IndexedSeq[Instruction], labelId: Int): CompilerResponse = {
        val (newInstructions, afterWhenLabelId) = compileWhenThereforeSeq(whenThereforeSeq, otherwiseStatement, labelId)

        (instructions ++ newInstructions, afterWhenLabelId)
    }

    private def compileWhenThereforeSeq(whenThereforeSeq: IndexedSeq[WhenTherefore], otherwiseStatement: Statement, labelId: Int): CompilerResponse = whenThereforeSeq match {
        case WhenTherefore(condition, thereforeStatement) +: rest =>
            val elseLabelId = labelId
            val exitLabelId = labelId + 1

            val (thenInstructions, afterThenLabelId) = compile(thereforeStatement, IndexedSeq.empty, exitLabelId + 1)
            val (elseInstructions, afterElseLabelId) = compileWhenThereforeSeq(rest, otherwiseStatement, afterThenLabelId + 1)

            val newInstructions =
                TEST(condition) ++
                    GOTO(elseLabelId) ++
                    thenInstructions ++
                    GOTO(exitLabelId) ++
                    LABEL(elseLabelId) ++
                    elseInstructions ++
                    LABEL(exitLabelId)

            (newInstructions, afterElseLabelId)

        case _ =>
            compile(otherwiseStatement, IndexedSeq.empty, labelId)
    }

    private inline def compileLoop(condition: BooleanExpr, body: Statement, instructions: IndexedSeq[Instruction], labelId: Int): CompilerResponse = {
        val loopLabelId = labelId
        val exitLabelId = labelId + 1

        val (loopedInstructions, afterLoopLabelIdId) = compile(body, IndexedSeq.empty, exitLabelId + 1)

        val newInstructions =
            LABEL(loopLabelId) ++
                TEST(condition) ++
                GOTO(exitLabelId) ++
                loopedInstructions ++
                GOTO(loopLabelId) ++
                LABEL(exitLabelId)

        (instructions ++ newInstructions, afterLoopLabelIdId)
    }

    private inline def compileVariant[T](variantWhenTherefore: VariantWhenTherefore[T], chooseExpr: Expr[T], exitLabelId: Int, labelId: Int): CompilerResponse = {
        val variantExitLabelId = labelId
        val (variantInstructions, afterVariantLabelId) = compile(variantWhenTherefore.therefore, IndexedSeq.empty, variantExitLabelId + 1)

        val exprCondition = variantWhenTherefore.expressions
            .map(expression => chooseExpr === expression)
            .reduceLeft(_ || _)

        val condition = variantWhenTherefore.conditionOpt match {
            case Some(condition) => exprCondition && condition
            case None => exprCondition
        }

        val newInstructions =
            TEST(condition) ++
                GOTO(variantExitLabelId) ++
                variantInstructions ++
                GOTO(exitLabelId) ++
                LABEL(variantExitLabelId)

        (newInstructions, afterVariantLabelId)
    }

    private inline def compileChoose[T](expression: Expr[T], variants: Seq[VariantWhenTherefore[T]], otherwise: Statement, instructions: IndexedSeq[Instruction], labelId: Int): CompilerResponse = {
        val exitLabelId = labelId

        val (variantInstructions, afterVariantLabelId) = variants.foldLeft((IndexedSeq.empty[Instruction], exitLabelId + 1)) {
            case ((instructions, labelId), variant) =>
                val (newInstructions, afterVariantLabelId) = compileVariant(variant, expression, exitLabelId, labelId)
                (instructions ++ newInstructions, afterVariantLabelId)
        }

        val (otherwiseInstructions, afterOtherwiseLabelId) = compile(otherwise, IndexedSeq.empty, afterVariantLabelId)

        val newInstructions =
            variantInstructions ++
                otherwiseInstructions ++
                LABEL(exitLabelId)

        (instructions ++ newInstructions, afterOtherwiseLabelId)
    }
}
