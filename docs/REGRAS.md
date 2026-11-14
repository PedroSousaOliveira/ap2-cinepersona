# Regras do projeto

Sistema operacional do usuário: Windows

## Código

- Nomes de domínio em português sem acentos (Filme, Avaliacao, Lente); sufixos técnicos em inglês (Repository, Adapter, ViewModel). Um padrão só.
- Funções curtas (até ~25 linhas), uma responsabilidade. Proibido classes Helper/Manager/Utils genéricas.
- Sem código morto: imports não usados, métodos sem chamada, TODO, print/Log de debug, código comentado, endpoint ou tela fora do ESCOPO.
- Comentário só para o "porquê" não óbvio. Sem docstrings genéricas e sem comentários que repetem o código.
- Sem emojis em código, logs, commits ou README. Sem linguagem de marketing.
- Nunca engolir exceção (except: pass, catch vazio). API com códigos HTTP corretos (200, 201, 204, 404, 409, 422). O app mostra carregamento e mensagem de erro.
- Android: textos em strings.xml, cores em colors.xml; nada fixo em layout ou Java.
- Sem segredos no git: .env fora do repositório, .env.example dentro.

## Arquitetura

- Backend: routers -> services -> repositories -> models. A regra de pontuação é função pura em services/recomendacao.py, sem acesso a banco.
- Android: ui (Activity + ViewModel + Adapter) -> data/repository -> data/remote. Models em domain/model usados por todas as camadas (decisão consciente, sem mappers). Activity só cuida de View e navegação; Retrofit nunca na Activity. Sem UseCase que só repassa chamada.
- Dependências Android permitidas: Retrofit + Gson, Lifecycle (ViewModel/LiveData), RecyclerView, CardView, Material, JUnit. Proibido: Compose, Hilt/Dagger, Room, Glide/Picasso, Navigation Component, multi-módulo. Se não tiver certeza da versão de uma dependência, pare e me peça.

## Processo

- Uma fase por vez. Ao final: arquivos criados/alterados e como validar. Não avance sem eu confirmar.
- O que não está em docs/ESCOPO.md não se implementa; pergunte.
- Você não faz commits. Sugira a mensagem. Conventional Commits: feat|fix|refactor|test|docs|chore(escopo): descrição no imperativo, até 72 caracteres.
- Testes: pytest no backend (pontuação e avaliações); JUnit nas validações do Android.
- Antes de encerrar cada fase, auto-revise: imports não usados, código morto, nomes, duplicação.
