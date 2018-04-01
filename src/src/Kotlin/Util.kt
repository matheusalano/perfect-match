import java.util.Random

class Util {
    companion object {
        fun rand(from: Int, to: Int) : Int {
            val random = Random()
            return random.nextInt(to - from) + from
        }
    }
}