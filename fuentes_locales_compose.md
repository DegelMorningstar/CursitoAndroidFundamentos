# Fuentes Locales en Jetpack Compose

## 1. Agregar el archivo de fuente

Coloca los archivos `.ttf` o `.otf` en la carpeta `res/font/`:

```
res/
└── font/
    ├── roboto_regular.ttf
    ├── roboto_bold.ttf
    └── roboto_italic.ttf
```

> Los nombres de archivo solo pueden tener **letras minúsculas, números y guiones bajos**.

---

## 2. Definir la FontFamily

```kotlin
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle

val RobotoFamily = FontFamily(
    Font(R.font.roboto_regular, weight = FontWeight.Normal),
    Font(R.font.roboto_bold, weight = FontWeight.Bold),
    Font(R.font.roboto_italic, weight = FontWeight.Normal, style = FontStyle.Italic)
)
```

---

## 3. Usarla directamente

```kotlin
Text(
    text = "Hola mundo",
    fontFamily = RobotoFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp
)
```

---

## 4. Integrarla en el tema (recomendado)

En `ui/theme/Type.kt`:

```kotlin
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = RobotoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = RobotoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    )
)
```

En `ui/theme/Theme.kt`:

```kotlin
@Composable
fun MyAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = Typography,
        content = content
    )
}
```

---

## Opción alternativa: XML Font Resource

Crea un archivo XML en `res/font/roboto.xml` para agrupar variantes:

```xml
<?xml version="1.0" encoding="utf-8"?>
<font-family xmlns:app="http://schemas.android.com/apk/res-auto">
    <font
        app:fontStyle="normal"
        app:fontWeight="400"
        app:font="@font/roboto_regular" />
    <font
        app:fontStyle="normal"
        app:fontWeight="700"
        app:font="@font/roboto_bold" />
    <font
        app:fontStyle="italic"
        app:fontWeight="400"
        app:font="@font/roboto_italic" />
</font-family>
```

Y luego usarlo como una sola referencia:

```kotlin
val RobotoFamily = FontFamily(Font(R.font.roboto))
```

---

## Ventajas vs Google Fonts

| | Local | Google Fonts |
|---|---|---|
| Sin internet | ✅ | ❌ |
| Tamaño del APK | Aumenta | Sin cambio |
| Actualización | Manual | Automática |
| Setup | Simple | Un poco más complejo |
