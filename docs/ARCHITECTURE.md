# ARQUITECTURA

---

## Visión general

HollowForge utiliza una **arquitectura híbrida por capas** que combina Clean Architecture, ECS (Entity Component System) y Data-Oriented Design para lograr un equilibrio entre mantenibilidad, testabilidad y rendimiento en tiempo real.

```
┌─────────────────────────────────────────────────────┐
│                    app/ (entry point)               │
├─────────────────────────────────────────────────────┤
│                  presentation/                      │
│              (UI del juego, vistas, menús)          │
├─────────────────────────────────────────────────────┤
│              engine/ (runtime del juego)            │
│    audio · core · game · navigation · rendering     │
├───────────────────┬─────────────────────────────────┤
│     domain/       │        ecs/                     │
│  (reglas negocio) │  (simulación en tiempo real)    │
│                   │  component/ · system/ · core/   │
├───────────────────┴─────────────────────────────────┤
│           infrastructure/                           │
│      external/ (GitHub, APIs) · logging/            │
├─────────────────────────────────────────────────────┤
│               shared/ (utilidades genéricas)        │
└─────────────────────────────────────────────────────┘
```

---

## Capas del sistema

### 🧼 Domain — Clean Architecture

Reglas de negocio del juego, completamente independientes del motor y la infraestructura.

| Paquete | Contenido | Ejemplos |
|---------|----------|----------|
| `domain/entity/` | Entidades del dominio | `PlayerStats`, `Inventory`, `Quest` |
| `domain/usecase/` | Casos de uso | `ApplyDamage`, `LevelUp` |

**Reglas:**
- ✅ Sin dependencias hacia engine, ECS, presentation ni infrastructure
- ✅ Solo usa Java estándar (sin JavaFX)
- ✅ 100% testable

---

### 🎮 ECS — Entity Component System

Simulación del mundo del juego en tiempo real.

| Paquete | Contenido | Ejemplos |
|---------|----------|----------|
| `ecs/component/` | Componentes de datos | `Position`, `Velocity`, `Health`, `Damage` |
| `ecs/system/` | Sistemas de lógica | `MovementSystem`, `CombatSystem`, `AISystem` |
| `ecs/core/` | Infraestructura de storage | `EntityArray`, `ComponentChunk` (DOD) |

**Reglas:**
- ✅ Components: solo datos, sin lógica
- ✅ Systems: solo lógica, sin estado
- ✅ `ecs/core/` implementa patrones Data-Oriented Design (arrays planos, cache-friendly)
- ❌ No depende de domain ni engine

---

### 🧱 Engine — Runtime

Ejecución técnica del juego. Capa que orquesta el ciclo de vida de la aplicación.

| Paquete | Contenido |
|---------|----------|
| `engine/audio/` | Reproducción de música y efectos (`AudioManager`) |
| `engine/config/` | Constantes globales (`AppConstants`) |
| `engine/core/` | Bucle principal del juego (`GameLoop`) |
| `engine/game/` | Controlador del estado de juego (`GameController`) |
| `engine/navigation/` | Gestión de escenas y navegación (`SceneManager`) |
| `engine/rendering/` | Renderizado de la vista de juego (`GameView`) |

**Reglas:**
- ✅ Depende de `domain` y `ecs` (consume reglas de negocio y simulación)
- ✅ Depende de `infrastructure` (logging)
- ✅ Depende de `presentation` (carga vistas UI)
- ❌ No contiene lógica de negocio ni UI directamente

---

### 🖥 Presentation — Interfaz de Usuario

Vistas, menús y componentes visuales construidos con JavaFX.

| Paquete | Contenido |
|---------|----------|
| `presentation/ui/` | Pantallas del juego |
| `presentation/ui/components/` | Componentes reutilizables |

**Clases:**
- `MainMenuView` — menú principal con botones y redes sociales
- `OptionsView` — configuración de volumen
- `ContributorsView` — lista de contribuidores desde GitHub
- `PauseMenu` — menú de pausa durante la partida
- `UIButtonFactory` — fábrica de botones con textura
- `ImageBackground` — fondo de pantalla estático
- `FontLoader` — carga de fuentes personalizadas

**Reglas:**
- ✅ Depende de `engine` (audio, config) y `infrastructure`
- ❌ No contiene lógica de negocio

---

### 🔌 Infrastructure — Integraciones Externas

Servicios de IO, logging y APIs externas.

| Paquete | Contenido |
|---------|----------|
| `infrastructure/external/github/` | Integración con GitHub API |
| `infrastructure/external/github/dto/` | DTOs de GitHub |
| `infrastructure/logging/` | Sistema de logs |

**Clases:**
- `GitHubService` — cliente HTTP para la API de contribuidores
- `Contributor` — DTO con datos de un contribuidor
- `LogManager` — logger con salida a consola y archivo

---

### 📦 Shared — Utilidades Genéricas

Código transversal sin capa específica.

| Paquete | Contenido |
|---------|----------|
| `shared/util/` | Utilidades genéricas |
| `shared/dto/` | DTOs compartidos (reservado para futuro) |

**Clases:**
- `BrowserUtil` — apertura de URLs en navegador del sistema

---

## Reglas de dependencias entre capas

```
app ───→ engine ───→ domain
  │         │            │
  │         ├──→ ecs ────┘
  │         │
  │         └──→ presentation ───→ infrastructure
  │                                      │
  └──────────────────────────────────────┘
                         ↓
                      shared (visible a todas)
```

| Desde | Hacia | Dirección |
|-------|-------|-----------|
| `domain` | — | Independiente (raíz) |
| `ecs` | — | Independiente (raíz) |
| `engine` | `domain`, `ecs`, `presentation`, `infrastructure` | ↓ (consume) |
| `presentation` | `engine`, `infrastructure`, `shared` | ↓ (consume) |
| `infrastructure` | `shared` | ↓ (consume) |
| `shared` | — | Transversal (todos pueden usar) |

---

## Flujo de inicio de la aplicación

```
MainApp.main()
  └─ launch()
      └─ MainApp.start(stage)
          ├─ Crea GitHubService
          ├─ Crea AudioManager
          │   └─ Inicia música del menú
          ├─ Crea SceneManager
          └─ sceneManager.mostrarMenuPrincipal()
              ├─ Crea MainMenuView
              └─ Cambia escena (Scene)
```

---

## Stack tecnológico

| Área | Tecnología | Versión |
|------|-----------|---------|
| Lenguaje | Java | 25 |
| UI/Render | JavaFX | 25.0.2 |
| Build | Maven | 3.x |
| HTTP Client | java.net.http | (JDK estándar) |
| JSON | org.json | 20231013 |
| Logging | java.util.logging | (JDK estándar) |
| Control de versiones | Git | — |

---

## Atajos de ejecución

```bash
# Compilar
cd src/
mvn clean compile

# Ejecutar
mvn javafx:run
```

Clase principal: `dev.hollowforge.app.MainApp`
