enum class Direction {
    NORTH, SOUTH, WEST, EAST;

    fun getOposite() : Direction {
        return when (this) {
            NORTH -> SOUTH
            SOUTH -> NORTH
            WEST -> EAST
            EAST -> WEST
        }
    }

    fun turnRight() : Direction {
        return when (this) {
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
            WEST -> NORTH
        }
    }
}