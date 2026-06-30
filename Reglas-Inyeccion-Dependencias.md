# Reglas de inyección de dependencias (DI manual)

> Cómo se construyen y comparten las dependencias (repositorio, ViewModels) en
> este proyecto **sin usar Hilt**.
> Relacionado: [[Reglas-Capa-View.md]] (cómo el `Root` consume el ViewModel).

El proyecto usa **DI manual con un contenedor** (`AppContainer`) colgado del
`Application`. Objetivo: que **toda la app comparta una sola instancia del
repositorio** y que crear un ViewModel sea **una línea** en el `Root`, sin clases
`Factory` por pantalla.

---

## 1. Piezas del patrón

```
CursitoApplication (Application)
        │  contiene
        ▼
AppContainer (interfaz)  ──impl──►  DefaultAppContainer
        ├── repositorio: ContactoRepository      (1 sola instancia, by lazy)
        └── factory: ViewModelProvider.Factory   (1 sola, con N initializers)
```

- **`Application`**: dueño del container durante todo el ciclo de vida del proceso.
- **`AppContainer` (interfaz)**: contrato de dependencias → permite sustituirlo por
  un fake en tests/previews.
- **`DefaultAppContainer`**: implementación real; crea el repo único y la factory
  compartida.
- **`factory`**: una sola `viewModelFactory` con **un `initializer` por ViewModel**.

---

## 2. Código base

### 2.1. El container (`di/AppContainer.kt`)

```kotlin
interface AppContainer {
    val repositorio: ContactoRepository
    val factory: ViewModelProvider.Factory
}

class DefaultAppContainer : AppContainer {

    // Única instancia del repositorio para toda la app.
    // Mañana: cambiar por ContactoRepositoryApi() sin tocar ViewModels ni UI.
    override val repositorio: ContactoRepository by lazy { ContactoRepositoryFake() }

    // Una sola factory: cada initializer registra un ViewModel por su tipo.
    override val factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { ListaContactoViewModel(repositorio) }
        initializer { FavoritosViewModel(repositorio) }
        initializer { AgregarContactoViewModel(repositorio) }
        // ViewModels que leen args de ruta -> createSavedStateHandle()
        initializer { DetalleContactoViewModel(repositorio, createSavedStateHandle()) }
        initializer { EditarContactoViewModel(repositorio, createSavedStateHandle()) }
    }
}
```

### 2.2. El `Application` (`CursitoApplication.kt`)

```kotlin
class CursitoApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
```

Registrarlo en `AndroidManifest.xml`:

```xml
<application
    android:name=".CursitoApplication"
    ... >
```

### 2.3. Helper para acceder al container desde Compose

```kotlin
@Composable
fun appContainer(): AppContainer =
    (LocalContext.current.applicationContext as CursitoApplication).container
```

---

## 3. Reglas

1. **Una sola instancia del repositorio.** Vive en `DefaultAppContainer` (`by
   lazy`). **Prohibido** hacer `ContactoRepositoryFake()` en cualquier otro lugar
   (Root, Screen, AppHost, ViewModel).
2. **Inyección por constructor.** Todo ViewModel recibe sus dependencias como
   parámetros del constructor (`repositorio`, `SavedStateHandle`). Nunca las crea
   por dentro. Esto es lo que hace la futura migración a Hilt mecánica.
3. **Una sola `factory` compartida.** No se crean clases `XxxViewModelFactory`. Para
   un ViewModel nuevo se agrega **un `initializer`** a la factory del container.
4. **Args de ruta vía `SavedStateHandle`.** Si el ViewModel necesita el `id` de la
   ruta, se le pasa `createSavedStateHandle()` en su `initializer`, y dentro del VM
   se lee con `savedStateHandle.toRoute<MiRuta>()`. **No** se pasa el `id` por
   parámetro del `Root` ni con `LaunchedEffect`.
5. **El container es interfaz.** Se depende de `AppContainer`, no de
   `DefaultAppContainer`, para poder sustituirlo en tests.
6. **El `Application` solo expone el container.** Nada de lógica de negocio ahí.

---

## 4. Cómo se consume en el `Root`

La creación del ViewModel queda en **una línea**:

```kotlin
// Pantalla sin args de ruta
@Composable
fun ListaContactoRoot(/* lambdas de navegación */) {
    val viewModel: ListaContactoViewModel = viewModel(factory = appContainer().factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) { /* ... */ }
    LaunchedEffect(Unit) { viewModel.getContactList() }
    ListaContactoScreen(uiState = uiState, actions = actions)
}
```

```kotlin
// Pantalla CON args de ruta: el VM lee el id solo (SavedStateHandle) y carga en init {}
@Composable
fun DetalleContactoRoot(onBack: () -> Unit, onEdit: () -> Unit, onEliminado: () -> Unit) {
    val viewModel: DetalleContactoViewModel = viewModel(factory = appContainer().factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) { /* ... */ }
    // Sin LaunchedEffect(id): el VM ya conoce el id.
    DetalleContactoScreen(uiState = uiState, actions = actions)
}
```

> Para que `createSavedStateHandle().toRoute<...>()` funcione, el `viewModel()`
> debe invocarse **dentro del `composable<Ruta>`** correspondiente (su
> `ViewModelStoreOwner` es el `NavBackStackEntry` que trae los args). El `Root`
> ya se llama ahí, así que se cumple solo.

---

## 5. Checklist: agregar un ViewModel nuevo

- [ ] El ViewModel recibe sus dependencias **por constructor** (`repositorio`, y
      `SavedStateHandle` si lee args de ruta).
- [ ] Agregar **un `initializer { ... }`** en `DefaultAppContainer.factory`.
- [ ] En el `Root`: `viewModel(factory = appContainer().factory)` (una línea).
- [ ] **No** crear clase `Factory`, **no** instanciar el repositorio, **no** pasar
      `id` por `LaunchedEffect` si se puede usar `SavedStateHandle`.

---

## 6. Nota — estudiar Hilt más adelante

Este container manual es la decisión correcta **para el tamaño actual** del
proyecto (pocas pantallas, un repositorio): cero librerías, fácil de entender, y
deja ver con claridad el flujo `UiState → ViewModel → repositorio`.

**Es eficiente en runtime incluso con muchos ViewModels**, pero **no escala en
mantenibilidad**: a partir de ~15–20 ViewModels (o al partir la app en módulos
Gradle), el container único se vuelve un *god object* — un solo archivo que
ensambla a mano todo el grafo de dependencias, con conflictos de merge y sin
scoping por feature.

**Umbral de migración a Hilt:**

| Tamaño | Recomendación |
|---|---|
| ~5–15 ViewModels, 1 módulo | Container manual (este patrón) ✅ |
| ~15–20+ ViewModels o varios módulos Gradle | Migrar a **Hilt** (o partir el container por feature) |

Cuando llegue ese momento, **estudiar Hilt**:
- `@HiltAndroidApp` en el `Application`, `@AndroidEntryPoint` en la Activity.
- `@HiltViewModel` + `@Inject constructor(...)` en cada ViewModel (reemplaza la
  factory manual).
- Módulos `@Module @InstallIn(...)` con `@Provides`/`@Binds` para el repositorio
  (reemplaza `DefaultAppContainer`).
- Scopes: `@Singleton`, `@ActivityRetainedScoped`, `@ViewModelScoped`.

La migración será **mecánica** porque ya usamos inyección por constructor: solo se
anota lo que hoy se cablea a mano en el container.

> Alternativa más ligera que Hilt si se quiere DI por DSL en runtime: **Koin**
> (`viewModel { }`, módulos por feature). Menos seguridad en tiempo de
> compilación, pero menor curva.
