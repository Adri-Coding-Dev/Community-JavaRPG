# HollowForge — Referencia Técnica Completa

> RPG 2D Pixel Art open source desarrollado colaborativamente con JavaFX.
>
> Versión: **0.1.0-SNAPSHOT** — Fase de prototipo inicial

---

## 1. Introducción

### 1.1 Descripción del sistema

HollowForge es un videojuego RPG sandbox 2D construido desde cero con Java y JavaFX. El proyecto utiliza una **arquitectura híbrida por capas** que combina Clean Architecture, Entity Component System (ECS) y Data-Oriented Design (DOD) para lograr un equilibrio entre mantenibilidad, testabilidad y rendimiento en tiempo real.

### 1.2 Objetivo del proyecto

- Demostrar que una comunidad puede construir un videojuego serio mientras aprende desarrollo profesional
- Servir como plataforma educativa de ingeniería de software
- Crear un producto open source modular, mantenible y escalable

### 1.3 Contexto general

El proyecto se encuentra en **fase de prototipo inicial (v0.1.0-SNAPSHOT)**. La arquitectura está completamente implementada a nivel estructural, con 6 capas bien definidas, pero la integración entre capas (domain → ecs → engine loop) aún no está operativa. Actualmente el juego consiste en un sistema de menús funcionales con audio, navegación entre escenas y visualización de contribuidores desde la API de GitHub. El bucle de juego (GameLoop) está creado pero vacío de lógica de simulación.

---

## 2. Arquitectura del sistema

### 2.1 Diagrama de capas

```
┌─────────────────────────────────────────────────────────┐
│                    app/ (entry point)                    │
├─────────────────────────────────────────────────────────┤
│                   presentation/                          │
│               (UI del juego, vistas, menús)              │
├─────────────────────────────────────────────────────────┤
│               engine/ (runtime del juego)                │
│     audio · config · core · game · navigation · rendering│
├────────────────────┬────────────────────────────────────┤
│     domain/        │         ecs/                        │
│  (reglas negocio)  │   (simulación en tiempo real)       │
│                    │   component/ · system/ · core/      │
├────────────────────┴────────────────────────────────────┤
│            infrastructure/                                │
│       external/ (GitHub, APIs) · logging/                │
├─────────────────────────────────────────────────────────┤
│                shared/ (utilidades genéricas)             │
└─────────────────────────────────────────────────────────┘
```

### 2.2 Módulos principales

| Capa | Propósito | Tecnología | Depende de |
|------|-----------|------------|------------|
| **domain** | Reglas de negocio del juego, independientes del motor | Java estándar | — (ninguna) |
| **ecs** | Simulación del mundo en tiempo real | Java estándar | — (ninguna) |
| **engine** | Ejecución técnica (runtime) | JavaFX | domain, ecs, presentation, infrastructure |
| **presentation** | Interfaces de usuario y componentes visuales | JavaFX | engine, infrastructure, shared |
| **infrastructure** | IO, logging, APIs externas | java.net.http, org.json | shared |
| **shared** | Utilidades transversales | Java estándar | — (transversal) |

### 2.3 Reglas de dependencias

```
domain  ──→ (ninguna)         ← independiente, raíz del sistema
ecs     ──→ (ninguna)         ← independiente, raíz del sistema
engine  ──→ domain, ecs, presentation, infrastructure
presentation ──→ engine, infrastructure, shared
infrastructure ──→ shared
shared  ──→ (ninguna)         ← visible a todas las capas
```

**Prohibiciones:**
- ❌ `domain` no puede importar nada de `engine`, `ecs`, `presentation` ni `infrastructure`
- ❌ `ecs` no puede importar nada de `domain` ni `engine`
- ❌ `presentation` no puede contener lógica de negocio
- ❌ `engine` no puede contener UI directa

### 2.4 Stack tecnológico

| Área | Tecnología | Versión |
|------|-----------|---------|
| Lenguaje | Java | 25 |
| UI / Renderizado | JavaFX | 25.0.2 |
| Build system | Maven | 3.x |
| HTTP Client | java.net.http | (JDK estándar) |
| JSON | org.json | 20231013 |
| Logging | java.util.logging | (JDK estándar) |
| Control de versiones | Git | — |

### 2.5 Relación entre componentes

```
MainApp.start()
  ├── Crea AudioManager         (engine/audio)
  ├── Crea GitHubService        (infrastructure/external/github)
  ├── Crea SceneManager         (engine/navigation)
  │     ├── Crea MainMenuView    (presentation/ui)
  │     ├── Crea OptionsView     (presentation/ui)
  │     ├── Crea ContributorsView (presentation/ui)
  │     └── Crea GameController  (engine/game)
  │           ├── Extiende GameLoop (engine/core)
  │           └── Crea GameView    (engine/rendering)
  │                 └── Crea PauseMenu (presentation/ui)
  └── SceneManager cambia escenas según navegación

domain/entity/*  ←── usados por domain/usecase/*
ecs/component/*  ←── procesados por ecs/system/*
ecs/core/*       ←── storage para componentes ECS
```

---

## 3. Flujo de trabajo (GitFlow)

### 3.1 Jerarquía de ramas

```
master ──────────► producción final (protegida)
  ↑
beta ────────────► pre-lanzamiento / cliente (protegida)
  ↑
develop ─────────► integración general (protegida)
  │
  ├── feat/* ─────► nuevas funcionalidades
  ├── fix/* ──────► corrección de errores
  ├── refactor/* ─► mejoras estructurales
  ├── docs/* ─────► documentación
  ├── chore/* ────► mantenimiento
  ├── test/* ─────► pruebas
  ├── perf/* ─────► rendimiento
  ├── ci/* ───────► integración continua
  └── build/* ────► compilación / artefactos
```

### 3.2 Ramas protegidas

| Rama | Propósito | Responsable del merge |
|------|-----------|----------------------|
| `master` | Código de producción. Solo versiones estables | Maintainers |
| `beta` | Release candidate para QA y testers | Maintainers |
| `develop` | Integración diaria de todo el trabajo | Maintainers |

> **Regla:** No se permite commit directo a ninguna rama protegida. Todo cambio debe entrar mediante Pull Request.

### 3.3 Ramas de trabajo

Todas se crean desde `develop`:

```
git checkout develop
git pull origin develop
git checkout -b feat/mi-funcionalidad
```

| Prefijo | Propósito |
|---------|-----------|
| `feat/*` | Nueva funcionalidad para el usuario |
| `fix/*` | Corrección de bug en producción o desarrollo |
| `refactor/*` | Cambio estructural que no altera comportamiento externo |
| `docs/*` | Cambios en documentación |
| `chore/*` | Mantenimiento: dependencias, limpieza, config |
| `test/*` | Nuevas pruebas o mejora de cobertura |
| `perf/*` | Optimización de rendimiento |
| `ci/*` | Pipelines, integración continua, despliegue |
| `build/*` | Sistema de compilación, scripts, artefactos |
| `revert/*` | Reversión de cambios anteriores |

### 3.4 Flujo diario para developers

```
1. git checkout develop
2. git pull origin develop
3. git checkout -b feat/mi-cambio
4. (trabajar y hacer commits)
5. git push origin feat/mi-cambio
6. Crear Pull Request → develop
7. Un maintainer revisa y aprueba
8. Merge a develop
```

### 3.5 Flujo de release

```
develop ──► beta ──► master
    │          │         │
    │    (rama de        │
    │     release)       │
    │                    │
    │        (versión estable)
    │
    (integración continua)
```

1. **develop** acumula funcionalidades completadas
2. Se corta **beta** desde develop para QA
3. Beta se estabiliza con PR de tipo `fix/*`
4. Beta se fusiona a **master** para producción
5. Master se etiqueta con versión semántica

---

## 4. Estructura del proyecto

### 4.1 Árbol de carpetas

```
src/main/java/dev/hollowforge/
├── app/
│   └── MainApp.java                        ← Entry point (JavaFX Application)
│
├── domain/                                 ← 🧼 Clean Architecture
│   ├── entity/                             ← Entidades del dominio
│   │   ├── PlayerStats.java                ←   Salud, nivel, experiencia
│   │   ├── Inventory.java                  ←   Inventario con slots
│   │   └── Quest.java                      ←   Estado de misiones
│   └── usecase/                            ← Casos de uso
│       ├── ApplyDamage.java                ←   Aplicar daño a entidad
│       └── LevelUp.java                    ←   Gestión de nivel y XP
│
├── ecs/                                    ← 🎮 ECS + DOD
│   ├── component/                          ← Componentes (datos planos)
│   │   ├── Position.java                   ←   Coordenadas x/y
│   │   ├── Velocity.java                   ←   Velocidad vx/vy
│   │   ├── Health.java                     ←   Salud actual/máxima
│   │   └── Damage.java                     ←   Cantidad de daño
│   ├── core/                               ← Storage DOD (cache-friendly)
│   │   ├── EntityArray.java                ←   Array denso de entidades
│   │   └── ComponentChunk.java             ←   Chunk de componentes
│   └── system/                             ← Sistemas (lógica pura)
│       ├── MovementSystem.java             ←   Actualiza posición por velocidad
│       ├── CombatSystem.java               ←   Aplica daño a salud
│       └── AISystem.java                   ←   Comportamiento de IA básico
│
├── engine/                                 ← 🧱 Runtime
│   ├── audio/
│   │   └── AudioManager.java               ←   Reproducción de música con fade
│   ├── config/
│   │   └── AppConstants.java               ←   Constantes globales (rutas, URLs, textos)
│   ├── core/
│   │   └── GameLoop.java                   ←   Bucle principal (abstracto, 60 FPS)
│   ├── game/
│   │   └── GameController.java             ←   Controlador de partida (extiende GameLoop)
│   ├── navigation/
│   │   └── SceneManager.java               ←   Navegación entre escenas
│   └── rendering/
│       └── GameView.java                   ←   Vista de juego (StackPane)
│
├── presentation/                           ← 🖥 UI
│   └── ui/
│       ├── MainMenuView.java               ←   Menú principal
│       ├── OptionsView.java                ←   Pantalla de opciones (volumen)
│       ├── ContributorsView.java           ←   Lista de contribuidores
│       ├── PauseMenu.java                  ←   Menú de pausa
│       └── components/
│           ├── UIButtonFactory.java        ←   Fábrica de botones con textura
│           ├── ImageBackground.java        ←   Fondo de imagen estático
│           └── FontLoader.java             ←   Carga de fuentes personalizadas
│
├── infrastructure/                         ← 🔌 Integraciones
│   ├── external/
│   │   └── github/
│   │       ├── GitHubService.java          ←   Cliente HTTP para API de GitHub
│   │       └── dto/
│   │           └── Contributor.java        ←   DTO de contribuidor
│   └── logging/
│       └── LogManager.java                 ←   Logger a consola y archivo
│
└── shared/                                 ← 📦 Utilidades
    ├── util/
    │   └── BrowserUtil.java                ←   Apertura de URLs en navegador
    └── dto/                                ←   (reservado para DTOs compartidos)
```

### 4.2 Recursos estáticos

```
src/main/resources/
├── assets/
│   ├── audio/
│   │   ├── HollowForgeMainTheme.wav        ←   Música del menú
│   │   └── HollowForgeCaveTheme.wav        ←   Música del juego
│   └── ui/
│       ├── background/
│       │   └── MainScreen.png              ←   Fondo del menú
│       ├── buttons/
│       │   ├── ButtonsBeginTexture.png     ←   Textura de botones
│       │   └── OptionsMenu.png             ←   Textura menú opciones
│       └── socials/
│           ├── DiscordLogo.png
│           ├── YoutubeLogo.png
│           ├── GitHubLogo.png
│           └── CartelToolTip.png
└── fonts/
    ├── HollowForgeMinecraftRegular.otf     ←   Fuente principal
    └── MinecraftBold-nMK1.otf              ←   Fuente bold
```

### 4.3 Dependencias externas (Maven)

| Dependencia | Versión | Propósito |
|-------------|---------|-----------|
| javafx-controls | 25 | Controles UI (botones, layouts) |
| javafx-graphics | 25 | Escenas, nodos, renderizado |
| javafx-fxml | 25 | Soporte FXML (reservado) |
| javafx-media | 25 | Reproducción de audio |
| javafx-web | 25 | WebView (reservado) |
| org.json | 20231013 | Parseo de respuestas JSON de GitHub |

---

## 5. Convenciones del proyecto

### 5.1 Formato de commits

```
type(scope): subject

body (explicación detallada del cambio, opcional)

#ISSUE_NUMBER (opcional)
```

### 5.2 Types válidos

| Type | Uso | Ejemplo |
|------|-----|---------|
| `feat` | Nueva funcionalidad | `feat(auth): implement login` |
| `fix` | Corrección de error | `fix(ui): correct button alignment` |
| `chore` | Mantenimiento, deps | `chore(deps): upgrade javafx` |
| `docs` | Documentación | `docs(api): update endpoints` |
| `style` | Formato (no lógica) | `style(ui): format indentation` |
| `refactor` | Cambio estructural | `refactor(project): align packages` |
| `test` | Pruebas | `test(domain): add PlayerStats tests` |
| `perf` | Rendimiento | `perf(ecs): optimize component lookup` |
| `ci` | CI/CD | `ci(build): add maven cache` |
| `build` | Compilación | `build(maven): add shade plugin` |
| `revert` | Reversión | `revert(auth): rollback JWT` |

### 5.3 Scopes recomendados

| Ámbito | Scopes |
|--------|--------|
| Frontend (UI) | `ui`, `styles`, `components`, `layout` |
| Backend (lógica) | `api`, `database`, `auth`, `server` |
| Sysadmin | `ci`, `cd`, `docker`, `build`, `scripts` |
| General | `docs`, `tests`, `config`, `project`, `git`, `deps` |

### 5.4 Nomenclatura de ramas

```
tipo/descripcion-corta-con-guiones
```

Ejemplos:

```
feat/inventario-ui
fix/colisiones-paredes
refactor/ecs-storage
docs/api-endpoints
chore/limpiar-dependencias
test/domain-usecases
```

### 5.5 Estilo de código

Ver [STYLEGUIDE.md](STYLEGUIDE.md) para:

- PascalCase para clases
- camelCase para variables y métodos
- UPPER_CASE para constantes
- Una responsabilidad por clase
- Métodos pequeños con nombres descriptivos
- Comentar el POR QUÉ, no el QUÉ

---

## 6. Integración con Issues

### 6.1 Relación commits → issues

Cuando un cambio está relacionado con un issue de GitHub/GitLab:

```
feat(ecs): add collision detection system

Implements AABB collision for entity movement.

#23
```

### 6.2 Cierre automático de issues

Usar `Closes`, `Fixes` o `Resolves` en el body para auto-cerrar al mergear:

```
fix(physics): correct wall collision

Fixes jitter when entity collides with walls.

Closes #23
```

### 6.3 Flujo issue → desarrollo

```
1. Se crea un issue en GitHub/GitLab
2. Developer asigna el issue a sí mismo
3. Se crea rama desde develop: feat/mi-cambio
4. Se trabaja y commitea con #issue en footer
5. Se abre Pull Request → develop
6. PR se revisa y se mergea
7. Issue se cierra automáticamente si se usó "Closes #issue"
```

### 6.4 Ausencia de issues

Si un cambio no está vinculado a ningún issue, simplemente se omite el footer. No inventar números.

---

## 7. CI/CD

### 7.1 Estado actual

**No se ha detectado ningún pipeline de CI/CD configurado en el repositorio.** No existen archivos de configuración para GitHub Actions, GitLab CI, Jenkins ni ninguna otra herramienta de integración continua.

### 7.2 Proceso de build actual

Actualmente la compilación y ejecución son completamente manuales:

```bash
cd src/
mvn clean compile    # Compilar
mvn javafx:run       # Ejecutar
```

### 7.3 Pruebas automatizadas

**No se ha detectado ningún framework de testing configurado.** No hay dependencias de JUnit, TestNG ni ningún otro framework en `pom.xml`. Tampoco existe el directorio `src/test/`.

### 7.4 Recomendación (futuro)

Para cuando el proyecto madure, se recomienda configurar:

1. **GitHub Actions** o **GitLab CI** con:
   - Build con `mvn compile` en cada push a `develop`
   - Ejecución de tests con `mvn test`
   - Análisis estático con Checkstyle o SpotBugs
2. **JUnit 5** como framework de testing
3. **Despliegue automático** develop → beta → master (manual con etiquetado)

---

## 8. Versionado semántico

### 8.1 Formato

```
MAJOR.MINOR.PATCH (según semver.org)
```

Actualmente: **0.1.0-SNAPSHOT**

### 8.2 Reglas de incremento

| Componente | Cuándo incrementar | Ejemplo |
|------------|-------------------|---------|
| **MAJOR** | Cambios incompatibles con versiones anteriores | `1.0.0` → `2.0.0` |
| **MINOR** | Nuevas funcionalidades compatibles hacia atrás | `1.0.0` → `1.1.0` |
| **PATCH** | Correcciones de bugs compatibles | `1.0.0` → `1.0.1` |

### 8.3 Aplicación en el flujo GitFlow

| Rama | Versión | Prefijo |
|------|---------|---------|
| `develop` | SNAPSHOT | `0.2.0-SNAPSHOT` |
| `beta` | Release candidate | `0.2.0-rc.1` |
| `master` | Producción | `0.2.0` |

### 8.4 Estado actual

El proyecto está en **0.1.0-SNAPSHOT**, lo que indica:

- **0** — Major: proyecto en fase inicial, sin API estable
- **1** — Minor: primera iteración de funcionalidades base
- **0** — Patch: sin correcciones de producción
- **SNAPSHOT** — Versión en desarrollo activo, no liberada

---

## 9. Agent Plan Mapping

### 9.1 Estado por módulo

#### 🧼 Domain (Clean Architecture)

| Componente del plan | Estado | Archivo |
|--------------------|--------|---------|
| `PlayerStats` | ✅ Implementado | `domain/entity/PlayerStats.java` |
| `Inventory` | ✅ Implementado | `domain/entity/Inventory.java` |
| `Quest` | ✅ Implementado | `domain/entity/Quest.java` |
| `ApplyDamage` | ✅ Implementado | `domain/usecase/ApplyDamage.java` |
| `LevelUp` | ✅ Implementado | `domain/usecase/LevelUp.java` |

> **Nota:** Entidades y casos de uso funcionales pero básicos. Pendiente de integración con el loop de juego.

#### 🎮 ECS

| Componente del plan | Estado | Archivo |
|--------------------|--------|---------|
| `Position` | ✅ Implementado | `ecs/component/Position.java` |
| `Velocity` | ✅ Implementado | `ecs/component/Velocity.java` |
| `Health` | ✅ Implementado | `ecs/component/Health.java` |
| `Damage` | ✅ Implementado | `ecs/component/Damage.java` |
| `MovementSystem` | ✅ Implementado | `ecs/system/MovementSystem.java` |
| `CombatSystem` | ✅ Implementado | `ecs/system/CombatSystem.java` |
| `AISystem` | ✅ Implementado | `ecs/system/AISystem.java` |

> **Nota:** Sistemas funcionales pero no conectados al GameLoop. `GameController.update()` está vacío.

#### ⚡ DOD

| Componente del plan | Estado | Archivo |
|--------------------|--------|---------|
| `EntityArray` | ✅ Implementado | `ecs/core/EntityArray.java` |
| `ComponentChunk` | ✅ Implementado | `ecs/core/ComponentChunk.java` |

> **Nota:** DOD se integró dentro de `ecs/core/` como infraestructura de storage, no como capa independiente.

#### 🧱 Engine

| Componente del plan | Estado | Archivo |
|--------------------|--------|---------|
| GameLoop | ✅ Implementado | `engine/core/GameLoop.java` |
| AudioManager | ✅ Implementado | `engine/audio/AudioManager.java` |
| SceneManager | ✅ Implementado | `engine/navigation/SceneManager.java` |
| GameController | ✅ Implementado | `engine/game/GameController.java` |
| GameView | ✅ Implementado | `engine/rendering/GameView.java` |
| Config | ✅ Implementado | `engine/config/AppConstants.java` |

**Faltante respecto al plan original:**

| Componente | Estado | Prioridad |
|-----------|--------|-----------|
| Physics | ❌ No implementado | 🔴 Alta |
| Input system dedicado | ❌ Solo teclado ESC | 🟡 Media |
| Networking | ❌ No implementado | 🟢 Baja |

#### 🖥 Presentation

| Componente | Estado | Archivo |
|-----------|--------|---------|
| MainMenuView | ✅ Implementado | `presentation/ui/MainMenuView.java` |
| OptionsView | ✅ Implementado | `presentation/ui/OptionsView.java` |
| ContributorsView | ✅ Implementado | `presentation/ui/ContributorsView.java` |
| PauseMenu | ✅ Implementado | `presentation/ui/PauseMenu.java` |
| UIButtonFactory | ✅ Implementado | `presentation/ui/components/UIButtonFactory.java` |
| ImageBackground | ✅ Implementado | `presentation/ui/components/ImageBackground.java` |
| FontLoader | ✅ Implementado | `presentation/ui/components/FontLoader.java` |

#### 🔌 Infrastructure

| Componente | Estado | Archivo |
|-----------|--------|---------|
| GitHubService | ✅ Implementado | `infrastructure/external/github/GitHubService.java` |
| Contributor DTO | ✅ Implementado | `infrastructure/external/github/dto/Contributor.java` |
| LogManager | ✅ Implementado | `infrastructure/logging/LogManager.java` |

#### 📦 Shared

| Componente | Estado | Archivo |
|-----------|--------|---------|
| BrowserUtil | ✅ Implementado | `shared/util/BrowserUtil.java` |
| DTOs compartidos | 📁 Reservado | `shared/dto/` (vacío) |

### 9.2 Desviaciones respecto al plan original

| Cambio | Razón |
|--------|-------|
| DOD se integró dentro de `ecs/core/` | DOD es infraestructura de storage del ECS, no una capa independiente |
| Se creó `presentation/` | El plan original (ARCHITECTURE.MD) no contemplaba UI como capa separada; se separó de engine para mantener responsabilidades |
| Se creó `infrastructure/` | El plan original no contemplaba servicios externos (GitHub) ni logging como capa propia |
| Se creó `shared/` | Utilidades genéricas (BrowserUtil) no pertenecen a ninguna capa específica |
| `VideoBackground` renombrado a `ImageBackground` | La clase cargaba imágenes estáticas, no video |

### 9.3 Mapa visual de cumplimiento

```
Plan original                    Implementación actual
─────────────────────            ────────────────────
Clean Architecture    ──────  ✅ domain/entity/ + usecase/
ECS Layer             ──────  ✅ ecs/component/ + system/
DOD                   ──────  ✅ ecs/core/ (dentro de ECS)
Engine Layer          ──────  ⚠️ Parcial (sin physics, input, networking)
Presentation          ──────  ✅ presentation/ui/ (nueva capa)
Infrastructure        ──────  ✅ infrastructure/ (nueva capa)
Shared                ──────  ✅ shared/ (nueva capa)
```

---

## 10. Conclusión técnica

### 10.1 Estado del proyecto

| Aspecto | Evaluación |
|---------|-----------|
| **Fase** | Prototipo inicial (Fase 0 — Fundación) |
| **Versión** | 0.1.0-SNAPSHOT |
| **Madurez arquitectónica** | Alta — 6 capas correctamente separadas |
| **Madurez funcional** | Baja — solo menús, audio y navegación |
| **Cobertura de tests** | Nula — sin framework de testing |
| **CI/CD** | No implementado |

### 10.2 Riesgos actuales

| Riesgo | Impacto | Prioridad |
|--------|---------|-----------|
| `GameController.update()` y `render()` vacíos | El juego no ejecuta lógica de simulación real | 🔴 Alta |
| Sin sistema de física | No hay colisiones ni movimiento | 🔴 Alta |
| Domain + ECS sin integrar en el GameLoop | Las reglas de negocio existen pero no se ejecutan | 🔴 Alta |
| Sin sistema de input dedicado | Solo ESC para pausa, sin input de jugador | 🟡 Media |
| Sin tests automatizados | Riesgo de regresiones al añadir código | 🟡 Media |
| Sin CI/CD | Proceso de build y deploy manual | 🟢 Baja |

### 10.3 Próximos pasos recomendados

1. **Integrar ECS con GameLoop** — conectar `ecs/system/*` dentro de `GameController.update()`
2. **Implementar input system** — teclado/ratón para movimiento del jugador
3. **Añadir JUnit 5** — tests para `domain/usecase/*` y `ecs/system/*`
4. **Configurar GitHub Actions** — build automático en cada PR a develop
5. **Comenzar Fase 1 (Mundo Base)** — tiles, generación procedural, colisiones

### 10.4 Resumen

HollowForge tiene una **arquitectura sólida y bien definida** que soportará el crecimiento del proyecto. La estructura de 6 capas con reglas de dependencia claras garantiza mantenibilidad a largo plazo. El esqueleto funcional (menús, audio, navegación) está completo y operativo. El siguiente esfuerzo debe centrarse en **integrar las capas de dominio y ECS con el bucle de juego** para comenzar a tener gameplay real.

---

## Índice de documentos relacionados

| Documento | Contenido |
|-----------|-----------|
| [README.md](../README.md) | Introducción general, cómo ejecutar, features |
| [ARCHITECTURE.MD](ARCHITECTURE.MD) | Arquitectura, capas, dependencias (resumido) |
| [GITFLOW.md](GITFLOW.md) | Flujo GitFlow, ramas, commits (resumido) |
| [AGENT_PLAN.md](AGENT_PLAN.md) | Mapeo plan vs implementación (resumido) |
| [CONTRIBUTING.md](CONTRIBUTING.md) | Guía de contribución para developers |
| [STYLEGUIDE.md](STYLEGUIDE.md) | Guía de estilo de código |
| [ROADMAP.md](ROADMAP.md) | Plan de desarrollo por fases |
