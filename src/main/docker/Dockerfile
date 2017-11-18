FROM docker.tradeshift.net/tradeshift-docker-java8-jre:59e70610a860eb07f96e250a6210b8fdbee374b6

MAINTAINER accounts team

ENV SERVICE_NAME     auditserver-gateway
ENV USER             auditservergateway
ENV PORT             65000
ENV LC_ALL           C.UTF-8
ENV JAVA_OPTS        "-server -Xmx1024m -Xms256m -XX:+AlwaysPreTouch -XX:+AggressiveOpts -XX:+UseCompressedOops"

COPY maven /

WORKDIR /opt/tradeshift/$SERVICE_NAME
RUN mkdir -p /etc/tradeshift/$SERVICE_NAME /var/log/tradeshift/$SERVICE_NAME
RUN groupadd --gid 5000 operations && useradd --uid $PORT --gid 5000 --system $USER
RUN chown $PORT:5000 /etc/tradeshift/$SERVICE_NAME /var/log/tradeshift/$SERVICE_NAME

VOLUME ["/etc/tradeshift/auditserver-gateway"]
VOLUME ["/var/log/tradeshift/auditserver-gateway"]

USER $USER:operations
EXPOSE $PORT

CMD ["java","-jar","bin/auditserver-gateway.jar","$JAVA_OPTS"]
