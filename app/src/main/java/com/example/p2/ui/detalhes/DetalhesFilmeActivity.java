package com.example.p2.ui.detalhes;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.p2.R;
import com.example.p2.databinding.ActivityDetalhesFilmeBinding;
import com.example.p2.domain.model.Avaliacao;
import com.example.p2.domain.model.AvaliacaoRequest;
import com.example.p2.domain.model.Filme;
import com.example.p2.ui.perfil.PerfilDiarioActivity;
import com.example.p2.util.Extras;
import com.example.p2.util.LenteIcone;
import com.example.p2.util.Resultado;
import com.example.p2.util.TextoCompartilhamento;
import com.example.p2.util.UrlTrailer;
import com.google.android.material.snackbar.Snackbar;

public class DetalhesFilmeActivity extends AppCompatActivity {
    private ActivityDetalhesFilmeBinding binding;
    private DetalhesViewModel viewModel;
    private Filme filme;
    private int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalhesFilmeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        filme = (Filme) getIntent().getSerializableExtra(Extras.FILME);
        usuarioId = getIntent().getIntExtra(Extras.USUARIO_ID, -1);
        if (filme == null) {
            finish();
            return;
        }
        viewModel = new ViewModelProvider(this).get(DetalhesViewModel.class);
        viewModel.getEstado().observe(this, this::mostrarResultado);
        preencherFicha();
        binding.buttonSalvar.setOnClickListener(view -> salvarAvaliacao());
        binding.buttonCompartilhar.setOnClickListener(view -> compartilhar());
        binding.buttonTrailer.setOnClickListener(view -> verTrailer());
    }

    private void preencherFicha() {
        binding.imageLenteDetalhes.setImageResource(LenteIcone.drawable(filme.getLenteAlvo()));
        binding.textTitulo.setText(filme.getTitulo());
        binding.textFicha.setText(getString(R.string.detalhes_ficha,
                filme.getAno(), filme.getDiretor(), filme.getGenero()));
        binding.textSinopse.setText(filme.getSinopse());
        binding.textMotivo.setText(filme.getMotivoReconhecimento());
        if (filme.getMatchPercentual() != null) {
            binding.textMatch.setText(getString(R.string.detalhes_match, filme.getMatchPercentual()));
        } else {
            binding.textMatch.setVisibility(View.GONE);
        }
    }

    private void salvarAvaliacao() {
        int nota = binding.spinnerNota.getSelectedItemPosition() + 1;
        String resenha = binding.editResenha.getText().toString().trim();
        viewModel.salvar(new AvaliacaoRequest(
                usuarioId, filme.getId(), nota, resenha.isEmpty() ? null : resenha));
    }

    private void mostrarResultado(Resultado<Avaliacao> resultado) {
        if (resultado == null) {
            return;
        }
        boolean carregando = resultado.getEstado() == Resultado.Estado.CARREGANDO;
        binding.progressDetalhes.setVisibility(carregando ? View.VISIBLE : View.GONE);
        binding.buttonSalvar.setEnabled(!carregando);
        if (resultado.getEstado() == Resultado.Estado.SUCESSO) {
            viewModel.limparEstado();
            Snackbar.make(binding.getRoot(), R.string.detalhes_salvo, Snackbar.LENGTH_LONG).show();
        } else if (resultado.getEstado() == Resultado.Estado.ERRO) {
            viewModel.limparEstado();
            mostrarErro(resultado);
        }
    }

    private void mostrarErro(Resultado<Avaliacao> resultado) {
        if (resultado.getCodigoHttp() != null && resultado.getCodigoHttp() == 409) {
            Snackbar.make(binding.getRoot(), R.string.detalhes_ja_avaliado, Snackbar.LENGTH_LONG)
                    .setAction(R.string.detalhes_abrir_diario, view -> abrirDiario())
                    .show();
            return;
        }
        String mensagem = resultado.getMensagem();
        String texto = mensagem == null || mensagem.trim().isEmpty()
                ? getString(R.string.detalhes_erro_fallback) : mensagem;
        Snackbar.make(binding.getRoot(), texto, Snackbar.LENGTH_LONG).show();
    }

    private void compartilhar() {
        int match = filme.getMatchPercentual() != null ? filme.getMatchPercentual() : 0;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, TextoCompartilhamento.gerar(filme.getTitulo(), match));
        try {
            startActivity(Intent.createChooser(intent, null));
        } catch (ActivityNotFoundException erro) {
            Snackbar.make(binding.getRoot(),
                    R.string.detalhes_sem_app_compartilhar, Snackbar.LENGTH_LONG).show();
        }
    }

    private void verTrailer() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(UrlTrailer.gerar(filme.getTitulo())));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException erro) {
            Snackbar.make(binding.getRoot(),
                    R.string.detalhes_sem_app_trailer, Snackbar.LENGTH_LONG).show();
        }
    }

    private void abrirDiario() {
        Intent intent = new Intent(this, PerfilDiarioActivity.class);
        intent.putExtra(Extras.USUARIO_ID, usuarioId);
        startActivity(intent);
    }
}
