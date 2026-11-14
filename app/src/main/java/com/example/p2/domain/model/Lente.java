package com.example.p2.domain.model;

import com.google.gson.annotations.SerializedName;

public enum Lente {
    @SerializedName("ROTEIRO_DE_VIRADA")
    ROTEIRO_DE_VIRADA,
    @SerializedName("PLANO_SEQUENCIA_AFETIVO")
    PLANO_SEQUENCIA_AFETIVO,
    @SerializedName("CORTE_SECO_ADRENALINA")
    CORTE_SECO_ADRENALINA,
    @SerializedName("FORA_DE_QUADRO")
    FORA_DE_QUADRO
}
