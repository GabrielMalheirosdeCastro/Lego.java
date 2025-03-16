package com.mycompany.lego.java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
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
 * Tela para gerenciamento de cores de Lego
 * @author Gabriel.Malheiros
 */
public class CoresTela extends JDialog {
    
    private final legoProjeto projeto;
    private final legoTela telaPrincipal;
    
    private JList<String> listaCores;
    private DefaultListModel<String> modeloLista;
    private JTextField txtNomeCor;
    private JButton btnAdicionar;
    private JButton btnEditar;
    private JButton btnRemover;
    private JButton btnFechar;
    private JButton btnCancelar;
    private JButton btnSelecionarCor;
    private JPanel painelPreview;
    private JLabel lblStatus;
    
    private boolean obrigatorio;
    private Pattern padraoNome;
    private boolean usuarioCancelou = false;
    private Color corSelecionada = Color.RED;
    
    /**
     * Construtor da tela de cores
     * @param owner Frame pai
     * @param projeto Referência para o projeto de legos
     * @param telaPrincipal Referência para a tela principal
     */
    public CoresTela(JFrame owner, legoProjeto projeto, legoTela telaPrincipal) {
        this(owner, projeto, telaPrincipal, false);
    }
    
    /**
     * Construtor da tela de cores com opção de torná-la obrigatória
     * @param owner Frame pai
     * @param projeto Referência para o projeto de legos
     * @param telaPrincipalRef Referência para a tela principal
     * @param obrigatorio Se verdadeiro, o usuário deve adicionar pelo menos uma cor
     */
    public CoresTela(JFrame owner, legoProjeto projeto, legoTela telaPrincipalRef, boolean obrigatorio) {
        super(owner, "Gerenciamento de Cores", true);
        this.projeto = projeto;
        this.telaPrincipal = telaPrincipalRef;
        this.obrigatorio = obrigatorio;
        
        // Padrão menos restritivo para o nome: aceita quaisquer caracteres,
        // apenas limitado pelo tamanho mínimo e máximo
        padraoNome = Pattern.compile("^.{3,30}$");
        
        inicializarComponentes();
        carregarCores();
        configurarEventos();
        
        setSize(400, 500);
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
        
        // Painel para lista de cores
        JPanel painelLista = new JPanel(new BorderLayout(5, 5));
        painelLista.setBorder(BorderFactory.createTitledBorder("Cores Existentes"));
        
        modeloLista = new DefaultListModel<>();
        listaCores = new JList<>(modeloLista);
        listaCores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollLista = new JScrollPane(listaCores);
        scrollLista.setPreferredSize(new Dimension(350, 200));
        painelLista.add(scrollLista, BorderLayout.CENTER);
        
        // Painel para formulário de cadastro
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Cadastro de Cor"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Campo de nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        painelFormulario.add(new JLabel("Nome da Cor:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.9;
        txtNomeCor = new JTextField(20);
        painelFormulario.add(txtNomeCor, gbc);
        
        // Painel de preview da cor
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        painelFormulario.add(new JLabel("Visualização:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.9;
        painelPreview = new JPanel();
        painelPreview.setBackground(corSelecionada);
        painelPreview.setPreferredSize(new Dimension(100, 25));
        painelFormulario.add(painelPreview, gbc);
        
        // Botão para escolher cor
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        btnSelecionarCor = new JButton("Escolher Cor...");
        painelFormulario.add(btnSelecionarCor, gbc);
        
        // Label de status
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        lblStatus = new JLabel(" ");
        lblStatus.setForeground(java.awt.Color.RED);
        painelFormulario.add(lblStatus, gbc);
        
        // Adiciona instruções se for obrigatório
        if (obrigatorio) {
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            JLabel lblObrigatorio = new JLabel("É necessário cadastrar pelo menos uma cor para continuar.");
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
     * Carrega as cores existentes na lista
     */
    private void carregarCores() {
        modeloLista.clear();
        // Usando getNomesCores() que retorna os nomes das cores
        String[] nomesCores = projeto.getNomesCores();
        for (String nomeCor : nomesCores) {
            modeloLista.addElement(nomeCor);
        }
        
        // Atualiza a cor do painel de preview se houver alguma cor na lista
        if (modeloLista.size() > 0) {
            String corSelecionada = modeloLista.getElementAt(0);
            Color cor = projeto.getCorPorNome(corSelecionada);
            if (cor != null) {
                this.corSelecionada = cor;
                painelPreview.setBackground(cor);
                painelPreview.setToolTipText(corSelecionada);
            }
        }
    }
    
    /**
     * Configura os eventos dos componentes
     */
    private void configurarEventos() {
        // Evento de seleção na lista
        listaCores.addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                boolean temSelecao = !listaCores.isSelectionEmpty();
                btnEditar.setEnabled(temSelecao);
                btnRemover.setEnabled(temSelecao);
                
                if (temSelecao) {
                    String nomeCor = listaCores.getSelectedValue();
                    txtNomeCor.setText(nomeCor);
                    
                    // Atualiza a cor selecionada e o painel de preview
                    corSelecionada = projeto.getCorPorNome(nomeCor);
                    if (corSelecionada != null) {
                        painelPreview.setBackground(corSelecionada);
                        painelPreview.setToolTipText(nomeCor);
                    }
                    
                    validarNome(txtNomeCor.getText());
                }
            }
        });
        
        // Evento de digitação no campo de texto
        txtNomeCor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validarNome(txtNomeCor.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validarNome(txtNomeCor.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validarNome(txtNomeCor.getText());
            }
        });
        
        // Evento do botão de selecionar cor
        btnSelecionarCor.addActionListener(e -> {
            Color cor = JColorChooser.showDialog(
                this, 
                "Selecione a Cor", 
                corSelecionada);
                
            if (cor != null) {
                corSelecionada = cor;
                painelPreview.setBackground(cor);
                
                // Atualiza o tooltip com os valores RGB para auxiliar na identificação
                String rgbValor = String.format("R:%d, G:%d, B:%d", cor.getRed(), cor.getGreen(), cor.getBlue());
                painelPreview.setToolTipText(txtNomeCor.getText().isEmpty() ? 
                    "Cor selecionada: " + rgbValor : 
                    txtNomeCor.getText() + " (" + rgbValor + ")");
            }
        });
        
        // Evento do botão Adicionar
        btnAdicionar.addActionListener(e -> {
            String nomeCor = txtNomeCor.getText().trim();
            
            if (!validarNome(nomeCor)) {
                return; // Mensagem já exibida pela validação
            }
            
            // Cria uma referência explícita à classe para evitar ambiguidade no 'this'
            CoresTela dialogoAtual = CoresTela.this;
            
            // Passa tanto o nome quanto o objeto Color para o método adicionarCor
            projeto.adicionarCor(nomeCor, corSelecionada);
            carregarCores();
            telaPrincipal.atualizarCores();
            txtNomeCor.setText("");
            JOptionPane.showMessageDialog(dialogoAtual, 
                "Cor adicionada com sucesso!", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Evento do botão Editar
        btnEditar.addActionListener(e -> {
            // Cria uma referência explícita à classe para evitar ambiguidade no 'this'
            CoresTela dialogoAtual = CoresTela.this;
            
            if (listaCores.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(dialogoAtual,
                    "Selecione uma cor para editar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String nomeAntigo = listaCores.getSelectedValue();
            String nomeNovo = txtNomeCor.getText().trim();
            
            if (!validarNome(nomeNovo)) {
                return; // Mensagem já exibida pela validação
            }
            
            // Se apenas a cor foi alterada sem alterar o nome
            if (nomeAntigo.equals(nomeNovo)) {
                // Usa editarCor com o mesmo nome mas nova cor
                if (projeto.editarCor(nomeAntigo, nomeNovo, corSelecionada)) {
                    carregarCores();
                    telaPrincipal.atualizarCores();
                    txtNomeCor.setText("");
                    listaCores.clearSelection();
                    painelPreview.setBackground(corSelecionada);
                    JOptionPane.showMessageDialog(dialogoAtual, 
                        "Cor atualizada com sucesso!", 
                        "Sucesso", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialogoAtual, 
                        "Não foi possível atualizar a cor.", 
                        "Erro", 
                        JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
            
            // Se o nome foi alterado, tenta editar com o novo nome e cor
            if (projeto.editarCor(nomeAntigo, nomeNovo, corSelecionada)) {
                carregarCores();
                telaPrincipal.atualizarCores();
                txtNomeCor.setText("");
                listaCores.clearSelection();
                JOptionPane.showMessageDialog(dialogoAtual, 
                    "Cor editada com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialogoAtual, 
                    "Não foi possível editar a cor porque ela está sendo usada ou o novo nome já existe.", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Evento do botão Remover
        btnRemover.addActionListener(e -> {
            // Cria uma referência explícita à classe para evitar ambiguidade no 'this'
            CoresTela dialogoAtual = CoresTela.this;
            
            if (listaCores.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(dialogoAtual,
                    "Selecione uma cor para remover.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String nomeCor = listaCores.getSelectedValue();
            int confirmacao = JOptionPane.showConfirmDialog(dialogoAtual,
                "Tem certeza que deseja remover a cor '" + nomeCor + "'?",
                "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                if (projeto.removerCor(nomeCor)) {
                    carregarCores();
                    telaPrincipal.atualizarCores();
                    txtNomeCor.setText("");
                    listaCores.clearSelection();
                    JOptionPane.showMessageDialog(dialogoAtual,
                        "Cor removida com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialogoAtual,
                        "Não foi possível remover a cor porque ela está sendo usada por pelo menos um Lego.",
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
                // Cria uma referência explícita à classe para evitar ambiguidade no 'this'
                CoresTela dialogoAtual = CoresTela.this;
                
                if (modeloLista.isEmpty()) {
                    int confirmacao = JOptionPane.showConfirmDialog(dialogoAtual,
                        "Nenhuma cor foi cadastrada. Se continuar, serão utilizadas as cores padrão.\n\nDeseja realmente cancelar?",
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
     * Valida o nome da cor
     * @param nome Nome a validar
     * @return true se o nome é válido, false caso contrário
     */
    private boolean validarNome(String nome) {
        nome = nome.trim();
        
        if (nome.isEmpty()) {
            lblStatus.setText("O nome da cor não pode estar vazio");
            btnAdicionar.setEnabled(false);
            btnEditar.setEnabled(false);
            return false;
        }
        
        if (nome.length() < 3) {
            lblStatus.setText("O nome deve ter pelo menos 3 caracteres");
            btnAdicionar.setEnabled(false);
            btnEditar.setEnabled(listaCores.getSelectedValue() != null);
            return false;
        }
        
        if (nome.length() > 30) {
            lblStatus.setText("O nome deve ter no máximo 30 caracteres");
            btnAdicionar.setEnabled(false);
            btnEditar.setEnabled(listaCores.getSelectedValue() != null);
            return false;
        }
        
        // Verifica duplicidade para adição
        if (modeloLista.contains(nome) && 
            (listaCores.isSelectionEmpty() || !listaCores.getSelectedValue().equals(nome))) {
            lblStatus.setText("Esta cor já existe");
            btnAdicionar.setEnabled(false);
            btnEditar.setEnabled(listaCores.getSelectedValue() != null);
            return false;
        }
        
        // Se chegou aqui, o nome é válido
        lblStatus.setText(" ");
        btnAdicionar.setEnabled(!modeloLista.contains(nome));
        btnEditar.setEnabled(!listaCores.isSelectionEmpty());
        return true;
    }
    
    /**
     * Verifica se pode fechar a janela
     */
    private void verificarFechamento() {
        // Cria uma referência explícita para evitar ambiguidade
        CoresTela dialogoAtual = CoresTela.this;
        
        if (obrigatorio && modeloLista.isEmpty()) {
            JOptionPane.showMessageDialog(dialogoAtual,
                "É necessário cadastrar pelo menos uma cor antes de continuar.",
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