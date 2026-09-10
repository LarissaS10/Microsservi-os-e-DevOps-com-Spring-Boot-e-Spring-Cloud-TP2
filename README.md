# TP2 — Do Docker ao Kubernetes: Sistema de Filmes

## Estrutura do projeto

tp2-microservicos/
├── filmeservice/ (microsserviço de filmes)
│ ├── src/
│ ├── pom.xml
│ └── Dockerfile
├── avaliacaoservice/ (microsserviço de avaliações)
│ ├── src/
│ ├── pom.xml
│ └── Dockerfile
├── docker-compose.yml
├── deployment.yaml (Deployment do filme-service)
├── service.yaml (Service do filme-service)
├── avaliacao-deployment.yaml (Deployment do avaliacao-service)
└── avaliacao-service.yaml (Service do avaliacao-service)


## Serviços

| Serviço | Porta | Responsabilidade |
|---|---|---|
| filme-service | 8081 | Cadastro e consulta de filmes |
| avaliacao-service | 8082 | Avaliações de filmes (consulta o filme-service antes de salvar) |

## Tecnologias utilizadas
- Java 21
- Spring Boot 4.1.1
- Dados em memória (sem banco de dados nesta etapa)
- Docker / Docker Compose
- Kubernetes (via Docker Desktop)

---

## Opção 1: Rodar com Docker Compose

### Pré-requisitos
- JDK 21
- Docker Desktop

### Passos

1. Gerar o `.jar` de cada microsserviço (o Compose usa esse `.jar` pronto, não compila o Maven sozinho):
```bash
cd filmeservice
mvn package
cd ../avaliacaoservice
mvn package
cd ..
```

2. Subir tudo com um único comando, na pasta raiz do projeto:
```bash
docker compose up
```
Isso constrói as imagens (usando os Dockerfiles de cada pasta) e sobe os dois containers, já na mesma rede e comunicando entre si.

3. Acessar:

http://localhost:8081/filmes
http://localhost:8082/avaliacoes


---

## Opção 2: Rodar no Kubernetes

### Pré-requisitos
- Docker Desktop com Kubernetes habilitado (Settings → Kubernetes → Enable Kubernetes → método **Kubeadm**)
- `kubectl` instalado (já vem junto com o Docker Desktop)

### Passos

1. Gerar os `.jar` e construir as imagens localmente (o Kubernetes local usa as imagens já existentes no Docker, não baixa de lugar nenhum):
```bash
cd filmeservice
mvn package
docker build -t filme-image .
cd ../avaliacaoservice
mvn package
docker build -t avaliacao-image .
cd ..
```

2. Aplicar os 4 arquivos YAML no cluster, na pasta raiz do projeto:
```bash
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
kubectl apply -f avaliacao-deployment.yaml
kubectl apply -f avaliacao-service.yaml
```

3. Conferir se subiu:
```bash
kubectl get pods
kubectl get services
```

4. Acessar (portas definidas nos Services, tipo NodePort):

http://localhost:30081/filmes
http://localhost:30082/avaliacoes


### Aumentar o número de réplicas do filme-service
Edite `deployment.yaml`, altere o valor de `replicas` (ex: de 1 para 3), e reaplique:
```bash
kubectl apply -f deployment.yaml
```

---

## Exemplos de requisições

### Criar um filme

POST http://localhost:8081/filmes (ou :30081 no Kubernetes)
Content-Type: application/json

{
"titulo": "Matrix",
"genero": "Ficção Científica",
"anoLancamento": 1999,
"sinopse": "Um hacker descobre que a realidade é uma simulação."
}


### Listar filmes

GET http://localhost:8081/filmes


### Criar uma avaliação (valida o filme automaticamente)

POST http://localhost:8082/avaliacoes (ou :30082 no Kubernetes)
Content-Type: application/json

{
"filmeId": 1,
"nota": 5,
"comentario": "Excelente!"
}

Se o `filmeId` não existir, retorna `400 Bad Request`.
