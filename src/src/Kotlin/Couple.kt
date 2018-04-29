
class Couple(husband: Agent, wife: Agent, position: Position) : Agent(Util.getCoupleIndex(), ElementKind.COUPLE, position) {
    val husband = husband
    val wife = wife
    private val couplePreference = Util.getCouplePreference(husband, wife)!!

    override fun checkNeighborhood(): Boolean {
        val neighbors = Matrix.instance.getNeighbors(position, 2, false).filter {it is Agent}.map { it as Agent }
        val filteredNeighbors = neighbors.filter {
            if (it is Couple) {
                couplePreference > (Util.getCouplePreference(this.husband, it.wife)!! + Util.getCouplePreference(this.wife, it.husband)!!)/2.0
            } else {
                couplePreference > Util.getCouplePreference(if(it.kind == ElementKind.MAN) wife else husband, it)!!
            }
        }
        val sortedNeighbors = filteredNeighbors.sortedBy {
            if (it is Couple) {
                (Util.getCouplePreference(this.husband, it.wife)!! + Util.getCouplePreference(this.wife, it.husband)!!)/2.0
            } else {
                if (it.kind == ElementKind.MAN) {
                    this.wife.matchPreference.indexOf(it.id).toDouble()
                } else {
                    this.husband.matchPreference.indexOf(it.id).toDouble()
                }
            }
        }

        val office = Matrix.instance.getNearestOfficeFrom(this.position)
        sortedNeighbors.forEach { agent  ->
            if (agent.receiveProposalFrom(this, office)) {
                newPartnerID = agent.id
                newPartnerKind = agent.kind
                aStar(office.position)
                return true
            }
        }

        return false
    }

    override fun receiveProposalFrom(agent: Agent, office: RegistryOffice) : Boolean {
        if (newPartnerID != null) {
            var betterPartnerFound = false
            val newPartner = Matrix.instance.getAgentByID(newPartnerID!!, newPartnerKind!!)!!
            if (agent.kind == newPartnerKind!!) {
                if (agent is Couple) {
                    val couple: Couple = newPartner as Couple
                    val minNewPartner = (Util.getCouplePreference(this.husband, couple.wife)!! + Util.getCouplePreference(this.wife, couple.husband)!!)/2.0
                    val minAgent = (Util.getCouplePreference(this.husband, agent.wife)!! + Util.getCouplePreference(this.wife, agent.husband)!!)/2.0
                    if (minAgent < minNewPartner) betterPartnerFound = true
                } else if (agent.kind == ElementKind.MAN) {
                    if (Util.getCouplePreference(wife, agent)!! < Util.getCouplePreference(wife, newPartner)!!)  betterPartnerFound = true
                } else if (agent.kind == ElementKind.WOMAN) {
                    if (Util.getCouplePreference(husband, agent)!! < Util.getCouplePreference(husband, newPartner)!!)  betterPartnerFound = true
                }
            } else {
                when(newPartnerKind) {
                    ElementKind.COUPLE -> {
                        val couple: Couple = newPartner as Couple
                        val minNewPartner = (Util.getCouplePreference(this.husband, couple.wife)!! + Util.getCouplePreference(this.wife, couple.husband)!!)/2.0
                        if (agent.kind == ElementKind.MAN) {
                            if (Util.getCouplePreference(wife, agent)!! < minNewPartner) betterPartnerFound = true
                        } else if (agent.kind == ElementKind.WOMAN) {
                            if (Util.getCouplePreference(husband, agent)!! < minNewPartner) betterPartnerFound = true
                        }
                    }
                    ElementKind.WOMAN -> {
                        if (agent is Couple) {
                            val minAgent = (Util.getCouplePreference(this.husband, agent.wife)!! + Util.getCouplePreference(this.wife, agent.husband)!!)/2.0
                            if (minAgent < Util.getCouplePreference(husband, newPartner)!!) betterPartnerFound = true
                        } else if (agent.kind == ElementKind.MAN) {
                            if (Util.getCouplePreference(wife, agent)!! < Util.getCouplePreference(husband, newPartner)!!) betterPartnerFound = true
                        }
                    }
                    ElementKind.MAN -> {
                        if (agent is Couple) {
                            val minAgent = (Util.getCouplePreference(this.husband, agent.wife)!! + Util.getCouplePreference(this.wife, agent.husband)!!)/2.0
                            if (minAgent < Util.getCouplePreference(wife, newPartner)!!) betterPartnerFound = true
                        } else if (agent.kind == ElementKind.WOMAN) {
                            if (Util.getCouplePreference(husband, agent)!! < Util.getCouplePreference(wife, newPartner)!!) betterPartnerFound = true
                        }
                     }
                }
            }

            if (betterPartnerFound) {
                Matrix.instance.updateAgentStateByID(newPartnerID!!, newPartnerKind!!, AgentState.WALKING)
                newPartnerID = agent.id
                newPartnerKind = agent.kind
                aStar(office.position)
                return true
            }
        } else {
            if (agent is Couple) {
                val minAgent = (Util.getCouplePreference(this.husband, agent.wife)!! + Util.getCouplePreference(this.wife, agent.husband)!!)/2.0
                if (minAgent < this.couplePreference) {
                    newPartnerID = agent.id
                    newPartnerKind = agent.kind
                    aStar(office.position)
                    return true
                }
            } else if (agent.kind == ElementKind.MAN) {
                if (Util.getCouplePreference(wife, agent)!! < this.couplePreference) {
                    newPartnerID = agent.id
                    newPartnerKind = agent.kind
                    aStar(office.position)
                    return true
                }
            } else if (agent.kind == ElementKind.WOMAN) {
                if (Util.getCouplePreference(husband, agent)!! < this.couplePreference) {
                    newPartnerID = agent.id
                    newPartnerKind = agent.kind
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