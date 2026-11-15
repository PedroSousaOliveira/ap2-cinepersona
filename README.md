# CinePersona

Aplicativo Android que recomenda filmes a partir de um questionário rápido sobre o momento de vida, o clima desejado para a sessão e as prioridades do dia. As respostas somam pontos em quatro lentes cinematográficas; a lente vencedora define a recomendação e cada filme recebe um percentual de match calculado no backend. O usuário também mantém um diário pessoal de avaliações, com nota de 1 a 5 e resenha opcional. A classificação é uma pontuação ponderada com regras fixas, descritas abaixo.

## Tecnologias

- Android: Java, XML, ViewBinding, Retrofit + Gson, Lifecycle (ViewModel e LiveData), RecyclerView, CardView, Material Components, JUnit
- Backend: Python, FastAPI, Pydantic, SQLAlchemy, SQLite, Uvicorn, pytest

## Arquitetura

```
ap2-app/
  app/                           modulo Android
    src/main/java/com/example/p2/
      ui/                        Activity + ViewModel + Adapter por tela
      data/remote/               ApiService (Retrofit), RetrofitClient, parser de erro da API
      data/repository/           repositorios que expoem LiveData<Resultado<T>>
      domain/model/              modelos compartilhados (Usuario, Filme, Avaliacao, Lente, ...)
      util/                      validacoes e regras puras cobertas por JUnit
    src/test/                    testes JUnit
  backend/
    app/
      routers/                   endpoints FastAPI
      services/                  regras de negocio; pontuacao pura em recomendacao.py
      repositories/              acesso a dados com SQLAlchemy
      seed/                      catalogo inicial de 32 filmes
      models.py  schemas.py  database.py  main.py
    tests/                       pytest (pontuacao e API)
  docs/                          escopo, regras, briefing e screenshots
```

Decisões principais:

- Os modelos de `domain/model` são usados por todas as camadas do Android, sem mappers.
- Todo o cálculo de pontuação fica no backend, em `services/recomendacao.py`, como função pura sem acesso a banco.
- O banco SQLite é gravado apenas através da API.

## Pré-requisitos

- Windows com PowerShell
- Python 3.12 ou mais recente
- Android Studio com SDK 37 e um emulador configurado

## Executando o backend (PowerShell)

```powershell
cd backend
python -m venv .venv
.venv\Scripts\python -m pip install -r requirements.txt
.venv\Scripts\python -m uvicorn app.main:app --port 8000
```

Na primeira subida a API cria e povoa o banco `cinepersona.db`. Deixe essa janela aberta enquanto usa o app.

Testes do backend:

```powershell
.venv\Scripts\python -m pytest -q
```

Documentação interativa (Swagger): http://localhost:8000/docs

## Executando o Android

Emulador:

1. Abra a pasta do projeto no Android Studio e aguarde a sincronização do Gradle.
2. Suba o backend como descrito acima.
3. Clique em Run com um emulador selecionado. O app usa `http://10.0.2.2:8000/` por padrão, que é o endereço do PC visto de dentro do emulador.

Celular físico:

1. Adicione ao `local.properties` a linha `api.baseUrl=http://localhost:8000/` e recompile.
2. Ative a depuração USB no aparelho e conecte o cabo.
3. Rode `adb reverse tcp:8000 tcp:8000` e instale o app pelo Run. O comando precisa ser repetido se o aparelho for reconectado.
4. Para voltar ao emulador, remova a linha do `local.properties` e recompile.

Testes do Android:

```powershell
.\gradlew.bat testDebugUnitTest
```

## Endpoints da API

| Método | Rota | Descrição | Códigos |
| --- | --- | --- | --- |
| POST | /usuarios | Cadastra usuário ou recupera o existente pelo e-mail | 201, 200, 422 |
| GET | /filmes?q= | Lista o catálogo; `q` filtra título e diretor sem diferenciar caixa | 200 |
| POST | /recomendar | Calcula a lente da sessão e a lista de filmes ordenada por match | 200, 404, 422 |
| POST | /avaliacoes | Cria uma avaliação (uma por usuário e filme) | 201, 404, 409, 422 |
| GET | /usuarios/{id}/avaliacoes | Lista as avaliações do usuário com os dados de cada filme | 200, 404 |
| PUT | /avaliacoes/{id} | Atualiza nota e comentário | 200, 404, 422 |
| DELETE | /avaliacoes/{id} | Remove a avaliação | 204, 404 |

## Pontuação

| Resposta | Efeito |
| --- | --- |
| Momento (RadioGroup) | +40 na lente escolhida |
| Clima (Spinner) | +30 na lente escolhida |
| Cada prioridade marcada (CheckBox) | +15 na lente correspondente |
| Total máximo | 85 |

- Lente vencedora: maior total; em empate vale a lente do momento.
- `reconhecimento_percentual = arredondar(100 * total_vencedora / 85)`
- `match_percentual = limitar(75 + arredondar(24 * total_da_lente_do_filme / 85) + bonus, 75, 99)`, com bônus de 5 quando o `is_cult` do filme coincide com o modo lado B escolhido.
- `arredondar(x) = int(x + 0.5)`
- Ordenação da lista: match decrescente e, em empate, título.

## Telas

| Tela | Screenshot |
| --- | --- |
| Login | ![Login](docs/screenshots/01-login.png) |
| Questionário | ![Questionário](docs/screenshots/02-questionario.png) |
| Recomendações | ![Recomendações](docs/screenshots/03-recomendacoes.png) |
| Detalhes do filme | ![Detalhes](docs/screenshots/04-detalhes.png) |
| Meu diário | ![Diário](docs/screenshots/05-diario.png) |
