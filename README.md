### Índice
- <a href="#01">Introducción</a>
- <a href="#02">Requerimientos</a>
- <a href="#03">Descripción de la prueba realizada</a>
- <a href="#04">Cómo vamos a ejecutar los tests de Gatling</a>
- <a href="#05">Cómo lanzar los tests de este proyecto</a>
- <a href="#06">Pasos a seguir</a>
- <a href="#07">Explicación de ficheros del proyecto</a>
- <a href="#08">Nuestro test</a>
- <a href="#09">Conceptos que maneja Gatling que hemos utilizado en el desarrollo del test</a>
- <a href="#10">El test actual está configurado de la siguiente manera</a>
- <a href="#11">Conclusión</a>
- <a href="#12">Documentación utilizada</a>



### <a name="01">Introducción</a>

Gatling permite programar tests de rendimiento/performance utilizando tests "de código" sin herramientas visuales como podría ser Jmeter.
En versiones anteriores de Gatling el lenguaje que se utilizaba para escribir los tests era Scala pero en las últimas versiones han añadido soporte para Java y Kotlin, aunque en última instancia parece que siempre ejecuta código Scala.

Es una tecnología muy configurable, lo que aporta un gran valor en este tipo de pruebas y puede utilizarse para la mayoría de los escenarios que se necesiten. Además genera un informe bastante completo con todos los resultados desgranados.

### <a name="02">Requerimientos</a>

- Java 11 o + (Recomendamos revisar https://www.adictosaltrabajo.com/2017/04/10/gestiona-tus-herramientas-de-desarrollo-con-sdkman/ )
- Maven 3.x
- Tu ide favorito. Nosotros utilizaremos Intellij.
- Proyecto de ejemplo en github  http://github.TODO.COM

### <a name="03">Descripción de la prueba realizada</a>

Vamos a realizar las pruebas sobre un proyecto Java + Springboot (http://github.TODO.COM) con un único endpoint rest. Haremos tests de rendimiento utilizando tests con Gatling programados en Java.

### <a name="04">Cómo vamos a ejecutar los tests de Gatling</a>

La forma que hemos elegido para ejecutarlo es mediante el plugin de maven (también se puede utilizar Gradle).

Existen otras formas de ejecutar Gatling pero no son parte de este tutorial.

Si queréis investigar más sobre el plugin de maven y sus configuraciones https://gatling.io/docs/gatling/reference/current/extensions/maven_plugin/


### <a name="05">Cómo lanzar los tests de este proyecto</a>

- Primero tendremos que descargar el proyecto de ejemplo para este tutorial http://github.TODO.COM
- Una vez importado debéis ver algo así:

![estructura](doc/images/estructura.png)

Este es un proyecto "dummy" con un ejemplo de endpoint que crea personajes de videojuegos de forma aleatoria.

Hemos añadido además un test de integración con Springboot para poder probarlo en local y confirmar que funciona (y para que juguéis).

Para que el informe de Gatling nos quede lo más completo visible para este tutorial, el proyecto dummy lanza excepciones de forma aleatoria.

### <a name="06">Pasos a seguir</a>

- Una vez descargado el proyecto y compilado con

mvn clean install

- Deberemos arrancar en local la aplicación. Si utilizáis un ide basta con seleccionar la clase GatlingTutorialJavaApplication y ejecutarla.
  Ya tenemos la aplicación levantada, la hemos configurado para el puerto 8023 por defecto (en el application.properties)

![start](doc/images/start.png)

- Ahora es el momento de ejecutar el comando para lanzar los tests de gatling, desde la raíz del proyecto (donde está el pom.xml).

mvn clean gatling:test

- Cuanto termina la ejecución muestra un resumen en modo “texto” y un link al informe generado en html:

![resultado](doc/images/resultado.png)

- Como se puede ver el informe html se genera y se guarda en target/gatling. Si no se utiliza el “clean” de maven se verían todas las ejecuciones que se han realizado, cada una en una carpeta con el timestamp de cuándo se ejecutó.

![targetInforme](doc/images/targetInforme.png)

- Al abrir index.html con tu navegador favorito podrás ver algo como ésto

![informe](doc/images/informe.png)


### <a name="07">Explicación de ficheros del proyecto</a>

Vamos a dar pequeñas pinceladas de todos los ficheros referentes a Gatling que hemos tenido que utilizar:

- De src/test/resources:
    - gatling.conf. Es la configuración base de Gatling, para este ejemplo no se ha tocado, pero como se puede ver es bastante configurable.
    - logback-test.xml. A partir de este fichero de configuración de logs se pueden mostrar las trazas de las respuestas por ejemplo, poniendo a debug la siguiente paquetería “io.gatling.http.response".
    - recorder.conf. Es la configuración de la grabación de Gatling. No se ha tocado para esta prueba.
- De src/test/java/gatling/base. Todos estos ficheros se han copiado tal cual de la documentación:
    - Engine.java. Es el motor de gatling y se puede ver que es un main, seguramente con el que arranca todo.
    - IDEPathHelper.java. Por lo que entiendo aquí es donde se buscan todas las rutas de maven para coger las configuraciones y dejar los resultados.
    - Recorder.java. La configuración del grabador.
- De src/test/java/gatling/com/autentia/rvillalba/gatling/tutorial/java:
    - NonPlayableCharacterSimulation.java. Esto es el test propiamente dicho.

Los demás ficheros anteriores serían únicamente para poder levantar y configurar Gatling, son comunes a todos los tests. Así nuestras "Simulation" ya solo contienen código de test y queda todo bastante limpio.

### <a name="08">Nuestro test</a>

Realiza varias llamadas variando el parámetro quantity a nuestro único endpoint. Bastante sencillo pero suficiente para este tutorial.

![test](doc/images/test.png)

### <a name="09">Conceptos que maneja Gatling que hemos utilizado en el desarrollo del test</a>

Como se puede ver en el test Gatling tiene varios conceptos, que aunque simples, pueden ser un poco liosos al principio (hay una pequeña curva de aprendizaje):

- Chain. Un chain es un grupo de acciones que se van a ejecutar en el orden que se establecen.
    - En nuestro caso hemos creado 2 chain progressiveRandoms y massiveRandoms. Ambos contienen 3 llamadas http.
- Action. Las actions en Gatling se representan por métodos, explicamos los que hemos utilizado y alguno más que nos ha parecido interesante:
    - exec - Se suele utilizar para realizar las llamadas http a los endpoints.
    - pause - Entre llamada y llamada hace una parada por el tiempo indicado.
    - repeat - Permite repetir n veces otra acción, por ejemplo un exec.
    - http. Es lo que se utiliza para formar una llamada en Gatling, en nuestro caso un GET.
      Aquí es donde podemos añadir un body, headers y demás cuestiones. Gatling ofrece también clases para leer ficheros json y csv y poder hacer acciones a partir de ficheros de datos.
      Existe una cantidad ingente de métodos que nos permite hacer validaciones sobre los valores que llegan en una respuesta y reutilizarlos en posteriores acciones (para guardar con saveAs)
      Además trae utilidades para lectura de csvs y lanzar acciones por cada registro del csv y así realizar pruebas parametrizadas.
- HttpProtocolBuilder. Este builder sirve para configurar todas las cosas genéricas de varias peticiones a un mismo servicio. Se puede indicar desde la baseUrl hasta las headers generales para no tener toda esta información duplicada por cada action.
- Scenario. Un escenario puede contener varios Chain, aunque nosotros en el ejemplo solo hemos puesto uno. Cada escenario se lanza de forma separada y puede llevar configuraciones de usuarios concurrentes y duración distintas. Esto nos puede permitir probar en paralelo flujos distintos.

En el informe html aparecerá todo separado por Scenario, Chain y Action, por eso hemos puesto nombres descriptivos en cada elemento.

### <a name="10">El test actual está configurado de la siguiente manera</a>

Por cada usuario se realizarán los escenarios progressiveCharactersScenario y randomCharactersScenario durante 10 segundos.

Está en modo “rampUsers” con un máximo de 1000 usuarios concurrentes.
Esto quiere decir que irá metiendo peticiones por usuarios distintos hasta que tenga 1000 en paralelo, se puede ver bien en las gráficas del informe.

Con esta configuración se lanzan 6000 peticiones en alrededor de 2 minutos.

### <a name="11">Conclusión</a>

Gatling funciona bastante bien y permite realizar muchas llamadas en paralelo.
Aun así está sujeto, igual que otras herramientas, a los recursos de la máquina en la que se lanza.
Por lo que estos resultados pueden variar según el equipo desde donde se ejecute. Su uso debería estar ligado a un entorno controlado donde estuviera la aplicación desplegada.
Para complementar los resultados ofrecidos por Gatling sería muy interesante utilizar un APM (como https://glowroot.org/).

Además, resulta muy interesante para “acercar al código” las pruebas de rendimiento.
Históricamente se realizan con herramientas tipo Jmeter que nos obliga a utilizar una aplicación externa al código y que conllevan un aprendizaje sobre una herramienta "externa al código",
pero si se quisiera, Gatling se puede utilizar con su "Gatling Recorder" que es una aplicación de escritorio para crear y lanzar las pruebas.

Al poder ejecutarse con maven o desde plugins de ci/cd puede automatizarse de forma sencilla para ejecutarse asiduamente.

Como el informe es una web estática también podría plantearse tener un histórico de resultados vía web o utilizar los plugins de CI/CD que permiten ver el informe generado embebido.

![plugins](doc/images/plugins.png)

#### <a name="12">Documentación utilizada</a>
- https://gatling.io/
- https://gatling.io/docs/gatling/tutorials/advanced/
- https://github.com/gatling/gatling-maven-plugin-demo-java
- https://github.com/gatling/gatling/tree/main/gatling-samples/src/main