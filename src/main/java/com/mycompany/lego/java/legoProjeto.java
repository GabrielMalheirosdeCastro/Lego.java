/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lego.java;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel.Malheiros
 */
public class legoProjeto {
    private final List<LegoJava> listaLegos;
    private static final String[] CATEGORIAS = {"Cidade", "Tecnico", "Minecraft", "Star Wars", "Super Herois"};
    private static final String[] CORES = {"Vermelho", "Azul", "Verde", "Amarelo", "Preto", "Branco"};
    
    public legoProjeto() {
        this.listaLegos = new ArrayList<>();
    }
    
    public String[] getCategorias() {
        return CATEGORIAS;
    }
    
    public String[] getCores() {
        return CORES;
    }
    
    public boolean verificarDuplicidade(String categoria) {
        return listaLegos.stream()
                .anyMatch(lego -> CATEGORIAS[lego.getCategoria() - 1].equals(categoria));
    }
    
    public void adicionarLego(LegoJava lego) {
        listaLegos.add(lego);
    }
    
    public List<LegoJava> getListaLegos() {
        return listaLegos;
    }
    
    public void ordenarLegos() {
        if (!listaLegos.isEmpty()) {
            listaLegos.sort((a, b) -> a.getCategoria() - b.getCategoria());
        }
    }
    
    public String getCategoriaNome(int index) {
        return CATEGORIAS[index - 1];
    }
    
    public boolean listaVazia() {
        return listaLegos.isEmpty();
    }
}
