# JWT e matriz de permissoes

O backend usa JWT stateless. Apenas login, cadastro de aluno, cadastro publico de
professor e Swagger sao publicos. Todas as demais rotas exigem `Bearer <token>`.

## Papeis

| Perfil | Authorities |
| --- | --- |
| Aluno | `ROLE_ALUNO` |
| Professor | `ROLE_PROFESSOR` |
| Professor coordenador | `ROLE_PROFESSOR`, `ROLE_COORDENADOR` |

## Matriz resumida

| Recurso | Aluno | Professor | Coordenador |
| --- | --- | --- | --- |
| Perfil proprio | consultar/editar | consultar/editar | consultar/editar como professor |
| TCC | somente o proprio | somente orientandos | todos |
| Submissoes | criar/listar as proprias | listar orientandos e emitir parecer | todas |
| Arquivos | enviar/baixar os proprios | arquivos de orientandos | todos |
| Bancas | consultar as proprias | consultar participacoes e registrar nota | CRUD completo |
| Feedbacks | consultar os proprios | criar para orientandos | CRUD completo |
| Avaliacoes | consultar as proprias | criar quando avaliador | CRUD completo |
| Usuarios/auditoria/areas/avaliadores | sem acesso administrativo | sem acesso administrativo | administrar |
| Coordenadores | sem acesso | sem acesso | listar, cadastrar e promover professor |

As regras gerais estao em `SecurityConfig`. Regras por operacao usam
`@PreAuthorize` nos controllers. Regras de propriedade ficam nos services, pois
dependem dos relacionamentos persistidos (aluno do TCC, orientador, coorientador
e participacao na banca).

## Variaveis obrigatorias/recomendadas

- `JWT_SECRET`: segredo com pelo menos 32 bytes; obrigatorio em producao.
- `JWT_EXPIRATION_MS`: validade do token; padrao de 3.600.000 ms.
- `CORS_ALLOWED_ORIGINS`: origens do frontend separadas por virgula.
- `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`: PostgreSQL.

O logout e client-side: o token de acesso e removido do navegador. Nao existe
sessao HTTP no servidor nem refresh token nesta arquitetura.
