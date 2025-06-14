FROM mcr.microsoft.com/mssql/server:2022-latest

ENV SA_PASSWORD=Qwerty.1
ENV ACCEPT_EULA=Y

USER root

# Create a config directory
RUN mkdir -p /usr/config
WORKDIR /usr/config

# Bundle config source
COPY ./config /usr/config

# Grant permissions for our scripts to be executable
RUN chmod +x /usr/config/entrypoint.sh
RUN chmod +x /usr/config/configure-db.sh

ENTRYPOINT ["./entrypoint.sh"]
