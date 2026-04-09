# Finaliza.TCC - Sistema Gerenciador de Trabalhos de Conclusão de Curso

O **Finaliza.TCC** é uma plataforma robusta desenvolvida para orquestrar o ciclo de vida dos Trabalhos de Conclusão de Curso (TCC) no IFPB - Campus Monteiro. O sistema visa eliminar a fragmentação de arquivos e a falta de rastreabilidade, centralizando o fluxo entre alunos, orientadores e a coordenação.

---

## 📌 Links do Projeto
* **Gestão do Projeto (Notion):** [Acompanhe o Cronograma e Backlog](https://www.notion.so/326afc40b9af801abc5fed0cd34c8b30?v=326afc40b9af8001a42a000c6b35f58a&source=copy_link)

---

## 🚀 Funcionalidades Principais

### 🎓 Para o Aluno
* **Depósito Digital:** Upload de manuscritos e documentos complementares em PDF.
* **Rastreabilidade:** Acompanhamento em tempo real do status do trabalho (Enviado, Em Análise, Necessita de Correção ou Aprovado).
* **Central de Feedback:** Histórico consolidado de todos os pareceres e versões corrigidas.

### 👨‍🏫 Para o Orientador
* **Gestão de Orientandos:** Painel cronológico para gerenciar submissões de diversos alunos.
* **Avaliação Qualitativa:** Inserção de notas, comentários e upload de versões com correções.
* **Automação Administrativa:** Lançamento de notas finais e geração automática de atas de defesa.

### 🏢 Para a Coordenação
* **Dashboard de Governança:** Relatórios de desempenho, índices de aprovação e estatísticas por área de pesquisa.
* **Acervo Histórico:** Consulta e exportação de dados persistentes de anos de produção acadêmica.

---

## 🛠️ Tecnologias Utilizadas

O projeto utiliza uma stack moderna para garantir performance e segurança:

* **Backend:** Java 21+ com [Spring Boot](https://spring.io/projects/spring-boot)
* **Segurança:** [Spring Security](https://spring.io/projects/spring-security) + [JWT (JSON Web Token)](https://jwt.io/) para autenticação sem estado.
* **Frontend:** [React.js](https://reactjs.org/) para uma interface fluida e intuitiva.
* **Banco de Dados:** [PostgreSQL](https://www.postgresql.org/) (Persistência relacional e integridade de dados).
* **Documentação da API:** Swagger/OpenAPI.

---

## 📊 Arquitetura e Modelagem

O sistema foi modelado para suportar auditoria completa e diversos perfis de acesso. Abaixo, as principais entidades mapeadas:

* **Usuários:** Alunos, Professores e Coordenadores.
* **TCCs:** Gestão do título, área de pesquisa e vínculos de orientação.
* **Submissões & Arquivos:** Versionamento detalhado dos documentos enviados.
* **Bancas & Avaliações:** Registro formal das defesas e notas dos avaliadores.
* **Auditoria:** Registro de todas as ações críticas no sistema (Quem, Quando, O quê).

---

## ⚙️ Como Executar o Projeto (Desenvolvimento)

### Pré-requisitos
* JDK 21 ou superior
* PostgreSQL instalado e rodando

### Passo a Passo
1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/finaliza-tcc.git](https://github.com/seu-usuario/finaliza-tcc.git)
    ```

2.  **Configuração do Backend:**
    * Navegue até a pasta `backend`.
    * Configure as credenciais do banco de dados no arquivo `src/main/resources/application.properties`.
    * Execute: `./mvnw spring-boot:run`

---

## 👥 Desenvolvedores
* **Pedro Vitor Barbosa Florentino**
* **Lara Bezerra de Menezes**

---
**IFPB - Campus Monteiro** *Curso de Tecnologia em Análise e Desenvolvimento de Sistemas* *Disciplina: Desenvolvimento de Aplicações Corporativas*
