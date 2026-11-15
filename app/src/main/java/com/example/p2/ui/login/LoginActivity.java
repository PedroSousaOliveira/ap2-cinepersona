package com.example.p2.ui.login;

import android.os.Bundle;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.p2.databinding.ActivityLoginBinding;
import com.example.p2.domain.model.Usuario;
import com.example.p2.util.Extras;
import com.example.p2.util.Resultado;
import com.example.p2.util.Validadores;
import com.example.p2.ui.questionario.QuestionarioActivity;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.getEstado().observe(this, this::mostrarResultado);
        binding.buttonEntrar.setOnClickListener(view -> tentarEntrar());
    }

    private void tentarEntrar() {
        String nome = binding.editNome.getText().toString().trim();
        String email = binding.editEmail.getText().toString().trim();
        binding.editNome.setError(null);
        binding.editEmail.setError(null);
        if (!Validadores.nomeValido(nome)) {
            binding.editNome.setError(getString(com.example.p2.R.string.login_name_required));
        }
        if (!Validadores.emailValido(email)) {
            binding.editEmail.setError(getString(com.example.p2.R.string.login_email_invalid));
        }
        if (Validadores.nomeValido(nome) && Validadores.emailValido(email)) {
            viewModel.entrar(nome, email);
        }
    }

    private void mostrarResultado(Resultado<Usuario> resultado) {
        if (resultado == null) {
            return;
        }
        boolean carregando = resultado.getEstado() == Resultado.Estado.CARREGANDO;
        binding.progressLogin.setVisibility(carregando ? android.view.View.VISIBLE : android.view.View.GONE);
        binding.buttonEntrar.setEnabled(!carregando);
        if (resultado.getEstado() == Resultado.Estado.SUCESSO) {
            abrirQuestionario(resultado.getValor());
        } else if (resultado.getEstado() == Resultado.Estado.ERRO) {
            mostrarErro(resultado.getMensagem());
        }
    }

    private void abrirQuestionario(Usuario usuario) {
        viewModel.limparEstado();
        if (usuario == null) {
            mostrarErro(null);
            return;
        }
        Intent intent = new Intent(this, QuestionarioActivity.class);
        intent.putExtra(Extras.USUARIO_ID, usuario.getId());
        intent.putExtra(Extras.NOME, usuario.getNome());
        startActivity(intent);
    }

    private void mostrarErro(String mensagem) {
        viewModel.limparEstado();
        binding.progressLogin.setVisibility(android.view.View.GONE);
        binding.buttonEntrar.setEnabled(true);
        String texto = mensagem == null || mensagem.trim().isEmpty()
                ? getString(com.example.p2.R.string.login_error_fallback) : mensagem;
        Snackbar.make(binding.getRoot(), texto, Snackbar.LENGTH_LONG).show();
    }
}
