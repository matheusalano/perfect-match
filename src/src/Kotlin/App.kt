@file:JvmName("App")

fun main(args: Array<String>) {
    println("Enter the matrix size:")
    val matrixSize = readLine()?.toInt()
    val coupleAndOffices = readLine()?.split( ' ')
    val numOfCouples = coupleAndOffices!![0].toInt()
    val numOfOffices = coupleAndOffices!![1].toInt()

    val agents: ArrayList<Agent> = ArrayList()

    Matrix.instance.init(matrixSize!!, numOfOffices!!)

    for (i in 1..numOfCouples) { //MEN
        val input = readLine()?.split(' ')
        val inputInt = input!!.map {inpString -> inpString.toInt()}
        val agent = Agent(inputInt[0], 'M', Matrix.instance.getAvailablePosition())
        agent.matchPreference = inputInt.subList(1, 3).toTypedArray()
        agents.add(agent)
        Matrix.instance.addElement(agent, agent.position)
    }

    for (i in 1..numOfCouples) { //WOMEN
        val input = readLine()?.split(' ')
        val inputInt = input!!.map {inpString -> inpString.toInt()}
        val agent = Agent(inputInt[0], 'W', Matrix.instance.getAvailablePosition())
        agent.matchPreference = inputInt.subList(1, 3).toTypedArray()
        agents.add(agent)
        Matrix.instance.addElement(agent, agent.position)
    }


    Matrix.instance.printMatrix()
}