package com.example.p2.ui.questionario;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.p2.R;
import com.example.p2.databinding.ActivityQuestionarioBinding;
import com.example.p2.domain.model.Lente;
import com.example.p2.domain.model.RecomendacaoRequest;
import com.example.p2.domain.model.ResultadoRecomendacao;
import com.example.p2.ui.recomendacoes.RecomendacoesActivity;
import com.example.p2.util.Extras;
import com.example.p2.util.QuestionarioRegras;
import com.example.p2.util.Resultado;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class QuestionarioActivity extends AppCompatActivity {
    private ActivityQuestionarioBinding binding;
    private QuestionarioViewModel viewModel;
    private int usuarioId;
    private boolean carregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(QuestionarioViewModel.class);
        usuarioId = getIntent().getIntExtra(Extras.USUARIO_ID, -1);
        String nome = getIntent().getStringExtra(Extras.NOME);
        binding.textGreeting.setText(getString(R.string.questionario_greeting, nome));
        configurarFormulario();
        viewModel.getEstado().observe(this, this::mostrarResultado);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        limparFormulario();
        viewModel.limparEstado();
    }

    private void configurarFormulario() {
        binding.groupMomento.setOnCheckedChangeListener((group, checkedId) -> atualizarBotao());
        binding.spinnerClima.setSelection(0);
        binding.spinnerClima.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atualizarBotao();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                atualizarBotao();
            }
        });
        binding.buttonRecomendar.setOnClickListener(view -> enviarQuestionario());
        atualizarBotao();
    }

    private void enviarQuestionario() {
        int posicaoMomento = posicaoMomento();
        int posicaoClima = binding.spinnerClima.getSelectedItemPosition();
        if (!QuestionarioRegras.botaoHabilitado(posicaoMomento, posicaoClima)) {
            atualizarBotao();
            return;
        }
        RecomendacaoRequest request = new RecomendacaoRequest(
                usuarioId,
                QuestionarioRegras.lenteDaPosicao(posicaoMomento),
                QuestionarioRegras.lenteDoClima(posicaoClima),
                obterPrioridades(),
                binding.switchLadoB.isChecked()
        );
        viewModel.recomendar(request);
    }

    private List<Lente> obterPrioridades() {
        List<Lente> prioridades = new ArrayList<>();
        adicionarPrioridade(binding.checkPrioridade0.isChecked(), 0, prioridades);
        adicionarPrioridade(binding.checkPrioridade1.isChecked(), 1, prioridades);
        adicionarPrioridade(binding.checkPrioridade2.isChecked(), 2, prioridades);
        adicionarPrioridade(binding.checkPrioridade3.isChecked(), 3, prioridades);
        return prioridades;
    }

    private void adicionarPrioridade(boolean marcada, int posicao, List<Lente> prioridades) {
        if (marcada) {
            prioridades.add(QuestionarioRegras.lenteDaPosicao(posicao));
        }
    }

    private int posicaoMomento() {
        int idMarcado = binding.groupMomento.getCheckedRadioButtonId();
        return binding.groupMomento.indexOfChild(binding.groupMomento.findViewById(idMarcado));
    }

    private void atualizarBotao() {
        int posicaoClima = binding.spinnerClima.getSelectedItemPosition();
        binding.buttonRecomendar.setEnabled(!carregando
                && QuestionarioRegras.botaoHabilitado(posicaoMomento(), posicaoClima));
    }

    private void mostrarResultado(Resultado<ResultadoRecomendacao> resultado) {
        if (resultado == null) {
            return;
        }
        carregando = resultado.getEstado() == Resultado.Estado.CARREGANDO;
        binding.progressQuestionario.setVisibility(carregando ? View.VISIBLE : View.GONE);
        atualizarBotao();
        if (resultado.getEstado() == Resultado.Estado.SUCESSO) {
            abrirRecomendacoes(resultado.getValor());
        } else if (resultado.getEstado() == Resultado.Estado.ERRO) {
            mostrarErro(resultado.getMensagem());
        }
    }

    private void abrirRecomendacoes(ResultadoRecomendacao resultado) {
        viewModel.limparEstado();
        if (resultado == null) {
            mostrarErro(null);
            return;
        }
        Intent intent = new Intent(this, RecomendacoesActivity.class);
        intent.putExtra(Extras.RESULTADO_RECOMENDACAO, resultado);
        intent.putExtra(Extras.USUARIO_ID, usuarioId);
        startActivity(intent);
    }

    private void mostrarErro(String mensagem) {
        viewModel.limparEstado();
        carregando = false;
        atualizarBotao();
        binding.progressQuestionario.setVisibility(View.GONE);
        String texto = mensagem == null || mensagem.trim().isEmpty()
                ? getString(R.string.questionario_error_fallback) : mensagem;
        Snackbar.make(binding.getRoot(), texto, Snackbar.LENGTH_LONG).show();
    }

    private void limparFormulario() {
        carregando = false;
        binding.groupMomento.clearCheck();
        binding.spinnerClima.setSelection(0);
        binding.checkPrioridade0.setChecked(false);
        binding.checkPrioridade1.setChecked(false);
        binding.checkPrioridade2.setChecked(false);
        binding.checkPrioridade3.setChecked(false);
        binding.switchLadoB.setChecked(false);
        binding.progressQuestionario.setVisibility(View.GONE);
        atualizarBotao();
    }
}
