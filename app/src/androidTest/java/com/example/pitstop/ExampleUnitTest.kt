package com.example.pitstop

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pitstop.controlador.PitStop_Controller
import com.example.pitstop.modelo.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PitStopControllerTest {

    private lateinit var controller: PitStop_Controller
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        controller = PitStop_Controller(context)
        controller.limpiarParadas()
    }

    @Test
    fun testGuardarYObtenerParada() {
        val parada = Parada(
            piloto = Piloto("Carlos"),
            escuderia = Escuderia("Ferrari"),
            tiempoSegundos = 10.5,
            neumatico = Neumatico("Blandos", 4),
            estado = Estado("OK"),
            observacion = null,
            mecanico = Mecanico("Juan"),
            fecha = "22/10/2025 13:30"
        )

        controller.guardarParada(parada)
        val lista = controller.obtenerParadas()

        assertEquals(1, lista.size)
        assertEquals("Carlos", lista[0].piloto.nombre)
        assertEquals(10.5, lista[0].tiempoSegundos, 0.001)
    }

    @Test
    fun testEliminarParada() {
        val parada1 = Parada(
            Piloto("Carlos"), Escuderia("Ferrari"), 9.8,
            Neumatico("Medios", 4), Estado("OK"), null, Mecanico("Juan"), "22/10/2025 13:30"
        )
        val parada2 = Parada(
            Piloto("Luis"), Escuderia("Red Bull"), 11.2,
            Neumatico("Duros", 4), Estado("OK"), null, Mecanico("Pedro"), "22/10/2025 14:00"
        )

        controller.guardarParada(parada1)
        controller.guardarParada(parada2)
        controller.eliminarParada(0)

        val lista = controller.obtenerParadas()
        assertEquals(1, lista.size)
        assertEquals("Luis", lista[0].piloto.nombre)
    }

    @Test
    fun testBuscarParadaPorPiloto() {
        val parada = Parada(
            Piloto("Carlos"), Escuderia("Ferrari"), 12.3,
            Neumatico("Blandos", 4), Estado("OK"), null, Mecanico("Juan"), "22/10/2025 13:30"
        )
        controller.guardarParada(parada)

        val encontrada = controller.buscarParadaPorPiloto("Carlos")

        assertNotNull(encontrada)
        assertEquals("Ferrari", encontrada?.escuderia?.nombre)
    }

    @Test
    fun testObtenerResumen() {
        controller.limpiarParadas()
        controller.guardarParada(
            Parada(Piloto("Carlos"), Escuderia("Ferrari"), 10.0, Neumatico("Blandos", 4), Estado("OK"), null, Mecanico("Juan"), "22/10/2025 13:30")
        )
        controller.guardarParada(
            Parada(Piloto("Luis"), Escuderia("Red Bull"), 20.0, Neumatico("Duros", 4), Estado("OK"), null, Mecanico("Pedro"), "22/10/2025 14:00")
        )

        val resumen = controller.obtenerResumen()
        assertTrue(resumen.contains("Total paradas: 2"))
        assertTrue(resumen.contains("Promedio tiempo: 15.00"))
    }
}
