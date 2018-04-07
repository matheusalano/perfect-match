@file:JvmName("Matrix")

import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

class Matrix private constructor() {
    private var matrix: Array<Array<Element>> = emptyArray()
    private var size = -1
    var registryOffices : ArrayList<Element> = ArrayList()

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
        val wallsNum = round((size.toDouble() / 5.0) + 0.5)
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

                val office = Element(ElementKind.REGISTRY_OFFICE, Position(x, y))
                registryOffices.add(office)
                matrix[x][y] = office
            }
        }
    }

    fun addElement(element: Element, pos: Position) {
        matrix[pos.x][pos.y] = element
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
}