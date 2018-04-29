class RegistryOffice(pos: Position) : Element(ElementKind.REGISTRY_OFFICE, pos) {
    var couplesUsingIt = 0
    val isAvailable get() = couplesUsingIt == 0

    companion object {
        fun registryOffice(agent1: Agent, agent2: Agent) {
            Matrix.instance.updateOfficeStatus(agent1.officeGoal!!, true)
            if (agent1 is Couple && agent2 is Couple) {
                val couple1 = Couple(agent1.husband, agent2.wife, agent1.position)
                val couple2 = Couple(agent2.husband, agent1.wife, agent2.position)

                Matrix.instance.removeAgent(agent1)
                Matrix.instance.removeAgent(agent2)
                Matrix.instance.addAgent(couple1, agent1.position)
                Matrix.instance.addAgent(couple2, agent2.position)
                if (verbose == 2) {
                    println("Couple ${agent1.symbol} and ${agent2.symbol} got divorced; Couple ${couple1.symbol} and ${couple2.symbol} got married")
                }
            } else if (agent1 is Couple && agent2.kind == ElementKind.MAN) {
                val couple = Couple(agent2, agent1.wife, agent2.position)

                Matrix.instance.removeAgent(agent1)
                agent1.husband.position = agent1.position
                agent1.husband.state = AgentState.WALKING
                Matrix.instance.addAgent(agent1.husband, agent1.position)
                Matrix.instance.removeAgent(agent2)
                Matrix.instance.addAgent(couple, agent2.position)
                if (verbose == 2) println("Couple ${agent1.symbol} got divorced. Couple ${couple.symbol} got married")
            } else if (agent1 is Couple && agent2.kind == ElementKind.WOMAN) {
                val couple = Couple(agent1.husband, agent2, agent2.position)

                Matrix.instance.removeAgent(agent1)
                agent1.wife.position = agent1.position
                agent1.wife.state = AgentState.WALKING
                Matrix.instance.addAgent(agent1.wife, agent1.position)
                Matrix.instance.removeAgent(agent2)
                Matrix.instance.addAgent(couple, agent2.position)
                if (verbose == 2) println("Couple ${agent1.symbol} got divorced. Couple ${couple.symbol} got married")
            } else if (agent2 is Couple && agent1.kind == ElementKind.MAN) {
                val couple = Couple(agent1, agent2.wife, agent1.position)

                Matrix.instance.removeAgent(agent2)
                agent2.husband.position = agent2.position
                agent2.husband.state = AgentState.WALKING
                Matrix.instance.addAgent(agent2.husband, agent2.position)
                Matrix.instance.removeAgent(agent1)
                Matrix.instance.addAgent(couple, agent1.position)
                if (verbose == 2) println("Couple ${agent2.symbol} got divorced. Couple ${couple.symbol} got married")
            } else if (agent2 is Couple && agent1.kind == ElementKind.WOMAN) {
                val couple = Couple(agent2.husband, agent1, agent1.position)

                Matrix.instance.removeAgent(agent2)
                agent2.wife.position = agent2.position
                agent2.wife.state = AgentState.WALKING
                Matrix.instance.addAgent(agent2.wife, agent2.position)
                Matrix.instance.removeAgent(agent1)
                Matrix.instance.addAgent(couple, agent1.position)
                if (verbose == 2) println("Couple ${agent2.symbol} got divorced. Couple ${couple.symbol} got married")
            } else {
                if (agent1.kind == ElementKind.MAN) {
                    val couple = Couple(agent1, agent2, agent1.position)
                    Matrix.instance.removeAgent(agent1)
                    Matrix.instance.removeAgent(agent2)
                    Matrix.instance.addAgent(couple, agent1.position)
                    if (verbose == 2) println("Couple ${couple.symbol} got married")
                } else {
                    val couple = Couple(agent2, agent1, agent2.position)
                    Matrix.instance.removeAgent(agent2)
                    Matrix.instance.removeAgent(agent1)
                    Matrix.instance.addAgent(couple, agent2.position)
                    if (verbose == 2) println("Couple ${couple.symbol} got married")
                }
            }
        }
    }
}