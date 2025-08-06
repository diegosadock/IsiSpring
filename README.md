# IsiSpring 🌐

**IsiSpring** é um mini *framework web* desenvolvido com Java puro, focado em compreender e implementar a infraestrutura base de um servidor HTTP. Criado como parte da experiência educacional na **IsiFLIX**, o objetivo é aplicar, na prática, os fundamentos de desenvolvimento web e conceitos teóricos como **Teoria dos Grafos**, **Recursividade**, **Java Reflection** e **Injeção de Dependência**.

> 🔍 Aqui, você não usa o Spring Framework — você *cria* o seu próprio Spring!

---

## 🎯 Objetivos do Projeto

- Criar um servidor HTTP do zero
- Implementar roteamento e tratamento de requisições (GET/POST)
- Usar Java Reflection para invocar controladores dinamicamente
- Trabalhar com Injeção de Dependência usando interfaces e implementações
- Reforçar o uso de conceitos de estrutura de dados e algoritmos no backend

---

## 🛠️ Tecnologias & Conceitos Utilizados

- Java (puro)
- Sockets e Manipulação de Requisições HTTP
- Java Reflection
- Injeção de dependência
- Estrutura MVC
- Teoria dos Grafos (para roteamento)
- Recursividade
- Interfaces e Implementações
- Manipulação de arquivos `.html`

---

## 🚀 Como executar

### Pré-requisitos

- Java 17 ou superior instalado

### Rodando a aplicação

```bash
git clone https://github.com/diegosadock/IsiSpring.git
cd IsiSpring
javac -d out $(find ./src -name "*.java")
java -cp out isi.server.WebServer
```


Depois acesse no navegador:
```
http://localhost:8080
```
📡 Exemplo de uso

```java
@IsiController
public class HelloController {
	
	@IsiInjected IService service;
	
	@IsiGetMethod("/hello")
	public String sayHelloWorld() {
		return "Hello World";
	}

	@IsiGetMethod("/teste")
	public String sayTeste() {
		return "Teste funcionando";
	}
	
	@IsiGetMethod("/produto")
	public Produto exibirProduto() {
		Produto p = new Produto();
		p.setId(123456);
		p.setNome("Computador");
		p.setPreco(2500.00);
		p.setLinkFoto("computador.jpg");
		return p;
	}
	
	@IsiPostMethod("/produto")
	public Produto cadastrarProduto(@IsiBody Produto novo) {
		System.out.println(novo);
		return novo;
		
	}
	
	@IsiGetMethod("/injected")
	public String sayCustomMessage() {
		return service.sayCustomMessage("Hello World");
	}
	
}
```

---

📦 Utilização em outros projetos
Assim como no Spring Framework, para utilizar o IsiSpring em uma aplicação real:

1. Crie um projeto Java separado.

2. Adicione o projeto IsiSpring como dependência no classpath (por exemplo, via .jar ou configuração no IDE).

3. Utilize as anotações @IsiController, @IsiGetMethod, @IsiPostMethod, @IsiInjected, entre outras, conforme o padrão do framework.

4. Rode a aplicação com o isi.server.WebServer.

---

🧠 Aprendizados
Esse projeto é ideal para quem deseja:

- Entender como funcionam os bastidores de frameworks como Spring e Jakarta EE

- Aprender sobre roteamento manual, servidor HTTP e Reflection

- Criar sua própria lógica de injeção de dependência

- Consolidar os estudos em algoritmos, estrutura de dados e Java avançado

---

📖 Créditos:
Este projeto faz parte da plataforma de ensino IsiFLIX, com conteúdo idealizado pelo professor Isidro.

---

🤝 Contribuindo
Este projeto é educacional, mas se quiser contribuir com melhorias, fique à vontade:

Fork o projeto

Crie uma branch com sua feature: feature/nome

Envie um Pull Request com suas mudanças

---



