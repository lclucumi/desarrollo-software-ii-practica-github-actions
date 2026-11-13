# Ejemplo de entregas: pruebas correctas y fallidas

La regla real es 2000 por kilometro. Las entregas prioritarias agregan el 25 %.
Por tanto, para cinco kilometros se esperan 12500 con prioridad y 10000 sin ella.

Las pruebas originales y la prueba adicional comprueban los valores correctos.
DemostracionPruebasFallidasTest contiene dos expectativas incorrectas a proposito.
Al ejecutar todas las pruebas, esas dos deben fallar y la tarea de Gradle terminar
con error; es el comportamiento esperado para esta demostracion.

Para dejar todas las pruebas en verde, modifiquen los valores esperados de esa clase
a 12500 y 10000 respectivamente, o retiren la clase de demostracion. No cambien
la formula correcta solo para que una expectativa incorrecta pase.

Con Java 21: en macOS/Linux ejecuten ./gradlew test; en Windows gradlew.bat test.
El Wrapper puede necesitar acceso a internet para descargar Gradle y dependencias.
El workflow no esta incluido: se crea en clase.
