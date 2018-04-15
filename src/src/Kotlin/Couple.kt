import kotlin.math.min

class Couple(husband: Agent, wife: Agent, position: Position) : Agent(Util.getCoupleIndex(), ElementKind.COUPLE, position) {
    val husband = husband
    val wife = wife

    override fun checkNeighborhood(): Boolean {
        val neighbors = Matrix.instance.getNeighbors(position, 2, false).filter {it is Agent}.map { it as Agent }
        val sortedNeighbors = neighbors.sortedBy {
            if (it is Couple) {
                min(this.husband.matchPreference.indexOf(it.wife.id), this.wife.matchPreference.indexOf(it.husband.id))
            } else {
                if (it.kind == ElementKind.MAN) {
                    this.wife.matchPreference.indexOf(it.id)
                } else {
                    this.husband.matchPreference.indexOf(it.id)
                }
            }
        }

        sortedNeighbors.forEach { agent  ->
            if (agent.receiveProposalFrom(this)) {
                newPartnerID = agent.id
                newPartnerKind = agent.kind
                val office = Matrix.instance.getNearestOfficeFrom(this.position)
                aStar(office.position)
                return true
            }
        }

        return false
    }

    override fun receiveProposalFrom(agent: Agent) : Boolean {
        if (newPartnerID != null) {
            var betterPartnerFound = false
            if (agent.kind == newPartnerKind!!) {
                if (agent is Couple) {
                    val couple: Couple = Matrix.instance.getAgentByID(newPartnerID!!, newPartnerKind!!) as Couple
                    val minNewPartner = min(this.husband.matchPreference.indexOf(couple.wife.id), this.wife.matchPreference.indexOf(couple.husband.id))
                    val minAgent = min(this.husband.matchPreference.indexOf(agent.wife.id), this.wife.matchPreference.indexOf(agent.husband.id))
                    if (minAgent < minNewPartner) betterPartnerFound = true
                } else if (agent.kind == ElementKind.MAN) {
                    if (wife.matchPreference.indexOf(agent.id) < wife.matchPreference.indexOf(newPartnerID)) betterPartnerFound = true
                } else if (agent.kind == ElementKind.WOMAN) {
                    if (husband.matchPreference.indexOf(agent.id) < husband.matchPreference.indexOf(newPartnerID)) betterPartnerFound = true
                }
            } else {
                when(newPartnerKind) {
                    ElementKind.COUPLE -> {
                        val couple: Couple = Matrix.instance.getAgentByID(newPartnerID!!, newPartnerKind!!) as Couple
                        val minNewPartner = min(this.husband.matchPreference.indexOf(couple.wife.id), this.wife.matchPreference.indexOf(couple.husband.id))
                        if (agent.kind == ElementKind.MAN) {
                            if (this.wife.matchPreference.indexOf(agent.id) < minNewPartner) betterPartnerFound = true
                        } else if (agent.kind == ElementKind.WOMAN) {
                            if (this.husband.matchPreference.indexOf(agent.id) < minNewPartner) betterPartnerFound = true
                        }
                    }
                    ElementKind.WOMAN -> {
                        if (agent is Couple) {
                            val minAgent = min(this.husband.matchPreference.indexOf(agent.wife.id), this.wife.matchPreference.indexOf(agent.husband.id))
                            if (minAgent < this.husband.matchPreference.indexOf(newPartnerID)) betterPartnerFound = true
                        } else if (agent.kind == ElementKind.MAN) {
                            if (wife.matchPreference.indexOf(agent.id) < husband.matchPreference.indexOf(newPartnerID)) betterPartnerFound = true
                        }
                    }
                    ElementKind.MAN -> {
                        if (agent is Couple) {
                            val minAgent = min(this.husband.matchPreference.indexOf(agent.wife.id), this.wife.matchPreference.indexOf(agent.husband.id))
                            if (minAgent < this.wife.matchPreference.indexOf(newPartnerID)) betterPartnerFound = true
                        } else if (agent.kind == ElementKind.WOMAN) {
                            if (husband.matchPreference.indexOf(agent.id) < wife.matchPreference.indexOf(newPartnerID)) betterPartnerFound = true
                        }
                     }
                }
            }

            if (betterPartnerFound) {
                Matrix.instance.updateAgentStateByID(newPartnerID!!, newPartnerKind!!, AgentState.WALKING)
                newPartnerID = agent.id
                newPartnerKind = agent.kind
                val office = Matrix.instance.getNearestOfficeFrom(agent.position)
                aStar(office.position)
                return true
            }
        } else {
            if (agent is Couple) {
                val minCouple = min(this.husband.matchPreference.indexOf(this.wife.id), this.wife.matchPreference.indexOf(this.husband.id))
                val minAgent = min(this.husband.matchPreference.indexOf(agent.wife.id), this.wife.matchPreference.indexOf(agent.husband.id))
                if (minAgent < minCouple) {
                    newPartnerID = agent.id
                    newPartnerKind = agent.kind
                    val office = Matrix.instance.getNearestOfficeFrom(agent.position)
                    aStar(office.position)
                    return true
                }
            } else if (agent.kind == ElementKind.MAN) {
                if (wife.matchPreference.indexOf(agent.id) < wife.matchPreference.indexOf(husband.id)) {
                    newPartnerID = agent.id
                    newPartnerKind = agent.kind
                    val office = Matrix.instance.getNearestOfficeFrom(agent.position)
                    aStar(office.position)
                    return true
                }
            } else if (agent.kind == ElementKind.WOMAN) {
                if (husband.matchPreference.indexOf(agent.id) < husband.matchPreference.indexOf(wife.id)) {
                    newPartnerID = agent.id
                    newPartnerKind = agent.kind
                    val office = Matrix.instance.getNearestOfficeFrom(agent.position)
                    aStar(office.position)
                    return true
                }
            }
        }
        return false
    }

    override fun getSymbol(): String {
        return "M${this.husband.id}W${this.wife.id}"
    }
}