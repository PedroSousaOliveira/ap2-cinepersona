# CinePersona

App Android (Java + XML) que recomenda filmes por "lente cinematográfica". O usuário responde um questionário curto, recebe a lente que mais combina com a sessão do dia, uma lista de filmes ordenada por afinidade e um diário pessoal de avaliações. A classificação é uma pontuação ponderada, não uma análise psicológica.

## 1. Questionário "Sua Sessão de Hoje"

Em vez de perguntar "qual gênero você gosta?", o app coleta o momento, o clima da sessão e as prioridades do usuário em 4 passos rápidos e o classifica em uma de 4 Lentes.

### As 4 Lentes

**Roteiro de Virada** (superação, propósito e recomeço)
- Para quem: está iniciando um novo ciclo, enfrentando metas difíceis ou precisando de energia e perspectiva.
- Exemplos: À Procura da Felicidade, A Vida Secreta de Walter Mitty, Whiplash, Rocky: Um Lutador.

**Plano-Sequência Afetivo** (memória, laços e emoção)
- Para quem: está mais reflexivo, nostálgico, valorizando relações humanas, família, amizade, ou precisando de acolhimento.
- Exemplos: Questão de Tempo, Divertida Mente, Brilho Eterno de uma Mente sem Lembranças, Cinema Paradiso.

**Corte Seco & Adrenalina** (tensão, mistério e estratégia)
- Para quem: está com a mente inquieta, analítica, lidando com pressão e buscando histórias intensas com reviravoltas.
- Exemplos: A Origem, Parasita, Ilha do Medo, O Lobo de Wall Street.

**Fora de Quadro** (imaginário, crítica e descoberta)
- Para quem: quer fugir da rotina, questionar padrões ou mergulhar em universos criativos que mudam a forma de ver o mundo.
- Exemplos: Interestelar, O Show de Truman, A Viagem de Chihiro, Tudo em Todo o Lugar ao Mesmo Tempo.

O catálogo completo (32 filmes, 8 por lente) está em docs/ESCOPO.md.

### Como o cálculo funciona (no backend)

- Pergunta 1, Momento Atual (RadioGroup/RadioButton): +40 pontos na lente escolhida.
- Pergunta 2, Clima da Sessão (Spinner: Inspirador, Emocionante, Tenso/Intrigante, Criativo/Surpreendente): +30 pontos na lente correspondente.
- Pergunta 3, O que mais importa hoje (CheckBox: Superação pessoal, Conexões reais, Inteligência e estratégia, Liberdade e imaginação): +15 pontos em cada lente marcada.
- Modo Lado B (Switch): "Priorizar filmes fora do circuito óbvio (Cult/Autoral)". Desligado, os sucessos mais acessíveis sobem na lista. Ligado, os filmes autorais sobem.
- Resultado: a lente com mais pontos vence (empate: a lente do momento). O app mostra a afinidade com a lente (reconhecimento_percentual) e um match_percentual entre 75% e 99% para cada filme.
- A fórmula exata está em docs/ESCOPO.md.

## 2. As 5 Telas (Java + XML)

Visual inspirado no Letterboxd, tema escuro único: fundo #14181C, cards #2C3440, destaque verde #00E054 e laranja #FF8000. Usa 11 tipos de View: TextView, EditText, Button, ImageView, RadioButton, Spinner, CheckBox, Switch, CardView, RecyclerView e ProgressBar.

| Tela (Activity) | Papel no fluxo | Views | Ações e Intents |
|---|---|---|---|
| 1. LoginActivity | Identificação rápida (nome + e-mail) | ImageView (logo), TextView, EditText x2, Button, ProgressBar | POST /usuarios. Sucesso abre a Tela 2 por Intent explícita com usuario_id e nome. Erro em Snackbar. |
| 2. QuestionarioActivity | Formulário da sessão | TextView, RadioGroup/RadioButton, Spinner, CheckBox, Switch, Button | POST /recomendar. Sucesso abre a Tela 3 por Intent explícita com o resultado. |
| 3. RecomendacoesActivity | Card da lente descoberta e lista de filmes | TextView, CardView, RecyclerView, ImageView, ProgressBar, Button | ProgressBar mostra a afinidade com a lente. Clique no filme abre a Tela 4 (Intent explícita). Botão "Meu diário" abre a Tela 5 (Intent explícita). |
| 4. DetalhesFilmeActivity | Ficha do filme, motivo do match e avaliação | ImageView, TextView, Spinner (nota 1 a 5), EditText (resenha), Button | POST /avaliacoes (Create). Intent implícita 1: ACTION_SEND para compartilhar o match. Intent implícita 2: ACTION_VIEW para buscar o trailer no YouTube. |
| 5. PerfilDiarioActivity | Diário com as avaliações salvas | TextView, CardView, RecyclerView, Button | GET /usuarios/{id}/avaliacoes (Read). PUT /avaliacoes/{id} por AlertDialog (Update). DELETE /avaliacoes/{id} com confirmação (Delete). |

Detalhes de implementação:
- Capa do filme: ícone vetorial local da lente (sem imagens da internet).
- Trailer: abre https://www.youtube.com/results?search_query=trailer+TITULO (título codificado).
- Compartilhar: Intent.createChooser com o texto "Meu match no CinePersona: TITULO (MATCH%)".
- Busca no catálogo (campo de texto na Tela 3, GET /filmes?q=): opcional, só se sobrar tempo.

## 3. Banco relacional (SQLite) e API REST (FastAPI + Swagger)

O arquivo cinepersona.db é criado automaticamente ao rodar o servidor, com 3 tabelas relacionadas:

```
[usuarios] 1 ----< N [avaliacoes] N >---- 1 [filmes]
```

- usuarios: id (PK), nome, email (único), lente_atual
- filmes: id (PK), titulo, ano, diretor, genero, lente_alvo, is_cult, motivo_reconhecimento, sinopse
- avaliacoes: id (PK), usuario_id (FK), filme_id (FK), nota (1 a 5), comentario; um usuário avalia cada filme uma única vez

### Endpoints (documentados em http://IP:8000/docs)

- POST /usuarios: cadastra ou recupera usuário pelo e-mail.
- GET /filmes: lista o catálogo; aceita ?q= para buscar por título ou diretor.
- POST /recomendar: processa o questionário, grava a lente no perfil e retorna os filmes ordenados com match_percentual.
- POST /avaliacoes: registra nota e comentário (Create).
- GET /usuarios/{id}/avaliacoes: histórico do usuário com dados do filme (Read).
- PUT /avaliacoes/{id}: atualiza nota e comentário (Update).
- DELETE /avaliacoes/{id}: remove do diário (Delete).

## 4. Fora do escopo

Top 4 Favoritos, DNA/Wrapped, capas baixadas da internet, login com senha, uso offline, animações, tema claro.