# Guía de Estilo
__Filosofía General__

> [!NOTE]
> El código debe ser:
> legible,
> explícito,
> consistente,
> mantenible.

## Convenciones de Nombres

### __Utiliza PascalCase:__

PlayerEntity
WorldRenderer
InventorySystem

## Métodos y Variables

### __Utiliza camelCase:__

loadWorld()
renderPlayer()
currentHealth

## Constantes

### __Utiliza UPPERCASE:__

MAX_HEALTH
TILE_SIZE

## Diseño de Clases
### __Mantén las clases enfocadas__

Cada clase debe tener una única responsabilidad clara.

Evita clases gigantes multifunción.

## Métodos

> [!TIP] 
> Preferimos:
> métodos cortos,
> nombres descriptivos,
> comportamiento explícito.

> [!CAUTION]
> Evita:
> efectos secundarios ocultos,
> métodos excesivamente largos.

## Comentarios

### __Comenta:__

POR QUÉ existe algo,
no QUÉ hace código obvio.

> [!CAUTION]
> Incorrecto:
> // incrementa x
> x++;

> [!NOTE]
> Correcto:
> // Ajuste necesario para compensar alineación del sprite
> x++;


## Magic Numbers

### __Evita valores hardcodeados.__

> [!TIP]
> Utiliza:
> constantes,
> configuraciones,
>enums.

## Formateo

> [!TIP]
> Mantén:
> indentación consistente,
> espacios lógicos,
> saltos de línea legibles.

## Principios de Clean Code

> [!NOTE]
> Preferimos:
> composición,
> nombres claros,
> modularidad,
> dependencias explícitas.

> [!CAUTION]
> Evita:
> abstracciones innecesarias,
> optimización prematura,
> herencia profunda.

## Enfoque Educativo

## __Este repositorio también es educativo.__

> [!IMPORTANT]
> Escribe código que:
> principiantes puedan leer,
> contribuidores puedan entender,
> futuros maintainers puedan mantener.
