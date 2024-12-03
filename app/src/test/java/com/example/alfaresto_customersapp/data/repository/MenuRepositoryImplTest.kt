package com.example.alfaresto_customersapp.data.repository

import com.example.alfaresto_customersapp.data.model.MenuResponse
import com.example.alfaresto_customersapp.domain.model.Menu
import com.example.alfaresto_customersapp.domain.repository.MenuRepository
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MenuRepositoryImplTest {
    @Mock
    private lateinit var menusRef: CollectionReference
    private lateinit var menuRepository: MenuRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        menuRepository = MenuRepositoryImpl(menusRef)
    }

    @Test
    fun `getMenus returns empty list when there is an error`() = runBlocking {
        val task = Tasks.forException<QuerySnapshot>(Exception("Error"))
        `when`(menusRef.get()).thenReturn(task)

        val menus = menuRepository.getMenus().first()

        assertEquals(0, menus.size)
    }

    @Test
    fun `getMenus returns empty list when no menus are found`() = runBlocking {
        val querySnapshot = mock(QuerySnapshot::class.java)
        `when`(querySnapshot.toObjects(MenuResponse::class.java)).thenReturn(emptyList())
        val task = Tasks.forResult(querySnapshot)
        `when`(menusRef.get()).thenReturn(task)

        val menus = menuRepository.getMenus().first()

        assertEquals(0, menus.size)
    }

//    @Test
//    fun `getMenus returns list of menus when snapshot is updated`() = runBlocking {
//        val menuResponse = MenuResponse(id = "1", name = "Pizza", stock = 10)
//        val querySnapshot = mock(QuerySnapshot::class.java)
//        `when`(querySnapshot.toObjects(MenuResponse::class.java)).thenReturn(listOf(menuResponse))
//        val task = Tasks.forResult(querySnapshot)
//        `when`(menusRef.get()).thenReturn(task)
//
//        val menus = menuRepository.getMenus().first()
//
//        assertEquals(1, menus.size)
//        assertEquals("Pizza", menus[0].name)
//    }
}