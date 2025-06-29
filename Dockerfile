FROM openjdk:17 AS build

# Maven'ı ve proje dosyalarını kopyala
COPY pom.xml mvnw ./
# Windows satır sonlarını kaldır
RUN sed -i 's/\r//' mvnw && chmod +x mvnw

COPY .mvn .mvn

# Bağımlılıkları çöz
RUN ./mvnw dependency:resolve

# Kaynak kodu kopyala ve paket oluştur (testleri atla)
COPY src src
RUN ./mvnw package -DskipTests

FROM openjdk:17
WORKDIR /arzuamber
COPY --from=build target/*.jar arzuamber.jar
ENTRYPOINT ["java", "-jar", "arzuamber.jar"]