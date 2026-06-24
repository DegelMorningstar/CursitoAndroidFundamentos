# Arquitectura MVVM — Propuesta

**Contacts Manager · Jetpack Compose · Kotlin**
_Propuesta basada en los archivos existentes en `views/`, `data/`, `viewmodels/` y `navigation/`._

---

## 1. Objetivo

Pasar de la fase actual ("datos en duro" leídos directo en las pantallas) a una
arquitectura **MVVM con flujo de datos unidireccional (UDF)**, de modo que:

- Las pantallas (`views/`) queden **sin lógica ni estado propio**: solo dibujan un
  `UiState` y emiten eventos.
- El estado y la lógica vivan en **ViewModels** (`viewmodels/`).
- La fuente de datos sea intercambiable: hoy `ContactoRepositoryFake`, mañana una
  implementación con Retrofit, **sin tocar ViewModels ni UI**.

Lo que ya está alineado con esto:

| Pieza existente | Estado |
|---|---|
| `views/*Screen.kt` | Ya son stateless: reciben parámetros + callbacks (`onGuardar`, `onNavigateToDetail`...). ✅ |
| `data/Contacto.kt` | `data class` plano, `@Serializable`. ✅ |
| `data/ContactoRepository.kt` | Interfaz `suspend` con forma REST. ✅ |
| `data/ContactoRepositoryFake.kt` | Implementación en memoria (datos en duro). ✅ |
| `viewmodels/ListaContactoViewModel.kt` | Existe pero **vacío**. ⏳ |
| `views/components/FormularioContactoCard.kt` | Ya expone `value + onXChange + error` → listo para hoisting. ✅ |
| Lectura directa de `ContactosMuestra` en `AppHost` y pantallas | **Temporal**, se elimina con MVVM. ❌ |

---

## 2. Las tres capas y el flujo unidireccional

```
        ┌─────────────────────────────────────────────────────────┐
        │                      UI (views/)                         │
        │   Composables stateless: pintan UiState, emiten eventos  │
        └───────────────▲─────────────────────────┬───────────────┘
        estado (StateFlow)                    eventos (lambdas)
        ┌───────────────┴─────────────────────────▼───────────────┐
        │                ViewModel (viewmodels/)                   │
        │   Mantiene UiState, ejecuta casos de uso, llama al repo  │
        └───────────────▲─────────────────────────┬───────────────┘
        datos (suspend / Flow)                 acciones (suspend)
        ┌───────────────┴─────────────────────────▼───────────────┐
        │                    Datos (data/)                         │
        │   ContactoRepository ── Fake (hoy) / Api (mañana)        │
        └─────────────────────────────────────────────────────────┘
```

**Regla de oro (UDF):** el estado **baja** (de ViewModel a UI) y los eventos
**suben** (de UI a ViewModel). La UI nunca modifica datos directamente ni guarda
estado de negocio.

---

## 3. Estructura de paquetes propuesta

Se conserva todo lo actual; en **negrita** lo que se agrega.

```
com.yaeldev.cursitodefundamentosandroid
│
├── data/                         ← capa de datos (YA EXISTE)
│   ├── Contacto.kt
│   ├── ContactoRepository.kt
│   ├── ContactoRepositoryFake.kt
│   ├── ContactosMuestra.kt
│   └── **ContactoRepositoryApi.kt**   ← futuro (Retrofit)
│
├── **di/**                       ← inyección de dependencias manual
│   └── **AppContainer.kt**           ← crea y comparte el repositorio único
│
├── viewmodels/                   ← capa de presentación (YA EXISTE el paquete)
│   ├── ListaContactoViewModel.kt     ← lista + búsqueda (hoy vacío)
│   ├── **FavoritosViewModel.kt**
│   ├── **DetalleContactoViewModel.kt**
│   ├── **FormularioContactoViewModel.kt** ← agregar y editar comparten formulario
│   └── **uistate/**                  ← un UiState por pantalla
│       ├── **ListaUiState.kt**
│       ├── **DetalleUiState.kt**
│       └── **FormularioUiState.kt**
│
├── views/                        ← capa de UI (YA EXISTE, queda stateless)
│   ├── ListaContactoScreen.kt
│   ├── FavoritosScreen.kt
│   ├── DetalleContactoScreen.kt
│   ├── AgregarContactoScreen.kt
│   ├── EditarContactoScreen.kt
│   └── components/ ...
│
├── navigation/                   ← YA EXISTE
│   ├── AppRoutes.kt
│   └── AppHost.kt                    ← obtiene los ViewModels por destino
│
├── ui/theme/                     ← YA EXISTE
│
└── models/                       ← ejercicio de fundamentos (NO se toca)
```

> Nota: `models/` (la jerarquía polimórfica `Contacto`, `AgendaGlobal`, etc.) es
> material del curso y queda **intacto**. La app vive en `data/` + `viewmodels/` +
> `views/`.

---

## 4. Responsabilidades por capa

### 4.1 Capa de datos — `data/`
- **Única fuente de verdad** de los contactos.
- `ContactoRepository` expone operaciones `suspend` con forma REST (ya hecho).
- La implementación (`Fake` o `Api`) es un detalle oculto tras la interfaz.
- **No conoce** Compose, ViewModels ni Android UI.

### 4.2 Capa de presentación — `viewmodels/`
- Cada ViewModel:
  - Mantiene un `StateFlow<XxxUiState>` (estado observable e inmutable).
  - Traduce eventos de la UI en llamadas al repositorio (dentro de
    `viewModelScope`).
  - Mapea el resultado del repo a `UiState` (incluye `cargando` y `error`).
- **No conoce** Composables concretos ni el `NavController` (la navegación se
  resuelve en la UI/`AppHost` mediante callbacks o eventos one-shot).

### 4.3 Capa de UI — `views/`
- Composables **stateless**: reciben `uiState` + lambdas de eventos.
- Patrón **stateful wrapper**: una función "ruta" obtiene el ViewModel y llama a
  la pantalla stateless (ver §6). Así las `@Preview` siguen funcionando sin
  ViewModel.
- Sin `remember { mutableStateOf(...) }` de datos de negocio; solo estado
  puramente visual (scroll, foco) si hace falta.

### 4.4 Navegación — `navigation/`
- Las rutas siguen llevando solo el `id` (ya hecho).
- `AppHost` deja de leer `ContactosMuestra`: cada destino obtiene su ViewModel,
  que carga el dato por id desde el repositorio.

---

## 5. Patrón de estado: `UiState` + eventos

Un `data class` inmutable por pantalla. Ejemplo para la lista:

```kotlin
// viewmodels/uistate/ListaUiState.kt
data class ListaUiState(
    val contactos: List<Contacto> = emptyList(),
    val query: String = "",
    val cargando: Boolean = false,
    val error: String? = null
) {
    // Lógica de presentación derivada (no se guarda, se calcula):
    val contactosFiltrados: List<Contacto>
        get() = if (query.isBlank()) contactos
                else contactos.filter { it.nombreCompleto.contains(query, ignoreCase = true) }

    val vacio: Boolean get() = !cargando && contactosFiltrados.isEmpty()
}
```

**Eventos one-shot** (snackbar "Contacto agregado ✓", navegar tras guardar): no
van en el `UiState` (se re-emitirían al rotar). Se exponen con un `Channel` →
`Flow`, o un `SharedFlow`:

```kotlin
private val _eventos = Channel<FormularioEvento>()
val eventos = _eventos.receiveAsFlow()

sealed interface FormularioEvento {
    data object Guardado : FormularioEvento          // la UI hace navigateUp + snackbar
    data class Error(val mensaje: String) : FormularioEvento
}
```

---

## 6. ViewModels propuestos

| ViewModel | Pantalla(s) | Responsabilidad |
|---|---|---|
| `ListaContactoViewModel` | `ListaContactoScreen` | Cargar todos, búsqueda en vivo, alternar favorito |
| `FavoritosViewModel` | `FavoritosScreen` | Cargar solo `favorite = true`, alternar favorito |
| `DetalleContactoViewModel` | `DetalleContactoScreen` | Cargar por `id`, alternar favorito, eliminar, compartir |
| `FormularioContactoViewModel` | `Agregar` + `Editar` | Estado del formulario, validación, crear/actualizar/eliminar |

> Alternativa: un único `ContactosViewModel` compartido a nivel de grafo de
> navegación para lista + favoritos + detalle. Es más simple para que el favorito
> se refleje en todas las pantallas, pero acopla más. **Recomendado empezar con
> uno por pantalla** y dejar que el favorito se sincronice a través del
> repositorio (única fuente de verdad).

### 6.1 Sketch — `ListaContactoViewModel`

```kotlin
class ListaContactoViewModel(
    private val repositorio: ContactoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListaUiState(cargando = true))
    val uiState: StateFlow<ListaUiState> = _uiState.asStateFlow()

    init { cargar() }

    fun cargar() = viewModelScope.launch {
        _uiState.update { it.copy(cargando = true, error = null) }
        runCatching { repositorio.obtenerTodos() }
            .onSuccess { lista -> _uiState.update { it.copy(contactos = lista, cargando = false) } }
            .onFailure { e -> _uiState.update { it.copy(error = e.message, cargando = false) } }
    }

    fun onQueryChange(nueva: String) = _uiState.update { it.copy(query = nueva) }

    fun onToggleFavorito(id: Int) = viewModelScope.launch {
        repositorio.alternarFavorito(id)
        cargar() // o actualizar la lista en memoria
    }
}
```

### 6.2 Sketch — formulario (agregar/editar) con `SavedStateHandle`

El VM de edición lee el `id` de la ruta directo del `SavedStateHandle`
(Navigation 2.9 soporta `toRoute`):

```kotlin
class FormularioContactoViewModel(
    private val repositorio: ContactoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // id presente => modo editar; ausente => modo agregar
    private val idEdicion: Int? = savedStateHandle.toRoute<EditaContacto>().id

    private val _uiState = MutableStateFlow(FormularioUiState())
    val uiState: StateFlow<FormularioUiState> = _uiState.asStateFlow()

    fun onGuardar() = viewModelScope.launch {
        val s = _uiState.value
        if (!validar(s)) return@launch
        val contacto = Contacto(
            id = idEdicion ?: 0, first = s.nombre, last = s.apellido,
            phone = s.telefono, email = s.correo, company = s.empresa
        )
        if (idEdicion == null) repositorio.agregar(contacto)
        else repositorio.actualizar(contacto)
        _eventos.send(FormularioEvento.Guardado)
    }
}
```

### 6.3 Patrón stateful wrapper en la UI

```kotlin
// La pantalla stateless NO cambia: sigue recibiendo datos + callbacks.
// Se agrega un wrapper que conecta el ViewModel:

@Composable
fun ListaContactoRoute(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToAddContact: () -> Unit,
    onNavigateToFavoritos: () -> Unit,
    viewModel: ListaContactoViewModel = viewModel(factory = AppContainer.factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ListaContactoScreen(
        uiState = uiState,
        onQueryChange = viewModel::onQueryChange,
        onToggleFavorito = viewModel::onToggleFavorito,
        onNavigateToDetail = onNavigateToDetail,
        onNavigateToAddContact = onNavigateToAddContact,
        onNavigateToFavoritos = onNavigateToFavoritos
    )
}
```

---

## 7. Inyección de dependencias

Hoy no hay framework de DI. Para que **todos los ViewModels compartan la misma
instancia de repositorio** (clave para que el favorito se vea reflejado en todas
las pantallas), se propone **DI manual** con un contenedor:

```kotlin
// di/AppContainer.kt
object AppContainer {
    // Única instancia del repositorio (cambiar por ContactoRepositoryApi en el futuro).
    val repositorioContactos: ContactoRepository by lazy { ContactoRepositoryFake() }

    // Factory para ViewModels que necesitan el repositorio.
    val factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { ListaContactoViewModel(repositorioContactos) }
        initializer { FavoritosViewModel(repositorioContactos) }
        initializer { DetalleContactoViewModel(repositorioContactos, createSavedStateHandle()) }
        initializer { FormularioContactoViewModel(repositorioContactos, createSavedStateHandle()) }
    }
}
```

> **Evolución opcional:** cuando el proyecto crezca, migrar a **Hilt**
> (`@HiltViewModel`, `@Inject`, módulos). El cambio es mecánico porque las
> dependencias ya están definidas por constructor.

---

## 8. Camino a REST (sin tocar UI ni ViewModels)

1. Agregar Retrofit + el converter de `kotlinx.serialization` (ya está el plugin
   y `kotlinx-serialization-json` en `build.gradle.kts`).
2. Crear `ContactoApiService` (Retrofit) con los endpoints `@GET/@POST/@PUT/@DELETE`.
3. Crear `data/ContactoRepositoryApi.kt : ContactoRepository` que llama al service
   y mapea DTO ↔ `Contacto` (o usa `Contacto` directo, pues ya es `@Serializable`).
4. En `AppContainer`, cambiar **una línea**:
   `ContactoRepositoryFake()` → `ContactoRepositoryApi(...)`.

Como `Contacto` ya es `@Serializable` y la interfaz ya es `suspend`, los
ViewModels y la UI **no cambian**.

---

## 9. Plan de implementación incremental

- [ ] **1. UiState**: crear `ListaUiState`, `DetalleUiState`, `FormularioUiState`.
- [ ] **2. DI**: crear `di/AppContainer.kt` con el repositorio único y la factory.
- [ ] **3. ListaContactoViewModel**: implementar (carga + búsqueda + favorito) y
      conectar `ListaContactoScreen` vía `ListaContactoRoute`. Quitar
      `ContactosMuestra` de esa pantalla.
- [ ] **4. FavoritosViewModel**: ídem, filtrando favoritos.
- [ ] **5. DetalleContactoViewModel**: cargar por `id`; quitar la resolución por
      `ContactosMuestra` en `AppHost`.
- [ ] **6. FormularioContactoViewModel**: hoisting del formulario (ya hay
      `value/onXChange/error` en `FormularioContactoCard`), validación y
      crear/actualizar/eliminar. Conectar Agregar y Editar.
- [ ] **7. Eventos one-shot**: snackbars ("Contacto agregado ✓", "Cambios
      guardados ✓") y navegación tras guardar/eliminar.
- [ ] **8. Limpieza**: eliminar lecturas directas de `ContactosMuestra` en
      `views/` y `navigation/`. `ContactosMuestra` queda solo como semilla del
      `Fake`.
- [ ] **9. (Opcional)** Tests del repositorio fake y de los ViewModels.
- [ ] **10. (Futuro)** `ContactoRepositoryApi` + Retrofit.

---

## 10. Convenciones

- **Estado inmutable**: `data class` + `MutableStateFlow` + `.update { it.copy(...) }`.
- **Colección de estado en UI**: `collectAsStateWithLifecycle()`
  (artefacto `androidx.lifecycle:lifecycle-runtime-compose`).
- **Corrutinas**: toda operación de datos en `viewModelScope`; el repo es `suspend`.
- **Nombres en español** para dominio (`cargando`, `contactosFiltrados`,
  `onGuardar`) como el resto del proyecto; campos del modelo en inglés porque son
  el contrato del API (`first`, `phone`, `favorite`).
- **Una pantalla = un UiState = un ViewModel** (salvo Agregar/Editar, que
  comparten formulario).
- **La navegación se queda en la UI** (`AppHost`); los ViewModels emiten eventos,
  no navegan.

---

_Propuesta de arquitectura · Contacts Manager · Junio 2026_
```
