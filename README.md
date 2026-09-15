# LabSem06

Aplicación Android de ejemplo para guardar, leer y eliminar datos simples de forma persistente mediante **SharedPreferences** con `Context.MODE_PRIVATE`.

Las preferencias se almacenan en el archivo privado `user_preferences.xml` y se recuperan al volver a abrir la aplicación.

## Requisitos

- Android Studio (última versión estable)
- Android SDK 37
- JDK (Gradle puede aprovisionarlo automáticamente vía foojay)
- Gradle 9.5.0 — se descarga automáticamente con el wrapper incluido

### Stack

- Kotlin
- Jetpack AppCompat + Material Components (Material 3)
- ConstraintLayout + MaterialCardView
- minSdk 24 · targetSdk 37

## Cómo compilar y ejecutar

1. Clona el repositorio y ábrelo con Android Studio.
2. Espera a que Gradle sincronice (genera tu propio `local.properties` automáticamente).
3. Pulsa **Run** sobre el módulo `app`.

O por línea de comandos:

```bash
./gradlew :app:installDebug
```

## Cómo usar la app

1. **Nombre de usuario**: escribe tu nombre (máximo 24 caracteres). Es obligatorio para poder guardar; si está vacío, se muestra un error en el campo.
2. **Modo oscuro**: activa o desactiva el interruptor; el tema cambia al instante y queda recordado.
3. **Recordar sesión**: si está activo, al volver a abrir la aplicación tu nombre de usuario se autocompleta. Al alternar el interruptor se muestra un diálogo informativo. Si está desactivado, el campo arranca vacío (el dato guardado no se borra).
4. **Contador de aperturas**: suma 1 en cada arranque real de la aplicación.
5. **Último guardado**: muestra la hora en que se persistieron las preferencias por última vez; inicio "nunca".
6. **Guardar**: valida y persiste el nombre y el estado de los dos interruptores, y actualiza la hora del último guardado.
7. **Restablecer**: elimina todas las preferencias y devuelve la interfaz a sus valores iniciales (incluido el modo claro).

> Nota: los interruptores se guardan automáticamente al alternarlos; el nombre de usuario solo se persiste al pulsar **Guardar**.

## Datos persistidos

| Dato               | Clave              | Tipo    |
| ------------------ | ------------------ | ------- |
| Nombre de usuario  | `username`         | String  |
| Modo oscuro        | `dark_mode`        | Boolean |
| Recordar sesión    | `remember_session` | Boolean |
| Contador aperturas | `launch_count`     | Int     |
| Último guardado    | `last_saved`       | String  |

## Estructura del proyecto

```
app/src/main/
├── java/com/example/labsem06/
│   └── MainActivity.kt          # Lógica de SharedPreferences y UI
└── res/
    ├── layout/activity_main.xml # Formulario con los controles
    ├── values/                  # Tema claro azul, strings, colores
    ├── values-night/            # Tema oscuro azul
    └── drawable/                # Fondo degradado y estilo del campo
```