package com.mycompany.lego.java;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Classe que gerencia o projeto de Legos e sua persistência
 * @author Gabriel.Malheiros
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
        return categorias.toArray(String[]::new);
    }
    
    public String[] getNomesCores() {
        return cores.keySet().toArray(String[]::new);
    }
    
    public Color[] getCores() {
        return cores.values().toArray(Color[]::new);
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
    
    /**
     * Adiciona um Lego à lista
     * @param lego O Lego a ser adicionado
     */
    public void adicionarLego(LegoJava lego) {
        listaLegos.add(lego);
    }
    
    /**
     * Edita um Lego existente na lista
     * @param indice Índice do Lego na lista
     * @param novoLego Novo Lego que substituirá o antigo
     * @return true se a edição foi bem sucedida, false caso contrário
     */
    public boolean editarLego(int indice, LegoJava novoLego) {
        if (indice >= 0 && indice < listaLegos.size()) {
            LegoJava legoAntigo = listaLegos.get(indice);
            
            // Verificar duplicidade de categoria (exceto se for a mesma categoria)
            String categoriaNova = categorias.get(novoLego.getCategoria() - 1);
            String categoriaAntiga = categorias.get(legoAntigo.getCategoria() - 1);
            
            if (!categoriaNova.equals(categoriaAntiga) && verificarDuplicidade(categoriaNova)) {
                return false; // Já existe um Lego com esta categoria
            }
            
            listaLegos.set(indice, novoLego);
            return true;
        }
        return false;
    }
    
    /**
     * Remove um Lego da lista
     * @param indice Índice do Lego a ser removido
     * @return true se a remoção foi bem sucedida, false caso contrário
     */
    public boolean removerLego(int indice) {
        if (indice >= 0 && indice < listaLegos.size()) {
            listaLegos.remove(indice);
            return true;
        }
        return false;
    }
    
    /**
     * Obtém um Lego pelo índice
     * @param indice Índice do Lego a ser obtido
     * @return O Lego correspondente ou null se o índice for inválido
     */
    public LegoJava getLego(int indice) {
        if (indice >= 0 && indice < listaLegos.size()) {
            return listaLegos.get(indice);
        }
        return null;
    }
    
    /**
     * Obtém a lista de todos os Legos
     * @return A lista de Legos
     */
    public List<LegoJava> getListaLegos() {
        return listaLegos;
    }
    
    /**
     * Ordena os Legos por categoria
     */
    public void ordenarLegos() {
        if (!listaLegos.isEmpty()) {
            listaLegos.sort((a, b) -> a.getCategoria() - b.getCategoria());
        }
    }
    
    /**
     * Obtém o nome da categoria pelo índice
     * @param index Índice da categoria (base 1)
     * @return Nome da categoria
     */
    public String getCategoriaNome(int index) {
        return categorias.get(index - 1);
    }
    
    /**
     * Verifica se a lista de Legos está vazia
     * @return true se a lista estiver vazia, false caso contrário
     */
    public boolean listaVazia() {
        return listaLegos.isEmpty();
    }
    
    // Métodos para gerenciar categorias
    /**
     * Adiciona uma nova categoria
     * @param nome Nome da categoria
     */
    public void adicionarCategoria(String nome) {
        if (nome != null && !nome.trim().isEmpty() && !categorias.contains(nome.trim())) {
            categorias.add(nome.trim());
        }
    }
    
    /**
     * Remove uma categoria existente
     * @param nome Nome da categoria a ser removida
     * @return true se a remoção foi bem sucedida, false caso contrário
     */
    public boolean removerCategoria(String nome) {
        // Verifica se a categoria está em uso
        boolean categoriaEmUso = listaLegos.stream()
                .anyMatch(lego -> categorias.get(lego.getCategoria() - 1).equals(nome));
                
        if (!categoriaEmUso) {
            return categorias.remove(nome);
        }
        return false;
    }
    
    /**
     * Edita o nome de uma categoria
     * @param nomeAntigo Nome atual da categoria
     * @param nomeNovo Novo nome para a categoria
     * @return true se a edição foi bem sucedida, false caso contrário
     */
    public boolean editarCategoria(String nomeAntigo, String nomeNovo) {
        int index = categorias.indexOf(nomeAntigo);
        if (index != -1 && !categorias.contains(nomeNovo) && nomeNovo != null && !nomeNovo.trim().isEmpty()) {
            categorias.set(index, nomeNovo.trim());
            return true;
        }
        return false;
    }
    
    // Métodos para gerenciar cores
    /**
     * Adiciona uma nova cor
     * @param nome Nome da cor
     * @param cor Objeto Color correspondente
     */
    public void adicionarCor(String nome, Color cor) {
        if (nome != null && !nome.trim().isEmpty() && !cores.containsKey(nome.trim())) {
            cores.put(nome.trim(), cor);
        }
    }
    
    /**
     * Remove uma cor existente
     * @param nome Nome da cor a ser removida
     * @return true se a remoção foi bem sucedida, false caso contrário
     */
    public boolean removerCor(String nome) {
        // Verifica se a cor está em uso
        boolean corEmUso = listaLegos.stream()
                .anyMatch(lego -> {
                    if (lego instanceof LegoGrande legoGrande) {
                        return legoGrande.getCorNome() != null && 
                               legoGrande.getCorNome().equals(nome);
                    } else if (lego instanceof LegoPequeno legoPequeno) {
                        return legoPequeno.getCorNome() != null && 
                               legoPequeno.getCorNome().equals(nome);
                    }
                    return false;
                });
                
        if (!corEmUso) {
            return cores.remove(nome) != null;
        }
        return false;
    }
    
    /**
     * Edita uma cor existente
     * @param nomeAntigo Nome atual da cor
     * @param nomeNovo Novo nome para a cor
     * @param novaCor Novo objeto Color
     * @return true se a edição foi bem sucedida, false caso contrário
     */
    public boolean editarCor(String nomeAntigo, String nomeNovo, Color novaCor) {
        if (!cores.containsKey(nomeAntigo)) {
            return false;
        }
        
        if (nomeNovo != null && !nomeNovo.trim().isEmpty() && 
            (nomeAntigo.equals(nomeNovo) || !cores.containsKey(nomeNovo))) {
            
            // Verifica se a cor está em uso
            boolean corEmUso = listaLegos.stream()
                .anyMatch(lego -> {
                    if (lego instanceof LegoGrande legoGrande) {
                        return legoGrande.getCorNome() != null && 
                               legoGrande.getCorNome().equals(nomeAntigo);
                    } else if (lego instanceof LegoPequeno legoPequeno) {
                        return legoPequeno.getCorNome() != null && 
                               legoPequeno.getCorNome().equals(nomeAntigo);
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
    
    /**
     * Atualiza a cor em todos os Legos que a utilizam
     * @param nomeCor Nome da cor a ser atualizada
     * @param novaCor Nova cor
     */
    private void atualizarCorEmLegos(String nomeCor, Color novaCor) {
        for (LegoJava lego : listaLegos) {
            if (lego instanceof LegoGrande legoGrande) {
                if (nomeCor.equals(legoGrande.getCorNome())) {
                    legoGrande.setCor(novaCor);
                }
            } else if (lego instanceof LegoPequeno legoPequeno) {
                if (nomeCor.equals(legoPequeno.getCorNome())) {
                    legoPequeno.setCor(novaCor);
                }
            }
        }
    }

    /**
     * Salva os dados do projeto em arquivo
     */
    public void salvarDados() {
        try {
            // Cria um objeto Gson com adaptador personalizado para Color
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Color.class, new ColorAdapter());
            gsonBuilder.setPrettyPrinting(); // Para melhor legibilidade do JSON
            Gson gson = gsonBuilder.create();
            
            // Cria um objeto para armazenar todos os dados
            ProjetoDados dados = new ProjetoDados();
            dados.categorias = this.categorias;
            dados.coresNomes = new ArrayList<>(cores.keySet());
            
            // Converte objetos Color em arrays RGB
            dados.coresRGB = cores.values().stream()
                .map(cor -> new int[]{cor.getRed(), cor.getGreen(), cor.getBlue(), cor.getAlpha()})
                .collect(Collectors.toList());
            
            // Adiciona legos
            for (LegoJava lego : listaLegos) {
                if (lego instanceof LegoGrande legoGrande) {
                    dados.legosGrandes.add(new LegoGrandeDado(
                        legoGrande.getCategoria(),
                        legoGrande.getComprimento(),
                        legoGrande.getLargura(),
                        legoGrande.getNome(),
                        legoGrande.getConectores(),
                        legoGrande.getCorNome()
                    ));
                } else if (lego instanceof LegoPequeno legoPequeno) {
                    dados.legosPequenos.add(new LegoPequenoDado(
                        legoPequeno.getCategoria(),
                        legoPequeno.getComprimento(),
                        legoPequeno.getLargura(),
                        legoPequeno.getEncaixes(),
                        legoPequeno.getConectores(),
                        legoPequeno.getCorNome()
                    ));
                }
            }
            
            // Escreve o JSON no arquivo
            try (FileWriter writer = new FileWriter(ARQUIVO_JSON)) {
                gson.toJson(dados, writer);
                System.out.println("Dados salvos com sucesso!");
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carrega os dados do projeto do arquivo
     * @return O projeto carregado ou um novo projeto
     */
    public static legoProjeto carregarDados() {
        File arquivo = new File(ARQUIVO_JSON);
        
        if (arquivo.exists()) {
            try {
                // Cria um objeto Gson com adaptador personalizado para Color
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(Color.class, new ColorAdapter());
                Gson gson = gsonBuilder.create();
                
                // Lê o JSON do arquivo
                try (FileReader reader = new FileReader(arquivo)) {
                    ProjetoDados dados = gson.fromJson(reader, ProjetoDados.class);
                    
                    // Se os dados são inválidos, cria um novo projeto
                    if (dados == null) {
                        System.err.println("Arquivo de dados inválido. Criando novo projeto.");
                        return new legoProjeto();
                    }
                    
                    // Cria um novo projeto
                    legoProjeto projeto = new legoProjeto();
                    
                    // Carrega as categorias
                    if (dados.categorias != null) {
                        projeto.categorias = new ArrayList<>(dados.categorias);
                    } else {
                        projeto.categorias = new ArrayList<>();
                    }
                    
                    // Carrega as cores
                    projeto.cores = new HashMap<>();
                    
                    if (dados.coresNomes != null && dados.coresRGB != null && 
                        dados.coresNomes.size() == dados.coresRGB.size()) {
                        
                        for (int i = 0; i < dados.coresNomes.size(); i++) {
                            String nome = dados.coresNomes.get(i);
                            int[] rgb = dados.coresRGB.get(i);
                            
                            if (rgb.length >= 3) {
                                int alpha = rgb.length >= 4 ? rgb[3] : 255;
                                Color cor = new Color(rgb[0], rgb[1], rgb[2], alpha);
                                projeto.cores.put(nome, cor);
                            }
                        }
                    }
                    
                    // Se não houver cores, carrega as cores padrão
                    if (projeto.cores.isEmpty()) {
                        for (int i = 0; i < NOMES_CORES_PADRAO.length; i++) {
                            projeto.cores.put(NOMES_CORES_PADRAO[i], CORES_PADRAO[i]);
                        }
                    }
                    
                    // Carrega os Legos Grandes
                    if (dados.legosGrandes != null) {
                        for (LegoGrandeDado dado : dados.legosGrandes) {
                            Color cor = projeto.getCorPorNome(dado.corNome);
                            if (cor != null) {
                                LegoGrande legoGrande = new LegoGrande(
                                    dado.categoria,
                                    dado.comprimento,
                                    dado.largura,
                                    dado.nome,
                                    dado.conectores,
                                    dado.corNome,
                                    cor
                                );
                                projeto.listaLegos.add(legoGrande);
                            }
                        }
                    }
                    
                    // Carrega os Legos Pequenos
                    if (dados.legosPequenos != null) {
                        for (LegoPequenoDado dado : dados.legosPequenos) {
                            Color cor = projeto.getCorPorNome(dado.corNome);
                            if (cor != null) {
                                LegoPequeno legoPequeno = new LegoPequeno(
                                    dado.categoria,
                                    dado.comprimento,
                                    dado.largura,
                                    dado.encaixes,
                                    dado.conectores,
                                    dado.corNome,
                                    cor
                                );
                                projeto.listaLegos.add(legoPequeno);
                            }
                        }
                    }
                    
                    System.out.println("Dados carregados com sucesso!");
                    return projeto;
                }
            } catch (IOException e) {
                System.err.println("Erro ao carregar dados: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Erro ao processar dados: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // Se o arquivo não existe ou houve erro, retorna um novo projeto
        return new legoProjeto();
    }
    
    /**
     * Classe auxiliar para armazenamento de dados em JSON
     */
    private static class ProjetoDados {
        List<String> categorias = new ArrayList<>();
        List<String> coresNomes = new ArrayList<>();
        List<int[]> coresRGB = new ArrayList<>();
        List<LegoGrandeDado> legosGrandes = new ArrayList<>();
        List<LegoPequenoDado> legosPequenos = new ArrayList<>();
    }
    
    /**
     * Classe auxiliar para armazenamento de dados de Lego Grande em JSON
     */
    private static class LegoGrandeDado {
        int categoria;
        int comprimento;
        int largura;
        String nome;
        int conectores;
        String corNome;
        
        public LegoGrandeDado(int categoria, int comprimento, int largura, String nome, int conectores, String corNome) {
            this.categoria = categoria;
            this.comprimento = comprimento;
            this.largura = largura;
            this.nome = nome;
            this.conectores = conectores;
            this.corNome = corNome;
        }
    }
    
    /**
     * Classe auxiliar para armazenamento de dados de Lego Pequeno em JSON
     */
    private static class LegoPequenoDado {
        int categoria;
        int comprimento;
        int largura;
        String encaixes;
        int conectores;
        String corNome;
        
        public LegoPequenoDado(int categoria, int comprimento, int largura, String encaixes, int conectores, String corNome) {
            this.categoria = categoria;
            this.comprimento = comprimento;
            this.largura = largura;
            this.encaixes = encaixes;
            this.conectores = conectores;
            this.corNome = corNome;
        }
    }
}
