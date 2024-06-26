package net.uoneweb.android.til.collection

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MovableListTest {
    @Test
    fun moveToFirst() {
        val list = MovableList(mutableListOf(0, 1, 2, 3, 4))

        val actual = list.move(2, 0)

        assertThat(actual).isEqualTo(0)
        assertThat(list.toList()).isEqualTo(listOf(2, 0, 1, 3, 4))
    }

    @Test
    fun moveToPrev() {
        val list = MovableList(mutableListOf(0, 1, 2, 3, 4))

        val actual = list.move(2, 1)

        assertThat(actual).isEqualTo(1)
        assertThat(list.toList()).isEqualTo(listOf(0, 2, 1, 3, 4))
    }

    @Test
    fun moveToSameWouldNoChange() {
        val list = MovableList(mutableListOf(0, 1, 2, 3, 4))

        val actual = list.move(2, 2)

        assertThat(actual).isEqualTo(-1)
        assertThat(list.toList()).isEqualTo(listOf(0, 1, 2, 3, 4))
    }

    @Test
    fun moveToNext() {
        val list = MovableList(mutableListOf(0, 1, 2, 3, 4))

        val actual = list.move(2, 3)

        assertThat(actual).isEqualTo(3)
        assertThat(list.toList()).isEqualTo(listOf(0, 1, 3, 2, 4))
    }

    @Test
    fun moveToNextAfterNext() {
        val list = MovableList(mutableListOf(0, 1, 2, 3, 4))

        val actual = list.move(2, 4)

        assertThat(actual).isEqualTo(4)
        assertThat(list.toList()).isEqualTo(listOf(0, 1, 3, 4, 2))
    }

    @Test
    fun moveToLast() {
        val list = MovableList(mutableListOf(0, 1, 2, 3, 4))

        val actual = list.move(2, 5)

        assertThat(actual).isEqualTo(4)
        assertThat(list.toList()).isEqualTo(listOf(0, 1, 3, 4, 2))
    }
}
