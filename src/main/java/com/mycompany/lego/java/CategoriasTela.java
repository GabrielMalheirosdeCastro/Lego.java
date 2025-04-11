package com.mycompany.lego.java;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;

/**
 * Tela para gerenciamento de categorias de Lego
 * @author Gabriel.Malheiros
 */
public class CategoriasTela extends JDialog {
    
    private final legoProjeto projeto;
    private final legoTela telaPrincipal;
    
    private JList<String> listaCategorias;
    private DefaultListModel<String> modeloLista;
    private JTextField txtNomeCategoria;
    private JButton btnAdicionar;
    private JButton btnEditar;
    private JButton btnRemover;
    private JButton btnFechar;
    private JButton btnCancelar;
    private JLabel lblStatus;
    
    private boolean obrigatorio;
    private Pattern padraoNome;
    private boolean usuarioCancelou = false;
    
    /**
     * Construtor da tela de categorias
     * @param owner Frame pai
     * @param projeto Referência para o projeto de legos
     * @param telaPrincipal Referência para a tela principal
     */
    public CategoriasTela(JFrame owner, legoProjeto projeto, legoTela telaPrincipal) {
        this(owner, projeto, telaPrincipal, false);
    }
    
    /**
     * Construtor da tela de categorias com opção de torná-la obrigatória
     * @param owner Frame pai
     * @param projeto Referência para o projeto de legos
     * @param telaPrincipal Referência para a tela principal
     * @param obrigatorio Se verdadeiro, o usuário deve adicionar pelo menos uma categoria
     */
    public CategoriasTela(JFrame owner, legoProjeto projeto, legoTela telaPrincipal, boolean obrigatorio) {
        super(owner, "Gerenciamento de Categorias", true);
        this.projeto = projeto;
        this.telaPrincipal = telaPrincipal;
        this.obrigatorio = obrigatorio;
        
        // Padrão menos restritivo: aceita quaisquer caracteres, incluindo acentos, 
        // apenas limitado pelo tamanho mínimo e máximo
        padraoNome = Pattern.compile("^.{3,30}$");
        
        inicializarComponentes();
        carregarCategorias();
        configurarEventos();
        
        setSize(400, 450);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(obrigatorio ? JDialog.DO_NOTHING_ON_CLOSE : JDialog.DISPOSE_ON_CLOSE);
        
        // Se for obrigatório, adiciona evento de fechamento personalizado
        if (obrigatorio) {
            this.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    verificarFechamento();
                }
            });
        }
    }
    
    /**
     * Inicializa os componentes da interface
     */
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        
        // Painel para lista de categorias
        JPanel painelLista = new JPanel(new BorderLayout(5, 5));
        painelLista.setBorder(BorderFactory.createTitledBorder("Categorias Existentes"));
        
        modeloLista = new DefaultListModel<>();
        listaCategorias = new JList<>(modeloLista);
        listaCategorias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollLista = new JScrollPane(listaCategorias);
        scrollLista.setPreferredSize(new Dimension(350, 200));
        painelLista.add(scrollLista, BorderLayout.CENTER);
        
        // Painel para formulário de cadastro
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Cadastro de Categoria"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        painelFormulario.add(new JLabel("Nome da Categoria:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.9;
        txtNomeCategoria = new JTextField(20);
        painelFormulario.add(txtNomeCategoria, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        lblStatus = new JLabel(" ");
        lblStatus.setForeground(java.awt.Color.RED);
        painelFormulario.add(lblStatus, gbc);
        
        // Adiciona instruções se for obrigatório
        if (obrigatorio) {
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            JLabel lblObrigatorio = new JLabel("É necessário cadastrar pelo menos uma categoria para continuar.");
            lblObrigatorio.setForeground(new java.awt.Color(0, 0, 150));
            painelFormulario.add(lblObrigatorio, gbc);
        }
        
        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdicionar = new JButton("Adicionar");
        btnEditar = new JButton("Editar");
        btnRemover = new JButton("Remover");
        btnFechar = new JButton("Concluir");
        
        // Apenas adiciona o botão cancelar se for um diálogo obrigatório
        if (obrigatorio) {
            btnCancelar = new JButton("Cancelar");
            painelBotoes.add(btnCancelar);
        }
        
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnFechar);
        
        // Montagem da tela
        add(painelLista, BorderLayout.NORTH);
        add(painelFormulario, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
        
        // Estado inicial dos botões
        btnAdicionar.setEnabled(false);
        btnEditar.setEnabled(false);
        btnRemover.setEnabled(false);
    }
    
    /**
     * Carrega as categorias existentes na lista
     */
    private void carregarCategorias() {
        modeloLista.clear();
        String[] categorias = projeto.getCategorias();
        Arrays.sort(categorias); // Ordena as categorias em ordem alfabética
        for (String categoria : categorias) {
            modeloLista.addElement(categoria);
        }
    }
    
    /**
     * Configura os eventos dos componentes
     */
    private void configurarEventos() {
        // Evento de seleção na lista
        listaCategorias.addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                boolean temSelecao = !listaCategorias.isSelectionEmpty();
                btnEditar.setEnabled(temSelecao);
                btnRemover.setEnabled(temSelecao);
                
                if (temSelecao) {
                    txtNomeCategoria.setText(listaCategorias.getSelectedValue());
                    validarNome(txtNomeCategoria.getText());
                }
            }
        });
        
        // Evento de digitação no campo de texto
        txtNomeCategoria.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validarNome(txtNomeCategoria.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validarNome(txtNomeCategoria.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validarNome(txtNomeCategoria.getText());
            }
        });
        
        // Evento do botão Adicionar
        btnAdicionar.addActionListener(e -> {
            // Referência explícita à classe para evitar ambiguidade no 'this'
            CategoriasTela dialogoAtual = CategoriasTela.this;
            
            String nomeCategoria = txtNomeCategoria.getText().trim();
            
            if (!validarNome(nomeCategoria)) {
                return; // Mensagem já exibida pela validação
            }
            
            projeto.adicionarCategoria(nomeCategoria);
            carregarCategorias();
            telaPrincipal.atualizarCategorias();
            txtNomeCategoria.setText("");
            JOptionPane.showMessageDialog(dialogoAtual, 
                "Categoria adicionada com sucesso!", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Evento do botão Editar
        btnEditar.addActionListener(e -> {
            // Referência explícita à classe para evitar ambiguidade no 'this'
            CategoriasTela dialogoAtual = CategoriasTela.this;
            
            if (listaCategorias.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(dialogoAtual,
                    "Selecione uma categoria para editar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String nomeAntigo = listaCategorias.getSelectedValue();
            String nomeNovo = txtNomeCategoria.getText().trim();
            
            if (!validarNome(nomeNovo)) {
                return; // Mensagem já exibida pela validação
            }
            
            if (nomeAntigo.equals(nomeNovo)) {
                JOptionPane.showMessageDialog(dialogoAtual,
                    "O nome da categoria não foi alterado.",
                    "Informação",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            if (projeto.editarCategoria(nomeAntigo, nomeNovo)) {
                carregarCategorias();
                telaPrincipal.atualizarCategorias();
                txtNomeCategoria.setText("");
                listaCategorias.clearSelection();
                JOptionPane.showMessageDialog(dialogoAtual, 
                    "Categoria editada com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialogoAtual, 
                    "Não foi possível editar a categoria porque ela está sendo usada.", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Evento do botão Remover
        btnRemover.addActionListener(e -> {
            // Referência explícita à classe para evitar ambiguidade no 'this'
            CategoriasTela dialogoAtual = CategoriasTela.this;
            
            if (listaCategorias.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(dialogoAtual,
                    "Selecione uma categoria para remover.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String nomeCategoria = listaCategorias.getSelectedValue();
            int confirmacao = JOptionPane.showConfirmDialog(dialogoAtual,
                "Tem certeza que deseja remover a categoria '" + nomeCategoria + "'?",
                "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                if (projeto.removerCategoria(nomeCategoria)) {
                    carregarCategorias();
                    telaPrincipal.atualizarCategorias();
                    txtNomeCategoria.setText("");
                    listaCategorias.clearSelection();
                    JOptionPane.showMessageDialog(dialogoAtual,
                        "Categoria removida com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialogoAtual,
                        "Não foi possível remover a categoria porque ela está sendo usada por pelo menos um Lego.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Evento do botão Fechar/Concluir
        btnFechar.addActionListener(e -> verificarFechamento());
        
        // Evento do botão Cancelar (apenas se for diálogo obrigatório)
        if (obrigatorio && btnCancelar != null) {
            btnCancelar.addActionListener(e -> {
                // Referência explícita à classe para evitar ambiguidade no 'this'
                CategoriasTela dialogoAtual = CategoriasTela.this;
                
                if (modeloLista.isEmpty()) {
                    int confirmacao = JOptionPane.showConfirmDialog(dialogoAtual,
                        "Nenhuma categoria foi cadastrada. Se continuar, não será possível cadastrar Legos.\n\nDeseja realmente cancelar?",
                        "Confirmar Cancelamento",
                        JOptionPane.YES_NO_OPTION);
                    
                    if (confirmacao == JOptionPane.YES_OPTION) {
                        usuarioCancelou = true;
                        dispose();
                    }
                } else {
                    dispose();
                }
            });
        }
    }
    
    /**
     * Valida o nome da categoria
     * @param nome Nome a validar
     * @return true se o nome é válido, false caso contrário
     */
    private boolean validarNome(String nome) {
        nome = nome.trim();
        
        if (nome.isEmpty()) {
            lblStatus.setText("O nome da categoria não pode estar vazio");
            btnAdicionar.setEnabled(false);
            btnEditar.setEnabled(false);
            return false;
        }
        
        if (nome.length() < 3) {
            lblStatus.setText("O nome deve ter pelo menos 3 caracteres");
            btnAdicionar.setEnabled(false);
            btnEditar.setEnabled(listaCategorias.getSelectedValue() != null);
            return false;
        }
        
        if (nome.length() > 30) {
            lblStatus.setText("O nome deve ter no máximo 30 caracteres");
            btnAdicionar.setEnabled(false);
            btnEditar.setEnabled(listaCategorias.getSelectedValue() != null);
            return false;
        }
        
        // Verifica duplicidade para adição
        if (modeloLista.contains(nome) && 
            (listaCategorias.isSelectionEmpty() || !listaCategorias.getSelectedValue().equals(nome))) {
            lblStatus.setText("Esta categoria já existe");
            btnAdicionar.setEnabled(false);
            btnEditar.setEnabled(listaCategorias.getSelectedValue() != null);
            return false;
        }
        
        // Se chegou aqui, o nome é válido
        lblStatus.setText(" ");
        btnAdicionar.setEnabled(!modeloLista.contains(nome));
        btnEditar.setEnabled(!listaCategorias.isSelectionEmpty());
        return true;
    }
    
    /**
     * Verifica se pode fechar a janela
     */
    private void verificarFechamento() {
        // Referência explícita à classe para evitar ambiguidade no 'this'
        CategoriasTela dialogoAtual = CategoriasTela.this;
        
        if (obrigatorio && modeloLista.isEmpty()) {
            JOptionPane.showMessageDialog(dialogoAtual,
                "É necessário cadastrar pelo menos uma categoria antes de continuar.",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        dispose();
    }
    
    /**
     * Verifica se o usuário cancelou o diálogo
     * @return true se o usuário cancelou, false caso contrário
     */
    public boolean foiCancelado() {
        return usuarioCancelou;
    }
}