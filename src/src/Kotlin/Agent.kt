@file:JvmName("Agent")

import kotlin.math.pow
import kotlin.math.sqrt

class Agent(agentID: Int, agentSex: Char, pos: Position) : Element(if(agentSex == 'M') ElementKind.MAN else ElementKind.WOMAN, pos, "[$agentSex$agentID]") {
    val id = agentID
    val sex = agentSex
    var matchPreference: Array<Int> = emptyArray()


    fun aStar(goal: Position): ArrayList<Position> {
        val frontier = PriorityQueue<Position>()
        frontier.put(position, 0.0)
        val cameFrom: HashMap<Position, Position?> = HashMap()
        val costSoFar: HashMap<Position, Double> = HashMap()
        cameFrom[position] = null
        costSoFar[position] = 0.0

        while (!frontier.empty()) {
            val current = frontier.get()

            if (current == goal) break

            Matrix.instance.getNeighbors(current).forEach { nextElement ->
                if (nextElement.position != goal && nextElement.kind != ElementKind.GROUND) return@forEach
                val next = nextElement.position
                val newCost = (costSoFar[current]!!) + 1

                if(!costSoFar.containsKey(next) || newCost < costSoFar[next]!!) {
                    costSoFar[next] = newCost
                    val prio = newCost + heuristic(goal, next)
                    frontier.put(next, prio)
                    cameFrom[next] = current
                }
            }
        }
        val path: ArrayList<Position> = ArrayList()
        var currPos = goal
        do {
            currPos = cameFrom[currPos]!!
            path.add(currPos)
        } while (currPos != position)
        return path
    }

    private fun heuristic(goal: Position, current: Position) : Double {
        return sqrt((goal.x - current.x).toDouble().pow(2) + (goal.y - current.y).toDouble().pow(2))
    }
}