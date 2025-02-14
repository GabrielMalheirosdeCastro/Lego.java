package com.mycompany.lego.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UsoLego {
    private static final List<LegoJava> listaLegos = new ArrayList<>();
    private static final String[] CATEGORIAS = {"Cidade", "Tecnico", "Minecraft", "Star Wars", "Super Herois"};
    private static final String[] CORES = {"Vermelho", "Azul", "Verde", "Amarelo", "Preto", "Branco"};

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int opcao = -1;
            
            while (opcao != 0) {
                exibirMenu();
                opcao = lerInteiro(scanner, "Opção");
                
                switch (opcao) {
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    case 1:
                        adicionarLego(scanner);
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
    }
    
    private static void exibirMenu() {
        System.out.println("\nEscolha uma opção:");
        System.out.println("1 - Adicionar Lego");
        System.out.println("2 - Listar Legos");
        System.out.println("3 - Ordenar Legos");
        System.out.println("0 - Sair");
    }

    private static void adicionarLego(Scanner scanner) {
        try {
            // Primeiro pede e valida a categoria
            System.out.println("\nCategorias disponíveis:");
            for (int i = 0; i < CATEGORIAS.length; i++) {
                System.out.println((i + 1) + " - " + CATEGORIAS[i]);
            }
            System.out.print("Escolha a categoria (1-" + CATEGORIAS.length + "): ");
            int categoriaIndex = lerInteiroEntre(scanner, "categoria", 1, CATEGORIAS.length) - 1;
            String categoria = CATEGORIAS[categoriaIndex];

            // Verifica duplicidade antes de continuar
            if (verificarDuplicidade(categoria)) {
                System.out.println("Erro: Já existe um Lego cadastrado para a categoria " + categoria);
                return;
            }

            // Pede o tipo de Lego
            System.out.print("\nDigite o tipo de Lego (1 para Grande, 2 para Pequeno): ");
            int tipo = lerInteiroEntre(scanner, "tipo", 1, 2);

            // Pede as dimensões
            System.out.print("Digite o comprimento (deve ser positivo): ");
            int comprimento = lerInteiroPositivo(scanner, "comprimento");

            System.out.print("Digite a largura (deve ser positiva): ");
            int largura = lerInteiroPositivo(scanner, "largura");

            // Pede a quantidade de cores
            System.out.println("\nCores disponíveis:");
            for (int i = 0; i < CORES.length; i++) {
                System.out.println((i + 1) + " - " + CORES[i]);
            }
            System.out.print("Escolha a quantidade de cores (1-" + CORES.length + "): ");
            int quantidadeCores = lerInteiroEntre(scanner, "quantidade de cores", 1, CORES.length);

            switch (tipo) {
                case 1:
                    cadastrarLegoGrande(scanner, categoriaIndex, comprimento, largura, quantidadeCores);
                    break;
                case 2:
                    cadastrarLegoPequeno(scanner, categoriaIndex, comprimento, largura, quantidadeCores);
                    break;
                default:
                    System.out.println("Tipo de Lego inválido.");
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite apenas números válidos!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado ao adicionar Lego: " + e.getMessage());
        }
    }

    private static void cadastrarLegoGrande(Scanner scanner, int categoriaIndex, int comprimento, int largura, int quantidadeCores) {
        System.out.print("Digite o nome do Lego Grande: ");
        String nome = lerString(scanner);
        System.out.print("Digite o número de conectores (deve ser positivo): ");
        int conectores = lerInteiroPositivo(scanner, "número de conectores");
        
        LegoGrande legoGrande = new LegoGrande(categoriaIndex + 1, comprimento, largura, nome, conectores, quantidadeCores);
        adicionarLegoNaLista(legoGrande);
    }

    private static void cadastrarLegoPequeno(Scanner scanner, int categoriaIndex, int comprimento, int largura, int quantidadeCores) {
        System.out.print("Digite o encaixe do Lego Pequeno: ");
        String encaixes = lerString(scanner);
        System.out.print("Digite o número de conectores (deve ser positivo): ");
        int conectoresPequeno = lerInteiroPositivo(scanner, "número de conectores");

        LegoPequeno legoPequeno = new LegoPequeno(categoriaIndex + 1, comprimento, largura, encaixes, conectoresPequeno, quantidadeCores);
        adicionarLegoNaLista(legoPequeno);
    }

    private static int lerInteiro(Scanner scanner, String campo) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número válido para " + campo);
            }
        }
    }

    private static int lerInteiroPositivo(Scanner scanner, String campo) {
        while (true) {
            int valor = lerInteiro(scanner, campo);
            if (valor > 0) {
                return valor;
            }
            System.out.println("Por favor, digite um número positivo para " + campo);
        }
    }

    private static int lerInteiroEntre(Scanner scanner, String campo, int min, int max) {
        while (true) {
            int valor = lerInteiro(scanner, campo);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.printf("Por favor, digite um número entre %d e %d para %s%n", min, max, campo);
        }
    }

    private static String lerString(Scanner scanner) {
        String valor = scanner.nextLine().trim();
        while (valor.isEmpty()) {
            System.out.println("O valor não pode estar vazio. Digite novamente:");
            valor = scanner.nextLine().trim();
        }
        return valor;
    }

    private static void adicionarLegoNaLista(LegoJava lego) {
        listaLegos.add(lego);
        System.out.println("Lego adicionado com sucesso!");
    }

    private static boolean verificarDuplicidade(String categoria) {
        return listaLegos.stream()
                .anyMatch(lego -> CATEGORIAS[lego.getCategoria() - 1].equals(categoria));
    }

    private static void listarLegos() {
        if (listaLegos.isEmpty()) {
            System.out.println("Nenhum Lego cadastrado.");
            return;
        }

        for (LegoJava lego : listaLegos) {
            String categoriaNome = CATEGORIAS[lego.getCategoria() - 1];
            System.out.println("Categoria: " + categoriaNome + " - " + lego.cadastrar());
        }
    }

    private static void ordenarLegos() {
        if (listaLegos.isEmpty()) {
            System.out.println("Não há Legos para ordenar.");
            return;
        }

        listaLegos.sort((a, b) -> a.getCategoria() - b.getCategoria());
        System.out.println("Legos ordenados por categoria.");
    }
}
