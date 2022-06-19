package dod.game.model

case class LightSource(isEnabled: Boolean, range: Int, directionOffset: Double, angularWidth: Double) {

    def enabled: LightSource = if isEnabled then this else copy(isEnabled = true)

    def disabled: LightSource = if isEnabled then copy(isEnabled = false) else this
}
