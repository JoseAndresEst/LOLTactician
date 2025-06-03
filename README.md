# LOLTactician

> **Simulador de Draft para League of Legends** - Practica y mejora tus estrategias de selección de campeones

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
[![Firebase](https://img.shields.io/badge/Firebase-039BE5?style=for-the-badge&logo=Firebase&logoColor=white)](https://firebase.google.com/)
[![Riot Games API](https://img.shields.io/badge/Riot_Games-D32936?style=for-the-badge&logo=riot-games&logoColor=white)](https://developer.riotgames.com/)

## Acerca del Proyecto

LOLTactician es una aplicación Android diseñada para jugadores y entrenadores de League of Legends que desean practicar y perfeccionar sus estrategias de draft. La aplicación simula fielmente el proceso de selección y baneo de campeones, proporcionando una herramienta educativa y práctica para mejorar la toma de decisiones estratégicas.

### Características Principales

- **Simulación Completa de Draft**: Reproduce el sistema de picks y bans del juego oficial
- **Gestión de Usuarios**: Sistema seguro de registro e inicio de sesión con Firebase
- **Información Detallada**: Consulta estadísticas, habilidades y lore de todos los campeones
- **Guardado de Drafts**: Almacena tus simulaciones en la nube
- **Exportación de Imágenes**: Comparte tus drafts como imágenes
- **Datos Actualizados**: Integración con la API oficial de Riot Games

## Tecnologías Utilizadas

### Desarrollo
- **Java** - Lenguaje principal de desarrollo
- **XML** - Diseño de interfaces
- **Android Studio** - Entorno de desarrollo

### Backend y Servicios
- **Firebase Authentication** - Gestión segura de usuarios
- **Firebase Firestore** - Base de datos en la nube
- **Riot Games API (Data Dragon)** - Datos oficiales de campeones

### Librerías
- **Retrofit** - Cliente HTTP para APIs REST
- **Glide** - Carga y caché optimizado de imágenes
- **LiveData** - Arquitectura reactiva para UI

## Arquitectura

La aplicación sigue un **patrón de arquitectura por capas**:

📁 Estructura del Proyecto

├── Capa de Presentación (Activities, Fragments, Adapters)

├── Capa de Lógica de Negocio (Controladores)

├── Capa de Acceso a Datos (Repositorios, Modelos)

└── Capa de Servicios Externos (APIs)
