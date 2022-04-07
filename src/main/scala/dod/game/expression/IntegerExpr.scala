package dod.game.expression

enum IntegerExpr extends OrderedExpr[Int] {

    case Constant(value: Int)

    case Negation(expr: IntegerExpr)
    case Addition(expr1: IntegerExpr, expr2: IntegerExpr)
    case Subtraction(expr1: IntegerExpr, expr2: IntegerExpr)
    case Multiplication(expr1: IntegerExpr, expr2: IntegerExpr)
    case Division(expr1: IntegerExpr, expr2: IntegerExpr)
    case Reminder(expr1: IntegerExpr, expr2: IntegerExpr)

    case DecimalToInteger(expr: DecimalExpr)


    override def compare(x: Int, y: Int): Int = java.lang.Integer.compare(x, y)

    def unary_+ : IntegerExpr = this

    def unary_- : IntegerExpr = Negation(this)

    def +(that: IntegerExpr): IntegerExpr = Addition(this, that)

    def +(that: DecimalExpr): DecimalExpr = DecimalExpr.Addition(this.toDecimalExpr, that)

    def -(that: IntegerExpr): IntegerExpr = Subtraction(this, that)

    def -(that: DecimalExpr): DecimalExpr = DecimalExpr.Subtraction(this.toDecimalExpr, that)

    def *(that: IntegerExpr): IntegerExpr = Multiplication(this, that)

    def *(that: DecimalExpr): DecimalExpr = DecimalExpr.Multiplication(this.toDecimalExpr, that)

    def /(that: IntegerExpr): IntegerExpr = Division(this, that)

    def /(that: DecimalExpr): DecimalExpr = DecimalExpr.Division(this.toDecimalExpr, that)

    def %(that: IntegerExpr): IntegerExpr = Reminder(this, that)

    def toIntegerExpr: IntegerExpr = this

    def toDecimalExpr: DecimalExpr = DecimalExpr.IntegerToDecimal(this)

    def toStringExpr: StringExpr = StringExpr.IntegerToString(this)

}
