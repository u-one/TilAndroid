package net.uoneweb.android.til.collection

class MovableList<T>(private val list: MutableList<T>) : MutableList<T> by list {
    /**
     * Moves the item at [from] to [to].
     * The item at [from] will be removed from the list and inserted at [to].
     * The item at [to] will be shifted to the right.
     */
    fun move(from: Int, to: Int): Int {
        if (from < 0 || from >= size) return -1
        if (to < 0 || to > size) return -1
        if (to == from || to == from + 1) return -1

        return if (to < from) {
            val item = list.removeAt(from)
            list.add(to, item)
            to
        } else {
            val item = list.removeAt(from)
            list.add(to - 1, item)
            to - 1
        }
    }

}