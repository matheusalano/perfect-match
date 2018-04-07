@file:JvmName("Agent")

class Agent(agentID: Int, agentSex: Char, pos: Position) : Element(if(agentSex == 'M') ElementKind.MAN else ElementKind.WOMAN, pos, "[$agentSex$agentID]") {
    val id = agentID
    val sex = agentSex
    var direction = Direction.EAST
    var matchPreference: Array<Int> = emptyArray()

    private fun nextPosition() : Position {
        return when (direction) {
            Direction.NORTH -> Position(this.position.x - 1, this.position.y)
            Direction.SOUTH -> Position(this.position.x + 1, this.position.y)
            Direction.WEST -> Position(this.position.x, this.position.y - 1)
            Direction.EAST -> Position(this.position.x, this.position.y + 1)
        }
    }

    fun walk() {
        var newPos = nextPosition()
        var done = false

        while (!done) {
            if (newPos.y > Matrix.instance.size - 1 || newPos.y < 0) {
                direction = if (Util.rand(0,10) < 9) direction.getOposite() else direction.turnRight()
                newPos = nextPosition()
            } else if (newPos.x > Matrix.instance.size - 1 || newPos.x < 0 || !Matrix.instance.isAvailable(newPos)) {
                direction = if (Util.rand(0,10) < 5) direction.turnRight() else direction.turnLeft()
                newPos = nextPosition()
            } else {
                done = true
            }
        }
        val oldPosition = position
        position = nextPosition()
        Matrix.instance.updatePosition(this, oldPosition)
    }



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
                    val prio = newCost + Util.heuristic(goal, next)
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
}