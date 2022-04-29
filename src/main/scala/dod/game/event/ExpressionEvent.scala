package dod.game.event

import dod.game.expression.{Expr, StringExpr}

enum ExpressionEvent extends Event {
    case SetExpr(gameObjectId: StringExpr, exprName: StringExpr, expr: Expr[_])
    case SetCalculatedExpr(gameObjectId: StringExpr, exprName: StringExpr, expr: Expr[_])
    case RemoveExpr(gameObjectId: StringExpr, exprName: StringExpr)
    case RemoveAllExpr(gameObjectId: StringExpr)
}
