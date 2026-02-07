# Docker 镜像构建
# @author <a href="https://github.com/liyupi">程序员鱼皮</a>
# @from <a href="https://yupi.icu">编程导航知识星球</a>
FROM maven:3.8.1-jdk-8-slim as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM openjdk:8-jre-slim
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
WORKDIR /app
COPY --from=builder /app/target/ai-answer-backend-0.0.1-SNAPSHOT.jar .
CMD ["java","-Duser.timezone=Asia/Shanghai","-jar","ai-answer-backend-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]