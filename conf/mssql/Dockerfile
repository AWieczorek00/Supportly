FROM mcr.microsoft.com/mssql/server:2022-latest

ENV SA_PASSWORD=Qwerty.1
ENV ACCEPT_EULA=Y

USER root

# Install mssql-tools and dependencies
RUN apt-get update && \
    apt-get install -y curl gnupg2 apt-transport-https && \
    curl https://packages.microsoft.com/keys/microsoft.asc | apt-key add - && \
    curl https://packages.microsoft.com/config/debian/11/prod.list > /etc/apt/sources.list.d/mssql-release.list && \
    apt-get update && \
    ACCEPT_EULA=Y apt-get install -y mssql-tools18 unixodbc-dev && \
    echo 'export PATH="$PATH:/opt/mssql-tools18/bin"' >> /etc/profile.d/mssql-tools.sh && \
    rm -rf /var/lib/apt/lists/*

# Create a config directory
RUN mkdir -p /usr/config
WORKDIR /usr/config

# Bundle config source
COPY ./config /usr/config

# Grant permissions for our scripts to be executable
RUN chmod +x /usr/config/entrypoint.sh
RUN chmod +x /usr/config/configure-db.sh
RUN chmod +x /usr/config/healthcheck.sh

# Ensure PATH includes sqlcmd
ENV PATH="$PATH:/opt/mssql-tools18/bin"

ENTRYPOINT ["./entrypoint.sh"]