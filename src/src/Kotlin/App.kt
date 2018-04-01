@file:JvmName("App")

fun main(args: Array<String>) {
    println("Enter the matrix size:")
    val matrixSize = readLine()?.toInt()

    val matrix = Matrix(matrixSize!!, 0)

    matrix.addWalls()
    matrix.printMatrix()
}