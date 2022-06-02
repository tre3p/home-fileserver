## Badges

![Build](https://github.com/tre3p/home-fileserver/actions/workflows/build.yml/badge.svg)
![DockerHub](https://github.com/tre3p/home-fileserver/actions/workflows/dockerhub.yml/badge.svg)
<a href="https://codeclimate.com/github/tre3p/home-fileserver/maintainability"><img src="https://api.codeclimate.com/v1/badges/d1d0ffd23c3814c5a71a/maintainability" /></a>

## Description

Pet project, which main goal is to make home file server, which can retrieve files from user, zip, encrypt, store them, and give link to download it by request. For now it uses default Java zipping and encrypting(AES) utils.

## Diagram

![alt text](https://github.com/tre3p/home-fileserver/blob/main/diagram.png?raw=true)

## Java version

* Java 17

## ToDo:

I appreciate any help, so if you want to make this project a bit better - you can check Issues of this project, and i will be waiting for your PR :P

## Launch

```sh
git clone https://github.com/tre3p/home-fileserver.git file-server
cd file-server
./mvnw clean package -DskipTest -P production
docker-compose up
```

Application will be available at 5050 port. Default username and password:

```sh
USERNAME: serv
PASSWORD: testpass
ENCRYPTION KEY: vevnwecjc43cece[socmqweokcm34c[3
```

You can change all of this stuff in docker-compose file.
