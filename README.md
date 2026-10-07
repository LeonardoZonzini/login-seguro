# Login Seguro

Sistema de autenticação e autorização desenvolvido em Java com Spring Boot, Thymeleaf e MongoDB Atlas. Projeto modular, pensado para servir de base para outras aplicações sem precisar alterar a lógica central de login, cadastro e controle de acesso.

## Stack

- Java 17
- Spring Boot 3.3 (Web, Security, Data MongoDB, Validation, Session)
- Thymeleaf
- MongoDB Atlas
- Maven

## Pré-requisitos

- JDK 17 ou superior
- Maven 3.9+
- Uma conta no MongoDB Atlas com um cluster criado e o IP liberado no acesso à rede

## Configuração

O projeto lê a conexão com o banco e os dados do admin inicial por variáveis de ambiente. Copie o arquivo de exemplo:

```
cp .env.example .env
```

E preencha:

| Variável | Descrição |
|---|---|
| `MONGODB_URI` | String de conexão do cluster no MongoDB Atlas |
| `ADMIN_EMAIL` | E-mail do usuário administrador criado automaticamente na primeira execução |
| `ADMIN_SENHA` | Senha desse administrador (será salva com hash) |
| `PORT` | Porta da aplicação (opcional, padrão 8080) |

Antes de rodar, exporte as variáveis no terminal (ou configure no seu IDE):

```
export MONGODB_URI="mongodb+srv://usuario:senha@cluster.mongodb.net/login-seguro"
export ADMIN_EMAIL="admin@exemplo.com"
export ADMIN_SENHA="uma-senha-forte"
```

No Windows (PowerShell):

```
$env:MONGODB_URI="mongodb+srv://usuario:senha@cluster.mongodb.net/login-seguro"
$env:ADMIN_EMAIL="admin@exemplo.com"
$env:ADMIN_SENHA="uma-senha-forte"
```

## Executando localmente

```
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080`. Na primeira execução, se `ADMIN_EMAIL` e `ADMIN_SENHA` estiverem definidos e ainda não existir um admin cadastrado, a conta de administrador é criada automaticamente.

## Perfis de usuário

O sistema tem três perfis:

- **ADMIN**: acesso total, inclusive ao painel de gestão de usuários (trocar perfil, ativar/desativar contas)
- **MODERADOR**: acesso ao painel de moderação
- **USUARIO**: perfil padrão de quem se cadastra pela tela de registro

O controle de rotas por perfil fica em `SecurityConfig`, usando `hasRole`/`hasAnyRole` do Spring Security. Rotas sob `/painel/admin/**` exigem ADMIN, `/painel/moderador/**` exige ADMIN ou MODERADOR, e `/painel/**` exige apenas usuário autenticado.

## Sessões no MongoDB

As sessões HTTP não ficam em memória: são persistidas na coleção `sessoes` do MongoDB Atlas via Spring Session (`spring-session-data-mongodb`), configurado em `SessaoConfig`. Isso permite escalar a aplicação em mais de uma instância sem perder sessão de usuário.

## Estrutura do projeto

```
src/main/java/com/loginseguro/app
├── config          # Spring Security, sessão em Mongo, seed do admin inicial
├── controller       # rotas de autenticação e dos painéis
├── dto               # formulários de entrada
├── exception
├── model             # Usuario, Papel
├── repository
└── service

src/main/resources
├── templates
│   ├── fragments     # cabeçalho, rodapé e layout base reutilizados em todas as páginas
│   ├── auth           # login e registro
│   ├── painel         # um template por perfil
│   └── erro
└── static/css         # estilo.css, com variáveis CSS para customização de tema
```

## Customizando o tema

Toda a aparência fica isolada em `static/css/estilo.css`, usando variáveis CSS (`--cor-primaria`, `--cor-fundo`, etc.) no `:root`. Para aplicar um novo tema visual, basta trocar os valores dessas variáveis ou substituir o arquivo inteiro — nenhuma página HTML precisa ser alterada. A estrutura de página também é montada por fragmentos Thymeleaf (`fragments/layout.html`, `cabecalho.html`, `rodape.html`), então trocar o layout visual não exige tocar na lógica dos controllers.

## Fluxo de branches

O repositório segue Gitflow: `main` para releases estáveis, `develop` como branch de integração, e branches `feature/<nome>` para novas funcionalidades a partir de `develop`.
