package dod.game.expression

enum BooleanExpr extends Expr[Boolean] {

    case Constant(value: Boolean)
    
    case Not(expr: BooleanExpr)
    case And(expr1: BooleanExpr, expr2: BooleanExpr)
    case Or(expr1: BooleanExpr, expr2: BooleanExpr)

    case Equals[T](expr1: Expr[T], expr2: Expr[T])
    case UnEquals[T](expr1: Expr[T], expr2: Expr[T])

    case Less[T](expr1: OrderedExpr[T], expr2: OrderedExpr[T])
    case LessEquals[T](expr1: OrderedExpr[T], expr2: OrderedExpr[T])
    case Greater[T](expr1: OrderedExpr[T], expr2: OrderedExpr[T])
    case GreaterEquals[T](expr1: OrderedExpr[T], expr2: OrderedExpr[T])

    
    def unary_! : BooleanExpr = Not(this)

    def &&(that: BooleanExpr): BooleanExpr = And(this, that)

    def ||(that: BooleanExpr): BooleanExpr = Or(this, that)

}
