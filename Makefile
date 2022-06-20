start-prod:
	docker-compose -f docker-compose.yml up

start-dev:
	./mvnw clean package -P production
	docker-compose -f docker-compose-dev.yml up

start-db:
	docker-compose -f docker-compose-db.yml up

start-demo:
	docker-compose -f docker-compose-stand.yml up -d

lint:
	./mvnw checkstyle:check

migrations:
	./mvnw liquibase:diff
