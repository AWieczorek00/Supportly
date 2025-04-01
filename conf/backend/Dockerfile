FROM eclipse-temurin:21-jdk-alpine

# Kopiowanie plików aplikacji
COPY app/supportly-backend-1.jar /app/supportly-backend-1.jar


# Ustawienie katalogu roboczego
WORKDIR /app

# Uruchomienie aplikacji
CMD ["java", "-jar", "supportly-backend-1.jar"]

# Konfiguracja zdrowia kontenera
HEALTHCHECK --start-period=30s --interval=5s --timeout=3s --retries=3 CMD curl -k -s --fail http://localhost:8080/ping || exit 1
