# TP2 - Juego de Adivinanza (App Android)

Esta aplicación consiste en un simple juego de adivinanza numérica desarrollado con **Kotlin** y **Jetpack Compose**. El usuario debe adivinar un número aleatorio entre 1 y 5 y obtener puntaje según su desempeño. Los puntajes se guardan de forma persistente usando **DataStore**.

## Funcionamiento

- Al iniciar la app, el usuario ingresa su **nombre**.
- Se genera un **número aleatorio entre 1 y 5** al comenzar la partida.
- El usuario intenta adivinar el número.
- El puntaje obtenido depende de la cantidad de intentos fallidos:
  - 0 fallos: 10 puntos
  - 1 fallo: 8 puntos
  - 2 fallos: 6 puntos
  - 3 fallos: 4 puntos
  - 4 fallos: 2 puntos
- Si falla **5 veces seguidas**, **pierde la ronda** y si no acertó al menos una vez, **pierde todos sus puntos acumulados**.
- El puntaje se **acumula entre partidas** para cada jugador.
- Se guarda el **mejor puntaje individual**, el **puntaje actual de la ronda**, y el **puntaje total acumulado**.
- Se puede acceder a un **ranking global** que muestra el puntaje total más alto de cada jugador.

##  Almacenamiento

Utiliza **Jetpack DataStore (Preferences)** para persistir:
- Nombre del jugador
- Puntaje total
- Mejor puntaje
- Puntaje actual de ronda
- Ranking general

La información se guarda localmente, sin necesidad de conexión a Internet.

##  Tecnologías

- Kotlin
- Jetpack Compose
- Navigation Compose
- Jetpack DataStore
- Gson (para serialización de mapas en el ranking)

##  Pantallas

- **StartScreen**: Ingreso del nombre del jugador.
- **GameScreen**: Juego de adivinanza.
- **ResultScreen**: Muestra el resultado de la ronda y permite volver a jugar.
- **RankingScreen**: Lista de los jugadores con mayor puntaje acumulado.




