package dod.game.gameobject.expressions

import dod.game.expression.Expr

final class ExpressionsProperty(private[expressions] val expressions: Map[String, Expr[_]] = Map.empty) {

    def updateExpressions(expressionsTransformer: ExpressionsTransformer): ExpressionsProperty = {
        new ExpressionsProperty(expressionsTransformer(expressions))
    }
    
}
