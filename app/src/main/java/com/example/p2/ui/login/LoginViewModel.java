package com.example.p2.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.p2.data.repository.UsuarioRepository;
import com.example.p2.domain.model.Usuario;
import com.example.p2.domain.model.UsuarioRequest;
import com.example.p2.util.Resultado;

public class LoginViewModel extends ViewModel {
    private final UsuarioRepository repository = new UsuarioRepository();
    private final MediatorLiveData<Resultado<Usuario>> estado = new MediatorLiveData<>();
    private LiveData<Resultado<Usuario>> requisicaoAtual;

    public LiveData<Resultado<Usuario>> getEstado() {
        return estado;
    }

    public void entrar(String nome, String email) {
        removerRequisicaoAtual();
        requisicaoAtual = repository.criarOuObter(new UsuarioRequest(nome, email));
        estado.addSource(requisicaoAtual, this::publicarResultado);
    }

    public void limparEstado() {
        removerRequisicaoAtual();
        estado.setValue(null);
    }

    private void publicarResultado(Resultado<Usuario> resultado) {
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
