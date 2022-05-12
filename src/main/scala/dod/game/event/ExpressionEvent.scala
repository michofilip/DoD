package dod.game.event

import dod.game.expression.{Expr, StringExpr}

enum ExpressionEvent extends Event {
    case SetExpression(gameObjectId: StringExpr, exprName: StringExpr, expr: Expr[_])
    case SetValue(gameObjectId: StringExpr, exprName: StringExpr, expr: Expr[_])
    case RemoveExpr(gameObjectId: StringExpr, exprName: StringExpr)
    case RemoveAllExpr(gameObjectId: StringExpr)
}
