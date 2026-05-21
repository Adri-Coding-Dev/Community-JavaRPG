# GitFlow — Flujo de Trabajo

---

## Ramas del repositorio

```
master ────────► producción
beta ──────────► pre-lanzamiento / cliente
develop ───────► integración general
  ├── feat/* ──► nuevas funcionalidades
  ├── fix/* ───► corrección de errores
  ├── refactor/* ► mejoras estructurales
  ├── docs/* ──► documentación
  ├── chore/* ─► mantenimiento
  ├── test/* ──► pruebas
  ├── perf/* ──► rendimiento
  ├── ci/* ────► integración continua
  └── build/* ─► compilación / artefactos
```

---

## Jerarquía de ramas

### 🛡 Ramas protegidas (sin commits directos)

| Rama | Propósito | ¿Quién hace merge? |
|------|-----------|-------------------|
| `master` | Producción final. Solo código estable y releaseado | Maintainers |
| `beta` | Versión para cliente / testers | Maintainers |
| `develop` | Integración de todas las funcionalidades | Maintainers |

### 🔧 Ramas de trabajo (sin protección)

| Rama | Propósito | Base |
|------|-----------|------|
| `feat/*` | Nueva funcionalidad | `develop` |
| `fix/*` | Corrección de bug | `develop` |
| `refactor/*` | Mejora interna sin cambio de comportamiento | `develop` |
| `docs/*` | Documentación | `develop` |
| `chore/*` | Tareas de mantenimiento (limpieza, deps) | `develop` |
| `test/*` | Nuevas pruebas o mejora de cobertura | `develop` |
| `perf/*` | Optimización de rendimiento | `develop` |
| `ci/*` | Pipelines, CI/CD | `develop` |
| `build/*` | Sistema de compilación, artefactos | `develop` |

---

## Flujo de trabajo diario

```
1. git checkout develop
2. git pull origin develop
3. git checkout -b feat/mi-funcionalidad
4. ... (trabajar y hacer commits)
5. git push origin feat/mi-funcionalidad
6. Crear Pull Request → develop en GitHub/GitLab
7. Un maintainer revisa y hace merge
```

---

## Convención de commits

Cada commit debe seguir esta estructura:

```
type(scope): subject

body (explicación del cambio)

#ISSUE_NUMBER (opcional)
```

### Types válidos

| Type | Cuándo usarlo |
|------|---------------|
| `feat` | Nueva funcionalidad |
| `fix` | Corrección de error |
| `chore` | Mantenimiento, limpieza, dependencias |
| `docs` | Documentación |
| `style` | Formato, estilo de código (no lógica) |
| `refactor` | Cambio estructural que no altera comportamiento |
| `test` | Pruebas nuevas o modificadas |
| `perf` | Mejora de rendimiento |
| `ci` | Pipelines, CI/CD |
| `build` | Sistema de compilación, artefactos |
| `revert` | Reversión de un commit anterior |

### Scopes recomendados

| Ámbito | Scopes |
|--------|--------|
| Frontend | `ui`, `styles`, `components`, `layout` |
| Backend | `api`, `database`, `auth`, `server` |
| Sysadmin | `ci`, `cd`, `docker`, `build`, `scripts` |
| General | `docs`, `tests`, `config`, `project`, `git` |

### Ejemplos

```
feat(auth): implement login validation

Added JWT validation and login flow.

#12

fix(ui): correct mobile button alignment

Fixed responsive layout for small screens.

#15

refactor(project): align package structure with ARCHITECTURE.md

Reorganized project into 6 clean layers.

docs(architecture): update layer dependency rules
```

---

## Integración con issues

Si el cambio está relacionado con un issue:

```
feat(auth): implement login validation

#42
```

- Incluir el número en el footer del commit
- El issue se cierra automáticamente al mergear si se usa "Closes #42" en el body
- No inventar números de issue si no existen

---

## Flujo de release

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

1. **develop**: se integran todas las ramas de trabajo
2. **beta**: se corta una rama de release desde develop para QA y testing
3. **master**: se fusiona beta en master cuando está listo para producción

---

## Versionado semántico

Formato: `MAJOR.MINOR.PATCH` (según semver.org)

| Componente | Cuándo incrementar |
|------------|-------------------|
| MAJOR | Cambios incompatibles con versiones anteriores |
| MINOR | Nuevas funcionalidades compatibles hacia atrás |
| PATCH | Correcciones de bugs compatibles |

Actualmente: **0.1.0-SNAPSHOT** (fase de prototipo inicial)
