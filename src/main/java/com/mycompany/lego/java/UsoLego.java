package com.mycompany.lego.java;

import java.util.Scanner;

public class UsoLego {
    private static final LegoJava[] listaLegos = new LegoJava[10];

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int opcao = -1;
            
            while (opcao != 0) {
                exibirMenu();
                opcao = lerInteiro(scanner, "Opção");
                
                switch (opcao) {
                    case 0 -> System.out.println("Saindo...");
                    case 1 -> adicionarLego(scanner);
                    case 2 -> listarLegos();
                    case 3 -> ordenarLegos();
                    default -> System.out.println("Opção inválida! Tente novamente.");
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
            System.out.print("\nDigite o tipo de Lego (1 para Grande, 2 para Pequeno): ");
            int tipo = lerInteiro(scanner, "tipo");

            System.out.print("Digite a categoria: ");
            int categoria = lerInteiroPositivo(scanner, "categoria");

            System.out.print("Digite o comprimento: ");
            int comprimento = lerInteiroPositivo(scanner, "comprimento");

            System.out.print("Digite a largura: ");
            int largura = lerInteiroPositivo(scanner, "largura");

            if (verificarDuplicidade(categoria)) {
                System.out.println("Erro: Já existe um Lego com esta categoria.");
                return;
            }

            switch (tipo) {
                case 1 -> {
                    System.out.print("Digite o nome do Lego Grande: ");
                    String nome = lerString(scanner);
                    System.out.print("Digite o número de conectores: ");
                    int conectores = lerInteiroPositivo(scanner, "número de conectores");
                    System.out.print("Digite a quantidade de cores: ");
                    int quantidadeDeCores = lerInteiroPositivo(scanner, "quantidade de cores");
                    
                    LegoGrande legoGrande = new LegoGrande(categoria, comprimento, largura, nome, conectores, quantidadeDeCores);
                    adicionarLegoNaLista(legoGrande);
                }
                case 2 -> {
                    System.out.print("Digite o encaixe do Lego Pequeno: ");
                    String encaixes = lerString(scanner);
                    System.out.print("Digite o número de conectores: ");
                    int conectores = lerInteiroPositivo(scanner, "número de conectores");
                    System.out.print("Digite a quantidade de cores: ");
                    int quantidadeDeCores = lerInteiroPositivo(scanner, "quantidade de cores");

                    LegoPequeno legoPequeno = new LegoPequeno(categoria, comprimento, largura, encaixes, conectores, quantidadeDeCores);
                    adicionarLegoNaLista(legoPequeno);
                }
                default -> System.out.println("Tipo de Lego inválido.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao adicionar Lego: " + e.getMessage());
        }
    }

    private static boolean verificarDuplicidade(int categoria) {
        for (LegoJava lego : listaLegos) {
            if (lego != null && lego.getCategoria() == categoria) {
                return true;
            }
        }
        return false;
    }

    private static void adicionarLegoNaLista(LegoJava lego) {
        for (int i = 0; i < listaLegos.length; i++) {
            if (listaLegos[i] == null) {
                listaLegos[i] = lego;
                System.out.println("Lego adicionado com sucesso!");
                return;
            }
        }
        System.out.println("Erro: Não há espaço para adicionar mais Legos.");
    }

    private static void listarLegos() {
        boolean vazio = true;
        for (LegoJava lego : listaLegos) {
            if (lego != null) {
                System.out.println(lego.cadastrar());
                vazio = false;
            }
        }
        if (vazio) {
            System.out.println("Nenhum Lego cadastrado.");
        }
    }

    private static void ordenarLegos() {
        // Conta quantos Legos existem
        int count = 0;
        for (LegoJava lego : listaLegos) {
            if (lego != null) count++;
        }
        
        if (count == 0) {
            System.out.println("Não há Legos para ordenar.");
            return;
        }

        // Ordenação apenas dos elementos não nulos
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (listaLegos[j] != null && listaLegos[j + 1] != null && 
                    listaLegos[j].getCategoria() > listaLegos[j + 1].getCategoria()) {
                    LegoJava temp = listaLegos[j];
                    listaLegos[j] = listaLegos[j + 1];
                    listaLegos[j + 1] = temp;
                }
            }
        }
        System.out.println("Legos ordenados por categoria.");
    }

    // Métodos auxiliares para validação de entrada
    private static int lerInteiro(Scanner scanner, String campo) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Por favor, digite um número válido para " + campo);
                scanner.nextLine(); // Limpa o buffer
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

    private static String lerString(Scanner scanner) {
        scanner.nextLine(); // Limpa o buffer
        String valor = scanner.nextLine().trim();
        while (valor.isEmpty()) {
            System.out.println("O valor não pode estar vazio. Digite novamente:");
            valor = scanner.nextLine().trim();
        }
        return valor;
    }
}
