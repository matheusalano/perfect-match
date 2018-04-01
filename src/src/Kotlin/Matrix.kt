import kotlin.math.round

class Matrix(size: Int, officeNum: Int) {
    val matrix: Array<Array<Element>> = Array(size, {_ -> Array(size, {_ -> Element(ElementKind.GROUND)}) })
    val size = size


    fun addWalls() {
        val wallsNum = round((size.toDouble() / 5.0) + 0.5)
        val wallSize = size/2
        val posFactor = round(size / (wallsNum + 2) + 0.5)

        for(i in 1..wallsNum.toInt()) {
            var firstCell = Util.rand(2, (size/2 - 2))
            for (j in 1..wallSize) {
                matrix[firstCell][(posFactor * i).toInt()] = Element(ElementKind.WALL)
                firstCell++
            }
        }
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