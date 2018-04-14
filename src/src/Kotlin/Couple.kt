class Couple(husband: Agent, wife: Agent, position: Position) : Agent(Util.getCoupleIndex(), ElementKind.COUPLE, position) {
    val husband = husband
    val wife = wife

    override fun receiveProposalFrom(agent: Agent) : Boolean {
        if (newPartnerID != null) {
            var betterPartnerFound = false
            if (agent.kind == ElementKind.MAN && agent.kind == newPartnerKind!!) {
                if (wife.matchPreference.indexOf(agent.id) < wife.matchPreference.indexOf(newPartnerID)) betterPartnerFound = true
            } else if (agent.kind == ElementKind.MAN && agent.kind != newPartnerKind!!) {
                if (wife.matchPreference.indexOf(agent.id) < husband.matchPreference.indexOf(newPartnerID)) betterPartnerFound = true
            } else if (agent.kind == ElementKind.WOMAN && agent.kind == newPartnerKind!!) {
                if (husband.matchPreference.indexOf(agent.id) < husband.matchPreference.indexOf(newPartnerID)) betterPartnerFound = true
            } else if (agent.kind == ElementKind.WOMAN && agent.kind != newPartnerKind!!) {
                if (husband.matchPreference.indexOf(agent.id) < wife.matchPreference.indexOf(newPartnerID)) betterPartnerFound = true
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
            if (agent.kind == ElementKind.MAN) {
                if (wife.matchPreference.indexOf(agent.id) < wife.matchPreference.indexOf(husband.id)) {
                    newPartnerID = agent.id
                    newPartnerKind = agent.kind
                    val office = Matrix.instance.getNearestOfficeFrom(agent.position)
                    aStar(office.position)
                    return true
                }
            } else {
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

    override fun action() {
        if (state == AgentState.WALKING) {
            walk()
        } else if (state == AgentState.GOING_TO_OFFICE) {
            if (officePath.isEmpty()) state = AgentState.AT_OFFICE
            else if (Matrix.instance.isAvailable(officePath.last())) {
                val oldPosition = position
                position = officePath.removeAt(officePath.lastIndex)
                Matrix.instance.updatePosition(this, oldPosition)
            } else {
                val elem = Matrix.instance.getElementByPosition(officePath.last())
                if (elem is Agent && elem.state == AgentState.AT_OFFICE) {
                    aStar(officeGoal!!)
                }
            }
        }
    }

    override fun getSymbol(): String {
        return "[Co]"
    }
}