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

        fun registryOffice(agent1: Agent, agent2: Agent) {
            if (agent1 is Couple && agent2.kind == ElementKind.MAN) {
                val couple = Couple(agent2, agent1.wife, agent2.position)

                Matrix.instance.removeAgent(agent1.id, true)
                agent1.husband.position = agent1.position
                Matrix.instance.addAgent(agent1.husband, agent1.position)
                Matrix.instance.removeAgent(agent2.id, false)
                Matrix.instance.addAgent(couple, agent2.position)
            } else if (agent1 is Couple && agent2.kind == ElementKind.WOMAN) {
                val couple = Couple(agent1.husband, agent2, agent2.position)

                Matrix.instance.removeAgent(agent1.id, true)
                agent1.wife.position = agent1.position
                Matrix.instance.addAgent(agent1.wife, agent1.position)
                Matrix.instance.removeAgent(agent2.id, false)
                Matrix.instance.addAgent(couple, agent2.position)
            } else if (agent2 is Couple && agent1.kind == ElementKind.MAN) {
                val couple = Couple(agent1, agent2.wife, agent1.position)

                Matrix.instance.removeAgent(agent2.id, true)
                agent2.husband.position = agent2.position
                Matrix.instance.addAgent(agent2.husband, agent2.position)
                Matrix.instance.removeAgent(agent1.id, false)
                Matrix.instance.addAgent(couple, agent1.position)
            } else if (agent2 is Couple && agent2.kind == ElementKind.WOMAN) {
                val couple = Couple(agent2.husband, agent1, agent1.position)

                Matrix.instance.removeAgent(agent2.id, true)
                agent2.wife.position = agent2.position
                Matrix.instance.addAgent(agent2.wife, agent2.position)
                Matrix.instance.removeAgent(agent1.id, false)
                Matrix.instance.addAgent(couple, agent1.position)
            } else {
                if (agent1.kind == ElementKind.MAN) {
                    val couple = Couple(agent1, agent2, agent1.position)
                    Matrix.instance.removeAgent(agent1.id, false)
                    Matrix.instance.removeAgent(agent2.id, false)
                    Matrix.instance.addAgent(couple, agent1.position)
                } else {
                    val couple = Couple(agent2, agent1, agent2.position)
                    Matrix.instance.removeAgent(agent2.id, false)
                    Matrix.instance.removeAgent(agent1.id, false)
                    Matrix.instance.addAgent(couple, agent2.position)
                }
            }
        }
    }
}