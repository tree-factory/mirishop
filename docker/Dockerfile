FROM openjdk:17
ARG JAR_FILE=build/libs/app.jar
COPY ${JAR_FILE} app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java","-jar","/app.jar"]

# 실제로는 각 레포지토리에 docker 파일을 두고
# build 된 jar 파일을 실행시켜야함.
