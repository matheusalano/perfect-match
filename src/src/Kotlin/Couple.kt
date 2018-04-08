class Couple(husband: Agent, wife: Agent, position: Position) : Agent(null, ElementKind.COUPLE, position) {
    val husband = husband
    val wife = wife
}