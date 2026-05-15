# 🏗️ Arquitectura del Proyecto

---

# 🎯 Filosofía Arquitectónica

HollowForge prioriza:

* simplicidad,
* modularidad,
* legibilidad,
* mantenibilidad,
* y aprendizaje.

La complejidad solo debe añadirse cuando sea realmente necesaria.

---

# 🧱 Principios Fundamentales

## 1. Composición sobre herencia

Preferimos:

* componentes pequeños,
* composición,
* sistemas desacoplados.

Evita:

* jerarquías profundas,
* herencia innecesaria,
* abstracciones excesivas.

---

## 2. Sistemas desacoplados

Los sistemas deben mantenerse separados:

* render,
* input,
* físicas,
* audio,
* lógica.

---

## 3. Legibilidad primero

El código debe:

* ser fácil de entender,
* fácil de modificar,
* fácil de mantener.

---

# 📁 Estructura Planeada

```txt
engine/
render/
world/
entity/
audio/
input/
ui/
save/
resource/
modding/
```

---

# 🧍 Filosofía de Entidades

Las entidades deben ser ligeras.

El comportamiento debe delegarse en:

* sistemas,
* componentes,
* managers.

---

# 🎨 Renderizado

El renderizado será gestionado mediante JavaFX.

La pipeline debe mantenerse:

* simple,
* modular,
* comprensible.

---

# 🧩 Sistema de Mods

La primera versión del sistema de mods soportará:

* texturas,
* sonidos,
* traducciones,
* configuraciones.

Más adelante:

* scripting,
* lógica personalizada,
* comportamiento avanzado.

---

# 💾 Sistema de Guardado

El sistema de guardado debe ser:

* robusto,
* versionable,
* mantenible,
* legible cuando sea posible.

---

# 🚀 Objetivo Final

Construir una arquitectura:

* escalable,
* educativa,
* mantenible,
* y profesional.

---
