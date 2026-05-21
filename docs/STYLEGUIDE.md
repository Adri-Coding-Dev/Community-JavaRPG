# 🎨 Guía de Estilo

---

# ✨ Filosofía General

El código debe ser:

* legible,
* explícito,
* consistente,
* mantenible.

---

# 🏷️ Convenciones de Nombres

## Clases

Usa PascalCase:

```java
PlayerEntity
WorldRenderer
InventorySystem
```

---

## Variables y métodos

Usa camelCase:

```java
loadWorld()
renderPlayer()
currentHealth
```

---

## Constantes

Usa UPPER_CASE:

```java
MAX_HEALTH
TILE_SIZE
```

---

# 🧱 Diseño de Clases

Cada clase debe tener:

✅ Una única responsabilidad.

Evita:

❌ Clases monstruo.

---

# 🧠 Métodos

Preferimos:

* métodos pequeños,
* nombres descriptivos,
* comportamiento claro.

---

# 💬 Comentarios

Comenta:

> POR QUÉ existe algo.

No:

> QUÉ hace código obvio.

---

# 🔢 Magic Numbers

Evita valores hardcodeados.

Usa:

* constantes,
* configuraciones,
* enums.

---

# 📚 Enfoque Educativo

Este proyecto también busca enseñar.

Escribe código que:

* principiantes puedan entender,
* contribuidores puedan mantener,
* la comunidad pueda aprender.

---
