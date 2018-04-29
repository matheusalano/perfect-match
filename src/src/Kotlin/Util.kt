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

        fun getCouplePreference(agent1: Agent, agent2: Agent): Double? {
            if (agent1.kind == agent2.kind || agent1.kind == ElementKind.COUPLE || agent2.kind == ElementKind.COUPLE) return null
            return (agent1.matchPreference.indexOf(agent2.id) + agent2.matchPreference.indexOf(agent1.id)) / 2.0
        }

        fun registryOffice(agent1: Agent, agent2: Agent) {
            if (agent1 is Couple && agent2 is Couple) {
                val couple1 = Couple(agent1.husband, agent2.wife, agent1.position)
                val couple2 = Couple(agent2.husband, agent1.wife, agent2.position)

                Matrix.instance.removeAgent(agent1)
                Matrix.instance.removeAgent(agent2)
                Matrix.instance.addAgent(couple1, agent1.position)
                Matrix.instance.addAgent(couple2, agent2.position)
                if (verbose == 2) {
                    println("Couple ${agent1.symbol} and ${agent2.symbol} got divorced; Couple ${couple1.symbol} and ${couple2.symbol} got married")
                }
            } else if (agent1 is Couple && agent2.kind == ElementKind.MAN) {
                val couple = Couple(agent2, agent1.wife, agent2.position)

                Matrix.instance.removeAgent(agent1)
                agent1.husband.position = agent1.position
                agent1.husband.state = AgentState.WALKING
                Matrix.instance.addAgent(agent1.husband, agent1.position)
                Matrix.instance.removeAgent(agent2)
                Matrix.instance.addAgent(couple, agent2.position)
                if (verbose == 2) println("Couple ${agent1.symbol} got divorced. Couple ${couple.symbol} got married")
            } else if (agent1 is Couple && agent2.kind == ElementKind.WOMAN) {
                val couple = Couple(agent1.husband, agent2, agent2.position)

                Matrix.instance.removeAgent(agent1)
                agent1.wife.position = agent1.position
                agent1.wife.state = AgentState.WALKING
                Matrix.instance.addAgent(agent1.wife, agent1.position)
                Matrix.instance.removeAgent(agent2)
                Matrix.instance.addAgent(couple, agent2.position)
                if (verbose == 2) println("Couple ${agent1.symbol} got divorced. Couple ${couple.symbol} got married")
            } else if (agent2 is Couple && agent1.kind == ElementKind.MAN) {
                val couple = Couple(agent1, agent2.wife, agent1.position)

                Matrix.instance.removeAgent(agent2)
                agent2.husband.position = agent2.position
                agent2.husband.state = AgentState.WALKING
                Matrix.instance.addAgent(agent2.husband, agent2.position)
                Matrix.instance.removeAgent(agent1)
                Matrix.instance.addAgent(couple, agent1.position)
                if (verbose == 2) println("Couple ${agent2.symbol} got divorced. Couple ${couple.symbol} got married")
            } else if (agent2 is Couple && agent1.kind == ElementKind.WOMAN) {
                val couple = Couple(agent2.husband, agent1, agent1.position)

                Matrix.instance.removeAgent(agent2)
                agent2.wife.position = agent2.position
                agent2.wife.state = AgentState.WALKING
                Matrix.instance.addAgent(agent2.wife, agent2.position)
                Matrix.instance.removeAgent(agent1)
                Matrix.instance.addAgent(couple, agent1.position)
                if (verbose == 2) println("Couple ${agent2.symbol} got divorced. Couple ${couple.symbol} got married")
            } else {
                if (agent1.kind == ElementKind.MAN) {
                    val couple = Couple(agent1, agent2, agent1.position)
                    Matrix.instance.removeAgent(agent1)
                    Matrix.instance.removeAgent(agent2)
                    Matrix.instance.addAgent(couple, agent1.position)
                    if (verbose == 2) println("Couple ${couple.symbol} got married")
                } else {
                    val couple = Couple(agent2, agent1, agent2.position)
                    Matrix.instance.removeAgent(agent2)
                    Matrix.instance.removeAgent(agent1)
                    Matrix.instance.addAgent(couple, agent2.position)
                    if (verbose == 2) println("Couple ${couple.symbol} got married")
                }
            }
        }
    }
}