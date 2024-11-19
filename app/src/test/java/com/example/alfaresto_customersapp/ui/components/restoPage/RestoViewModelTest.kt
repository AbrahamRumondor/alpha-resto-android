//package com.example.alfaresto_customersapp.ui.components.restoPage
//
//import androidx.test.runner.AndroidJUnit4
//import com.example.alfaresto_customersapp.data.local.room.entity.CartEntity
//import com.example.alfaresto_customersapp.domain.model.Menu
//import com.example.alfaresto_customersapp.domain.usecase.cart.CartUseCase
//import com.example.alfaresto_customersapp.domain.usecase.menu.MenuUseCase
//import com.example.alfaresto_customersapp.domain.usecase.user.UserUseCase
//import io.mockk.MockKAnnotations
//import io.mockk.Runs
//import io.mockk.clearAllMocks
//import io.mockk.coEvery
//import io.mockk.impl.annotations.MockK
//import io.mockk.just
//import io.mockk.verify
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.test.runBlockingTest
//import org.junit.After
//import org.junit.Assert.*
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//class RestoViewModelTest {
//
//  @get:Rule
//  val hiltRule = HiltAndroidRule(this)
//
//  @MockK
//  lateinit var mockMenuUseCase: MenuUseCase
//
//  @MockK
//  lateinit var mockCartUseCase: CartUseCase
//
//  @MockK
//  lateinit var mockUserUseCase: UserUseCase
//
//  private lateinit var viewModel: RestoViewModel
//
//  @Before
//  fun setUp() {
//    // Initialize MockK
//    MockKAnnotations.init(this)
//
//    // Inject mocks into the ViewModel (constructor injection)
//    viewModel = RestoViewModel(mockMenuUseCase, mockCartUseCase, mockUserUseCase)
//  }
//
//  @Test
//  fun testFetchMenus() = runBlockingTest {
//    // Given a mocked response from the MenuUseCase
//    val mockMenus = listOf(Menu("1", "Pizza", "Delicious pizza"))
//    coEvery { mockMenuUseCase.getMenus() } returns flowOf(mockMenus)
//
//    // When fetchMenus is called
//    viewModel.fetchMenus()
//
//    // Then assert that menus are updated in the ViewModel
//    viewModel.menus.collectLatest { menus ->
//      assert(menus == mockMenus)
//    }
//  }
//
//  @Test
//  fun testFetchCart() = runBlockingTest {
//    // Given a mocked response from the CartUseCase
//    val mockCart = listOf(CartEntity(menuId = "1", menuQty = 2))
//    coEvery { mockCartUseCase.getCart() } returns flowOf(mockCart)
//
//    // When fetchCart is called
//    viewModel.fetchCart()
//
//    // Then assert that the cart is updated in the ViewModel
//    viewModel.cart.collectLatest { cart ->
//      assert(cart == mockCart)
//    }
//  }
//
//  @Test
//  fun testAddOrderQuantity() = runBlockingTest {
//    // Given a mocked stock value from the MenuUseCase
//    val menuId = "1"
//    val cartEntity = CartEntity(menuId = menuId, menuQty = 1)
//    coEvery { mockMenuUseCase.getMenuStock(menuId) } returns 5 // stock is 5
//
//    // When addOrderQuantity is called
//    viewModel.addOrderQuantity(mockContext, menuId = menuId, cart = cartEntity)
//
//    // Then check that the cart was updated or correct toast was shown
//    verify { mockCartUseCase.insertMenu(cartEntity.copy(menuQty = 2)) }
//  }
//
//  @Test
//  fun testDecreaseOrderQuantity() = runBlockingTest {
//    // Given a cart entity with quantity 1
//    val cartEntity = CartEntity(menuId = "1", menuQty = 1)
//    coEvery { mockCartUseCase.getCart() } returns flowOf(listOf(cartEntity))
//
//    // When decreaseOrderQuantity is called
//    viewModel.decreaseOrderQuantity(menuId = cartEntity.menuId, cart = cartEntity)
//
//    // Then verify that the cart quantity is decreased or item is removed
//    verify { mockCartUseCase.insertMenu(cartEntity.copy(menuQty = 0)) }
//    verify { mockCartUseCase.deleteMenu(cartEntity.menuId) }
//  }
//
//  @Test
//  fun testAddTokenToFirestore() = runBlockingTest {
//    // Given a mocked token value from Firebase
//    val mockToken = "mock_token"
//    coEvery { mockUserUseCase.saveTokenToDB(any(), any()) } just Runs
//
//    // When getToken() is called
//    viewModel.getToken()
//
//    // Then verify that saveTokenToDB was called
//    verify { mockUserUseCase.saveTokenToDB(any(), mockToken) }
//  }
//
//  @After
//  fun tearDown() {
//    clearAllMocks()
//  }
//}