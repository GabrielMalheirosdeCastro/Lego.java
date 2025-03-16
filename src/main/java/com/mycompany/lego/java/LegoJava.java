/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lego.java;

/**
 * Classe base para objetos Lego
 * @author Gabriel.Malheiros
 */
public abstract class LegoJava {
    protected int categoria;
    protected int comprimento;
    protected int largura;
    protected int quantidadeDeCores;
    
    /**
     * Construtor da classe base
     * @param categoria Categoria do Lego
     * @param comprimento Comprimento do Lego
     * @param largura Largura do Lego
     */
    public LegoJava(int categoria, int comprimento, int largura) {
        this.categoria = categoria;
        this.comprimento = comprimento;
        this.largura = largura;
        this.quantidadeDeCores = 1; // Valor padrão
    }
    
    public int getCategoria() {
        return categoria;
    }
    
    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
    
    public int getComprimento() {
        return comprimento;
    }
    
    public void setComprimento(int comprimento) {
        this.comprimento = comprimento;
    }
    
    public int getLargura() {
        return largura;
    }
    
    public void setLargura(int largura) {
        this.largura = largura;
    }
    
    public int getQuantidadeDeCores() {
        return quantidadeDeCores;
    }
    
    public void setQuantidadeDeCores(int quantidadeDeCores) {
        this.quantidadeDeCores = quantidadeDeCores;
    }
    
    /**
     * Método base para cadastrar Lego
     * @return String com informações básicas do Lego
     */
    public String cadastrar() {
        return "Categoria: " + categoria + ", Dimensões: " + comprimento + "x" + largura;
    }
}