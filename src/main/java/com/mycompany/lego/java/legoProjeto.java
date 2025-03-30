package com.mycompany.lego.java;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 *
 * @author 
 */
public class legoProjeto {
    private final List<LegoJava> listaLegos = new ArrayList<>(); // Inicialização direta
    private List<String> categorias;
    private Map<String, Color> cores; // Alterado para um mapa que associa nome da cor ao objeto Color
    
    // Cores padrão para inicializar
    private static final String[] NOMES_CORES_PADRAO = {"Vermelho", "Azul", "Verde", "Amarelo", "Preto", "Branco"};
    private static final Color[] CORES_PADRAO = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.BLACK, Color.WHITE};
    
    private static final String ARQUIVO_JSON = "legos.json";

    public legoProjeto() {
        // Inicializa com uma lista vazia de categorias para que o usuário possa cadastrar as suas próprias
        this.categorias = new ArrayList<>();
        
        // Inicializa o mapa de cores com as cores padrão
        this.cores = new HashMap<>();
        for (int i = 0; i < NOMES_CORES_PADRAO.length; i++) {
            cores.put(NOMES_CORES_PADRAO[i], CORES_PADRAO[i]);
        }
    }
    
    public String[] getCategorias() {
        return categorias.toArray(new String[0]);
    }
    
    public String[] getNomesCores() {
        return cores.keySet().toArray(new String[0]);
    }
    
    public Color[] getCores() {
        return cores.values().toArray(new Color[0]);
    }
    
    public Map<String, Color> getMapaCores() {
        return cores;
    }
    
    public Color getCorPorNome(String nome) {
        return cores.get(nome);
    }
    
    public String getNomePorIndice(int indice) {
        String[] nomes = getNomesCores();
        if (indice >= 0 && indice < nomes.length) {
            return nomes[indice];
        }
        return null;
    }
    
    public boolean verificarDuplicidade(String categoria) {
        return listaLegos.stream()
                .anyMatch(lego -> categorias.get(lego.getCategoria() - 1).equals(categoria));
    }
    
    public void adicionarLego(LegoJava lego) {
        listaLegos.add(lego); // Certifique-se de que este método está sendo usado corretamente
    }
    
    public List<LegoJava> getListaLegos() {
        return listaLegos; // Certifique-se de que este método está sendo chamado corretamente
    }
    
    public void ordenarLegos() {
        if (!listaLegos.isEmpty()) {
            listaLegos.sort((a, b) -> a.getCategoria() - b.getCategoria());
        }
    }
    
    public String getCategoriaNome(int index) {
        return categorias.get(index - 1);
    }
    
    public boolean listaVazia() {
        return listaLegos.isEmpty(); // Verifique se este método está sendo chamado corretamente
    }
    
    // Métodos para gerenciar categorias
    public void adicionarCategoria(String nome) {
        if (nome != null && !nome.trim().isEmpty() && !categorias.contains(nome.trim())) {
            categorias.add(nome.trim());
        }
    }
    
    public boolean removerCategoria(String nome) {
        // Verifica se a categoria está em uso
        boolean categoriaEmUso = listaLegos.stream()
                .anyMatch(lego -> categorias.get(lego.getCategoria() - 1).equals(nome));
                
        if (!categoriaEmUso) {
            return categorias.remove(nome);
        }
        return false;
    }
    
    public boolean editarCategoria(String nomeAntigo, String nomeNovo) {
        int index = categorias.indexOf(nomeAntigo);
        if (index != -1 && !categorias.contains(nomeNovo) && nomeNovo != null && !nomeNovo.trim().isEmpty()) {
            categorias.set(index, nomeNovo.trim());
            return true;
        }
        return false;
    }
    
    // Métodos atualizados para gerenciar cores
    public void adicionarCor(String nome, Color cor) {
        if (nome != null && !nome.trim().isEmpty() && !cores.containsKey(nome.trim())) {
            cores.put(nome.trim(), cor);
        }
    }
    
    public boolean removerCor(String nome) {
        // Verifica se a cor está em uso
        boolean corEmUso = listaLegos.stream()
                .anyMatch(lego -> {
                    if (lego instanceof LegoGrande) {
                        return ((LegoGrande) lego).getCorNome() != null && 
                               ((LegoGrande) lego).getCorNome().equals(nome);
                    } else if (lego instanceof LegoPequeno) {
                        return ((LegoPequeno) lego).getCorNome() != null && 
                               ((LegoPequeno) lego).getCorNome().equals(nome);
                    }
                    return false;
                });
                
        if (!corEmUso) {
            return cores.remove(nome) != null;
        }
        return false;
    }
    
    public boolean editarCor(String nomeAntigo, String nomeNovo, Color novaCor) {
        if (!cores.containsKey(nomeAntigo)) {
            return false;
        }
        
        if (nomeNovo != null && !nomeNovo.trim().isEmpty() && 
            (nomeAntigo.equals(nomeNovo) || !cores.containsKey(nomeNovo))) {
            
            // Verifica se a cor está em uso
            boolean corEmUso = listaLegos.stream()
                .anyMatch(lego -> {
                    if (lego instanceof LegoGrande) {
                        return ((LegoGrande) lego).getCorNome() != null && 
                               ((LegoGrande) lego).getCorNome().equals(nomeAntigo);
                    } else if (lego instanceof LegoPequeno) {
                        return ((LegoPequeno) lego).getCorNome() != null && 
                               ((LegoPequeno) lego).getCorNome().equals(nomeAntigo);
                    }
                    return false;
                });
                
            if (corEmUso) {
                // Se a cor está em uso, atualiza apenas a cor, mantendo o nome
                if (nomeAntigo.equals(nomeNovo)) {
                    cores.put(nomeAntigo, novaCor);
                    atualizarCorEmLegos(nomeAntigo, novaCor);
                    return true;
                }
                return false;
            } else {
                // Se a cor não está em uso, remove a antiga e adiciona a nova
                cores.remove(nomeAntigo);
                cores.put(nomeNovo.trim(), novaCor);
                return true;
            }
        }
        return false;
    }
    
    // Método para atualizar a cor em legos existentes (quando a cor é editada)
    private void atualizarCorEmLegos(String nomeCor, Color novaCor) {
        for (LegoJava lego : listaLegos) {
            if (lego instanceof LegoGrande) {
                LegoGrande legoGrande = (LegoGrande) lego;
                if (nomeCor.equals(legoGrande.getCorNome())) {
                    legoGrande.setCor(novaCor);
                }
            } else if (lego instanceof LegoPequeno) {
                LegoPequeno legoPequeno = (LegoPequeno) lego;
                if (nomeCor.equals(legoPequeno.getCorNome())) {
                    legoPequeno.setCor(novaCor);
                }
            }
        }
    }

    public void salvarDados() {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(ARQUIVO_JSON)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    public static legoProjeto carregarDados() {
        Gson gson = new Gson();
        File arquivo = new File(ARQUIVO_JSON);
        if (arquivo.exists()) {
            try (FileReader reader = new FileReader(arquivo)) {
                Type tipoProjeto = new TypeToken<legoProjeto>() {}.getType();
                legoProjeto projeto = gson.fromJson(reader, tipoProjeto);

                // Inicializa atributos que podem estar null
                if (projeto.listaLegos == null) {
                    projeto.listaLegos = new ArrayList<>();
                }
                if (projeto.categorias == null) {
                    projeto.categorias = new ArrayList<>();
                }
                if (projeto.cores == null) {
                    projeto.cores = new HashMap<>();
                }

                return projeto;
            } catch (IOException e) {
                System.err.println("Erro ao carregar dados: " + e.getMessage());
            }
        }
        return new legoProjeto(); // Retorna um novo projeto caso o arquivo não exista ou ocorra erro
    }
}
