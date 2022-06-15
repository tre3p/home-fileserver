## Badges

![Build](https://github.com/tre3p/home-fileserver/actions/workflows/build.yml/badge.svg)
![DockerHub](https://github.com/tre3p/home-fileserver/actions/workflows/dockerhub.yml/badge.svg)
<a href="https://codeclimate.com/github/tre3p/home-fileserver/maintainability"><img src="https://api.codeclimate.com/v1/badges/d1d0ffd23c3814c5a71a/maintainability" /></a>

## Description

Pet project, which main goal is to make home file server, which can retrieve files from user, zip, encrypt, store them, and give link to download it by request. Downloading available in 2 ways: download by-click from UI, and copying link to file from UI.

## Diagram

![alt text](https://github.com/tre3p/home-fileserver/blob/main/diagram.png?raw=true)

## Java version

* Java 17

## ToDo:

I appreciate any help, so if you want to make this project a bit better - you can check Issues of this project.

## Development:

So, if you want to participate in development - make sure that you read this part of readme. Here you can see steps to make good PR:
1. Create branch for your changes.
2. When it's done - create PR from your branch to `test` branch. After that, pipeline with Ansible script will be started, and in 10-15 minutes you can see result of your work at fs-server-test.ru:5050 . Password for this test-stand also default.
3. When you sure it works - you can create PR from test branch to main.

## Launch

### Development

```sh
git clone https://github.com/tre3p/home-fileserver.git file-server
cd file-server
make start-dev
```

Application will be available at 5050 port. Default username and password:

```sh
USERNAME: serv
PASSWORD: testpass
```

### Production

```sh
git clone https://github.com/tre3p/home-fileserver.git file-server
make start-prod
```

This command will pull latest version of application from Dockerhub and launch it in Docker. Username and password are default, but you can change it in docker-compose. Make sure that `ENCRYPTION_KEY` is 128 bit, because in other way, application will not work properly. You can generate one [here](https://www.allkeysgenerator.com/Random/Security-Encryption-Key-Generator.aspx)
