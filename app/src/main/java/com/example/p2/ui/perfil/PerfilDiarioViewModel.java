package com.example.p2.ui.perfil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p2.data.repository.AvaliacaoRepository;
import com.example.p2.domain.model.Avaliacao;
import com.example.p2.domain.model.AvaliacaoAtualizarRequest;
import com.example.p2.util.Resultado;

import java.util.List;

public class PerfilDiarioViewModel extends ViewModel {
    private final AvaliacaoRepository repository = new AvaliacaoRepository();
    private final MediatorLiveData<Resultado<List<Avaliacao>>> estadoLista =
            new MediatorLiveData<>();
    private final MediatorLiveData<Resultado<?>> estadoOperacao = new MediatorLiveData<>();
    private LiveData<Resultado<List<Avaliacao>>> requisicaoLista;
    private LiveData<? extends Resultado<?>> requisicaoOperacao;
    private int usuarioId = -1;

    public LiveData<Resultado<List<Avaliacao>>> getEstadoLista() {
        return estadoLista;
    }

    public LiveData<Resultado<?>> getEstadoOperacao() {
        return estadoOperacao;
    }

    public void carregar(int usuarioId) {
        this.usuarioId = usuarioId;
        removerRequisicaoLista();
        requisicaoLista = repository.listarPorUsuario(usuarioId);
        estadoLista.addSource(requisicaoLista, this::publicarLista);
    }

    public void atualizar(int avaliacaoId, AvaliacaoAtualizarRequest request) {
        iniciarOperacao(repository.atualizar(avaliacaoId, request));
    }

    public void excluir(int avaliacaoId) {
        iniciarOperacao(repository.excluir(avaliacaoId));
    }

    public void limparOperacao() {
        removerRequisicaoOperacao();
        estadoOperacao.setValue(null);
    }

    private void publicarLista(Resultado<List<Avaliacao>> resultado) {
        estadoLista.setValue(resultado);
        if (resultado != null && resultado.getEstado() != Resultado.Estado.CARREGANDO) {
            removerRequisicaoLista();
        }
    }

    private <T> void iniciarOperacao(LiveData<Resultado<T>> fonte) {
        removerRequisicaoOperacao();
        requisicaoOperacao = fonte;
        estadoOperacao.addSource(fonte, this::publicarOperacao);
    }

    private void publicarOperacao(Resultado<?> resultado) {
        estadoOperacao.setValue(resultado);
        if (resultado != null && resultado.getEstado() != Resultado.Estado.CARREGANDO) {
            removerRequisicaoOperacao();
            if (resultado.getEstado() == Resultado.Estado.SUCESSO && usuarioId != -1) {
                carregar(usuarioId);
            }
        }
    }

    private void removerRequisicaoLista() {
        if (requisicaoLista != null) {
            estadoLista.removeSource(requisicaoLista);
            requisicaoLista = null;
        }
    }

    private void removerRequisicaoOperacao() {
        if (requisicaoOperacao != null) {
            estadoOperacao.removeSource(requisicaoOperacao);
            requisicaoOperacao = null;
        }
    }
}
