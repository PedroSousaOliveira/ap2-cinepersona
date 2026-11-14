from app.models import Lente, Usuario


def criar_usuario(client):
    resposta = client.post(
        "/usuarios",
        json={"nome": "Ana", "email": "ana@example.com"},
    )
    assert resposta.status_code == 201
    return resposta.json()


def criar_avaliacao(client, usuario_id, filme_id, nota=4):
    return client.post(
        "/avaliacoes",
        json={"usuario_id": usuario_id, "filme_id": filme_id, "nota": nota},
    )


def test_post_usuario_e_idempotente(client):
    primeira = client.post(
        "/usuarios", json={"nome": "Ana", "email": "ana@example.com"}
    )
    segunda = client.post(
        "/usuarios", json={"nome": "Outra Ana", "email": "ANA@example.com"}
    )
    assert primeira.status_code == 201
    assert segunda.status_code == 200
    assert segunda.json()["id"] == primeira.json()["id"]


def test_post_usuario_valida_nome_e_email(client):
    nome_invalido = client.post(
        "/usuarios", json={"nome": "   ", "email": "ana@example.com"}
    )
    email_invalido = client.post(
        "/usuarios", json={"nome": "Ana", "email": "email-invalido"}
    )
    assert nome_invalido.status_code == 422
    assert email_invalido.status_code == 422


def test_get_filmes_busca_titulo_e_diretor_sem_diferenciar_caixa(client):
    por_titulo = client.get("/filmes", params={"q": "ORIGEM"})
    por_diretor = client.get("/filmes", params={"q": "NOLAN"})
    assert [filme["titulo"] for filme in por_titulo.json()] == ["A Origem"]
    assert len(por_diretor.json()) == 3


def test_post_recomendar_grava_lente_e_devolve_catalogo(client):
    usuario = criar_usuario(client)
    resposta = client.post(
        "/recomendar",
        json={
            "usuario_id": usuario["id"],
            "momento": "ROTEIRO_DE_VIRADA",
            "clima": "CORTE_SECO_ADRENALINA",
            "prioridades": ["PLANO_SEQUENCIA_AFETIVO"],
            "modo_lado_b": False,
        },
    )
    atualizado = client.get(f"/usuarios/{usuario['id']}/avaliacoes")
    assert resposta.status_code == 200
    assert resposta.json()["lente"] == "ROTEIRO_DE_VIRADA"
    assert len(resposta.json()["filmes"]) == 32
    assert all(75 <= filme["match_percentual"] <= 99 for filme in resposta.json()["filmes"])
    matches = [filme["match_percentual"] for filme in resposta.json()["filmes"]]
    assert matches == sorted(matches, reverse=True)
    assert atualizado.status_code == 200
    with client.app.state.test_session() as db:
        usuario_atual = db.get(Usuario, usuario["id"])
        assert usuario_atual.lente_atual == Lente.ROTEIRO_DE_VIRADA


def test_recomendar_e_historico_rejeitam_usuario_inexistente(client):
    recomendacao = client.post(
        "/recomendar",
        json={
            "usuario_id": 999,
            "momento": "ROTEIRO_DE_VIRADA",
            "clima": "ROTEIRO_DE_VIRADA",
            "prioridades": [],
            "modo_lado_b": False,
        },
    )
    historico = client.get("/usuarios/999/avaliacoes")
    assert recomendacao.status_code == 404
    assert historico.status_code == 404


def test_criar_avaliacao_rejeita_dados_invalidos_e_recursos_ausentes(client):
    usuario = criar_usuario(client)
    filme_id = client.get("/filmes").json()[0]["id"]
    invalida = criar_avaliacao(client, usuario["id"], filme_id, nota=6)
    ausente = client.post(
        "/avaliacoes", json={"usuario_id": 999, "filme_id": filme_id, "nota": 4}
    )
    filme_ausente = client.post(
        "/avaliacoes", json={"usuario_id": usuario["id"], "filme_id": 999, "nota": 4}
    )
    assert invalida.status_code == 422
    assert ausente.status_code == 404
    assert filme_ausente.status_code == 404


def test_crud_avaliacoes(client):
    usuario = criar_usuario(client)
    filme_id = client.get("/filmes").json()[0]["id"]
    criada = criar_avaliacao(client, usuario["id"], filme_id)
    duplicada = criar_avaliacao(client, usuario["id"], filme_id)
    historico = client.get(f"/usuarios/{usuario['id']}/avaliacoes")
    atualizada = client.put(
        f"/avaliacoes/{criada.json()['id']}",
        json={"nota": 5, "comentario": "Reveria este filme."},
    )
    invalida_atualizacao = client.put(
        f"/avaliacoes/{criada.json()['id']}", json={"nota": 0}
    )
    avaliacao_ausente = client.put("/avaliacoes/999", json={"nota": 4})
    removida = client.delete(f"/avaliacoes/{criada.json()['id']}")
    inexistente = client.delete(f"/avaliacoes/{criada.json()['id']}")
    assert criada.status_code == 201
    assert duplicada.status_code == 409
    assert historico.status_code == 200 and historico.json()[0]["filme"]["id"] == filme_id
    assert atualizada.status_code == 200
    assert atualizada.json()["nota"] == 5
    assert invalida_atualizacao.status_code == 422
    assert avaliacao_ausente.status_code == 404
    assert removida.status_code == 204
    assert inexistente.status_code == 404
