import kotlin.math.round

class Matrix(size: Int, officeNum: Int) {
    val matrix: Array<Array<Element>> = Array(size, {_ -> Array(size, {_ -> Element(ElementKind.GROUND)}) })
    val size = size
    val numOfOffices = officeNum

    fun addWallsAndOffices() {
        val wallsNum = round((size.toDouble() / 5.0) + 0.5)
        val wallHeight = size/2
        val posFactor = round(size / (wallsNum + 2) + 0.5)
        val officesPerWall = numOfOffices / wallsNum
        var officesLeft = numOfOffices

        for(i in 1..wallsNum.toInt()) {
            var firstCell = Util.rand(2, (size/2 - 2))
            val wallX = firstCell
            val wallY = posFactor * i
            for (j in 1..wallHeight) {
                matrix[firstCell][wallY.toInt()] = Element(ElementKind.WALL)
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

                matrix[x][y] = Element(ElementKind.REGISTRY_OFFICE)
            }
        }
    }

    fun addElement(element: Element, pos: Position) {
        matrix[pos.x][pos.y] = element
    }

    fun getAvailablePosition() : Position {
        var x = 0
        var y = 0
        do {
            x = Util.rand(0, size - 1)
            y = Util.rand(0, size - 1)
        } while (matrix[x][y].kind != ElementKind.GROUND)
        return Position(x, y)
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