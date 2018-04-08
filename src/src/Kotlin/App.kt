@file:JvmName("App")

fun main(args: Array<String>) {
    println("Enter the matrix size:")
    val matrixSize = readLine()?.toInt()
    val coupleAndOffices = readLine()?.split( ' ')
    val numOfCouples = coupleAndOffices!![0].toInt()
    val numOfOffices = coupleAndOffices!![1].toInt()

    Matrix.instance.init(matrixSize!!, numOfOffices!!)

    for (i in 1..numOfCouples) { //MEN
        val input = readLine()?.split(' ')
        val inputInt = input!!.map {inpString -> inpString.toInt()}
        val agent = Agent(inputInt[0], ElementKind.MAN, Matrix.instance.getAvailablePosition())
        agent.matchPreference = inputInt.subList(1, 3).toTypedArray()
        Matrix.instance.addAgent(agent, agent.position)
    }

    for (i in 1..numOfCouples) { //WOMEN
        val input = readLine()?.split(' ')
        val inputInt = input!!.map {inpString -> inpString.toInt()}
        val agent = Agent(inputInt[0], ElementKind.WOMAN, Matrix.instance.getAvailablePosition())
        agent.matchPreference = inputInt.subList(1, 3).toTypedArray()
        Matrix.instance.addAgent(agent, agent.position)
    }

    Matrix.instance.printMatrix()

    var round = 0

    while (round < 100) {
        val agents = Matrix.instance.agents
        agents.forEach { agent ->
            agent.action()
        }
        round++
        Thread.sleep(1_000)
        val waitFor = ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor() //Clear windows CMD
        Matrix.instance.printMatrix()
    }
}