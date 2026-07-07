# Reglas de la capa View (presentación)

> Convenciones para construir pantallas en este proyecto.
> Pantalla de referencia (patrón canónico): **`views/listaContacto/`**.

El objetivo es que toda pantalla siga el mismo molde MVVM, de forma que el
`AppHost` no conozca nada de estado ni de repositorios: solo conecta navegación.

---

## 1. Estructura de archivos por pantalla

Cada pantalla vive en **su propia carpeta** bajo `views/<nombrePantalla>/` y se
compone de **tres archivos**:

```
views/
└── <nombrePantalla>/
    ├── <Nombre>UiState.kt   ← estado + acciones + preview provider
    ├── <Nombre>Screen.kt    ← Composable STATELESS (solo pinta)
    └── <Nombre>Root.kt      ← Composable STATEFUL (conecta el ViewModel)
```

El `ViewModel` (y su `Factory`) vive aparte, en `viewmodels/<Nombre>ViewModel.kt`.

| Capa | Conoce a... | NO conoce a... |
|------|-------------|----------------|
| `AppHost` | los `Root` y las rutas | UiState, Actions, ViewModel, repositorio |
| `Root` | ViewModel, Screen, UiState, Actions | NavController / rutas |
| `Screen` | UiState, Actions, componentes | ViewModel, repositorio, navegación |
| `ViewModel` | UiState, repositorio | Screen, Root, navegación |

---

## 2. `<Nombre>UiState.kt`

Contiene **tres** declaraciones y nada más:

### 2.1. El `UiState` — usar **sealed interface**

El estado de pantalla se modela como **sealed interface** con un subtipo por
cada estado mutuamente excluyente. Esto elimina estados imposibles (no se puede
estar "cargando" y "con error" a la vez).

Estados base esperados: **`Loading`, `Empty`, `Error`, `Success`**.

> **Regla del `query`/búsqueda:** lo transversal a un estado concreto vive
> **dentro de ese estado**, no en un wrapper global. En particular, **la búsqueda
> solo importa si hay contenido**, así que `query` (y el filtrado) viven dentro
> de `Success`. En `Loading`/`Empty`/`Error` no hay `SearchBar`.

```kotlin
sealed interface ListaContactoUiState {

    data object Loading : ListaContactoUiState

    data object Empty : ListaContactoUiState

    data class Error(val message: String) : ListaContactoUiState

    data class Success(
        val contactos: List<Contacto>,
        val query: String = ""
    ) : ListaContactoUiState {

        // Estado derivado: se calcula, NO se guarda como campo mutable aparte.
        val contactosFiltrados: List<Contacto>
            get() = if (query.isBlank()) contactos
                    else contactos.filter {
                        it.nombreCompleto.contains(query, ignoreCase = true)
                    }
    }
}
```

Reglas del UiState:
- **Inmutable** (`val`, `data class`/`data object`).
- **Solo datos de presentación** (ya formateados para pintar). Nada de tipos de
  red, entidades de BD ni lógica de negocio.
- El **estado derivado** (ej. `contactosFiltrados`) se expone como propiedad
  calculada (`get()`), no como campo duplicado.

### 2.2. El `Actions` — solo callbacks

Un `data class` que agrupa **todos los eventos** que la pantalla emite hacia
arriba. Son funciones, nunca lógica.

```kotlin
data class ListaContactoActions(
    val onNavigateToAddContact: () -> Unit = {},
    val onNavigateToDetail: (Contacto) -> Unit = {},
    val onNavigateToFavoritos: () -> Unit = {},
    val onToggleFavorito: (Contacto) -> Unit = {},
    val onQueryChange: (String) -> Unit = {},
    val onClear: () -> Unit = {}
)
```

Reglas:
- Todos los callbacks con **valor por defecto vacío** (`= {}`) → habilita previews
  y tests sin pasar nada.
- Nombrar los eventos por lo que **ocurre** (`onQueryChange`, `onToggleFavorito`),
  no por lo que hace el ViewModel internamente.

### 2.3. El `PreviewParameterProvider`

Provee una muestra de cada estado posible para los `@Preview`. Con la sealed
interface, debe cubrir **todos los subtipos**.

```kotlin
class ListaContactoPreviewParameterProvider :
    PreviewParameterProvider<ListaContactoUiState> {
    override val values = sequenceOf(
        ListaContactoUiState.Loading,
        ListaContactoUiState.Empty,
        ListaContactoUiState.Error("No se pudo cargar"),
        ListaContactoUiState.Success(contactos = ContactosMuestra.lista),
        ListaContactoUiState.Success(contactos = ContactosMuestra.lista, query = "Yael")
    )
}
```

---

## 3. `<Nombre>Screen.kt` — STATELESS

El `Screen` **solo pinta** un `UiState` y **emite eventos** vía `Actions`. No
recuerda estado propio de negocio, no instancia ViewModels, no navega.

Firma obligatoria:

```kotlin
@Composable
fun ListaContactoScreen(
    modifier: Modifier = Modifier,
    uiState: ListaContactoUiState,
    actions: ListaContactoActions
)
```

Reglas:
- Recibe **exactamente** `uiState` + `actions` (+ `modifier`). Nada de parámetros
  sueltos tipo `fullName`, `onGuardar`, etc.
- Resuelve los estados con un `when` **exhaustivo** sobre la sealed interface.
  El `SearchBar` se pinta **solo dentro de `Success`** (ver regla del `query`):

```kotlin
when (uiState) {
    ListaContactoUiState.Loading  -> LoadingState()
    ListaContactoUiState.Empty    -> EmptyContactos()
    is ListaContactoUiState.Error -> ErrorState(uiState.message)
    is ListaContactoUiState.Success -> {
        SearchBar(
            query = uiState.query,
            onQueryChange = actions.onQueryChange,
            onClear = actions.onClear
        )
        LazyColumn {
            items(uiState.contactosFiltrados) { contacto -> /* ... */ }
        }
    }
}
```

- El `@Preview` consume el `PreviewParameterProvider` y pasa `Actions()` por
  defecto.

---

## 4. `<Nombre>Root.kt` — STATEFUL

El `Root` es el **único punto** que conoce el ViewModel. Su trabajo:

1. Crear repositorio + factory + ViewModel.
2. Observar el `uiState`.
3. Construir el `Actions` mapeando cada callback a un método del ViewModel o a
   una lambda de navegación recibida por parámetro.
4. Disparar la carga inicial (`LaunchedEffect`).
5. Llamar al `Screen`.

```kotlin
@Composable
fun ListaContactoRoot(
    onNavigateToAddContact: () -> Unit,
    onNavigateToDetail: (Contacto) -> Unit,
    onNavigateToFavoritos: () -> Unit
) {
    val repository = remember { ContactoRepositoryFake() }
    val factory = remember { ListaContactoViewModelFactory(repository) }
    val viewModel: ListaContactoViewModel = viewModel(factory = factory)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val actions = remember(viewModel) {
        ListaContactoActions(
            onNavigateToAddContact = onNavigateToAddContact,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToFavoritos = onNavigateToFavoritos,
            onToggleFavorito = { contacto -> viewModel.alternarFavorito(contacto.id) },
            onQueryChange = { viewModel.onQueryChange(it) },
            onClear = { viewModel.onClear() }
        )
    }

    LaunchedEffect(Unit) { viewModel.getContactList() }

    ListaContactoScreen(uiState = uiState, actions = actions)
}
```

Reglas:
- Las **lambdas de navegación** entran como **parámetros** del `Root` (las provee
  el `AppHost`); el `Root` no toca `NavController`.
- Si la pantalla necesita un argumento (ej. `id`), entra como parámetro del `Root`
  y se carga en `LaunchedEffect(id) { viewModel.cargar(id) }`.
- `actions` se memoiza con `remember(viewModel)`.

---

## 5. `AppHost` — solo navegación

El `AppHost` **solo** mapea ruta → `Root` y pasa lambdas de navegación.
**Prohibido** en el `AppHost`: construir `UiState`, construir `Actions`,
instanciar ViewModels, leer del repositorio o resolver datos (ej.
`ContactosMuestra.porId`).

```kotlin
composable<ListaContacto> {
    ListaContactoRoot(
        onNavigateToAddContact = { navController.navigate(AgregaContacto) },
        onNavigateToDetail = { c -> navController.navigate(DetalleContacto(id = c.id)) },
        onNavigateToFavoritos = { navController.navigateToTab(Favoritos) }
    )
}
```

Si una pantalla recibe argumentos de ruta, el `AppHost` los extrae y los pasa al
`Root` como parámetro simple:

```kotlin
composable<DetalleContacto> { backStackEntry ->
    val ruta = backStackEntry.toRoute<DetalleContacto>()
    DetalleContactoRoot(
        id = ruta.id,
        onBack = { navController.navigateUp() },
        onEdit = { navController.navigate(EditaContacto(id = ruta.id)) },
        onEliminado = { navController.popBackStack(/* ... */) }
    )
}
```

---

## 6. `ViewModel` (recordatorio — vive en `viewmodels/`)

- Expone `val uiState: StateFlow<<Nombre>UiState>` (privado `MutableStateFlow`,
  público `asStateFlow()`).
- Cambia de estado con `_uiState.update { ... }`, emitiendo el subtipo correcto
  de la sealed interface (`Loading` → `Success`/`Empty`/`Error`).
- Maneja el **caso de error** envolviendo la llamada al repositorio en
  `try/catch` y emitiendo `Error(...)`.
- No conoce navegación: para acciones que terminan navegando (guardar, eliminar)
  recibe un callback `onListo: () -> Unit` que el `Root` conecta a la navegación.

---

## 7. Checklist para una pantalla nueva

- [ ] Carpeta `views/<nombre>/` con `UiState.kt`, `Screen.kt`, `Root.kt`.
- [ ] `UiState` como **sealed interface** con `Loading`/`Empty`/`Error`/`Success`
      (el estado transversal vive en el subtipo donde aplica, p.ej. `query` en
      `Success`).
- [ ] `Actions` con todos los callbacks por defecto vacíos.
- [ ] `PreviewParameterProvider` cubriendo todos los subtipos.
- [ ] `Screen` stateless: firma `(modifier, uiState, actions)` + `when` exhaustivo.
- [ ] `Root`: repositorio + factory + viewModel + actions + `LaunchedEffect`.
- [ ] `ViewModel` + `Factory` en `viewmodels/`, con manejo de `Error`.
- [ ] `AppHost`: solo `Root` + lambdas de navegación.
