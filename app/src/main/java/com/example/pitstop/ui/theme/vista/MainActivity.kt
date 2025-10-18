package com.example.pitstop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

// Navigation imports
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.pitstop.controlador.PistaController
import com.example.pitstop.controlador.PistaController.PistaEntity
import com.example.pitstop.modelo.*
import java.text.SimpleDateFormat
import java.util.*

// Alternativa a enableEdgeToEdge
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    private val pistaController = PistaController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // alternativa portable a enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // carga datos de ejemplo
        pistaController.preloadEjemplo()

        setContent {
            MaterialTheme {
                AppNav(pistaController)
            }
        }
    }
}

@Composable
fun AppNav(controller: PistaController) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "summary") {
        // pantallas
        composable("summary") { SummaryScreen(navController, controller) }
        composable("list")    { ListScreen(navController, controller) }
        composable("register") { RegisterPitStopScreen(navController, controller) } // <-- nueva ruta
        composable("add")     { AddEditScreen(navController, controller, editId = null) }
        composable(
            route = "edit/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { back ->
            val id = back.arguments?.getInt("id")
            AddEditScreen(navController, controller, editId = id)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(nav: NavHostController, controller: PistaController) {
    val total by remember { derivedStateOf { controller.totalPistas() } }
    val avg by remember { derivedStateOf { controller.promedioTiempos() } }
    val fastest by remember { derivedStateOf { controller.pistaMasRapida() } }
    val recent by remember { derivedStateOf { controller.datosParaGrafica() } }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Resumen") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { nav.navigate("register") }) {
                Icon(Icons.Default.Add, contentDescription = "Registro")
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card {
                Column(Modifier.padding(12.dp)) {
                    Text("Total de paradas: $total", style = MaterialTheme.typography.titleMedium)
                    Text("Promedio: $avg s")
                    fastest?.let {
                        Text("Mejor: ${it.pista.tiempoSegundos}s — ${it.pista.piloto.nombre}")
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Botón grande y claro para abrir la pantalla de registro
            Button(
                onClick = { nav.navigate("register") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Registrar Pit Stop", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.height(8.dp))

            Text("Últimos tiempos:")
            LazyColumn { // reuse LazyColumn para simplicidad
                items(recent) { t ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Text("${t}s", Modifier.padding(12.dp))
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { nav.navigate("list") }, modifier = Modifier.weight(1f)) { Text("Ver Lista") }
                Button(onClick = { nav.navigate("add") }, modifier = Modifier.weight(1f)) { Text("Crear Pit Stop (form antiguo)") }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(nav: NavHostController, controller: PistaController) {
    var items by remember { mutableStateOf(controller.getAllWithIds()) }

    DisposableEffect(controller) {
        val listener = object : PistaController.OnDataChangedListener {
            override fun onDataChanged(all: List<PistaEntity>) { items = all }
        }
        controller.addListener(listener)
        onDispose { controller.removeListener(listener) }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Lista de Pit Stops") }) },
        floatingActionButton = {
            // Desde la lista también un acceso rápido a registro
            FloatingActionButton(onClick = { nav.navigate("register") }) { Icon(Icons.Default.Add, contentDescription = "Registro") }
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(12.dp)) {
            if (items.isEmpty()) Text("No hay registros", Modifier.padding(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items) { item ->
                    Card(Modifier.fillMaxWidth()) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("Piloto: ${item.pista.piloto.nombre}", style = MaterialTheme.typography.titleMedium)
                                Text("${item.pista.escuderia.nombre} • ${item.pista.tiempoSegundos}s")
                                Text(formatTimestampSafe(item.pista.fecha))
                            }
                            IconButton(onClick = { nav.navigate("edit/${item.id}") }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                            IconButton(onClick = { controller.eliminarPista(item.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(nav: NavHostController, controller: PistaController, editId: Int?) {
    var piloto by remember { mutableStateOf("") }
    var escuderia by remember { mutableStateOf("") }
    var mecanico by remember { mutableStateOf("") }
    var neum by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("4") }
    var tiempo by remember { mutableStateOf("0") }
    var estado by remember { mutableStateOf("OK") }
    var motivo by remember { mutableStateOf("") }

    LaunchedEffect(editId) {
        editId?.let {
            controller.findById(it)?.let { p ->
                piloto = p.piloto.nombre
                escuderia = p.escuderia.nombre
                mecanico = p.mecanico.nombre
                neum = p.neumatico.nombre
                cantidad = p.neumatico.cantidad.toString()
                tiempo = p.tiempoSegundos.toString()
                estado = p.estado.tipoEstado
                motivo = p.motivoFallo ?: ""
            }
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text(if (editId == null) "Crear Pit Stop" else "Editar Pit Stop") }) }) { padding ->
        Column(Modifier.padding(padding).padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = piloto, onValueChange = { piloto = it }, label = { Text("Piloto") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = escuderia, onValueChange = { escuderia = it }, label = { Text("Escudería") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = mecanico, onValueChange = { mecanico = it }, label = { Text("Mecánico") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = neum, onValueChange = { neum = it }, label = { Text("Neumático") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = cantidad, onValueChange = { cantidad = it }, label = { Text("Cantidad") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = tiempo, onValueChange = { tiempo = it }, label = { Text("Tiempo (s)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = estado, onValueChange = { estado = it }, label = { Text("Estado (OK/Fallido)") }, modifier = Modifier.fillMaxWidth())
            if (estado.equals("Fallido", ignoreCase = true)) {
                OutlinedTextField(value = motivo, onValueChange = { motivo = it }, label = { Text("Motivo") }, modifier = Modifier.fillMaxWidth())
            }

            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    val tiempoInt = tiempo.toIntOrNull() ?: 0
                    val cantidadInt = cantidad.toIntOrNull() ?: 0
                    val p = Piloto(piloto.ifBlank { "Piloto" })
                    val e = Escuderia(escuderia.ifBlank { "Escudería" })
                    val m = Mecanico(mecanico.ifBlank { "Mecánico" })
                    val n = Neumatico(neum.ifBlank { "Neumático" }, cantidadInt)
                    val est = Estado(estado.ifBlank { "OK" })
                    val fechaNow = System.currentTimeMillis()
                    val nueva = Pista(p, e, tiempoInt, n, est, motivo.ifBlank { null }, m, fechaNow)

                    if (editId == null) controller.crearPista(nueva)
                    else controller.actualizarPista(editId, nueva)

                    nav.popBackStack()
                }, modifier = Modifier.weight(1f)) {
                    Text("Guardar")
                }

                Button(onClick = { nav.popBackStack() }, modifier = Modifier.weight(1f)) {
                    Text("Cancelar")
                }
            }
        }
    }
}

/* -------------------------
   UTIL: formatear timestamp seguro
   ------------------------- */
fun formatTimestampSafe(value: Any?): String {
    return try {
        when (value) {
            is Long -> {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                sdf.format(Date(value))
            }
            is Number -> {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                sdf.format(Date(value.toLong()))
            }
            else -> ""
        }
    } catch (e: Exception) { "" }
}

// -------------------------
// Formulario RegisterPitStopScreen
// -------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPitStopScreen(
    navController: NavHostController,
    controller: PistaController
) {
    // Estados del formulario
    var piloto by remember { mutableStateOf("") }
    var escuderia by remember { mutableStateOf("") }
    var mecanico by remember { mutableStateOf("") }
    var neum by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("4") }
    var tiempo by remember { mutableStateOf("0") }
    var estado by remember { mutableStateOf("OK") }
    var motivo by remember { mutableStateOf("") }
    var timestamp by remember { mutableStateOf(System.currentTimeMillis()) }

    // Snackbar + scope para lanzar coroutines
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // LocalContext y calendar para Date/Time pickers
    val context = LocalContext.current
    val calendar = remember { java.util.Calendar.getInstance().apply { timeInMillis = timestamp } }

    fun openDatePicker() {
        val dp = android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(java.util.Calendar.YEAR, year)
                calendar.set(java.util.Calendar.MONTH, month)
                calendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth)
                timestamp = calendar.timeInMillis
            },
            calendar.get(java.util.Calendar.YEAR),
            calendar.get(java.util.Calendar.MONTH),
            calendar.get(java.util.Calendar.DAY_OF_MONTH)
        )
        dp.show()
    }

    fun openTimePicker() {
        val tp = android.app.TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                calendar.set(java.util.Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(java.util.Calendar.MINUTE, minute)
                timestamp = calendar.timeInMillis
            },
            calendar.get(java.util.Calendar.HOUR_OF_DAY),
            calendar.get(java.util.Calendar.MINUTE),
            true
        )
        tp.show()
    }

    fun fmt(ts: Long): String {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(ts))
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Registrar Pit Stop") }) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            OutlinedTextField(
                value = piloto,
                onValueChange = { piloto = it },
                label = { Text("Piloto") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = escuderia,
                onValueChange = { escuderia = it },
                label = { Text("Escudería") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = mecanico,
                onValueChange = { mecanico = it },
                label = { Text("Mecánico responsable") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = neum,
                    onValueChange = { neum = it },
                    label = { Text("Neumático (tipo)") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Cantidad") },
                    modifier = Modifier.width(120.dp),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = tiempo,
                onValueChange = { tiempo = it.filter { ch -> ch.isDigit() } },
                label = { Text("Tiempo (s)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Estado: ", modifier = Modifier.padding(end = 6.dp))
                RadioButton(selected = estado == "OK", onClick = { estado = "OK" })
                Text("OK", modifier = Modifier.padding(end = 8.dp))
                RadioButton(selected = estado == "Fallido", onClick = { estado = "Fallido" })
                Text("Fallido")
            }

            if (estado.equals("Fallido", true)) {
                OutlinedTextField(
                    value = motivo,
                    onValueChange = { motivo = it },
                    label = { Text("Motivo (requerido)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Fecha/hora: ${fmt(timestamp)}", modifier = Modifier.weight(1f))
                Button(onClick = { openDatePicker() }) { Text("Fecha") }
                Button(onClick = { openTimePicker() }) { Text("Hora") }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    // VALIDACIONES
                    val tiempoInt = tiempo.toIntOrNull()
                    val cantInt = cantidad.toIntOrNull()

                    when {
                        piloto.isBlank() -> scope.launch { snackbarHostState.showSnackbar("Ingrese el nombre del piloto") }
                        escuderia.isBlank() -> scope.launch { snackbarHostState.showSnackbar("Ingrese la escudería") }
                        mecanico.isBlank() -> scope.launch { snackbarHostState.showSnackbar("Ingrese el mecánico responsable") }
                        neum.isBlank() -> scope.launch { snackbarHostState.showSnackbar("Ingrese el tipo de neumático") }
                        cantInt == null || cantInt < 0 -> scope.launch { snackbarHostState.showSnackbar("Cantidad inválida") }
                        tiempoInt == null || tiempoInt < 0 -> scope.launch { snackbarHostState.showSnackbar("Tiempo inválido") }
                        estado.equals("Fallido", true) && motivo.isBlank() -> scope.launch { snackbarHostState.showSnackbar("Ingrese motivo para parada fallida") }
                        else -> {
                            // construir objetos modelo y guardar
                            val p = Piloto(piloto.trim())
                            val e = Escuderia(escuderia.trim())
                            val m = Mecanico(mecanico.trim())
                            val n = Neumatico(neum.trim(), cantInt!!)
                            val est = Estado(estado.trim())
                            val nueva = Pista(p, e, tiempoInt!!, n, est, motivo.ifBlank { null }, m, timestamp)

                            controller.crearPista(nueva)
                            scope.launch { snackbarHostState.showSnackbar("Pit stop registrado") }
                            navController.popBackStack()
                        }
                    }
                }, modifier = Modifier.weight(1f)) {
                    Text("Guardar")
                }

                OutlinedButton(onClick = { navController.popBackStack() }, modifier = Modifier.weight(1f)) {
                    Text("Cancelar")
                }
            }
        }
    }
}
