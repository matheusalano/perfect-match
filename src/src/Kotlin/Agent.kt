@file:JvmName("Agent")

open class Agent(agentID: Int?, agentSex: ElementKind, pos: Position) : Element(agentSex, pos, "[${agentSex.symbol}${agentID ?: ""}]"){
    val id = agentID
    private var direction = Direction.EAST
    var matchPreference: Array<Int> = emptyArray()
    var state = AgentState.WALKING
    internal var officePath: ArrayList<Position> = ArrayList()
    private val oppositeSex = if (kind == ElementKind.MAN) ElementKind.WOMAN else ElementKind.MAN
    internal open var newPartnerID: Int? = null
    internal open var newPartnerKind: ElementKind? = null

    private fun nextPosition() : Position {
        return when (direction) {
            Direction.NORTH -> Position(this.position.x - 1, this.position.y)
            Direction.SOUTH -> Position(this.position.x + 1, this.position.y)
            Direction.WEST -> Position(this.position.x, this.position.y - 1)
            Direction.EAST -> Position(this.position.x, this.position.y + 1)
        }
    }

    open fun action() {
        if (state == AgentState.WALKING) {
            if (!checkNeighborhood()) walk()
        } else if (state == AgentState.GOING_TO_OFFICE) {
            if (officePath.isEmpty()) state = AgentState.AT_OFFICE
            else if (Matrix.instance.isAvailable(officePath[0])) {
                val oldPosition = position
                position = officePath.removeAt(0)
                Matrix.instance.updatePosition(this, oldPosition)
            }
        }
    }

    internal fun walk() {
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

    private fun checkNeighborhood() : Boolean {
        val neighbors = Matrix.instance.getNeighbors(position, 2, false).filter {it.kind == ElementKind.COUPLE || it.kind == oppositeSex}
        val sortedNeighbors = neighbors.map { it as Agent }.sortedBy {
            if (it is Couple) {
                if (oppositeSex == ElementKind.WOMAN) matchPreference.indexOf(it.wife.id)
                else matchPreference.indexOf(it.husband.id)
            } else {
                matchPreference.indexOf(it.id)
            }
        }

        sortedNeighbors.forEach { agent  ->
            if (agent.receiveProposalFrom(this)) {
                newPartnerID = agent.id
                newPartnerKind = agent.kind
                val office = Matrix.instance.getNearestOfficeFrom(this.position)
                aStar(office.position)
                return true
            }
        }
        return false
    }

    open fun receiveProposalFrom(agent: Agent) : Boolean {
        if (newPartnerID != null) {
            var betterPartnerFound = false
            if (newPartnerKind == ElementKind.COUPLE) {
                if (oppositeSex == ElementKind.MAN) {
                    if (matchPreference.indexOf(agent.id) < matchPreference.indexOf(Matrix.instance.getCoupleByID(newPartnerID!!)!!.husband.id)) betterPartnerFound = true
                } else {
                    if (matchPreference.indexOf(agent.id) < matchPreference.indexOf(Matrix.instance.getCoupleByID(newPartnerID!!)!!.wife.id)) betterPartnerFound = true
                }
            } else if (matchPreference.indexOf(agent.id) < matchPreference.indexOf(newPartnerID)) betterPartnerFound = true

            if (betterPartnerFound) {
                Matrix.instance.updateAgentStateByID(newPartnerID!!, AgentState.WALKING, newPartnerKind == ElementKind.COUPLE)
                newPartnerID = agent.id
                newPartnerKind = agent.kind
                val office = Matrix.instance.getNearestOfficeFrom(agent.position)
                aStar(office.position)
                return true
            }
        } else {
            newPartnerID = agent.id
            newPartnerKind = agent.kind
            val office = Matrix.instance.getNearestOfficeFrom(agent.position)
            aStar(office.position)
            return true
        }
        return false
    }

    fun aStar(goal: Position) {
        if (state == AgentState.WALKING) {
            state = AgentState.GOING_TO_OFFICE
            officePath = aStarPath(goal)
        }
    }

    private fun aStarPath(goal: Position): ArrayList<Position> {
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