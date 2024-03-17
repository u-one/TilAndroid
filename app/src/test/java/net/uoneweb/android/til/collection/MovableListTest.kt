package net.uoneweb.android.til.collection

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MovableListTest {

    @Test
    fun moveToFirst() {
        val list = MovableList(mutableListOf(0, 1, 2, 3, 4))
        list.move(2, 0)
        assertThat(list.toList()).isEqualTo(listOf(2, 0, 1, 3, 4))
    }

    @Test
    fun moveToPrev() {
        val list = MovableList(mutableListOf(0, 1, 2, 3, 4))
        list.move(2, 1)
        assertThat(list.toList()).isEqualTo(listOf(0, 2, 1, 3, 4))
    }

    @Test
    fun moveToSameWouldNoChange() {
        val list = MovableList(mutableListOf(0, 1, 2, 3, 4))
        list.move(2, 2)
        assertThat(list.toList()).isEqualTo(listOf(0, 1, 2, 3, 4))
    }

    @Test
    fun moveToNextWouldNoChange() {
        val list = MovableList(mutableListOf(0, 1, 2, 3, 4))
        list.move(2, 3)
        assertThat(list.toList()).isEqualTo(listOf(0, 1, 2, 3, 4))
    }

    @Test
    fun moveToNextAfterNext() {
        val list = MovableList(mutableListOf(0, 1, 2, 3, 4))
        list.move(2, 4)
        assertThat(list.toList()).isEqualTo(listOf(0, 1, 3, 2, 4))
    }

    @Test
    fun moveToLast() {
        val list = MovableList(mutableListOf(0, 1, 2, 3, 4))
        list.move(2, 5)
        assertThat(list.toList()).isEqualTo(listOf(0, 1, 3, 4, 2))
    }
}