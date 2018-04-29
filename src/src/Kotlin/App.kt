@file:JvmName("App")

import java.io.File

var verbose = 0

fun main(args: Array<String>) {
    verbose = if (args.isNotEmpty()) args[0].toInt() else 3
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

//    Matrix.instance.printMatrix()

    var round = 0
    val maxRound = if (args.size > 1) args[1].toInt() else 10000
    var roundsNoMarriage = 0
    val maxRoundsNoMarriage = if (args.size > 2) args[2].toInt() else 200

    while (round < maxRound && roundsNoMarriage < maxRoundsNoMarriage) {
        round++
        val agents = Matrix.instance.agents.clone() as ArrayList<Agent>
        var marriage = false
        agents.forEach { agent ->
            if (Matrix.instance.getAgentByID(agent.id, agent.kind) == null) return@forEach
            if (agent.state == AgentState.AT_OFFICE) {
                val partnerAgent = Matrix.instance.getAgentByID(agent.newPartnerID!!, agent.newPartnerKind!!)!!
                if (partnerAgent.state == AgentState.AT_OFFICE) {
                    RegistryOffice.registryOffice(agent, partnerAgent)
                    marriage = true
                    roundsNoMarriage = 0
                }
            } else {
                agent.action()
            }
        }
        if (!marriage) roundsNoMarriage++
        if (verbose == 1) {
            Thread.sleep(800)
            val waitFor = ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor() //Clear windows CMD
            Matrix.instance.printMatrix()
        }
    }
    if (round == maxRound) println("Execution finished because max round was reached.")
    else if (roundsNoMarriage == maxRoundsNoMarriage) println("Execution finished because there were $roundsNoMarriage rounds with no marriage.")
    Matrix.instance.printCouples()
}