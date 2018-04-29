class PriorityQueue<T>() {
    var queue: ArrayList<T> = ArrayList()
    var priority: ArrayList<Double> = ArrayList()

    fun put(element: T, ePriority: Double) {
        queue.add(element)
        priority.add(ePriority)
    }

    fun get() : T {
        var minIndex = 0
        var minValue = priority[0]
        for (i in priority.indices) {
            if (minValue > priority[i]) {
                minValue = priority[i]
                minIndex = i
            }
        }

        priority.removeAt(minIndex)
        return queue.removeAt(minIndex)
    }

    fun empty(): Boolean {
        return queue.isEmpty()
    }
}