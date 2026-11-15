package com.example.p2.ui.perfil;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.p2.R;
import com.example.p2.databinding.ActivityPerfilDiarioBinding;
import com.example.p2.databinding.DialogAvaliacaoBinding;
import com.example.p2.domain.model.Avaliacao;
import com.example.p2.domain.model.AvaliacaoAtualizarRequest;
import com.example.p2.util.Extras;
import com.example.p2.util.Resultado;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class PerfilDiarioActivity extends AppCompatActivity
        implements AvaliacaoAdapter.OnAvaliacaoAcaoListener {
    private ActivityPerfilDiarioBinding binding;
    private PerfilDiarioViewModel viewModel;
    private int usuarioId;
    private int mensagemSucesso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilDiarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        usuarioId = getIntent().getIntExtra(Extras.USUARIO_ID, -1);
        binding.recyclerAvaliacoes.setLayoutManager(new LinearLayoutManager(this));
        viewModel = new ViewModelProvider(this).get(PerfilDiarioViewModel.class);
        viewModel.getEstadoLista().observe(this, this::mostrarLista);
        viewModel.getEstadoOperacao().observe(this, this::mostrarOperacao);
        viewModel.carregar(usuarioId);
    }

    @Override
    public void onEditar(Avaliacao avaliacao) {
        DialogAvaliacaoBinding dialogBinding = DialogAvaliacaoBinding.inflate(getLayoutInflater());
        dialogBinding.spinnerNotaDialog.setSelection(avaliacao.getNota() - 1);
        if (avaliacao.getComentario() != null) {
            dialogBinding.editComentarioDialog.setText(avaliacao.getComentario());
        }
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.perfil_editar_titulo)
                .setView(dialogBinding.getRoot())
                .setNegativeButton(R.string.perfil_cancelar, null)
                .setPositiveButton(R.string.perfil_salvar, (dialog, botao) ->
                        salvarEdicao(avaliacao, dialogBinding))
                .show();
    }

    @Override
    public void onExcluir(Avaliacao avaliacao) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.perfil_excluir_titulo)
                .setMessage(getString(R.string.perfil_excluir_mensagem,
                        avaliacao.getFilme().getTitulo()))
                .setNegativeButton(R.string.perfil_cancelar, null)
                .setPositiveButton(R.string.perfil_excluir, (dialog, botao) -> {
                    mensagemSucesso = R.string.perfil_excluida;
                    viewModel.excluir(avaliacao.getId());
                })
                .show();
    }

    private void salvarEdicao(Avaliacao avaliacao, DialogAvaliacaoBinding dialogBinding) {
        int nota = dialogBinding.spinnerNotaDialog.getSelectedItemPosition() + 1;
        String comentario = dialogBinding.editComentarioDialog.getText().toString().trim();
        mensagemSucesso = R.string.perfil_atualizada;
        viewModel.atualizar(avaliacao.getId(), new AvaliacaoAtualizarRequest(
                nota, comentario.isEmpty() ? null : comentario));
    }

    private void mostrarLista(Resultado<List<Avaliacao>> resultado) {
        if (resultado == null) {
            return;
        }
        boolean carregando = resultado.getEstado() == Resultado.Estado.CARREGANDO;
        binding.progressDiario.setVisibility(carregando ? View.VISIBLE : View.GONE);
        if (resultado.getEstado() == Resultado.Estado.SUCESSO) {
            preencherLista(resultado.getValor());
        } else if (resultado.getEstado() == Resultado.Estado.ERRO) {
            mostrarListaVazia();
            Snackbar.make(binding.getRoot(), R.string.perfil_erro_carregar, Snackbar.LENGTH_LONG)
                    .setAction(R.string.perfil_tentar_novamente,
                            view -> viewModel.carregar(usuarioId))
                    .show();
        }
    }

    private void preencherLista(List<Avaliacao> avaliacoes) {
        if (avaliacoes == null || avaliacoes.isEmpty()) {
            mostrarListaVazia();
            return;
        }
        binding.textDiarioVazio.setVisibility(View.GONE);
        binding.recyclerAvaliacoes.setVisibility(View.VISIBLE);
        binding.recyclerAvaliacoes.setAdapter(new AvaliacaoAdapter(avaliacoes, this));
    }

    private void mostrarListaVazia() {
        binding.recyclerAvaliacoes.setVisibility(View.GONE);
        binding.textDiarioVazio.setVisibility(View.VISIBLE);
    }

    private void mostrarOperacao(Resultado<?> resultado) {
        if (resultado == null) {
            return;
        }
        if (resultado.getEstado() == Resultado.Estado.SUCESSO) {
            viewModel.limparOperacao();
            Snackbar.make(binding.getRoot(), mensagemSucesso, Snackbar.LENGTH_LONG).show();
        } else if (resultado.getEstado() == Resultado.Estado.ERRO) {
            viewModel.limparOperacao();
            String mensagem = resultado.getMensagem();
            String texto = mensagem == null || mensagem.trim().isEmpty()
                    ? getString(R.string.perfil_erro_operacao) : mensagem;
            Snackbar.make(binding.getRoot(), texto, Snackbar.LENGTH_LONG).show();
        }
    }
}
