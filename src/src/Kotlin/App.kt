@file:JvmName("App")

import java.io.File

fun main(args: Array<String>) {
    println("Enter the matrix size:")
    val matrixSize = readLine()?.toInt()

    println("Enter file name: ")
    val text = File(readLine()).inputStream().bufferedReader().use { it.readText() }
    val numbers = text.trim().split("\\W+".toRegex()).toCollection(ArrayList())

    val numOfCouples = numbers.removeAt(0).toInt()
    val numOfOffices = numbers.removeAt(0).toInt()

    Matrix.instance.init(matrixSize!!, numOfOffices)

    for (i in 1..numOfCouples) { //MEN
        val agent = Agent(numbers.removeAt(0).toInt(), ElementKind.MAN, Matrix.instance.getAvailablePosition())
        for (i in 1..numOfCouples) {
            agent.matchPreference.add(numbers.removeAt(0).toInt())
        }
        Matrix.instance.addAgent(agent, agent.position)
    }

    for (i in 1..numOfCouples) { //WOMEN
        val agent = Agent(numbers.removeAt(0).toInt(), ElementKind.WOMAN, Matrix.instance.getAvailablePosition())
        for (i in 1..numOfCouples) {
            agent.matchPreference.add(numbers.removeAt(0).toInt())
        }
        Matrix.instance.addAgent(agent, agent.position)
    }

    Matrix.instance.printMatrix()

    var round = 0

    while (round < 10000) {
        val agents = Matrix.instance.agents.clone() as ArrayList<Agent>
        agents.forEach { agent ->
            if (Matrix.instance.getAgentByID(agent.id, agent.kind) == null) return@forEach
            if (agent.state == AgentState.AT_OFFICE) {
                val partnerAgent = Matrix.instance.getAgentByID(agent.newPartnerID!!, agent.newPartnerKind!!)!!
                if (partnerAgent.state == AgentState.AT_OFFICE) {
                    Util.registryOffice(agent, partnerAgent)
                }
            } else {
                agent.action()
            }
        }
        round++
//        Thread.sleep(1_000)
//        val waitFor = ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor() //Clear windows CMD
//        Matrix.instance.printMatrix()
    }
    Matrix.instance.printCouples()
}