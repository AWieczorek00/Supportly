# Etap 1: Budowanie Angulara
FROM node:20-alpine as builder

WORKDIR /app
COPY supportly-frontend ./

RUN npm install && npm run build -- --output-path=dist/supportly-frontend --base-href=/

# Etap 2: Serwowanie przez nginx
FROM nginx:stable-alpine

# ⬇️ KLUCZOWA POPRAWKA TUTAJ: Kopiujemy zawartość katalogu 'browser'
COPY --from=builder conf/frontend/supportly-frontend/browser/ /usr/share/nginx/html
COPY conf/frontend/supportly-frontend/browser/assets/nginx.conf /etc/nginx/conf.d/default.conf
#COPY conf/frontend/nginx.conf /etc/nginx/conf.d/default.conf


EXPOSE 80