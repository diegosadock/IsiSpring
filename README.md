# IsiSpring

Mini framework web educacional em Java para explorar, por dentro, conceitos usados por frameworks como o Spring. O projeto implementa roteamento, execução de controllers por reflection e injeção de dependência, usando Tomcat embarcado como servidor HTTP.

## O que o projeto demonstra

- Controllers com anotações para rotas GET e POST.
- Resolução de handlers com Java Reflection.
- Injeção de dependências baseada em interfaces.
- Leitura do corpo da requisição e serialização JSON com Gson.
- Organização de uma aplicação web em camadas MVC.

## Stack

- Java 17+
- Maven
- Tomcat embarcado
- Gson

## Como executar

Pré-requisitos: JDK 17 ou superior e Maven.

```bash
git clone https://github.com/diegosadock/IsiSpring.git
cd IsiSpring
mvn clean compile
```

Abra o projeto como aplicação Maven na IDE e execute a classe de entrada `br.com.sadock.isispring.IsiSpringTestApplication` com as dependências do Maven no classpath. O servidor embarcado inicia na porta `8081`.

## Rotas de exemplo

A aplicação de demonstração inclui endpoints como:

- `GET /hello`
- `GET /teste`
- `GET /produto`
- `POST /produto`
- `GET /injected`

O endpoint de produto demonstra a conversão JSON de objetos; `/injected` mostra a injeção de uma dependência no controller.

## Contexto

O IsiSpring nasceu como projeto educacional ligado à plataforma IsiFLIX. A ideia é estudar mecanismos fundamentais de frameworks web por meio de uma implementação pequena e explorável.
