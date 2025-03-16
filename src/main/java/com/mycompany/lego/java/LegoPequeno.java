package com.mycompany.lego.java;

import java.awt.Color;

/**
 *
 * @author Gabriel.Malheiros
 */
public class LegoPequeno extends LegoJava {
    protected String encaixes;
    protected int conectores;
    protected String corNome; // Nome da cor selecionada
    protected Color cor;      // Objeto Color para representação visual

    // Construtor atualizado para receber nome da cor e objeto Color
    public LegoPequeno(int categoria, int comprimento, int largura, String encaixes, int conectores, String corNome, Color cor) {
        super(categoria, comprimento, largura);
        this.encaixes = encaixes;
        this.conectores = conectores;
        this.corNome = corNome;
        this.cor = cor;
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
        return super.cadastrar() + ", Encaixes: " + encaixes + ", Conectores: " + conectores + ", Cor: " + corNome;
    }
}
