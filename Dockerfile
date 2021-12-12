FROM bellsoft/liberica-openjdk-alpine-musl:11 AS node-build
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp/src
RUN javac *.java