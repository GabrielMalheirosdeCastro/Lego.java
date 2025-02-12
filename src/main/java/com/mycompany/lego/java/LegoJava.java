/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lego.java;

/**
 *
 * @author Gabriel.Malheiros
 */
public class LegoJava {
    protected final int categoria;
    protected final int comprimento;
    protected final int largura;
    
    public LegoJava(int categoria, int comprimento, int largura) {
        this.categoria = categoria;
        this.comprimento = comprimento;
        this.largura = largura;
    }
    
    public int getCategoria() {
        return categoria;
    }

    public int getComprimento() {
        return comprimento;
    }

    public int getLargura() {
        return largura;
    }

    // Método para cadastro, pode ser sobrescrito nas subclasses
    public String cadastrar() {
        return "Categoria: " + categoria + ", Comprimento: " + comprimento + ", Largura: " + largura;
    }
}