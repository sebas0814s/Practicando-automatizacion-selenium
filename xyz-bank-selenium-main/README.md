# Ejercicio automatización

Crear un nuevo proyecto de automatización en base al proyecto trabajado en las clases, automatizando los siguientes escenarios de la página:
https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login

Escenarios
- Deposito: ingresar, seleccionar un usuario, realizar un deposito y validar Deposit Successful.
- Consulta de saldo: verificar que el saldo se actualiza tras un deposito.
- Retiro: ingresar, retirar un monto y validar Transaction successful, y que el saldo se reduce.

Estructura del proyecto
- xyz-bank-selenium-main: código fuente y tests
- pom.xml: dependencias Maven para Selenium y JUnit
- .gitignore actualizado para evitar binarios y drivers

Cómo ejecutar
- Requisitos: Java 11+, Maven
- En la carpeta xyz-bank-selenium-main, ejecutar: mvn test

Notas
- Se recomienda usar WebDriverManager para gestionar los drivers de navegador de forma automática (evita incluir binarios en el repo).
- Los selectores pueden requerir ajustes si la UI cambia; se recomienda hacerlos más robustos (IDs, nombres, etc.).
