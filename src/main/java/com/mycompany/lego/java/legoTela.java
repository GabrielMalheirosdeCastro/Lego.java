package com.mycompany.lego.java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class legoTela extends JFrame {
    private final legoProjeto projeto;
    
    // Componentes da interface
    private JComboBox<String> comboTipoLego;
    private JTextField txtComprimento;
    private JTextField txtLargura;
    private JComboBox<String> comboCategorias;
    private JTextField txtNome;
    private JTextField txtConectores;
    private JTextField txtEncaixes;
    private JComboBox<String> comboCores; // Substituído por combobox
    private JPanel painelPreviewCor;      // Painel para mostrar a cor selecionada
    private JTable tabelaLegos;
    private DefaultTableModel modeloTabela;
    private JButton btnAdicionar;
    private JButton btnGerenciarCategorias;
    private JButton btnGerenciarCores;
    private JLabel lblInfoCategorias;
    
    private NumberFormat formatoNumero;
    private JPanel painelEntrada;
    private GridBagConstraints gbc;
    
    public legoTela(legoProjeto projeto) {
        this.projeto = projeto;
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        // Configuração básica da janela
        setTitle("Sistema de Cadastro de Legos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);
        
        // Configura o formato numérico para o padrão brasileiro
        formatoNumero = NumberFormat.getInstance(Locale.of("pt", "BR"));
        if (formatoNumero instanceof DecimalFormat df) {
            df.setGroupingUsed(false);
        }
        
        // Painel principal
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        
        painelEntrada = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Inicialização dos componentes
        comboTipoLego = new JComboBox<>(new String[]{"Lego Grande", "Lego Pequeno"});
        comboCategorias = new JComboBox<>(projeto.getCategorias());
        txtComprimento = new JTextField(10);
        txtLargura = new JTextField(10);
        txtNome = new JTextField(20);
        txtConectores = new JTextField(10);
        txtConectores.setToolTipText("Digite apenas números inteiros positivos");
        txtEncaixes = new JTextField(20);
        
        // Inicialização do seletor de cores
        comboCores = new JComboBox<>(projeto.getNomesCores());
        
        // Renderizador personalizado para exibir cores como amostras coloridas
        comboCores.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
                String nomeCor = (String) value;
                JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, nomeCor, index, isSelected, cellHasFocus);
                
                // Define a cor do fundo como a cor associada ao nome
                Color corAtual = projeto.getCorPorNome(nomeCor);
                if (corAtual != null) {
                    label.setOpaque(true);
                    // Usa cor de texto contrastante para melhor leitura
                    label.setBackground(corAtual);
                    // Define cor do texto baseada no brilho da cor de fundo
                    double brilho = (0.299 * corAtual.getRed() + 0.587 * corAtual.getGreen() + 0.114 * corAtual.getBlue()) / 255;
                    label.setForeground(brilho > 0.5 ? Color.BLACK : Color.WHITE);
                }
                return label;
            }
        });
        
        // Painel para preview da cor selecionada
        painelPreviewCor = new JPanel();
        painelPreviewCor.setPreferredSize(new Dimension(30, 20));
        painelPreviewCor.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        if (comboCores.getItemCount() > 0) {
            String corSelecionada = (String) comboCores.getSelectedItem();
            painelPreviewCor.setBackground(projeto.getCorPorNome(corSelecionada));
            painelPreviewCor.setToolTipText(corSelecionada);
        }
        
        // Adiciona os componentes na ordem correta
        adicionarComponentesAoPainel();
        
        // Inicialização da tabela com configurações melhoradas
        String[] colunas = {"Categoria", "Tipo", "Nome/Encaixes", "Cor", "Comprimento", "Largura", "Conectores"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex >= 4 && columnIndex <= 6) { // Comprimento, Largura, Conectores
                    return Integer.class;
                }
                return String.class;
            }
        };
        
        tabelaLegos = new JTable(modeloTabela);
        tabelaLegos.setPreferredScrollableViewportSize(new Dimension(750, 200));
        tabelaLegos.getColumnModel().getColumn(0).setPreferredWidth(100); // Categoria
        tabelaLegos.getColumnModel().getColumn(1).setPreferredWidth(80);  // Tipo
        tabelaLegos.getColumnModel().getColumn(2).setPreferredWidth(150); // Nome/Encaixes
        tabelaLegos.getColumnModel().getColumn(3).setPreferredWidth(80);  // Cor
        tabelaLegos.getColumnModel().getColumn(4).setPreferredWidth(100); // Comprimento
        tabelaLegos.getColumnModel().getColumn(5).setPreferredWidth(100); // Largura
        tabelaLegos.getColumnModel().getColumn(6).setPreferredWidth(100); // Conectores
        
        tabelaLegos.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        JScrollPane scrollTabela = new JScrollPane(tabelaLegos);
        
        // Adiciona um DocumentFilter para permitir apenas números inteiros no campo de conectores
        ((javax.swing.text.PlainDocument) txtConectores.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                // Concatena o texto existente com o novo texto para verificação
                String novoTexto = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
                if (novoTexto.matches("\\d*")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                // Obtém o texto atual
                String textoAtual = fb.getDocument().getText(0, fb.getDocument().getLength());
                // Calcula o novo texto após a substituição
                String novoTexto = textoAtual.substring(0, offset) + text + textoAtual.substring(offset + length);
                if (novoTexto.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
        
        // Botões
        btnAdicionar = new JButton("Adicionar Lego");
        btnGerenciarCategorias = new JButton("Gerenciar Categorias");
        btnGerenciarCores = new JButton("Gerenciar Cores");
        
        // Estilo destacado para o botão Gerenciar Categorias
        btnGerenciarCategorias.setBackground(new Color(100, 180, 255));
        btnGerenciarCategorias.setForeground(Color.BLACK);
        Font fonteBotao = btnGerenciarCategorias.getFont();
        btnGerenciarCategorias.setFont(new Font(fonteBotao.getName(), Font.BOLD, fonteBotao.getSize()));
        btnGerenciarCategorias.setToolTipText("Clique aqui para adicionar, editar ou remover categorias");
        
        // Estilo destacado para o botão Gerenciar Cores
        btnGerenciarCores.setBackground(new Color(255, 100, 100));
        btnGerenciarCores.setForeground(Color.BLACK);
        btnGerenciarCores.setFont(new Font(fonteBotao.getName(), Font.BOLD, fonteBotao.getSize()));
        btnGerenciarCores.setToolTipText("Clique aqui para adicionar, editar ou remover cores");
        
        // Painel para área de gerenciamento
        JPanel painelGerenciamento = new JPanel();
        painelGerenciamento.setLayout(new BoxLayout(painelGerenciamento, BoxLayout.Y_AXIS));
        painelGerenciamento.setBorder(BorderFactory.createTitledBorder("Gerenciamento"));
        
        // Label informativa
        lblInfoCategorias = new JLabel("<html>Para adicionar suas próprias categorias e cores, clique nos botões abaixo.<br>As categorias serão usadas para organizar seus Legos e as cores para personaliza-los.</html>");
        lblInfoCategorias.setAlignmentX(CENTER_ALIGNMENT);
        lblInfoCategorias.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Adiciona os componentes no painel de gerenciamento
        painelGerenciamento.add(lblInfoCategorias);
        
        JPanel botoesGerenciamentoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoesGerenciamentoPanel.add(btnGerenciarCategorias);
        botoesGerenciamentoPanel.add(btnGerenciarCores);
        painelGerenciamento.add(botoesGerenciamentoPanel);
        
        // Painel de botões principais
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.add(btnAdicionar);
        
        // Montagem final dos painéis
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.add(painelGerenciamento, BorderLayout.NORTH);
        painelCentral.add(painelBotoes, BorderLayout.CENTER);
        
        painelPrincipal.add(painelEntrada, BorderLayout.NORTH);
        painelPrincipal.add(painelCentral, BorderLayout.CENTER);
        painelPrincipal.add(scrollTabela, BorderLayout.SOUTH);
        
        add(painelPrincipal);
        
        // Adicionar listeners aos botões
        configurarEventos();
        
        // Mostrar/esconder campos específicos baseado no tipo de Lego
        atualizarCamposEspecificos();
        
        setLocationRelativeTo(null);
        
        // Revalida o painel principal
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }
    
    private void adicionarComponentesAoPainel() {
        // Remove todos os componentes existentes
        painelEntrada.removeAll();
        
        int gridY = 0;
        
        // Tipo de Lego
        gbc.gridx = 0; gbc.gridy = gridY;
        painelEntrada.add(new JLabel("Tipo de Lego:"), gbc);
        gbc.gridx = 1;
        painelEntrada.add(comboTipoLego, gbc);
        
        // Categoria
        gridY++;
        gbc.gridx = 0; gbc.gridy = gridY;
        painelEntrada.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1;
        painelEntrada.add(comboCategorias, gbc);
        
        // Nome/Encaixes
        gridY++;
        gbc.gridx = 0; gbc.gridy = gridY;
        boolean isLegoGrande = comboTipoLego.getSelectedIndex() == 0;
        JLabel labelNomeEncaixes = new JLabel(isLegoGrande ? "Nome:" : "Encaixes:");
        painelEntrada.add(labelNomeEncaixes, gbc);
        gbc.gridx = 1;
        JTextField campoAtivo = isLegoGrande ? txtNome : txtEncaixes;
        painelEntrada.add(campoAtivo, gbc);
        
        // Cor (substitui Quantidade de Cores)
        gridY++;
        gbc.gridx = 0; gbc.gridy = gridY;
        painelEntrada.add(new JLabel("Cor do Lego:"), gbc);
        
        // Painel para comboCores e preview da cor
        JPanel painelCorSelecao = new JPanel(new BorderLayout(5, 0));
        painelCorSelecao.add(comboCores, BorderLayout.CENTER);
        painelCorSelecao.add(painelPreviewCor, BorderLayout.EAST);
        
        gbc.gridx = 1;
        painelEntrada.add(painelCorSelecao, gbc);
        
        // Comprimento
        gridY++;
        gbc.gridx = 0; gbc.gridy = gridY;
        painelEntrada.add(new JLabel("Comprimento:"), gbc);
        gbc.gridx = 1;
        painelEntrada.add(txtComprimento, gbc);
        
        // Largura
        gridY++;
        gbc.gridx = 0; gbc.gridy = gridY;
        painelEntrada.add(new JLabel("Largura:"), gbc);
        gbc.gridx = 1;
        painelEntrada.add(txtLargura, gbc);
        
        // Conectores
        gridY++;
        gbc.gridx = 0; gbc.gridy = gridY;
        painelEntrada.add(new JLabel("Número de Conectores:"), gbc);
        gbc.gridx = 1;
        painelEntrada.add(txtConectores, gbc);
        
        // Revalida o painel para atualizar o layout
        painelEntrada.revalidate();
        painelEntrada.repaint();
    }
    
    private void configurarEventos() {
        comboTipoLego.addActionListener(e -> atualizarCamposEspecificos());
        btnAdicionar.addActionListener(e -> adicionarLego());
        
        // Evento para abrir a tela de gerenciamento de categorias
        btnGerenciarCategorias.addActionListener(e -> {
            CategoriasTela categoriasTela = new CategoriasTela(this, projeto, this);
            categoriasTela.setVisible(true);
        });
        
        // Evento para abrir a tela de gerenciamento de cores
        btnGerenciarCores.addActionListener(e -> {
            CoresTela coresTela = new CoresTela((JFrame)this, projeto, this);
            coresTela.setVisible(true);
        });
        
        // Evento para atualizar a visualização da cor quando selecionada
        comboCores.addActionListener(e -> {
            if (comboCores.getSelectedItem() != null) {
                String nomeCor = (String) comboCores.getSelectedItem();
                Color cor = projeto.getCorPorNome(nomeCor);
                painelPreviewCor.setBackground(cor);
                painelPreviewCor.setToolTipText(nomeCor);
            }
        });
    }
    
    private void atualizarCamposEspecificos() {
        // Guarda os valores atuais antes de qualquer modificação
        String valorAtual = comboTipoLego.getSelectedIndex() == 0 ? txtNome.getText() : txtEncaixes.getText();
        
        // Atualiza os componentes
        adicionarComponentesAoPainel();
        
        // Restaura o valor no campo apropriado
        if (comboTipoLego.getSelectedIndex() == 0) {
            txtNome.setText(valorAtual);
            txtEncaixes.setText("");
        } else {
            txtEncaixes.setText(valorAtual);
            txtNome.setText("");
        }
        
        // Define o foco no campo ativo
        if (comboTipoLego.getSelectedIndex() == 0) {
            txtNome.requestFocusInWindow();
        } else {
            txtEncaixes.requestFocusInWindow();
        }
    }
    
    private void validarCamposObrigatorios() throws ParseException {
        // Verificar se existem cores cadastradas
        if (projeto.getNomesCores().length == 0) {
            throw new ParseException("É necessário cadastrar pelo menos uma cor antes de adicionar um Lego.", 0);
        }
        
        // Verificar se uma cor está selecionada
        if (comboCores.getSelectedItem() == null) {
            throw new ParseException("É necessário selecionar uma cor para o Lego.", 0);
        }
        
        // Validação do nome/encaixes
        boolean isLegoGrande = comboTipoLego.getSelectedIndex() == 0;
        if (isLegoGrande && txtNome.getText().trim().isEmpty()) {
            throw new ParseException("O campo Nome é obrigatório para Lego Grande", 0);
        }
        if (!isLegoGrande && txtEncaixes.getText().trim().isEmpty()) {
            throw new ParseException("O campo Encaixes é obrigatório para Lego Pequeno", 0);
        }
        
        // Validação dos campos numéricos
        if (txtComprimento.getText().trim().isEmpty()) {
            throw new ParseException("O campo Comprimento é obrigatório", 0);
        }
        if (txtLargura.getText().trim().isEmpty()) {
            throw new ParseException("O campo Largura é obrigatório", 0);
        }
        if (txtConectores.getText().trim().isEmpty()) {
            throw new ParseException("O campo Conectores é obrigatório", 0);
        }
    }
    
    private double parseNumero(String texto) throws ParseException {
        // Já validamos se está vazio em validarCamposObrigatorios()
        if (texto == null) {
            throw new ParseException("Campo não pode ser nulo", 0);
        }
        
        // Substitui vírgula por ponto para fazer o parse
        texto = texto.replace(',', '.');
        try {
            if (texto.contains(".") || texto.contains(",")) {
                throw new ParseException("O número de conectores deve ser um valor inteiro", 0);
            }
            double valor = Double.parseDouble(texto);
            if (valor <= 0) {
                throw new ParseException("O valor deve ser maior que zero", 0);
            }
            if (valor != Math.floor(valor)) {
                throw new ParseException("O número de conectores deve ser um valor inteiro", 0);
            }
            return valor;
        } catch (NumberFormatException e) {
            throw new ParseException("Formato de número inválido. Use apenas números inteiros", 0);
        }
    }
    
    private void adicionarLego() {
        try {
            // Verifica se existem cores cadastradas
            if (projeto.getNomesCores().length == 0) {
                int resposta = JOptionPane.showConfirmDialog(this,
                        "Não há cores cadastradas. Deseja cadastrar agora?",
                        "Cores Necessárias",
                        JOptionPane.YES_NO_OPTION);
                
                if (resposta == JOptionPane.YES_OPTION) {
                    CoresTela coresTela = new CoresTela((JFrame)this, projeto, this, true);
                    coresTela.setVisible(true);
                    
                    // Verifica novamente após fechar o diálogo
                    if (projeto.getNomesCores().length == 0) {
                        if (coresTela.foiCancelado()) {
                            JOptionPane.showMessageDialog(this, 
                                "Operação cancelada pelo usuário.",
                                "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, 
                                "É necessário cadastrar pelo menos uma cor para adicionar um Lego.",
                                "Aviso", JOptionPane.WARNING_MESSAGE);
                        }
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "É necessário cadastrar pelo menos uma cor para adicionar um Lego.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Verifica se existem categorias cadastradas
            if (comboCategorias.getItemCount() == 0) {
                int resposta = JOptionPane.showConfirmDialog(this,
                        "Não há categorias cadastradas. Deseja cadastrar agora?",
                        "Categorias Necessárias",
                        JOptionPane.YES_NO_OPTION);
                
                if (resposta == JOptionPane.YES_OPTION) {
                    CategoriasTela categoriasTela = new CategoriasTela(this, projeto, this, true);
                    categoriasTela.setVisible(true);
                    
                    // Verifica novamente após fechar o diálogo
                    if (comboCategorias.getItemCount() == 0) {
                        if (categoriasTela.foiCancelado()) {
                            JOptionPane.showMessageDialog(this, 
                                "Operação cancelada pelo usuário.",
                                "Aviso", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, 
                                "É necessário cadastrar pelo menos uma categoria para adicionar um Lego.",
                                "Aviso", JOptionPane.WARNING_MESSAGE);
                        }
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "É necessário cadastrar pelo menos uma categoria para adicionar um Lego.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            // Primeiro valida se há campos vazios
            validarCamposObrigatorios();
            
            int categoriaIndex = comboCategorias.getSelectedIndex();
            String categoria = projeto.getCategorias()[categoriaIndex];
            
            if (projeto.verificarDuplicidade(categoria)) {
                JOptionPane.showMessageDialog(this, 
                    "Já existe um Lego cadastrado para a categoria " + categoria,
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double comprimento = parseNumero(txtComprimento.getText());
            double largura = parseNumero(txtLargura.getText());
            double conectores = parseNumero(txtConectores.getText());
            
            // Obter a cor selecionada
            String nomeCor = (String) comboCores.getSelectedItem();
            Color corSelecionada = projeto.getCorPorNome(nomeCor);
            
            LegoJava novoLego;
            String nomeOuEncaixe;
            
            if (comboTipoLego.getSelectedIndex() == 0) {
                nomeOuEncaixe = txtNome.getText();
                novoLego = new LegoGrande(categoriaIndex + 1, 
                    (int)comprimento, (int)largura, nomeOuEncaixe, (int)conectores, nomeCor, corSelecionada);
            } else {
                nomeOuEncaixe = txtEncaixes.getText();
                novoLego = new LegoPequeno(categoriaIndex + 1,
                    (int)comprimento, (int)largura, nomeOuEncaixe, (int)conectores, nomeCor, corSelecionada);
            }
            
            projeto.adicionarLego(novoLego);
            atualizarTabela();
            
            JOptionPane.showMessageDialog(this, "Lego adicionado com sucesso!");
            limparCampos();
            
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Campo Inválido", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                "Erro de validação: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao adicionar: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        if (!projeto.listaVazia()) {
            projeto.ordenarLegos(); // Mantém a lista sempre ordenada
            for (LegoJava lego : projeto.getListaLegos()) {
                String categoriaNome = projeto.getCategoriaNome(lego.getCategoria());
                String corNome;
                
                Object[] linha;
                if (lego instanceof LegoGrande legoGrande) {
                    corNome = legoGrande.getCorNome();
                    linha = new Object[]{
                        categoriaNome,
                        "Grande",
                        legoGrande.getNome(),
                        corNome,
                        legoGrande.getComprimento(),
                        legoGrande.getLargura(),
                        legoGrande.getConectores()
                    };
                } else if (lego instanceof LegoPequeno legoPequeno) {
                    corNome = legoPequeno.getCorNome();
                    linha = new Object[]{
                        categoriaNome,
                        "Pequeno",
                        legoPequeno.getEncaixes(),
                        corNome,
                        legoPequeno.getComprimento(),
                        legoPequeno.getLargura(),
                        legoPequeno.getConectores()
                    };
                } else {
                    linha = null;
                }
                
                if (linha != null) {
                    modeloTabela.addRow(linha);
                }
            }
        }
    }
    
    private void limparCampos() {
        txtComprimento.setText("");
        txtLargura.setText("");
        txtNome.setText("");
        txtConectores.setText("");
        txtEncaixes.setText("");
        
        if (comboCores.getItemCount() > 0) {
            comboCores.setSelectedIndex(0);
        }
        
        comboTipoLego.setSelectedIndex(0);
        
        if (comboCategorias.getItemCount() > 0) {
            comboCategorias.setSelectedIndex(0);
        }
        
        // Força atualização dos campos
        atualizarCamposEspecificos();
    }
    
    /**
     * Atualiza o combobox de categorias após mudanças
     */
    public void atualizarCategorias() {
        String categoriaAtual = comboCategorias.getSelectedItem() != null ? 
                comboCategorias.getSelectedItem().toString() : null;
        
        // Remove todos os itens atuais
        comboCategorias.removeAllItems();
        
        // Adiciona as categorias atualizadas
        for (String categoria : projeto.getCategorias()) {
            comboCategorias.addItem(categoria);
        }
        
        // Tenta restaurar a seleção anterior
        if (categoriaAtual != null) {
            for (int i = 0; i < comboCategorias.getItemCount(); i++) {
                if (categoriaAtual.equals(comboCategorias.getItemAt(i))) {
                    comboCategorias.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    /**
     * Atualiza o combobox de cores após mudanças
     */
    public void atualizarCores() {
        // Guarda o valor atual
        String valorAtual = (String) comboCores.getSelectedItem();
        
        // Atualiza o modelo do combobox com as novas cores
        comboCores.removeAllItems();
        for (String cor : projeto.getNomesCores()) {
            comboCores.addItem(cor);
        }
        
        // Restaura a seleção anterior se possível
        if (valorAtual != null) {
            comboCores.setSelectedItem(valorAtual);
        }
        
        // Atualiza a tabela caso as cores de algum Lego tenham sido alteradas
        atualizarTabela();
    }
    
    public void iniciar() {
        // Verifica se há categorias ao iniciar a aplicação
        verificarCategorias();
        
        // Verifica se há cores ao iniciar a aplicação
        verificarCores();
        
        setVisible(true);
    }
    
    /**
     * Verifica se existem categorias e pergunta ao usuário se deseja criar caso não exista
     * @return true se existem categorias ou o usuário criou, false se o usuário cancelou
     */
    private boolean verificarCategorias() {
        if (projeto.getCategorias().length == 0) {
            int resposta = JOptionPane.showConfirmDialog(this,
                    "Não há categorias cadastradas. É necessário cadastrar pelo menos uma categoria para usar o sistema.\n\nDeseja cadastrar agora?",
                    "Categorias Necessárias",
                    JOptionPane.YES_NO_OPTION);
            
            if (resposta == JOptionPane.YES_OPTION) {
                CategoriasTela categoriasTela = new CategoriasTela(this, projeto, this, true);
                categoriasTela.setVisible(true);
                atualizarCategorias();
                
                if (categoriasTela.foiCancelado() || projeto.getCategorias().length == 0) {
                    JOptionPane.showMessageDialog(this,
                        "Nenhuma categoria foi cadastrada. Algumas funcionalidades estarão limitadas.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                return true;
            } else {
                JOptionPane.showMessageDialog(this,
                    "Nenhuma categoria foi cadastrada. Algumas funcionalidades estarão limitadas.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }
    
    /**
     * Verifica se existem cores e pergunta ao usuário se deseja criar caso não exista
     * @return true se existem cores ou o usuário criou, false se o usuário cancelou
     */
    private boolean verificarCores() {
        if (projeto.getNomesCores().length == 0) {
            int resposta = JOptionPane.showConfirmDialog(this,
                    "Não há cores cadastradas. É necessário cadastrar pelo menos uma cor para usar o sistema.\n\nDeseja cadastrar agora?",
                    "Cores Necessárias",
                    JOptionPane.YES_NO_OPTION);
            
            if (resposta == JOptionPane.YES_OPTION) {
                CoresTela coresTela = new CoresTela((JFrame)this, projeto, this, true);
                coresTela.setVisible(true);
                atualizarCores();
                
                if (coresTela.foiCancelado() || projeto.getNomesCores().length == 0) {
                    JOptionPane.showMessageDialog(this,
                        "Nenhuma cor foi cadastrada. Algumas funcionalidades estarão limitadas.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                return true;
            } else {
                JOptionPane.showMessageDialog(this,
                    "Nenhuma cor foi cadastrada. Algumas funcionalidades estarão limitadas.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }
}
