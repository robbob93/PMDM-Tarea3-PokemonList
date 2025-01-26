# Pokémon Manager App


## 🐾 Introducción

La **Pokémon Manager App** es una aplicación que permite a los usuarios gestionar su Pokédex personalizada. Los usuarios pueden consultar una lista de Pokémon, capturarlos, liberarlos y configurar preferencias relacionadas con el idioma o la funcionalidad de la app. El propósito principal de esta aplicación es ofrecer una experiencia divertida y funcional para los fanáticos de Pokémon mientras exploran y administran sus Pokémon capturados.

---
## 🌟 Características principales

- **Autenticación de usuarios**: Los usuarios pueden registrarse e iniciar sesión de forma segura utilizando Firebase Authentication.
- **Pokédex**: Consulta la lista completa de Pokémon desde la API de PokéAPI, con información básica y detalles adicionales.
- **Lista de Pokémon capturados**: Los usuarios pueden capturar Pokémon, almacenarlos en una lista personalizada y liberarlos según deseen. Los datos se sincronizan con Firestore en tiempo real.
- **Ajustes personalizados**: Configuración de idioma (inglés o español), opción para habilitar/deshabilitar funciones como la liberación de Pokémon y acceso a información sobre la aplicación.
- **Interfaz amigable**: Uso de RecyclerView para mostrar listas de Pokémon con un diseño limpio e interactivo.
---

## 🛠️ Tecnologías utilizadas

Esta aplicación ha sido desarrollada con las siguientes tecnologías y librerías:

- **Firebase**
  - **Authentication**: Para gestionar el inicio de sesión y registro de usuarios.
  - **Firestore**: Base de datos en tiempo real para almacenar y recuperar la lista de Pokémon capturados.
- **Retrofit**: Librería para realizar llamadas HTTP y consumir la API de PokéAPI.
- **Gson**: Conversión de datos JSON a objetos Java.
- **RecyclerView**: Para mostrar listas de Pokémon con un diseño dinámico.
- **Picasso**: Librería para cargar imágenes de manera eficiente.
- **PreferenceFragmentCompat**: Para implementar configuraciones personalizables en la aplicación.
- **Java**: Lenguaje principal del desarrollo de la aplicación.

---
## 📖 Instrucciones de uso
1. **Clonar el repositorio**:
   - Haz clic en Code y descarga el proyecto.
2. **Configurar Firebase:**
   - Accede a Firebase Console y crea un nuevo proyecto.
   - Descarga el archivo google-services.json y colócalo en la carpeta app del proyecto.
3. **Configurar dependencias:**
   - Asegúrate de tener Android Studio instalado en tu sistema.
   - Abre el proyecto en Android Studio.
   - Sincroniza las dependencias en el archivo build.gradle.
4. **Ejecutar la aplicación:**
   - Conecta un dispositivo Android o utiliza un emulador.
   - Haz clic en el botón "Run" en Android Studio para compilar y ejecutar la aplicación.
5. **Usar la aplicación:**
   - Regístrate o inicia sesión para acceder a la Pokédex y gestionar tu lista de Pokémon capturados.
   - Configura el idioma o las preferencias en el menú de ajustes.

---
## 📋 Conclusiones del desarrollador
El desarrollo de esta aplicación ha sido una experiencia enriquecedora y desafiante. Algunos aspectos destacados del proceso son:

- Desafíos técnicos: La integración de Retrofit para buscar los pokemon utilizando por primera vez una API y después completar sus datos para, posteriormente, incorporarlos en Firestore permitiendo la sincronización de datos en tiempo real entre la base de datos y las listas locales, fueron retos clave.
- Aprendizajes obtenidos: Mejoré mis habilidades en la gestión de datos compartidos, además de profundizar en el uso de librerías como Picasso y asentar los conocimientos sobre RecyclerView para optimizar la experiencia del usuario.
- Reflexión final: Este proyecto no solo me permitió consolidar conocimientos en Android, sino también explorar mejores prácticas en el desarrollo móvil, como la estructuración del código y la persistencia de los datos.
---
## 📈 Nuevas vías de desarrollo
- La posibilidad de que cada usuario tenga una lista de pokemon capturados independiente.
- Implementación de modo oscuro.
- Ampliación del número de pokemon a listar y poder capturar.

## Capturas de pantalla

- Inicio de la app   
![inicio](https://github.com/user-attachments/assets/5aa3350d-6dd4-475a-b048-a883d5ace808)

- Lista de pokemon   
![capt_lista](https://github.com/user-attachments/assets/0fc46dab-0d08-4722-b5ed-906c4760e914)

- Lista de pokemon capturados   
![captura capt](https://github.com/user-attachments/assets/59e86db5-197d-435c-8598-4793749a8303)

- Detalles del pokemon capturado   
![capt_detalles](https://github.com/user-attachments/assets/31c5ec17-1030-499c-9fc2-4f5856a03633)

- Pantalla ajustes   
![capt_ajustes](https://github.com/user-attachments/assets/ba579094-c5b6-4202-ac5e-153afb75b787)


