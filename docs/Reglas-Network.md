# Network — Retrofit + Random User API

> Reglas e instrucciones para consumir red en este proyecto.
> Fuente de datos de ejemplo: **Random User API** (`https://randomuser.me/`).
> Relacionado: [[Reglas-Inyeccion-Dependencias.md]] (dónde se crea la instancia),
> [[Reglas-Capa-View.md]] (cómo el estado llega a la UI).

La instancia de red (`OkHttpClient → Retrofit → service → repositorio`) se construye
**sin Hilt**, dentro del `DefaultAppContainer` con `by lazy`. La UI y los ViewModels
**no conocen Retrofit**: solo dependen de la interfaz `ContactoRepository`.

```
RandomUserApiService (Retrofit + interceptores)
   → ContactoRepositoryApi (mapea DTO → Contacto, puede lanzar)
   → ListaContactoViewModel (runCatching → Loading/Success/Empty/Error)
   → ListaContactoScreen (when exhaustivo)
```

---

## 1. Decisiones previas (gotchas del modelo)

Random User devuelve `name.{first,last}`, `email`, `phone`/`cell`,
`picture.{large,thumbnail}`, `login.uuid` — **pero no tiene `id` entero ni `company`**.

| Campo de `Contacto` | Decisión |
|---|---|
| `id: Int` | Random User no trae id entero → **se genera local con el índice** (`index + 1`) al mapear. (Alternativa futura: cambiar a `String` con `login.uuid`). |
| `company` | La API no lo provee → queda **vacío**. |
| avatar | Hoy se pinta con iniciales. Para usar la foto real: añadir `pictureUrl` a `Contacto` y renderizar con **Coil** (`AsyncImage`). *(Fuera del alcance de esta guía base).* |

---

## 2. Paso 0 — Dependencias

**`gradle/libs.versions.toml`:**
```toml
[versions]
retrofit = "2.11.0"
okhttp = "4.12.0"
retrofitSerialization = "1.0.0"

[libraries]
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
okhttp-logging = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "okhttp" }
retrofit-serialization = { group = "com.jakewharton.retrofit", name = "retrofit2-kotlinx-serialization-converter", version.ref = "retrofitSerialization" }
```

**`app/build.gradle.kts`:**
```kotlin
implementation(libs.retrofit)
implementation(libs.retrofit.serialization)  // converter kotlinx.serialization (el plugin ya está)
implementation(libs.okhttp.logging)
```
El permiso `INTERNET` ya está en `AndroidManifest.xml`. ✅

---

## 3. Paso 1 — DTOs (`data/remote/dto/`)

Reflejan el JSON; `ignoreUnknownKeys` permite ignorar lo que no se usa.
```kotlin
@Serializable
data class RandomUserResponse(val results: List<UserDto> = emptyList())

@Serializable
data class UserDto(
    val name: NameDto,
    val email: String = "",
    val phone: String = "",
    val cell: String = "",
    val login: LoginDto,
    val picture: PictureDto
)
@Serializable data class NameDto(val first: String = "", val last: String = "")
@Serializable data class LoginDto(val uuid: String = "")
@Serializable data class PictureDto(val large: String = "", val thumbnail: String = "")
```

> **Regla:** los DTO son la forma del JSON, **no** se usan en la UI. Se mapean a
> `Contacto` (modelo de dominio) en el repositorio.

---

## 4. Paso 2 — Servicio Retrofit (`data/remote/RandomUserApiService.kt`)

```kotlin
interface RandomUserApiService {
    @GET("api/")
    suspend fun obtenerUsuarios(@Query("results") cantidad: Int = 20): RandomUserResponse
}
```

---

## 5. Paso 3 — Interceptores (ejemplo básico)

```kotlin
// Agrega headers a cada request
val headerInterceptor = Interceptor { chain ->
    val request = chain.request().newBuilder()
        .addHeader("Accept", "application/json")
        .build()
    chain.proceed(request)
}

// Log de request/response en Logcat (idealmente solo en debug)
val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}
```

> El **orden importa**: el `loggingInterceptor` se añade **al final** para que
> registre la request ya con los headers puestos.

---

## 6. Paso 4 — Generar la instancia SIN Hilt (en el container)

Todo el grafo de red se arma `by lazy` **una sola vez** dentro de
`DefaultAppContainer` (ver [[Reglas-Inyeccion-Dependencias.md]]).

```kotlin
class DefaultAppContainer : AppContainer {

    private val json = Json { ignoreUnknownKeys = true }

    private val okHttp by lazy {
        OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)        // logging al final
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .client(okHttp)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    private val apiService: RandomUserApiService by lazy {
        retrofit.create(RandomUserApiService::class.java)
    }

    // El repositorio único pasa de Fake a Api: una sola línea, el resto no se entera.
    override val repositorio: ContactoRepository by lazy {
        ContactoRepositoryApi(apiService)
    }

    override val factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { ListaContactoViewModel(repositorio) }
        // ... resto de ViewModels
    }
}
```

Imports a resolver con el IDE:
- `asConverterFactory` → artefacto `retrofit2-kotlinx-serialization-converter` (Paso 0).
- `toMediaType()` → OkHttp.
- `TimeUnit` → `java.util.concurrent`.

> Buena práctica: envolver `addInterceptor(loggingInterceptor)` en
> `if (BuildConfig.DEBUG) { ... }` para no loguear en release.

---

## 7. Paso 5 — Repositorio API + mapper (`data/ContactoRepositoryApi.kt`)

```kotlin
class ContactoRepositoryApi(
    private val api: RandomUserApiService
) : ContactoRepository {

    override suspend fun obtenerTodos(): List<Contacto> =
        api.obtenerUsuarios(cantidad = 20).results
            .mapIndexed { index, dto -> dto.toContacto(id = index + 1) }

    override suspend fun obtenerPorId(id: Int): Contacto? =
        obtenerTodos().find { it.id == id }

    // Random User no es CRUD: estos quedan en memoria o sin implementar por ahora.
    override suspend fun agregar(contacto: Contacto): Contacto = TODO("no aplica en Random User")
    override suspend fun actualizar(contacto: Contacto): Contacto = TODO("no aplica en Random User")
    override suspend fun eliminar(id: Int) = TODO("no aplica en Random User")
    override suspend fun alternarFavorito(id: Int): Contacto = TODO("no aplica en Random User")
}

private fun UserDto.toContacto(id: Int) = Contacto(
    id = id,
    first = name.first,
    last = name.last,
    phone = phone.ifBlank { cell },
    email = email,
    company = "",          // la API no lo provee
    favorite = false
)
```

---

## 8. Paso 6 — Manejo de errores (en el ViewModel)

Los `suspend` de Retrofit **lanzan excepciones**:

| Excepción | Causa |
|---|---|
| `IOException` | sin conexión, timeout, DNS |
| `HttpException` | respuesta HTTP no-2xx (404, 500, …) |
| `SerializationException` | JSON inesperado / no parseable |

Se traducen a estado de UI con `runCatching` (encaja con la **sealed `UiState`** —
ver Propuesta 1 en [[Reglas-Capa-View.md]]):

```kotlin
fun getContactList() {
    viewModelScope.launch {
        _uiState.value = ListaContactoUiState.Loading
        runCatching { repository.obtenerTodos() }
            .onSuccess { contactos ->
                _uiState.value =
                    if (contactos.isEmpty()) ListaContactoUiState.Empty
                    else ListaContactoUiState.Success(contactos)
            }
            .onFailure { e ->
                _uiState.value = ListaContactoUiState.Error(e.aMensaje())
            }
    }
}

private fun Throwable.aMensaje(): String = when (this) {
    is IOException -> "Sin conexión. Revisa tu internet."
    is HttpException -> "Error del servidor (${code()})."
    else -> "Algo salió mal. Inténtalo de nuevo."
}
```

En la pantalla, el `when (uiState)` pinta `Error(message)` con un botón
**Reintentar** que vuelve a llamar `getContactList()`.

---

## 9. Reglas de la capa network

1. **DTO ≠ dominio.** Los `*Dto` reflejan el JSON; se mapean a `Contacto` en el
   repositorio. La UI/VM nunca ven un DTO.
2. **La instancia se crea solo en el container** (`DefaultAppContainer`), `by lazy`.
   Prohibido construir `Retrofit`/`OkHttpClient` en ViewModels, Roots o pantallas.
3. **Una sola instancia** de `OkHttpClient`/`Retrofit` para toda la app (reusa el
   pool de conexiones).
4. **El repositorio puede lanzar**; el **ViewModel** es quien captura y traduce a
   `UiState.Error`. La UI no maneja excepciones.
5. **Logging solo en debug** (`BuildConfig.DEBUG`).
6. **Timeouts explícitos** en el `OkHttpClient`.
7. `Json { ignoreUnknownKeys = true }` para tolerar campos extra del API.

---

## 10. Checklist — agregar un endpoint nuevo

- [ ] DTO(s) `@Serializable` del JSON en `data/remote/dto/`.
- [ ] Método `suspend` en el `ApiService` con `@GET/@POST` + `@Query/@Path`.
- [ ] Mapper `Dto.toDominio()` en el repositorio.
- [ ] Implementación en el `ContactoRepositoryApi` (o repo correspondiente).
- [ ] El ViewModel envuelve la llamada en `runCatching` → `Loading/Success/Empty/Error`.
- [ ] No tocar la instancia del container salvo que cambie la `baseUrl`/cliente.

