# AdSentinel - Automação Operacional de Marketing

Sistema de automação operacional para Analistas de Marketing, focado em gestão segura e auditável de contas Gmail e Google Ads.

## 🚀 Funcionalidades

- **Dashboard Operacional**: Visão geral de contas ativas, em aquecimento e riscos.
- **Gestão de Gmail**: Controle de aquecimento, status e histórico.
- **Gestão Google Ads**: Checklist obrigatório antes da ativação, controle de fases.
- **Auditoria Completa**: Logs de todas as ações dos operadores.
- **Interface Dupla**:
    - **Desktop (JavaFX)**: Para operação diária robusta.
    - **Web (Thymeleaf)**: Para acesso rápido e visualização de relatórios.

## 🛠 Tecnologias

- **Java 17+**
- **Spring Boot 3.2**
- **JavaFX 21** (Interface Desktop)
- **Thymeleaf + Bootstrap 5** (Interface Web)
- **H2 Database** (Persistência local baseada em arquivo)
- **JPA / Hibernate**

## 📦 Instalação e Execução

### Pré-requisitos
- JDK 17 ou superior instalado (Detectado: JDK 21).
- **Maven**: Necessário para gerenciar dependências e rodar o projeto.

### Instalação do Maven (Windows)
Caso o comando `mvn` não seja reconhecido:

1. **Download**: Baixe o arquivo "Binary zip archive" no site oficial: [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
2. **Extração**: Extraia a pasta (ex: `apache-maven-3.9.6`) para `C:\Program Files\Maven`.
3. **Configuração**:
   - Abra o menu Iniciar e digite "Variáveis de Ambiente".
   - Em "Variáveis do Sistema", edite a variável `Path`.
   - Clique em "Novo" e adicione o caminho da pasta `bin` (ex: `C:\Program Files\Maven\apache-maven-3.9.6\bin`).
4. **Verificação**: Abra um novo terminal e digite:
   ```bash
   mvn -version
   ```

### Passos para Execução
1. Clone o repositório.
2. Navegue até a pasta do projeto:
   ```bash
   cd AdSentinel
   ```
3. Execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```

A aplicação iniciará simultaneamente:
- **Janela Desktop**: Interface principal para o operador.
- **Servidor Web**: Acessível em `http://localhost:8080`.

## 🔒 Segurança e Compliance
- O sistema impede ativação de contas Ads sem checklist completo.
- Todas as alterações de status são logadas com ID do operador.

## 📂 Estrutura do Projeto
- `domain`: Entidades e regras de negócio (DDD).
- `application`: Serviços e Casos de Uso.
- `infrastructure`: Persistência e configurações.
- `presentation`:
    - `desktop`: Controllers e FXML do JavaFX.
    - `web`: Controllers e Templates HTML.

---
**Desenvolvido por AdSentinel Tech**
