package dod.game.expression

enum DecimalExpr extends OrderedExpr[Double] {

    case Constant(value: Double)

    case Negation(expr: DecimalExpr)
    case Addition(expr1: DecimalExpr, expr2: DecimalExpr)
    case Subtraction(expr1: DecimalExpr, expr2: DecimalExpr)
    case Multiplication(expr1: DecimalExpr, expr2: DecimalExpr)
    case Division(expr1: DecimalExpr, expr2: DecimalExpr)

    case IntegerToDecimal(expr: IntegerExpr)


    override def compare(x: Double, y: Double): Int = java.lang.Double.compare(x, y)

    def unary_+ : DecimalExpr = this

    def unary_- : DecimalExpr = Negation(this)

    def +(that: IntegerExpr): DecimalExpr = Addition(this, that.toDecimalExpr)

    def +(that: DecimalExpr): DecimalExpr = Addition(this, that)

    def -(that: IntegerExpr): DecimalExpr = Subtraction(this, that.toDecimalExpr)

    def -(that: DecimalExpr): DecimalExpr = Subtraction(this, that)

    def *(that: IntegerExpr): DecimalExpr = Multiplication(this, that.toDecimalExpr)

    def *(that: DecimalExpr): DecimalExpr = Multiplication(this, that)

    def /(that: IntegerExpr): DecimalExpr = Division(this, that.toDecimalExpr)

    def /(that: DecimalExpr): DecimalExpr = Division(this, that)

    def toIntegerExpr: IntegerExpr = IntegerExpr.DecimalToInteger(this)

    def toDecimalExpr: DecimalExpr = this

}
