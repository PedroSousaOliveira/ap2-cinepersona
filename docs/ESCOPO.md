# Escopo CinePersona (entrega e apresentação em 16/11)
Prevalece sobre docs/BRIEFING_ORIGINAL.md onde divergir. O módulo Android fica em app/.
## Prioridade
Funcionar ao vivo > requisitos do enunciado > extras. Se atrasar, cortar nesta ordem: busca no catálogo, testes Android, polimento visual. Requisito do enunciado nunca se corta.
## Requisitos do enunciado
- Java/XML, no mínimo 5 Activities, Intents explícitas na navegação, no mínimo 1 Intent implícita
- No mínimo 6 tipos de View, UI consistente, feedback visual (loading, validação, Snackbar)
- API REST com Swagger/OpenAPI, JSON, cadastro/consulta/alteração/remoção
- Banco relacional (SQLite) gravado apenas via API
- Repositório GitHub público com Android + backend + README (título, proposta, tecnologias, execução, prints, descrição da API, link do Swagger)
## Ajustes ao briefing
1. Sem poster_url/trailer_url. Capa = vetor local por lente. Trailer = ACTION_VIEW em https://www.youtube.com/results?search_query=trailer+<titulo codificado>.
2. descricao_lente não é coluna; nome e descrição de cada lente são constantes em services/lentes.py.
3. UNIQUE(usuario_id, filme_id) em avaliacoes.
4. Sem autenticação além de nome + e-mail. usuario_id trafega por Intent extras.
5. Compartilhar = ACTION_SEND text/plain via Intent.createChooser, texto "Meu match no CinePersona: {titulo} ({match}%)". Nunca chamar resolveActivity; usar try/catch de ActivityNotFoundException.
6. O cálculo fica no backend.
## Lentes (enum, ordem fixa)
ROTEIRO_DE_VIRADA, PLANO_SEQUENCIA_AFETIVO, CORTE_SECO_ADRENALINA, FORA_DE_QUADRO
## Pontuação (exata)
- momento (RadioGroup): +40 na lente escolhida
- clima (Spinner): +30 na lente escolhida
- prioridades (CheckBox, 0 a 4; cada uma corresponde à lente de mesma posição): +15 em cada lente marcada
- PONTUACAO_MAXIMA = 85
- lente vencedora = maior total; empate = lente do momento
- reconhecimento_percentual = arredondar(100 * total[vencedora] / 85)
- match_filme = limitar(75 + arredondar(24 * total[lente_do_filme] / 85) + (5 se filme.is_cult == modo_lado_b), 75, 99)
- arredondar = int(x + 0.5) (não usar round do Python)
- ordenação: match desc, depois título
- O app converte a opção da UI em Lente pela posição na ordem do enum e envia o enum à API.
- Opções da UI: momento = "Começando um novo ciclo", "Reflexivo e nostálgico", "Mente inquieta, sob pressão", "Querendo fugir da rotina"; clima = "Inspirador", "Emocionante", "Tenso/Intrigante", "Criativo/Surpreendente"; prioridades = "Superação pessoal", "Conexões reais", "Inteligência e estratégia", "Liberdade e imaginação".
## API (todas no Swagger)
POST /usuarios -> 201 novo | 200 existente (por e-mail); valida e-mail e nome
GET /filmes?q= -> 200; q filtra título/diretor sem diferenciar maiúsculas
POST /recomendar body {usuario_id, momento, clima, prioridades[], modo_lado_b} -> 200 {lente, nome_lente, descricao_lente, reconhecimento_percentual, filmes[{id, titulo, ano, diretor, genero, lente_alvo, is_cult, motivo_reconhecimento, sinopse, match_percentual}]}; grava usuarios.lente_atual; 404 se usuário não existe
POST /avaliacoes {usuario_id, filme_id, nota 1..5, comentario?} -> 201 | 404 | 409 | 422
GET /usuarios/{id}/avaliacoes -> 200 com dados do filme | 404
PUT /avaliacoes/{id} {nota, comentario?} -> 200 | 404 | 422
DELETE /avaliacoes/{id} -> 204 | 404
## Tabelas
usuarios(id, nome, email UNIQUE, lente_atual NULL)
filmes(id, titulo, ano, diretor, genero, lente_alvo, is_cult, motivo_reconhecimento, sinopse)
avaliacoes(id, usuario_id FK, filme_id FK, nota, comentario NULL, UNIQUE(usuario_id, filme_id))
## Catálogo (32 filmes; titulo | ano | diretor | genero | is_cult)
ROTEIRO_DE_VIRADA
À Procura da Felicidade | 2006 | Gabriele Muccino | Drama | 0
Rocky: Um Lutador | 1976 | John G. Avildsen | Drama esportivo | 0
Um Sonho de Liberdade | 1994 | Frank Darabont | Drama | 0
Estrelas Além do Tempo | 2016 | Theodore Melfi | Drama biográfico | 0
A Vida Secreta de Walter Mitty | 2013 | Ben Stiller | Aventura | 1
Whiplash | 2014 | Damien Chazelle | Drama musical | 1
Billy Elliot | 2000 | Stephen Daldry | Drama | 1
Pequena Miss Sunshine | 2006 | Jonathan Dayton e Valerie Faris | Comédia dramática | 1
PLANO_SEQUENCIA_AFETIVO
Questão de Tempo | 2013 | Richard Curtis | Romance | 0
Divertida Mente | 2015 | Pete Docter | Animação | 0
Up: Altas Aventuras | 2009 | Pete Docter | Animação | 0
Coco | 2017 | Lee Unkrich | Animação | 0
Brilho Eterno de uma Mente sem Lembranças | 2004 | Michel Gondry | Drama romântico | 1
Cinema Paradiso | 1988 | Giuseppe Tornatore | Drama | 1
O Fabuloso Destino de Amélie Poulain | 2001 | Jean-Pierre Jeunet | Comédia romântica | 1
Antes do Amanhecer | 1995 | Richard Linklater | Romance | 1
CORTE_SECO_ADRENALINA
A Origem | 2010 | Christopher Nolan | Ficção científica | 0
O Lobo de Wall Street | 2013 | Martin Scorsese | Drama biográfico | 0
Seven: Os Sete Crimes Capitais | 1995 | David Fincher | Suspense | 0
Clube da Luta | 1999 | David Fincher | Drama | 0
Parasita | 2019 | Bong Joon-ho | Suspense | 1
Ilha do Medo | 2010 | Martin Scorsese | Suspense | 1
Oldboy | 2003 | Park Chan-wook | Suspense | 1
Amnésia | 2000 | Christopher Nolan | Suspense | 1
FORA_DE_QUADRO
Interestelar | 2014 | Christopher Nolan | Ficção científica | 0
O Show de Truman | 1998 | Peter Weir | Comédia dramática | 0
Matrix | 1999 | Lana e Lilly Wachowski | Ficção científica | 0
Corra! | 2017 | Jordan Peele | Terror | 0
A Viagem de Chihiro | 2001 | Hayao Miyazaki | Animação | 1
Tudo em Todo o Lugar ao Mesmo Tempo | 2022 | Daniel Kwan e Daniel Scheinert | Ficção científica | 1
Ela | 2013 | Spike Jonze | Drama romântico | 1
O Labirinto do Fauno | 2006 | Guillermo del Toro | Fantasia | 1
## Telas (detalhes no briefing)
1 Login, 2 Questionario, 3 Recomendacoes, 4 DetalhesFilme, 5 PerfilDiario. Paleta: fundo #14181C, cards #2C3440, destaque #00E054 e #FF8000. Tema escuro único.
Extras de Intent: usuario_id, nome; Questionario -> Recomendacoes leva ResultadoRecomendacao (Serializable); Recomendacoes -> Detalhes leva Filme (Serializable, com match) + usuario_id.
## Fora de escopo
Top 4 Favoritos, DNA/Wrapped, capas por rede, login com senha/JWT, Room/offline, Hilt, Compose, animações, tema claro, testes de UI, paginação, CI.
