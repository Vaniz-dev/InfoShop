# InfoShop

Aplicação web de loja virtual (e-commerce simples) desenvolvida em **Java + Spring Boot**, com cadastro/login de usuários, controle de acesso por perfil (admin/usuário) e páginas renderizadas com **Thymeleaf**.

## 🧱 Tecnologias utilizadas

- **Java 25**
- **Spring Boot 4.1.2**
- Spring Security (autenticação por formulário + autorização por perfil)
- Spring Data JPA (Hibernate)
- Thymeleaf (templates HTML)
- Lombok
- Bean Validation (`spring-boot-starter-validation`)
- MySQL (banco principal, configurado em `application.properties`)
- H2 (dependência presente para testes em memória)
- Maven (com Maven Wrapper — `mvnw` / `mvnw.cmd`)

## 📁 Estrutura do projeto

```
InfoShop/
├── InfoShop/                         # Projeto Spring Boot
│   ├── src/main/java/com/Vaniz/InfoShop/
│   │   ├── InfoShopApplication.java  # Classe principal
│   │   ├── config/
│   │   │   ├── DadosIniciais.java    # Cria um usuário admin padrão ao subir a app
│   │   │   └── SecurityConfig.java   # Regras de autenticação/autorização
│   │   ├── controller/
│   │   │   ├── AdminController.java  # Área administrativa (/admin/**)
│   │   │   ├── AuthController.java   # Login e registro
│   │   │   └── HomeController.java   # Página inicial (/home)
│   │   ├── dto/
│   │   │   └── RegistroForm.java     # Validações do formulário de cadastro
│   │   ├── model/
│   │   │   ├── Perfil.java           # Enum: ADMIN, USER
│   │   │   ├── PerfilConverter.java
│   │   │   └── Usuario.java
│   │   ├── repository/
│   │   │   └── UsuarioRepository.java
│   │   └── service/
│   │       ├── CadastroService.java       # Regras de cadastro (checa e-mail duplicado)
│   │       └── UsuarioDetailsService.java  # Integração com Spring Security
│   ├── src/main/resources/
│   │   ├── templates/
│   │   │   ├── admin/panel.html
│   │   │   ├── home.html
│   │   │   ├── login.html
│   │   │   └── registro.html
│   │   ├── static/index.html
│   │   └── application.properties
│   ├── pom.xml
│   ├── mvnw / mvnw.cmd
│   └── HELP.md
└── criar_e_popular_o_banco.sql       # Script de criação e carga inicial do banco
```

## ✅ Pré-requisitos

- **JDK 25**
- **MySQL** rodando localmente (ou ajuste a conexão para outro host/instância)
- Não precisa instalar o Maven separadamente — o projeto já vem com o Maven Wrapper (`mvnw`)

## ⚙️ Configuração do banco de dados

O banco usado é o **MySQL**, com o schema `jr_loja`.

1. Rode o script `criar_e_popular_o_banco.sql` para criar o banco, as tabelas (`usuario`, `produtos`, `venda`, `venda_produto`) e inserir alguns dados de exemplo:

```bash
mysql -u root -p < criar_e_popular_o_banco.sql
```

2. Confira a conexão em `InfoShop/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/jr_loja
spring.datasource.username=root
spring.datasource.password=root
```

Ajuste usuário/senha/host conforme o seu ambiente.

> ⚠️ **Atenção:** o script do banco grava as senhas de teste (`admin`, `jose123`, `marcos123`) em texto puro. A aplicação, porém, espera senhas com hash **BCrypt** (usadas pelo Spring Security). Isso significa que os usuários do script SQL **não conseguem logar** direto — veja a seção "Usuário de acesso" abaixo para o login que realmente funciona.

## ▶️ Como executar o projeto

Todos os comandos abaixo devem ser rodados dentro da pasta `InfoShop/InfoShop` (onde está o `pom.xml`).

### Linux/macOS

```bash
cd InfoShop/InfoShop
./mvnw spring-boot:run
```

### Windows

```cmd
cd InfoShop\InfoShop
mvnw.cmd spring-boot:run
```

### Ou gerando o `.jar`

```bash
./mvnw clean package
java -jar target/InfoShop-0.0.1-SNAPSHOT.jar
```

A aplicação sobe por padrão em:

```
http://localhost:8080
```

## 🔑 Usuário de acesso

Ao iniciar, `DadosIniciais.java` cria automaticamente (caso ainda não exista) um usuário administrador com senha já criptografada corretamente:

- **E-mail:** `admin@infoshop.com`
- **Senha:** `admin123`

Use esse login para entrar como **ADMIN**. Para testar como usuário comum, cadastre uma nova conta pela tela `/registro`.

## 🧭 Rotas principais

| Rota | Acesso | Descrição |
|---|---|---|
| `/` | público | Redireciona para `/home` |
| `/login` | público | Tela de login |
| `/registro` | público | Cadastro de novo usuário |
| `/home` | autenticado | Página inicial (mostra nome do usuário e se é admin) |
| `/admin/painel` | perfil ADMIN | Painel administrativo |

> **Nota:** o `AdminController` retorna a view `admin/painel`, mas o arquivo em `templates/admin/` se chama `panel.html`. Vale confirmar/corrigir esse nome (view ou arquivo) para o painel funcionar sem erro de template não encontrado.

## 📌 Observações

- Não há `.gitignore` no projeto — a pasta `target/` (arquivos compilados) está sendo versionada. Recomenda-se criar um `.gitignore` com `target/` para evitar conflitos de merge e reduzir o tamanho do repositório.
- O `pom.xml` inclui o banco H2 em memória como dependência, mas a aplicação está configurada para usar MySQL (`application.properties`). O H2 fica disponível caso queira trocar o ambiente de testes.
