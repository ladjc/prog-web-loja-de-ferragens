package com.ads.lojinha;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class Produto {
    
    private int codigo;
    private String nome;
    private String descritivo;
    private int quantidade;
    private double valor;
    private double promo;
    private int destaque;

}
