# Preparación para producción y ambientes (debug / qa / release)

> Checklist para llevar la app a producción y añadir tres ambientes de build.
> Es una guía de pasos; **el código actual sigue siendo la fuente de verdad**.
>
> **Decisión tomada:** un **solo proyecto Firebase** (`contactosapp-ec515`, plan Spark)
> para los tres ambientes. Solo cambian `applicationId`, nombre de la app y la URL del
> Worker de push vía `BuildConfig`.
> **Riesgo aceptado:** QA escribe sobre los datos y usuarios **de producción** (comparten
> Firestore/Auth). Mitigación barata: usar cuentas de prueba dedicadas y ser disciplinado.

---

## Estado de partida (antes de estos cambios)

- Build types: solo `debug` (implícito) y `release`, **sin minify** y **sin signing config propia**
  (release se firma con la debug key → Play Store la rechaza).
- `WORKER_PUSH_URL` **hardcodeada** en `core/util/Catalogo.kt`.
- Un solo `google-services.json` en `app/`.
- Firestore deserializa los **modelos de dominio por reflexión** → R8 los rompe si no se protegen.
- `versionCode = 1`, `versionName = "1.0"`.

---

## A) Checklist de producción

### A.1 Firma de release (lo más importante)

1. Generar el keystore (una sola vez, guardarlo **fuera del repo**):
   ```powershell
   keytool -genkey -v -keystore cursito-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias cursito
   ```
2. Crear `keystore.properties` en la raíz (**no subir a git**):
   ```properties
   storeFile=C:/ruta/segura/cursito-release.jks
   storePassword=...
   keyAlias=cursito
   keyPassword=...
   ```
3. Añadir a `.gitignore`:
   ```gitignore
   keystore.properties
   *.jks
   ```

### A.2 Ofuscación / R8 (y el gotcha de Firestore)

En `release`: `isMinifyEnabled = true` + `isShrinkResources = true`.
Firestore mapea documentos a modelos por reflexión y las rutas usan kotlinx-serialization;
sin reglas keep, R8 los rompe. Añadir en `app/proguard-rules.pro`:

```proguard
# Modelos que Firestore deserializa por reflexión (Contacto, Usuario, Mensaje, Chat...)
-keep class com.yaeldev.cursitodefundamentosandroid.**.domain.models.** { *; }
-keep class com.yaeldev.cursitodefundamentosandroid.**.data.**.dto.** { *; }

# kotlinx-serialization (rutas @Serializable)
-keepattributes *Annotation*, InnerClasses
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> { *** Companion; }
-keepclasseswithmembers class ** { kotlinx.serialization.KSerializer serializer(...); }

# Retrofit + converter kotlinx-serialization
-keepattributes Signature, Exceptions
-dontwarn okhttp3.**
-dontwarn retrofit2.**
```

> ⚠️ Los fallos de R8 solo aparecen en runtime. **Probar el APK de release en un device real**
> antes de publicar (crear/editar/listar contactos, chat, perfil, push).

### A.3 Sacar `WORKER_PUSH_URL` a `BuildConfig`

- Mover la URL a `buildConfigField` por build type (ver sección B).
- En `core/util/Catalogo.kt`, cambiar la constante por:
  ```kotlin
  val WORKER_PUSH_URL = BuildConfig.WORKER_PUSH_URL
  ```

### A.4 Versionado

- Definir política: incrementar `versionCode` en cada release publicado en Play; `versionName`
  semántico (`1.0.0`, `1.1.0`, ...).

### A.5 Logging

- Verificar que no haya `Log.d`/prints con datos sensibles en release; envolverlos en
  `if (BuildConfig.DEBUG) { ... }`.

### A.6 Backup

- Revisar `android:allowBackup="true"` + `res/xml/data_extraction_rules.xml`. Ajustar si no se
  quiere respaldar datos sensibles.

### A.7 Firebase — reglas e índices

- Confirmar reglas + índice compuesto desplegados antes de publicar:
  ```powershell
  npx -y firebase-tools@latest deploy --only firestore --project contactosapp-ec515
  ```

### A.8 (Opcional) Crashlytics

- Funciona en plan Spark (no requiere Blaze). Casi obligatorio en producción. Requiere el plugin
  `firebase-crashlytics` + dependencia del SDK.

---

## B) Ambientes debug / qa / release (build types)

Con un solo backend, la vía correcta son **build types** (no product flavors). Con
`applicationIdSuffix` los tres conviven instalados en el mismo device.

Reemplazar el bloque de build types en `app/build.gradle.kts`:

```kotlin
// arriba del archivo, junto a los imports
import java.util.Properties

val keystoreProps = Properties().apply {
    val f = rootProject.file("keystore.properties")
    if (f.exists()) load(f.inputStream())
}

android {
    // ...
    signingConfigs {
        create("release") {
            if (keystoreProps.isNotEmpty()) {
                storeFile = file(keystoreProps.getProperty("storeFile"))
                storePassword = keystoreProps.getProperty("storePassword")
                keyAlias = keystoreProps.getProperty("keyAlias")
                keyPassword = keystoreProps.getProperty("keyPassword")
            }
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true   // necesario para buildConfigField
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-DEV"
            isMinifyEnabled = false
            resValue("string", "app_name", "Cursito (Dev)")
            buildConfigField("String", "WORKER_PUSH_URL",
                "\"https://cursito-push.yaelmontes181122.workers.dev\"")
        }
        create("qa") {
            initWith(getByName("release"))          // hereda minify/proguard de release
            applicationIdSuffix = ".qa"
            versionNameSuffix = "-QA"
            signingConfig = signingConfigs.getByName("debug") // firma con debug key para repartir fácil
            isDebuggable = true
            matchingFallbacks += listOf("release")
            resValue("string", "app_name", "Cursito (QA)")
            buildConfigField("String", "WORKER_PUSH_URL",
                "\"https://cursito-push-qa.yaelmontes181122.workers.dev\"")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("string", "app_name", "Cursito")
            buildConfigField("String", "WORKER_PUSH_URL",
                "\"https://cursito-push.yaelmontes181122.workers.dev\"")
        }
    }
}
```

Cambios que lo acompañan:

1. **`res/values/strings.xml`**: eliminar `<string name="app_name">...</string>` (ahora lo genera
   `resValue` por build type; si se deja, choca con el generado).
2. **`core/util/Catalogo.kt`**: `WORKER_PUSH_URL` lee de `BuildConfig` (sección A.3).

### El paso obligatorio de Firebase con `applicationIdSuffix`

Al añadir `.dev`/`.qa`, el `applicationId` deja de coincidir con el de `google-services.json` y el
plugin `com.google.gms.google-services` **falla el build** (*"No matching client found for package
name ..."*). Dos salidas:

- **Registrar los packages en el mismo proyecto** (recomendado): en Firebase Console →
  `contactosapp-ec515` → añadir app Android con package
  `com.yaeldev.cursitodefundamentosandroid.dev` y otra con `...qa`, **descargar de nuevo**
  `google-services.json` (traerá los 3 packages) y reemplazarlo en `app/`. Los 3 ambientes conviven
  instalados.
- **No usar sufijos** (más simple): quitar las líneas `applicationIdSuffix` de `debug` y `qa`. No se
  toca Firebase, pero los 3 ambientes comparten `applicationId` y **no conviven** en el device.

> ⚠️ **El `applicationIdSuffix` del Gradle y el package registrado en Firebase deben ser el mismo
> string.** Aquí el ambiente dev usa `.dev` (no `.debug`); el package a registrar es
> `com.yaeldev.cursitodefundamentosandroid.dev`. Como el Auth es correo/contraseña, **no** hace falta
> registrar SHA-1 por variante.

Packages finales por ambiente:

| Build type | `applicationIdSuffix` | Package a registrar en Firebase |
|---|---|---|
| debug | `.dev` | `com.yaeldev.cursitodefundamentosandroid.dev` |
| qa | `.qa` | `com.yaeldev.cursitodefundamentosandroid.qa` |
| release | (ninguno) | `com.yaeldev.cursitodefundamentosandroid` |

---

## Comandos resultantes

```powershell
.\gradlew.bat assembleDebug      # Cursito (Dev), .dev
.\gradlew.bat assembleQa         # Cursito (QA), .qa, minificado
.\gradlew.bat assembleRelease    # Cursito, firmado + R8
.\gradlew.bat bundleRelease      # .aab para subir a Play Store
```

---

## Orden sugerido de ejecución

1. Generar keystore + `keystore.properties` (fuera de git) — A.1
2. `signingConfigs` + `buildTypes` (`debug`/`qa`/`release`) + `buildConfig = true` — B
3. Registrar packages `.dev`/`.qa` en Firebase y re-bajar `google-services.json` — B
4. `WORKER_PUSH_URL` → `buildConfigField` y `Catalogo.kt` leyendo `BuildConfig` — A.3
5. `minify` + `shrinkResources` en release **con las reglas keep** — A.2 (⚠️ probar release en device real)
6. Quitar `app_name` de `strings.xml` — B.1
7. `versionCode`/`versionName`, logging tras `BuildConfig.DEBUG`, verificar reglas Firestore — A.4/A.5/A.7
8. `bundleRelease` → `.aab` para Play
