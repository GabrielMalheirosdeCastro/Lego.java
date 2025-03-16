/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lego.java;

import java.awt.Color;

/**
 *
 * @author Gabriel.Malheiros
 */

public class LegoGrande extends LegoJava {
    protected String nome;
    protected int conectores;
    protected String corNome; // Nome da cor selecionada
    protected Color cor;     // Objeto Color para representação visual

    // Construtor atualizado para receber nome da cor e objeto Color
    public LegoGrande(int categoria, int comprimento, int largura, String nome, int conectores, String corNome, Color cor) {
        super(categoria, comprimento, largura);
        this.nome = nome;
        this.conectores = conectores;
        this.corNome = corNome;
        this.cor = cor;
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

    public String getCorNome() {
        return corNome;
    }

    public void setCorNome(String corNome) {
        this.corNome = corNome;
    }

    public Color getCor() {
        return cor;
    }

    public void setCor(Color cor) {
        this.cor = cor;
    }

    // Sobrescrevendo o método cadastrar
    @Override
    public String cadastrar() {
        return super.cadastrar() + ", Nome: " + nome + ", Conectores: " + conectores + ", Cor: " + corNome;
    }
}
