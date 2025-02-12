/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lego.java;

/**
 *
 * @author Gabriel.Malheiros
 */
public class LegoPequeno extends LegoJava {
    protected String encaixes;
    protected int conectores;
    protected int quantidadeDeCores;

    // Construtor que chama o construtor da classe base
    public LegoPequeno(int categoria, int comprimento, int largura, String encaixes, int conectores, int quantidadeDeCores) {
        super(categoria, comprimento, largura);
        this.encaixes = encaixes;
        this.conectores = conectores;
        this.quantidadeDeCores = quantidadeDeCores;
    }

    // Getters e Setters
    public String getEncaixes() {
        return encaixes;
    }

    public void setEncaixes(String encaixes) {
        this.encaixes = encaixes;
    }

    public int getConectores() {
        return conectores;
    }

    public void setConectores(int conectores) {
        this.conectores = conectores;
    }

    public int getQuantidadeDeCores() {
        return quantidadeDeCores;
    }

    public void setQuantidadeDeCores(int quantidadeDeCores) {
        this.quantidadeDeCores = quantidadeDeCores;
    }

    // Sobrescrevendo o método cadastrar
    @Override
    public String cadastrar() {
        return super.cadastrar() + ", Encaixes: " + encaixes + ", Conectores: " + conectores + ", Quantidade de Cores: " + quantidadeDeCores;
    }
}
