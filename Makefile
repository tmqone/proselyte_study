DOCKER_COMPOSE = docker compose
NEXUS_URL = http://localhost:9081
NEXUS_URL_DOCKER = http://nexus:8081

.PHONY: all up build start

all: up build start

ifeq ($(OS),Windows_NT)
WAIT_NEXUS = powershell -Command "while ($$true) { \
		try { \
			Invoke-WebRequest -UseBasicParsing -Uri $(NEXUS_URL)/service/rest/v1/status -ErrorAction Stop; \
			break \
		} \
		catch { \
			Write-Host 'Nexus not ready, sleeping...'; \
			Start-Sleep -Seconds 5 \
		} \
	}"
else
WAIT_NEXUS = until curl -sf $(NEXUS_URL)/service/rest/v1/status; do \
	echo 'Nexus not ready, sleeping...'; sleep 5; \
done
endif

up:
	$(DOCKER_COMPOSE) up -d nexus
	@echo "Waiting for Nexus to be healthy..."
	@$(WAIT_NEXUS)
	@echo "Nexus is healthy!"

build:
	@echo "Building person-service image..."
	NEXUS_URL_DOCKER=$(NEXUS_URL_DOCKER) $(DOCKER_COMPOSE) build person-service
	@echo "Building transaction-service image..."
    NEXUS_URL_DOCKER=$(NEXUS_URL_DOCKER) $(DOCKER_COMPOSE) build transaction-service
	@echo "Building individuals-api image..."
	NEXUS_URL_DOCKER=$(NEXUS_URL_DOCKER) $(DOCKER_COMPOSE) build individuals-api

start:
	@echo "Starting all services..."
	$(DOCKER_COMPOSE) up -d