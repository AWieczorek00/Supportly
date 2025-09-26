FROM eclipse-temurin:21-jdk-alpine

# Zainstaluj curl
RUN apk add --no-cache curl

# Kopiowanie plików aplikacji
COPY app/supportly-backend-1.jar /app/supportly-backend-1.jar

# Ustawienie katalogu roboczego
WORKDIR /app

# Domyślny profil (możesz nadpisać w docker-compose)
ENV SPRING_PROFILES_ACTIVE=default

# Uruchomienie aplikacji z profilem
CMD ["java", "-jar", "supportly-backend-1.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]

# Konfiguracja zdrowia kontenera
HEALTHCHECK --start-period=30s --interval=5s --timeout=3s --retries=3 \
  CMD curl -k -s --fail http://localhost:8080/ping || exit 1
