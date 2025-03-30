package com.mycompany.lego.java;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Classe principal que inicia o sistema de cadastro de Legos
 */
public class UsoLego {
    
    /**
     * Método principal 
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        // Tenta aplicar o look and feel do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Em caso de erro, usa o look and feel padrão
            System.out.println("Erro ao definir aparência: " + e.getMessage());
        }
        
        legoProjeto projeto = legoProjeto.carregarDados();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            projeto.salvarDados();
            System.out.println("Dados salvos com sucesso!");
        }));

        // Inicia a interface gráfica na thread de eventos do Swing
        SwingUtilities.invokeLater(() -> {
            legoTela tela = new legoTela(projeto); // Passa o projeto carregado
            tela.iniciar();
        });
    }
}
