package com.example.alfaresto_customersapp.ui.components.orderHistoryDetailPage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.alfaresto_customersapp.domain.model.OrderHistory
import com.example.alfaresto_customersapp.domain.model.OrderItem
import com.example.alfaresto_customersapp.domain.model.User
import com.example.alfaresto_customersapp.domain.usecase.order.OrderUseCase
import com.example.alfaresto_customersapp.domain.usecase.orderHistory.OrderHistoryUseCase
import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.lenient
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


@RunWith(MockitoJUnitRunner::class)
class OrderHistoryDetailViewModel_Test {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var orderHistoryUseCase: OrderHistoryUseCase

    @Mock
    private lateinit var userUseCase: UserUseCase

    @Mock
    private lateinit var orderUseCase: OrderUseCase

    private lateinit var viewModel: OrderHistoryDetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = OrderHistoryDetailViewModel(orderHistoryUseCase, userUseCase, orderUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchUser should update user state`() = runTest {
        // Arrange
        val mockUser = User(id = "123", name = "Test User")
        `when`(userUseCase.getCurrentUser()).thenReturn(flowOf(mockUser).stateIn(this))

        // Act
        viewModel.fetchUser()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(mockUser, viewModel.user.value)
    }

    @Test
    fun `fetchOrderHistory should update orderHistory state`() = runTest {
        // Arrange
        val orderId = "order123"
        val mockOrderHistories = listOf(
            OrderHistory(orderId = "order123", orderTotalPrice = 100),
            OrderHistory(orderId = "order456", orderTotalPrice = 100)
        )
        val orderHistoryCaptor = argumentCaptor<(List<OrderHistory>) -> Unit>()

        `when`(orderHistoryUseCase.getOrderHistories(orderHistoryCaptor.capture())).then {
            orderHistoryCaptor.firstValue.invoke(mockOrderHistories)
        }

        // Act
        viewModel.fetchOrderHistory(orderId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(mockOrderHistories.first(), viewModel.orderHistory.value)
    }

    @Test
    fun `fetchOrderItems should update orderItems state`() = runTest {
        // Arrange
        val orderId = "order123"
        val mockOrderItems = listOf(
            OrderItem(id = "item1", menuName = "Product 1", quantity = 2),
            OrderItem(id = "item2", menuName = "Product 2", quantity = 1)
        )
        `when`(orderUseCase.getOrderItems(orderId)).thenReturn(flowOf(mockOrderItems).stateIn(this))

        // Act
        viewModel.fetchOrderItems(orderId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(mockOrderItems, viewModel.orderItems.value)
    }

    @Test
    fun `init should call fetchUser`() = runTest {
        // Arrange
        val mockUser = User(id = "123", name = "Test User")
        lenient().`when`(userUseCase.getCurrentUser()).thenReturn(flowOf(mockUser).stateIn(this))

        // Act
        // The init block is called during the constructor
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertNotNull(viewModel.user.value)
    }

}