package dod.game.expression

enum StringExpr extends Expr[String] {

    case Constant(value: String)

    case Concatenate(expr1: StringExpr, expr2: StringExpr)

    case IntegerToString(expr: IntegerExpr)
    case DecimalToString(expr: DecimalExpr)


    def +(that: StringExpr): StringExpr = Concatenate(this, that)
}
