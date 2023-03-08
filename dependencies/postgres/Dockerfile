FROM postgres:14.4

RUN localedef -i en_US -c -f UTF-8 -A /usr/share/locale/locale.alias en_US.UTF-8
ENV LANG en_US.utf8
ENV POSTGRES_DB ocpp
ENV POSTGRES_HOST_AUTH_METHOD trust
COPY create-db.sql /docker-entrypoint-initdb.d/create-db.sql
