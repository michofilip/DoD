package dod.game.gameobject.expressions

import dod.game.expression.Expr

trait ExpressionsTransformer extends (Map[String, Expr[_]] => Map[String, Expr[_]])

object ExpressionsTransformer {

    def addExpr(exprName: String, expr: Expr[_]): ExpressionsTransformer = expressions => expressions + (exprName -> expr)

    def removeExpr(exprName: String): ExpressionsTransformer = expressions => expressions - exprName

    def removeAllExpr: ExpressionsTransformer = _ => Map.empty

}