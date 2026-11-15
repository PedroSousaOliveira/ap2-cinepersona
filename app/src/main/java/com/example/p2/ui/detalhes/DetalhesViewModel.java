package com.example.p2.ui.detalhes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p2.data.repository.AvaliacaoRepository;
import com.example.p2.domain.model.Avaliacao;
import com.example.p2.domain.model.AvaliacaoRequest;
import com.example.p2.util.Resultado;

public class DetalhesViewModel extends ViewModel {
    private final AvaliacaoRepository repository = new AvaliacaoRepository();
    private final MediatorLiveData<Resultado<Avaliacao>> estado = new MediatorLiveData<>();
    private LiveData<Resultado<Avaliacao>> requisicaoAtual;

    public LiveData<Resultado<Avaliacao>> getEstado() {
        return estado;
    }

    public void salvar(AvaliacaoRequest request) {
        removerRequisicaoAtual();
        requisicaoAtual = repository.criar(request);
        estado.addSource(requisicaoAtual, this::publicarResultado);
    }

    public void limparEstado() {
        removerRequisicaoAtual();
        estado.setValue(null);
    }

    private void publicarResultado(Resultado<Avaliacao> resultado) {
        estado.setValue(resultado);
        if (resultado != null && resultado.getEstado() != Resultado.Estado.CARREGANDO) {
            removerRequisicaoAtual();
        }
    }

    private void removerRequisicaoAtual() {
        if (requisicaoAtual != null) {
            estado.removeSource(requisicaoAtual);
            requisicaoAtual = null;
        }
    }
}
