start-prod:
	docker-compose -f docker-compose.yml up

start-dev:
	docker-compose -f docker-compose-dev.yml up

start-db:
	docker-compose -f docker-compose-db.yml up

start-demo:
	docker-compose -f docker-compose-dev.yml up -d

lint:
	./mvnw checkstyle:check