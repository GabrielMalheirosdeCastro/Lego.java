package com.mycompany.lego.java;

import java.util.Scanner;

public class legoTela {
    private final legoProjeto projeto;
    private final Scanner scanner;
    
    public legoTela() {
        this.projeto = new legoProjeto();
        this.scanner = new Scanner(System.in);
    }
    
    public void iniciar() {
        int opcao = -1;
        while (opcao != 0) {
            exibirMenu();
            opcao = lerInteiro("Opção");
            
            switch (opcao) {
                case 0:
                    System.out.println("Saindo...");
                    break;
                case 1:
                    adicionarLego();
                    break;
                case 2:
                    listarLegos();
                    break;
                case 3:
                    ordenarLegos();
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
                    break;
            }
        }
    }
    
    private void exibirMenu() {
        System.out.println("\nEscolha uma opção:");
        System.out.println("1 - Adicionar Lego");
        System.out.println("2 - Listar Legos");
        System.out.println("3 - Ordenar Legos");
        System.out.println("0 - Sair");
    }
    
    private void adicionarLego() {
        try {
            String[] categorias = projeto.getCategorias();
            System.out.println("\nCategorias disponíveis:");
            for (int i = 0; i < categorias.length; i++) {
                System.out.println((i + 1) + " - " + categorias[i]);
            }
            
            System.out.print("Escolha a categoria (1-" + categorias.length + "): ");
            int categoriaIndex = lerInteiroEntre("categoria", 1, categorias.length) - 1;
            String categoria = categorias[categoriaIndex];

            if (projeto.verificarDuplicidade(categoria)) {
                System.out.println("Erro: Já existe um Lego cadastrado para a categoria " + categoria);
                return;
            }

            System.out.print("\nDigite o tipo de Lego (1 para Grande, 2 para Pequeno): ");
            int tipo = lerInteiroEntre("tipo", 1, 2);

            System.out.print("Digite o comprimento (deve ser positivo): ");
            int comprimento = lerInteiroPositivo("comprimento");

            System.out.print("Digite a largura (deve ser positiva): ");
            int largura = lerInteiroPositivo("largura");

            String[] cores = projeto.getCores();
            System.out.println("\nCores disponíveis:");
            for (int i = 0; i < cores.length; i++) {
                System.out.println((i + 1) + " - " + cores[i]);
            }
            
            System.out.print("Escolha a quantidade de cores (1-" + cores.length + "): ");
            int quantidadeCores = lerInteiroEntre("quantidade de cores", 1, cores.length);

            switch (tipo) {
                case 1:
                    cadastrarLegoGrande(categoriaIndex, comprimento, largura, quantidadeCores);
                    break;
                case 2:
                    cadastrarLegoPequeno(categoriaIndex, comprimento, largura, quantidadeCores);
                    break;
                default:
                    System.out.println("Tipo de Lego inválido.");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Erro ao adicionar Lego: " + e.getMessage());
        }
    }
    
    private void cadastrarLegoGrande(int categoriaIndex, int comprimento, int largura, int quantidadeCores) {
        System.out.print("Digite o nome do Lego Grande: ");
        String nome = lerString();
        System.out.print("Digite o número de conectores (deve ser positivo): ");
        int conectores = lerInteiroPositivo("número de conectores");
        
        LegoGrande legoGrande = new LegoGrande(categoriaIndex + 1, comprimento, largura, nome, conectores, quantidadeCores);
        projeto.adicionarLego(legoGrande);
        System.out.println("Lego Grande adicionado com sucesso!");
    }

    private void cadastrarLegoPequeno(int categoriaIndex, int comprimento, int largura, int quantidadeCores) {
        System.out.print("Digite o encaixe do Lego Pequeno: ");
        String encaixes = lerString();
        System.out.print("Digite o número de conectores (deve ser positivo): ");
        int conectores = lerInteiroPositivo("número de conectores");

        LegoPequeno legoPequeno = new LegoPequeno(categoriaIndex + 1, comprimento, largura, encaixes, conectores, quantidadeCores);
        projeto.adicionarLego(legoPequeno);
        System.out.println("Lego Pequeno adicionado com sucesso!");
    }
    
    private void listarLegos() {
        if (projeto.listaVazia()) {
            System.out.println("Nenhum Lego cadastrado.");
            return;
        }

        for (LegoJava lego : projeto.getListaLegos()) {
            String categoriaNome = projeto.getCategoriaNome(lego.getCategoria());
            System.out.println("Categoria: " + categoriaNome + " - " + lego.cadastrar());
        }
    }
    
    private void ordenarLegos() {
        if (projeto.listaVazia()) {
            System.out.println("Não há Legos para ordenar.");
            return;
        }

        projeto.ordenarLegos();
        System.out.println("Legos ordenados por categoria.");
    }
    
    private int lerInteiro(String campo) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido para " + campo);
            }
        }
    }

    private int lerInteiroPositivo(String campo) {
        while (true) {
            int valor = lerInteiro(campo);
            if (valor > 0) {
                return valor;
            }
            System.out.println("Por favor, digite um número positivo para " + campo);
        }
    }

    private int lerInteiroEntre(String campo, int min, int max) {
        while (true) {
            int valor = lerInteiro(campo);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.printf("Por favor, digite um número entre %d e %d para %s%n", min, max, campo);
        }
    }

    private String lerString() {
        String valor = scanner.nextLine().trim();
        while (valor.isEmpty()) {
            System.out.println("O valor não pode estar vazio. Digite novamente:");
            valor = scanner.nextLine().trim();
        }
        return valor;
    }
}
