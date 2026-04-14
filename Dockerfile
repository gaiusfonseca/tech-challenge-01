
# Cria um Build Stage
FROM eclipse-temurin:21-jre-noble AS builder

# define a pasta builder como workdir
WORKDIR /builder

# atribui o jar o valor do parâmetro JAR_FILE
ARG JAR_FILE=target/*.jar

# Copia o jar como application.jar no container
COPY ${JAR_FILE} application.jar

# extrai as camadas do layered jar para a pasta extracted
# o layered jar precisa ser buildado com mvn package spring-boot:repackage
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# TODO cria um JRE personalizado com jlink

# Cria o Runtime Stage
FROM eclipse-temurin:21-jre-noble AS runtime

# cria variáveis para facilitar a manutenção do usuário não root
ARG USERNAME=spring
ARG GROUPNAME=${USERNAME}
ARG UID=1001
ARG GID=${UID}

# cria grupo de usuário sem privilérgios de root
RUN groupadd --gid ${GID} ${GROUPNAME} \
    # cria usuário sem privilégios e adiciona ao grupo
    && useradd --uid ${UID} --gid ${GID} --shell /bin/bash ${USERNAME}

# define a pasta application como workdir
WORKDIR /application
RUN chown ${GROUPNAME}:${USERNAME} /application

# copia cada uma das camadas, da que muda com menos frequência para a que muda com mais frequência
# de forma a tirar vantagem do caching de etapas do docker
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./

# expõe a porta 8080 na rede do container
EXPOSE 8080

# alterna para o usuário criado
USER spring

# Start the application jar - this is not the uber jar used by the builder
# This jar only contains application code and references to the extracted jar files
# This layout is efficient to start up and AOT cache (and CDS) friendly
ENTRYPOINT ["java", "-jar", "application.jar"]

# referências:
# https://docs.spring.io/spring-boot/reference/packaging/container-images/dockerfiles.html
# https://docs.spring.io/spring-boot/maven-plugin/using.html
# https://hub.docker.com/_/eclipse-temurin