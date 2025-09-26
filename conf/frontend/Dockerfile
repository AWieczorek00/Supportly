# =======================
# Etap 1: Budowanie Angulara
# =======================
FROM node:20-alpine AS builder

# Katalog roboczy w kontenerze
WORKDIR /app

# Skopiuj frontend (w tym package.json, angular.json itd.)
COPY supportly-frontend/ ./supportly-frontend

# Przejdź do katalogu aplikacji
WORKDIR /app/supportly-frontend

# Instalacja zależności i build Angulara
RUN npm install && npm run build -- --output-path=dist/supportly-frontend --base-href=/

# =======================
# Etap 2: Serwowanie przez nginx
# =======================
FROM nginx:stable-alpine

# Skopiuj zbudowany Angular (tylko zawartość katalogu browser!)
COPY --from=builder /app/supportly-frontend/dist/supportly-frontend/browser/ /usr/share/nginx/html

# Skopiuj własną konfigurację nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Domyślny port
EXPOSE 80
