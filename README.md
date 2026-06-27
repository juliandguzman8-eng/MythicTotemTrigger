# MythicTotemTrigger

Addon para **MythicMobs** que añade el trigger personalizado `~onTotemPop`.

Detecta cuando un **Tótem de Inmortalidad** salva a una entidad de morir y dispara
las skills de MythicMobs configuradas con ese trigger.

---

## Requisitos

| Software | Versión mínima |
|----------|---------------|
| Paper/Spigot | 1.21+ |
| MythicMobs | 5.6+ |
| Java | 17+ |

---

## Compilar

```bash
cd MythicTotemTrigger
mvn clean package
```

El `.jar` se genera en `target/MythicTotemTrigger-1.0.0.jar`.

---

## Instalación

1. Copia el `.jar` en la carpeta `plugins/` de tu servidor.
2. Asegúrate de que **MythicMobs** esté instalado.
3. Reinicia el servidor.
4. Verás en consola: `MythicTotemTrigger enabled — OnTotemPop trigger registered!`

---

## Uso en YAML de MythicMobs

### En mobs (`plugins/MythicMobs/Mobs/`)

```yaml
MiMob:
  Type: ZOMBIE
  Display: '&cZombie Inmortal'
  Health: 200
  Skills:
  - skill{s=ExplosionOnTotem} @self ~onTotemPop
  - effect:particles{p=totem;amount=50} @self ~onTotemPop
```

### En skills (`plugins/MythicMobs/Skills/`)

```yaml
ExplosionOnTotem:
  Skills:
  - effect:explosion @self
  - message{m="<mob.name> &asurvivió con el tótem!"} @PIR{r=20}
```

### Aliases aceptados

Cualquiera de estos funciona en el YAML:

```yaml
~onTotemPop    # nombre principal
~onTotem
~onUndying
~totem
~totempop
```

---

## ¿Cómo funciona?

1. Bukkit dispara `EntityResurrectEvent` cuando un tótem salva a una entidad.
2. El listener del plugin captura ese evento.
3. Busca si la entidad es un **ActiveMob** de MythicMobs.
4. Si lo es, dispara el trigger `OnTotemPop` sobre él → MythicMobs ejecuta todas las skills asociadas.

---

## Notas

- Funciona con **mobs** que tengan el tótem en el inventario (comportamiento vanilla).
- También detecta si un **jugador** que sea un mob de MythicMobs usa el tótem.
- Compatible con **MythicCrucible** (los ítems con skills no necesitan lógica extra,
  MythicMobs los gestiona internamente una vez registrado el trigger).
- El plugin no hace **nada más** — es un complemento mínimo y enfocado.
