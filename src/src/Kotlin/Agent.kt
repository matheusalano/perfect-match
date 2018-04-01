@file:JvmName("Agent")

import javafx.geometry.Pos
import kotlin.math.pow
import kotlin.math.sqrt

class Agent(agentID: Int, agentSex: Char, pos: Position) : Element(if(agentSex == 'M') ElementKind.MAN else ElementKind.WOMAN) {
    val id = agentID
    val sex = agentSex
    var position = pos
    var matchPreference: Array<Int> = emptyArray()


    fun aStar(neighbors: Array<Position>, goal: Position): ArrayList<Position> {
        val frontier = PriorityQueue<Position>()
        frontier.put(position, 0.0)
        val cameFrom: HashMap<Position, Position?> = HashMap()
        val costSoFar: HashMap<Position, Double> = HashMap()
        cameFrom[position] = null
        costSoFar[position] = 0.0

        while (!frontier.empty()) {
            val current = frontier.get()

            if (current == goal) break

            for(next in neighbors) { //Matrix.sharedInstance.getNeighbors(current)
                val newCost = (costSoFar[current] ?: 0.0) + 1

                if(!costSoFar.containsKey(next) || newCost < costSoFar[next]!!) {
                    costSoFar[next] = newCost
                    val prio = newCost + heuristic(goal, next)
                    frontier.put(next, prio)
                    cameFrom[next] = current
                }
            }
        }
        val path: ArrayList<Position> = ArrayList()
        var currPos: Position
        do {
            currPos = cameFrom[goal]!!
            path.add(currPos)
        } while (currPos != position)
        return path
    }

    private fun heuristic(goal: Position, current: Position) : Double {
        return sqrt((goal.x - current.x).toDouble().pow(2) + (goal.y - current.y).toDouble().pow(2))
    }
}