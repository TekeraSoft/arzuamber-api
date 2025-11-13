# 1. stage: build
FROM eclipse-temurin:17-jdk AS build

WORKDIR /build

# Maven wrapper ve pom'u kopyala
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Windows satır sonlarını temizle ve mvnw'ye execute izni ver
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Önce bağımlılıkları indir
RUN ./mvnw dependency:resolve

# Kaynak kodlarını kopyala
COPY src src

# Jar'ı build et (testleri atla)
RUN ./mvnw package -DskipTests

# 2. stage: run
FROM eclipse-temurin:17-jre

WORKDIR /arzuamber

# build stage'den jar'ı al
COPY --from=build /build/target/*.jar arzuamber.jar

ENTRYPOINT ["java", "-jar", "arzuamber.jar"]