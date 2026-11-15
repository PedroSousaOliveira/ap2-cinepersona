package com.example.p2.ui.recomendacoes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.p2.R;
import com.example.p2.databinding.ActivityRecomendacoesBinding;
import com.example.p2.domain.model.Filme;
import com.example.p2.domain.model.ResultadoRecomendacao;
import com.example.p2.ui.detalhes.DetalhesFilmeActivity;
import com.example.p2.ui.perfil.PerfilDiarioActivity;
import com.example.p2.util.Extras;
import com.example.p2.util.LenteIcone;

import java.util.List;

public class RecomendacoesActivity extends AppCompatActivity {
    private ActivityRecomendacoesBinding binding;
    private int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecomendacoesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        usuarioId = getIntent().getIntExtra(Extras.USUARIO_ID, -1);
        binding.buttonMeuDiario.setOnClickListener(view -> abrirDiario());
        ResultadoRecomendacao resultado = (ResultadoRecomendacao)
                getIntent().getSerializableExtra(Extras.RESULTADO_RECOMENDACAO);
        if (resultado == null) {
            mostrarListaVazia();
            return;
        }
        preencherLente(resultado);
        preencherFilmes(resultado.getFilmes());
    }

    private void preencherLente(ResultadoRecomendacao resultado) {
        binding.imageLente.setImageResource(LenteIcone.drawable(resultado.getLente()));
        binding.textNomeLente.setText(resultado.getNomeLente());
        binding.textDescricaoLente.setText(resultado.getDescricaoLente());
        binding.textReconhecimento.setText(getString(
                R.string.recomendacoes_reconhecimento, resultado.getReconhecimentoPercentual()));
        binding.progressReconhecimento.setProgress(resultado.getReconhecimentoPercentual());
    }

    private void preencherFilmes(List<Filme> filmes) {
        if (filmes == null || filmes.isEmpty()) {
            mostrarListaVazia();
            return;
        }
        binding.recyclerFilmes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerFilmes.setAdapter(new FilmeAdapter(filmes, this::abrirDetalhes));
    }

    private void mostrarListaVazia() {
        binding.recyclerFilmes.setVisibility(View.GONE);
        binding.textListaVazia.setVisibility(View.VISIBLE);
    }

    private void abrirDetalhes(Filme filme) {
        Intent intent = new Intent(this, DetalhesFilmeActivity.class);
        intent.putExtra(Extras.FILME, filme);
        intent.putExtra(Extras.USUARIO_ID, usuarioId);
        startActivity(intent);
    }

    private void abrirDiario() {
        Intent intent = new Intent(this, PerfilDiarioActivity.class);
        intent.putExtra(Extras.USUARIO_ID, usuarioId);
        startActivity(intent);
    }
}
