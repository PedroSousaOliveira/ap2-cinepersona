package com.example.p2.ui.perfil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p2.R;
import com.example.p2.databinding.ItemAvaliacaoBinding;
import com.example.p2.domain.model.Avaliacao;
import com.example.p2.util.LenteIcone;

import java.util.List;

public class AvaliacaoAdapter extends RecyclerView.Adapter<AvaliacaoAdapter.AvaliacaoViewHolder> {
    public interface OnAvaliacaoAcaoListener {
        void onEditar(Avaliacao avaliacao);

        void onExcluir(Avaliacao avaliacao);
    }

    private final List<Avaliacao> avaliacoes;
    private final OnAvaliacaoAcaoListener listener;

    public AvaliacaoAdapter(List<Avaliacao> avaliacoes, OnAvaliacaoAcaoListener listener) {
        this.avaliacoes = avaliacoes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AvaliacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAvaliacaoBinding binding = ItemAvaliacaoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new AvaliacaoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AvaliacaoViewHolder holder, int position) {
        holder.bind(avaliacoes.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return avaliacoes.size();
    }

    static class AvaliacaoViewHolder extends RecyclerView.ViewHolder {
        private final ItemAvaliacaoBinding binding;

        AvaliacaoViewHolder(ItemAvaliacaoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Avaliacao avaliacao, OnAvaliacaoAcaoListener listener) {
            binding.imageLenteAvaliacao.setImageResource(
                    LenteIcone.drawable(avaliacao.getFilme().getLenteAlvo()));
            binding.textTituloAvaliacao.setText(avaliacao.getFilme().getTitulo());
            binding.textAnoDiretorAvaliacao.setText(binding.getRoot().getContext()
                    .getString(R.string.filme_ano_diretor,
                            avaliacao.getFilme().getAno(), avaliacao.getFilme().getDiretor()));
            binding.textNota.setText(binding.getRoot().getContext()
                    .getString(R.string.perfil_nota, avaliacao.getNota()));
            String comentario = avaliacao.getComentario();
            if (comentario == null || comentario.trim().isEmpty()) {
                binding.textComentario.setVisibility(View.GONE);
            } else {
                binding.textComentario.setVisibility(View.VISIBLE);
                binding.textComentario.setText(comentario);
            }
            binding.buttonEditar.setOnClickListener(view -> listener.onEditar(avaliacao));
            binding.buttonExcluir.setOnClickListener(view -> listener.onExcluir(avaliacao));
        }
    }
}
