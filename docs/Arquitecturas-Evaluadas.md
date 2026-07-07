# Arquitecturas evaluadas para el proyecto

Documento comparativo de 5 arquitecturas consideradas para esta app Android
(Kotlin + Jetpack Compose + Firebase). Incluye pros, contras y la **justificación
de por qué no se adoptó** cada una (salvo la actual, MVVM + UDF, que sí se usa).

> Contexto: proyecto de curso ("cursito de fundamentos Android"), un solo módulo
> `:app`, feature-first, DI manual por feature, backend Firebase (Firestore + Auth).

---

## 1. MVVM + UDF — **la arquitectura actual (adoptada)**

**Pros**
- Estándar recomendado por Google para Compose; separación clara View / State / lógica.
- Flujo unidireccional (`StateFlow` → `UiState`) hace el estado predecible y testeable.
- Encaja natural con `collectAsStateWithLifecycle` y el patrón de 4 piezas del proyecto
  (`UiState` / `Screen` / `Root` / `ViewModel`).

**Contras**
- Los ViewModels tienden a engordar cuando la pantalla tiene mucha lógica.
- Sin capa de dominio estricta, la lógica de negocio puede filtrarse al VM
  (mitigado aquí con use cases y repositorios tras interfaz).

**Por qué SÍ se usó**
Es el objetivo didáctico del curso (practicar MVVM con UDF), el patrón oficial para
Compose y el que mejor equilibra rigor y simplicidad para el tamaño actual del proyecto.

---

## 2. MVI (Model-View-Intent)

**Pros**
- Estado único e inmutable + intents explícitos → aún más predecible que MVVM clásico.
- Excelente trazabilidad: cada cambio de estado nace de un evento nombrado (debug/replay).
- Encaja muy bien con Compose (reducers puros).

**Contras**
- Más boilerplate (Intents, Reducers, Effects) para pantallas simples como un form de contacto.
- Curva de aprendizaje mayor.

**Por qué NO se usó**
El foco del curso son los **fundamentos** de MVVM; MVI añade una capa de ceremonia
(intents/reducers/effects) que oscurecería esos fundamentos. Para pantallas simples
(CRUD de contactos, login) el costo de boilerplate no compensa la ganancia en
trazabilidad. Se considera la evolución natural una vez dominado MVVM.

---

## 3. Clean Architecture (capas estrictas domain / data / presentation)

**Pros**
- Dominio 100% Kotlin puro, independiente de Firebase/Compose → máxima testabilidad.
- Cambiar de backend toca solo la capa `data` (el proyecto ya está cerca con las
  interfaces de repositorio).
- Escala bien si el proyecto crece.

**Contras**
- Mucho boilerplate (mappers, interfaces, use cases por operación) para una app pequeña.
- Puede sentirse sobre-ingenierizado en contexto didáctico.

**Por qué NO se usó (por completo)**
El proyecto ya adopta **piezas** de Clean (interfaces de repositorio, use cases,
mappers, separación domain/data/presentation) sin imponer sus reglas estrictas.
Formalizarla al 100% (una interfaz y un use case por cada operación, inversión total
de dependencias) generaría boilerplate desproporcionado para una app de curso de un
solo módulo. Se toma "lo bueno" de Clean sin pagar su costo completo.

---

## 4. MVP (Model-View-Presenter)

**Pros**
- Separación View/lógica muy explícita mediante contratos (interfaces).
- Familiar para quien viene de Android "clásico" (Views/XML).

**Contras**
- **Anti-patrón con Compose**: el Presenter con referencia a la View choca con el
  modelo declarativo/reactivo.
- Contratos View manuales = más boilerplate y acoplamiento; frágil ante cambios de
  configuración.

**Por qué NO se usó**
Es incompatible con el paradigma declarativo de Jetpack Compose. El Presenter asume
una View mutable a la que "empuja" cambios, mientras Compose recompone a partir de
estado observable. Usar MVP obligaría a luchar contra el framework y no sobrevive
bien a cambios de configuración sin un ViewModel de por medio (que ya es MVVM).

---

## 5. Redux / State Container global

**Pros**
- Un único store como fuente de verdad centralizada para toda la app.
- Time-travel debugging; estado compartido entre features trivial (sesión, perfil…).

**Contras**
- Estado global mal particionado genera acoplamiento y renders innecesarios.
- Overkill para features mayormente independientes.

**Por qué NO se usó**
Las features del proyecto (contactos, auth, chat, perfil, onboarding) son en gran
medida independientes y no comparten un estado global grande, por lo que un store
único aportaría acoplamiento sin beneficio real. Además chocaría con el **DI manual
partido por feature** que ya organiza el proyecto. Lo poco compartido (sesión) se
resuelve vía repositorios inyectados, no un store global.

---

## Conclusión

La evolución más natural desde la arquitectura actual sería **MVVM → MVI** (mismo
paradigma reactivo, más rigor) o **formalizar Clean Architecture** (ya hay interfaces
de repositorio y use cases a medio camino). **MVP** y **Redux** se descartan como
recomendación real: el primero por incompatibilidad con Compose, el segundo por
sobre-ingeniería frente a features independientes.
