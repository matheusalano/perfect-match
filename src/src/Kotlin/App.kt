@file:JvmName("App")

fun main(args: Array<String>) {
    println("Enter the matrix size:")
    val matrixSize = readLine()?.toInt()

    println("Enter the number of offices:")
    val numOfOffices = readLine()?.toInt()

    val matrix = Matrix(matrixSize!!, numOfOffices!!)

    matrix.addWallsAndOffices()
    matrix.printMatrix()
}