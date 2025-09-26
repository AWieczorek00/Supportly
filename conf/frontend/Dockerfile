FROM nginx:stable-alpine

# Skopiuj gotowy build (tylko katalog browser)
COPY supportly-frontend/browser/ /usr/share/nginx/html

# Skopiuj konfigurację nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80