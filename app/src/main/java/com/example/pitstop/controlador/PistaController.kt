package com.example.pitstop.controlador

import com.example.pitstop.modelo.Pista as PistaModel
import com.example.pitstop.modelo.Piloto
import com.example.pitstop.modelo.Escuderia
import com.example.pitstop.modelo.Mecanico
import com.example.pitstop.modelo.Neumatico
import com.example.pitstop.modelo.Estado
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.round

class PistaController {

    data class PistaEntity(val id: Int, val pista: PistaModel)

    private val idCounter = AtomicInteger(1)
    private val storage = linkedMapOf<Int, PistaModel>()

    interface OnDataChangedListener { fun onDataChanged(all: List<PistaEntity>) }
    private val listeners = mutableListOf<OnDataChangedListener>()
    fun addListener(l: OnDataChangedListener) { if (!listeners.contains(l)) listeners.add(l) }
    fun removeListener(l: OnDataChangedListener) { listeners.remove(l) }
    private fun notifyChange() {
        val snapshot = getAllWithIds()
        listeners.toList().forEach { it.onDataChanged(snapshot) }
    }

    data class ResultCreate(val success: Boolean, val id: Int?, val error: String?)

    fun crearPista(pista: PistaModel): ResultCreate {
        val (ok, err) = validarPista(pista)
        if (!ok) return ResultCreate(false, null, err)
        val id = idCounter.getAndIncrement()
        storage[id] = pista
        notifyChange()
        return ResultCreate(true, id, null)
    }

    fun actualizarPista(id: Int, nueva: PistaModel): Pair<Boolean, String?> {
        if (!storage.containsKey(id)) return Pair(false, "Pista no encontrada")
        val (ok, err) = validarPista(nueva)
        if (!ok) return Pair(false, err)
        storage[id] = nueva
        notifyChange()
        return Pair(true, null)
    }

    fun eliminarPista(id: Int): Boolean {
        val removed = storage.remove(id) != null
        if (removed) notifyChange()
        return removed
    }

    fun getAll(): List<PistaModel> = storage.values.toList()
    fun getAllWithIds(): List<PistaEntity> = storage.map { PistaEntity(it.key, it.value) }
    fun findById(id: Int): PistaModel? = storage[id]

    fun buscar(query: String): List<PistaEntity> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return getAllWithIds()
        return storage.filter { (_, p) ->
            p.piloto.nombre.lowercase().contains(q) ||
                    p.escuderia.nombre.lowercase().contains(q) ||
                    p.estado.tipoEstado.lowercase().contains(q) ||
                    p.mecanico.nombre.lowercase().contains(q) ||
                    (p.motivoFallo?.lowercase()?.contains(q) == true) ||
                    p.tiempoSegundos.toString().contains(q)
        }.map { PistaEntity(it.key, it.value) }
    }

    fun pistaMasRapida(): PistaEntity? {
        val entry = storage.minByOrNull { it.value.tiempoSegundos }
        return entry?.let { PistaEntity(it.key, it.value) }
    }

    fun promedioTiempos(): Double {
        val list = storage.values.map { it.tiempoSegundos }
        if (list.isEmpty()) return 0.0
        val avg = list.average()
        return (round(avg * 100) / 100.0)
    }

    fun totalPistas(): Int = storage.size

    fun datosParaGrafica(maxElements: Int = 6): List<Double> {
        return storage.entries
            .sortedByDescending { it.value.fecha }
            .take(maxElements)
            .map { it.value.tiempoSegundos.toDouble() }
    }

    private fun validarPista(pista: PistaModel): Pair<Boolean, String?> {
        if (pista.tiempoSegundos < 0) return Pair(false, "El tiempo debe ser >= 0")
        if (pista.neumatico.cantidad < 0) return Pair(false, "Cantidad de neumáticos inválida")
        if (pista.estado.tipoEstado.equals("Fallido", ignoreCase = true) && pista.motivoFallo.isNullOrBlank())
            return Pair(false, "Si el estado es 'Fallido', debe indicar el motivo")

        return Pair(true, null)
    }

    // ✅ UTILIDADES / DATOS DE PRUEBA
    fun preloadEjemplo() {
        storage.clear()
        idCounter.set(1)

        val p1 = Piloto("Oliveiro")
        val p2 = Piloto("James")
        val p3 = Piloto("Mark")

        val esc1 = Escuderia("Mercedes")
        val esc2 = Escuderia("Red Bull")

        val m1 = Mecanico("Juan Pérez")
        val m2 = Mecanico("John Doe")

        val nSoft = Neumatico("Soft", 4)
        val nMedium = Neumatico("Medium", 4)

        val ok = Estado("OK")
        val fallo = Estado("Fallido")

        val ahora = System.currentTimeMillis()
        crearPista(PistaModel(p1, esc1, 2, nSoft.copy(cantidad = 4), ok, null, m1, ahora - 12 * 60 * 1000))
        crearPista(PistaModel(p2, esc2, 2, nMedium.copy(cantidad = 4), fallo, "Tuerca atascada", m2, ahora - 8 * 60 * 1000))
        crearPista(PistaModel(p3, esc1, 3, nSoft.copy(cantidad = 2), ok, null, m1, ahora - 6 * 60 * 1000))
    }
}
