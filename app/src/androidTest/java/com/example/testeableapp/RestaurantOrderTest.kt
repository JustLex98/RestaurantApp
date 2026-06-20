package com.example.testeableapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class RestaurantOrderTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    // 1. Mensaje de pedido vacío visible al inicio (1 pt)
    @Test
    fun test_MensajeVacioAlInicio_Visible() {
        // Buscamos el texto que indica que no hay nada en el carrito
        composeTestRule.onNodeWithText("Tu pedido está vacío", useUnmergedTree = true)
            .performScrollTo()
            .assertIsDisplayed()
    }

    // 2. Todos los items del menú visibles (1 pt)
    @Test
    fun test_ItemsMenu_Visibles() {
        // Verificamos que algunos elementos del MenuData aparezcan en pantalla
        composeTestRule.onNodeWithText("Patatas Bravas").assertExists()
        composeTestRule.onNodeWithText("Agua mineral").assertExists()
        composeTestRule.onNodeWithText("Tarta de queso").assertExists()
    }

    // 3. El total general se actualiza (2 pts)
    @Test
    fun test_TotalSeActualiza_AlAgregarItem() {
        // Buscamos el primer botón de agregar (+) y le damos click
        // Usamos contentDescription "Añadir" que es común en estos botones
        composeTestRule.onAllNodesWithContentDescription("Añadir", substring = true)
            .onFirst()
            .performClick()

        // Verificamos que el total ya no sea 0.00
        // Buscamos el nodo que contenga el símbolo de precio o la palabra Total
        composeTestRule.onNodeWithText("Total:", substring = true)
            .performScrollTo()
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("0.00", substring = true).assertDoesNotExist()
    }

    // --- ANÁLISIS E IMPLEMENTACIÓN DE PRUEBAS DE UI ADICIONALES (2 pts) ---

    // Adicional 1: Verificar que aparece el diálogo de confirmación
    @Test
    fun test_DialogoConfirmacion_ApareceAlOrdenar() {
        // Justificación: Es relevante para asegurar que el usuario reciba una confirmación
        // antes de finalizar la transacción legal/económica.

        // Agregamos algo y damos click al botón de confirmar pedido
        composeTestRule.onAllNodesWithContentDescription("Añadir", substring = true).onFirst().performClick()

        // Intentamos hacer click en el botón de "Confirmar" o "Realizar Pedido"
        composeTestRule.onNodeWithText("Realizar Pedido", ignoreCase = true)
            .performScrollTo()
            .performClick()

        // Verificamos que el diálogo de confirmación sea visible
        composeTestRule.onNodeWithText("¡Pedido realizado!", substring = true).assertIsDisplayed()
    }

    // Adicional 2: Verificar que el botón de "Aceptar" en el diálogo limpia la UI
    @Test
    fun test_BotonAceptar_LimpiaInterfaz() {
        // Justificación: Es relevante para garantizar que el estado de la UI se reinicie
        // correctamente y no queden datos residuales para el siguiente uso.

        // Llegamos hasta el diálogo
        test_DialogoConfirmacion_ApareceAlOrdenar()

        // Click en el botón del diálogo (Dismiss)
        composeTestRule.onNodeWithText("Aceptar").performClick()

        // El mensaje de vacío debería volver a aparecer
        composeTestRule.onNodeWithText("Tu pedido está vacío", useUnmergedTree = true)
            .performScrollTo()
            .assertIsDisplayed()
    }
}