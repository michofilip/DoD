package dod.game.event

import dod.game.expression.{Expr, StringExpr}

enum ExpressionEvent extends Event {
    case SetExpression(gameObjectId: StringExpr, exprName: StringExpr, expr: Expr[_])
    case SetValue(gameObjectId: StringExpr, exprName: StringExpr, expr: Expr[_])
    case RemoveExpression(gameObjectId: StringExpr, exprName: StringExpr)
    case RemoveAllExpressions(gameObjectId: StringExpr)
}
