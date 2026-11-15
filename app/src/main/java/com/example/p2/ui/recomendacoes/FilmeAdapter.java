package com.example.p2.ui.recomendacoes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p2.R;
import com.example.p2.databinding.ItemFilmeBinding;
import com.example.p2.domain.model.Filme;
import com.example.p2.util.LenteIcone;

import java.util.List;

public class FilmeAdapter extends RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder> {
    public interface OnFilmeClickListener {
        void onFilmeClick(Filme filme);
    }

    private final List<Filme> filmes;
    private final OnFilmeClickListener listener;

    public FilmeAdapter(List<Filme> filmes, OnFilmeClickListener listener) {
        this.filmes = filmes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FilmeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFilmeBinding binding = ItemFilmeBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new FilmeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmeViewHolder holder, int position) {
        holder.bind(filmes.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return filmes.size();
    }

    static class FilmeViewHolder extends RecyclerView.ViewHolder {
        private final ItemFilmeBinding binding;

        FilmeViewHolder(ItemFilmeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Filme filme, OnFilmeClickListener listener) {
            binding.imageLenteItem.setImageResource(LenteIcone.drawable(filme.getLenteAlvo()));
            binding.textTituloItem.setText(filme.getTitulo());
            binding.textAnoDiretor.setText(binding.getRoot().getContext()
                    .getString(R.string.filme_ano_diretor, filme.getAno(), filme.getDiretor()));
            binding.textSeloCult.setVisibility(filme.isCult() ? View.VISIBLE : View.GONE);
            if (filme.getMatchPercentual() != null) {
                binding.textMatchItem.setText(binding.getRoot().getContext()
                        .getString(R.string.filme_match, filme.getMatchPercentual()));
            }
            binding.getRoot().setOnClickListener(view -> listener.onFilmeClick(filme));
        }
    }
}
