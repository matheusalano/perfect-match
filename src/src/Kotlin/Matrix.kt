@file:JvmName("Matrix")

import kotlin.math.max
import kotlin.math.min
import kotlin.math.round
import kotlin.system.exitProcess

class Matrix private constructor() {
    private var matrix: Array<Array<Element>> = emptyArray()
    var size = -1
    private set
    var registryOffices : ArrayList<RegistryOffice> = ArrayList()
    var agents : ArrayList<Agent> = ArrayList()
    private set

    companion object {
        var instance: Matrix = Matrix()
    }

    fun init(size: Int, officeNum: Int) {
        matrix = Array(size, {x -> Array(size, {y -> Element(ElementKind.GROUND, Position(x, y))}) })
        this.size = size
        addWallsAndOffices(officeNum)
        instance = this
    }

    private fun addWallsAndOffices(numOfOffices: Int) {
        val wallsNum = round((size.toDouble() / 5.0) - 0.6)
        val wallHeight = size/2
        val posFactor = round(size / (wallsNum + 2) + 0.5)
        val officesPerWall = numOfOffices / wallsNum

        for(i in 1..wallsNum.toInt()) {
            var firstCell = Util.rand(2, (size/2 - 2))
            val wallX = firstCell
            val wallY = posFactor * i
            for (j in 1..wallHeight) {
                matrix[firstCell][wallY.toInt()] = Element(ElementKind.WALL, Position(firstCell, wallY.toInt()))
                firstCell++
            }

            var officeCount = round(officesPerWall)

            if (i == 1) {
                if (officesPerWall > round(officesPerWall)) {officeCount++}
                else if (officesPerWall < round(officesPerWall)) {officeCount--}
            }

            for(k in 1..officeCount.toInt()) {
                var x: Int
                var y: Int

                do {
                    val officeLeft = Util.rand(0, 12) //0 - Left; 1 - Right
                    val officePosY = Util.rand(0, wallHeight)
                    y = if (officeLeft < 6) wallY.toInt() - 1 else wallY.toInt() + 1
                    x = (wallX + officePosY)
                } while(matrix[x][y].kind == ElementKind.REGISTRY_OFFICE)

                val office = RegistryOffice(Position(x, y))
                registryOffices.add(office)
                matrix[x][y] = office
            }
        }
    }

    fun addAgent(agent: Agent, pos: Position) {
        agent.state = AgentState.WALKING
        agent.newPartnerID = null
        agent.newPartnerKind = null
        agents.add(agent)
        matrix[pos.x][pos.y] = agent
    }

    fun removeAgent(agent: Agent) {
        for (i in agents.indices) {
            if (agents[i].id == agent.id && agents[i].kind == agent.kind) {
                val position = agents[i].position
                matrix[position.x][position.y] = Element(ElementKind.GROUND, Position(position.x, position.y))
                agents.removeAt(i)
                return
            }
        }
    }

    fun getAgentByID(agentID: Int, agentKind: ElementKind) : Agent? {
        for (i in agents.indices) {
            if (agents[i].id == agentID && agents[i].kind == agentKind) {
                return agents[i]
            }
        }
        return null
    }

    fun updateAgentStateByID(id: Int, kind: ElementKind, state: AgentState) {
        for (i in agents.indices) {
            if (agents[i].id == id && agents[i].kind == kind) {
                agents[i].state = state
                agents[i].newPartnerKind = null
                agents[i].newPartnerID = null
                return
            }
        }
    }

    fun getAvailablePosition() : Position {
        var x: Int
        var y: Int
        do {
            x = Util.rand(0, size - 1)
            y = Util.rand(0, size - 1)
        } while (matrix[x][y].kind != ElementKind.GROUND)
        return Position(x, y)
    }

    fun isAvailable(pos: Position) : Boolean {
        return matrix[pos.x][pos.y].kind == ElementKind.GROUND
    }

    fun updatePosition(element: Element, old: Position) {
        matrix[element.position.x][element.position.y] = element
        matrix[old.x][old.y] = Element(ElementKind.GROUND, Position(old.x, old.y))
    }

    fun getElementByPosition(pos: Position) : Element {
        return matrix[pos.x][pos.y]
    }

    fun updateOfficeStatus(position: Position, availability: Boolean) {
        for (i in registryOffices.indices) {
            if (registryOffices[i].position == position) {
                if (availability) {
                    if (registryOffices[i].couplesUsingIt == 0) {
                        println("Office has negative couples using it.")
                        exitProcess(1)
                    }
                    registryOffices[i].couplesUsingIt--
                } else {
                    registryOffices[i].couplesUsingIt++
                }
                return
            }
        }
    }

    fun getNearestOfficeFrom(position: Position) : RegistryOffice {
        val sortedOffices = registryOffices.sortedBy { Util.heuristic(it.position, position) }

        sortedOffices.forEach {
            if (it.isAvailable) {
                updateOfficeStatus(it.position, false)
                return it
            }
        }
        val office = registryOffices.sortedBy { it.couplesUsingIt }.first()
        updateOfficeStatus(office.position, false)
        return office
    }

    fun getNeighbors(position: Position, radius: Int = 1, onlyGround: Boolean = false) : Array<Element> {
        val neighbors: ArrayList<Element> = ArrayList()

        val rangeX = IntRange(max(position.x - radius, 0), min(position.x + radius, size - 1))
        val rangeY = IntRange(max(position.y - radius, 0), min(position.y + radius, size - 1))
        for (x in rangeX) {
            for (y in rangeY) {
                if (x == position.x && y == position.y) continue
                if (onlyGround && matrix[x][y].kind != ElementKind.GROUND) continue

                neighbors.add(matrix[x][y])
            }
        }
        return neighbors.toTypedArray()
    }

    fun printMatrix() {
        for(i in matrix.indices) {
            for (j in matrix.indices) {
                print(matrix[i][j].symbol)
                print(" ")
            }
            println("")
        }
    }
    
    fun printCouples() {
        println("Couples:")
        agents.forEach { agent ->
            if (agent is Couple) {
                println("Couple ${agent.id}: Husband ${agent.husband.symbol} and Wife ${agent.wife.symbol}")
            }
        }
    }
}