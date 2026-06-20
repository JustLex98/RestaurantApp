package com.example.testeableapp

// Importa las clases de tu proyecto (esto es lo que suele fallar)
import com.example.testeableapp.model.MenuData
import com.example.testeableapp.model.MenuItem
import com.example.testeableapp.RestaurantViewModel

// Importaciones de JUnit para los tests
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class ExampleUnitTest {

    private lateinit var viewModel: RestaurantViewModel

    @Before
    fun setup() {
        viewModel = RestaurantViewModel()
    }

    @Test
    fun `agregar item al pedido actualiza las cantidades`() {
        val itemId = 1
        viewModel.addItem(itemId)

        // Usamos .value porque quantities es un StateFlow
        val qty = viewModel.quantities.value[itemId]
        assertEquals(1, qty)
    }

    @Test
    fun `incrementar y decrementar cantidad funciona correctamente`() {
        val itemId = 5
        viewModel.addItem(itemId)
        viewModel.incrementItem(itemId)
        assertEquals(2, viewModel.quantities.value[itemId])

        viewModel.decrementItem(itemId)
        assertEquals(1, viewModel.quantities.value[itemId])
    }

    @Test
    fun `eliminar item al decrementar desde 1`() {
        val itemId = 3
        viewModel.addItem(itemId)
        viewModel.decrementItem(itemId)

        assertFalse(viewModel.quantities.value.containsKey(itemId))
    }

    @Test
    fun `calculo del total a pagar es correcto`() {
        viewModel.addItem(1) // 5.50
        viewModel.addItem(5) // 1.50

        val totalEsperado = 7.0
        assertEquals(totalEsperado, viewModel.total.value, 0.01)
    }

    @Test
    fun `placeOrder genera una confirmacion correcta`() {
        viewModel.addItem(1)
        viewModel.placeOrder()

        assertNotNull(viewModel.confirmation.value)
        assertEquals(5.50, viewModel.confirmation.value?.total ?: 0.0, 0.01)
    }

    @Test
    fun `dismissConfirmation limpia el carrito completamente`() {
        viewModel.addItem(1)
        viewModel.dismissConfirmation()

        assertTrue(viewModel.quantities.value.isEmpty())
        assertEquals(0.0, viewModel.total.value, 0.0)
    }
}