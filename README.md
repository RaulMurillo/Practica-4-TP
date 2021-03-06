# Práctica 6 (final) - Tecnología de la Programación. UCM
## Autores:
  Raul Murillo Montero
  Antonio Valdivia de la Torre
----------------------------
## 2ºDG Ingenieria Informatica - Matematicas

### CAMBIOS SOBRE LA PRACTICA 4:
* Las constantes e instancias de enum ahora estan todas en mayusculas.
* Resuleto problema al introducir obstaculos y no dimension: ahora se crea un juego de 5x5 con n obstaculos (no da error).
* Mejorados algunos nombre de variables significativas (por ejemplo, se cambio destiny por target).
* Mejorados los bucles de recorrido de casillas colindantes.
* No se declaran variables sin inicializar.
* Mejor especificacion del algoritmo de simetria de obstaculos.
* Se ha eliminado la clase UtilsPr4. El unico metodo que contenia se ha movido a la clase desde la que se llama (AtaxxMove).

### CAMBIOS SOBRE LA PRACTICA 5:
* Se han eliminado los serialVersion 1L por los generados en todas las clases que nos corresponden
* La anterior clase SwingPlayer ha desaparecido. Ahora existe como clase interna de GenericSwingView y tiene menos responsabilidades.
* Se ha cambiado la operacion de cierre predeterminada de la vista grafica. Ahora tiene el mismo manejador que el boton "Quit".
* Se han sustituido los "==" del codigo por "equals()" para poder usar la aplicacion con servidor.
* Corregido el reseteo del juego para el ATTT.
* Las imagenes se cargan desde el src, por lo que ahora se veran incluso cuando la aplicacion se ejecute desde un .JAR.
* Se han ñadido llaves {} en todas las clausulas if... else...
* Las clases internas anonimas no dan fallo de compilacion con JDK 1.7, las variables de contexto ahora son final.
* Se utiliza Logging.

### EXTRAS SOBRE EL ENUNCIADO DE LA PRACTICA 6:
* Se han realizado los 3 apartados opcionales del enunciado de la practica 6.
* Ademas en la vista grafica existe un selector de tiempo para la ejecucion de los movimientos inteligentes (cuando estan disponibles).
* Se ha incluido ademas como parte opcional un lobby con un chat para cuya realizacion utilizamos el codigo del chat de prueba
  del campus virtual y lo hemos integrado en nuestra aplicacion.
* Tambien se permite que haya clientes conectados en modo observador a la partida.
* Finalmente se ha hecho una copia y posterior modificacion de la clase MinMax (no se podia extender por tener ciertos metodos privados)
  y ahora si en lugar de usar MinMax usamos MinMaxExt se calculan jugadas hasta el tiempo dado o la profundida maxima, para esto se van
  guardando los mejores movimientos calculados hasta el momento y se devuelve este en lugar de null si el hilo del jugador es interrumpido
  (Esta clase no se usa, solo se creo para experimentar con ella)
