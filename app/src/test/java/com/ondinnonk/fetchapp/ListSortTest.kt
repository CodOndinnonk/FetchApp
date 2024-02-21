package com.ondinnonk.fetchapp

import com.ondinnonk.fetchapp.repositiry.FetchItemModel
import org.junit.Assert
import org.junit.Test

class ListSortTest {


    @Test
    fun test() {
        val list = mutableListOf<FetchItemModel>()

        list.add(FetchItemModel(id = 1, listId = 1, name = ""))
        list.add(FetchItemModel(id = 1, listId = 3, name = ""))
        list.add(FetchItemModel(id = 1, listId = 3, name = null))
        list.add(FetchItemModel(id = 1, listId = 2, name = "name 2 aa"))
        list.add(FetchItemModel(id = 1, listId = 2, name = null))
        list.add(FetchItemModel(id = 1, listId = 2, name = "name 2 z"))
        list.add(FetchItemModel(id = 1, listId = 1, name = null))
        list.add(FetchItemModel(id = 1, listId = 3, name = "name 3 zzzzzz"))
        list.add(FetchItemModel(id = 1, listId = 1, name = "name 1 a"))
        list.add(FetchItemModel(id = 1, listId = 2, name = ""))
        list.add(FetchItemModel(id = 1, listId = 9, name = ""))
        list.add(FetchItemModel(id = 1, listId = 3, name = "name 3 aaaa"))
        list.add(FetchItemModel(id = 1, listId = 1, name = "name 1 z"))
        list.add(FetchItemModel(id = 1, listId = 5, name = null))


        list.prepareList().also {
            Assert.assertEquals(6, it.size)
            Assert.assertEquals(FetchItemModel(id = 1, listId = 1, name = "name 1 a"), it[0])
            Assert.assertEquals(FetchItemModel(id = 1, listId = 1, name = "name 1 z"), it[1])
            Assert.assertEquals(FetchItemModel(id = 1, listId = 2, name = "name 2 aa"), it[2])
            Assert.assertEquals(FetchItemModel(id = 1, listId = 2, name = "name 2 z"), it[3])
            Assert.assertEquals(FetchItemModel(id = 1, listId = 3, name = "name 3 aaaa"), it[4])
            Assert.assertEquals(FetchItemModel(id = 1, listId = 3, name = "name 3 zzzzzz"), it[5])
        }

    }
}