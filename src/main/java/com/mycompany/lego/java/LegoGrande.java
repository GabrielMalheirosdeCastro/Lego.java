/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lego.java;

/**
 *
 * @author Gabriel.Malheiros
 */

 public class LegoGrande extends LegoJava {
    protected String nome;
    protected int conectores;
    protected int quantidadeDeCores;

    // Construtor que chama o construtor da classe base
    public LegoGrande(int categoria, int comprimento, int largura, String nome, int conectores, int quantidadeDeCores) {
        super(categoria, comprimento, largura);
        this.nome = nome;
        this.conectores = conectores;
        this.quantidadeDeCores = quantidadeDeCores;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
        return super.cadastrar() + ", Nome: " + nome + ", Conectores: " + conectores + ", Quantidade de Cores: " + quantidadeDeCores;
    }
}
