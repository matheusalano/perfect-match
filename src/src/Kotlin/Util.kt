import java.util.Random
import kotlin.math.pow
import kotlin.math.sqrt

class Util {
    companion object {
        private var currentCouple = 0

        fun rand(from: Int, to: Int) : Int {
            val random = Random()
            return random.nextInt(to - from) + from
        }

        fun heuristic(goal: Position, current: Position) : Double {
            return sqrt((goal.x - current.x).toDouble().pow(2) + (goal.y - current.y).toDouble().pow(2))
        }

        fun getCoupleIndex() : Int {
            currentCouple++
            return currentCouple
        }
    }
}