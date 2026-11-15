package com.example.p2.ui.questionario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p2.data.repository.RecomendacaoRepository;
import com.example.p2.domain.model.RecomendacaoRequest;
import com.example.p2.domain.model.ResultadoRecomendacao;
import com.example.p2.util.Resultado;

public class QuestionarioViewModel extends ViewModel {
    private final RecomendacaoRepository repository = new RecomendacaoRepository();
    private final MediatorLiveData<Resultado<ResultadoRecomendacao>> estado =
            new MediatorLiveData<>();
    private LiveData<Resultado<ResultadoRecomendacao>> requisicaoAtual;

    public LiveData<Resultado<ResultadoRecomendacao>> getEstado() {
        return estado;
    }

    public void recomendar(RecomendacaoRequest request) {
        removerRequisicaoAtual();
        requisicaoAtual = repository.recomendar(request);
        estado.addSource(requisicaoAtual, this::publicarResultado);
    }

    public void limparEstado() {
        removerRequisicaoAtual();
        estado.setValue(null);
    }

    private void publicarResultado(Resultado<ResultadoRecomendacao> resultado) {
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
