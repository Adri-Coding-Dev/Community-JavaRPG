# 🤝 Guía de Contribución

Antes de nada:

Gracias por querer contribuir a HollowForge.

Este proyecto tiene un enfoque:

* educativo,
* colaborativo,
* profesional,
* y accesible.

---

## 📚 Antes de Empezar

Por favor, lee:

* [README.md](../README.md)
* [ARCHITECTURE.MD](ARCHITECTURE.MD)
* [STYLEGUIDE.md](STYLEGUIDE.md)
* [GITFLOW.md](GITFLOW.md)

antes de contribuir.

---

## 🧩 Tipos de Contribuciones

Aceptamos:

✅ Código

✅ Corrección de bugs

✅ Refactors

✅ Pixel art

✅ Música

✅ Traducciones

✅ Testing

✅ Optimización

✅ Documentación

---

## 🔀 Convención de Ramas (GitFlow)

Todas las ramas deben crearse desde `develop`:

| Propósito | Rama |
|-----------|------|
| Nueva funcionalidad | `feat/mi-cambio` |
| Corrección de error | `fix/mi-cambio` |
| Mejora estructural | `refactor/mi-cambio` |
| Documentación | `docs/mi-cambio` |
| Mantenimiento | `chore/mi-cambio` |
| Pruebas | `test/mi-cambio` |
| Rendimiento | `perf/mi-cambio` |
| CI/CD | `ci/mi-cambio` |

Ejemplo:

```txt
feat/inventario
fix/colisiones
refactor/render-system
docs/api-endpoints
```

Ver [GITFLOW.md](GITFLOW.md) para el detalle completo.

---

## 📝 Convención de Commits

```
type(scope): subject

body (opcional)

#ISSUE (opcional)
```

### Types válidos

`feat` · `fix` · `chore` · `docs` · `style` · `refactor` · `test` · `perf` · `ci` · `build` · `revert`

### Ejemplos

```
feat(auth): implement login validation

#42

fix(ui): correct mobile button alignment

#15

refactor(project): align package structure with ARCHITECTURE.md
```

No uses formatos antiguos como `[feature]` o `[FIX]`. Usa siempre `type(scope):`.

---

## 📌 Reglas Importantes

### 1. Prioriza simplicidad

No sobreingenierices.

Preferimos:

* código legible,
* modular,
* mantenible.

### 2. Mantén coherencia arquitectónica

Aunque algo funcione, puede rechazarse si:

* rompe la arquitectura definida en ARCHITECTURE.MD,
* introduce complejidad innecesaria,
* dificulta el mantenimiento.

### 3. Evita Pull Requests gigantes

Haz PR:

* pequeños,
* claros,
* específicos.

### 4. Respeta la estructura de capas

No:

* mezcles lógica de negocio en UI,
* pongas código de infraestructura en domain,
* crees dependencias circulares entre capas.

---

## 🔄 Flujo de contribución

```
1. git checkout develop
2. git pull origin develop
3. git checkout -b feat/mi-funcionalidad
4. (trabajar y hacer commits)
5. git push origin feat/mi-funcionalidad
6. Crear Pull Request → develop
7. Un maintainer revisa y hace merge
```

---

## 🌱 Principiantes Bienvenidos

Este repositorio también es educativo. No importa tu nivel.

Si tienes dudas:

* pregunta,
* abre discusiones,
* crea Draft PR,
* participa.

---

## 🚫 Prácticas Prohibidas

Evita:

❌ Clases gigantes

❌ Herencia excesiva

❌ Código duplicado

❌ Variables globales innecesarias

❌ Abstracciones absurdas

❌ Código imposible de leer

❌ Commits directos a `main`, `beta` o `develop`

---

## ❤️ Filosofía Final

El objetivo no es escribir:

> el código más inteligente.

El objetivo es escribir:

> el código más mantenible y entendible posible
