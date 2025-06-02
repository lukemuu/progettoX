package boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

// Import delle tue classi esistenti
import control.GestioneOrdini;
import exception.OperationException;
import exception.DAOException;
import exception.DBConnectionException;
import entity.EntityFattorino;
import entity.EntityOrdine;
import entity.EntityProdotto;
import database.FattorinoDAO;
import database.OrdineDAO;
import database.ProdottoDAO;
import boundary.BoundaryTempo;

public class GUI extends JFrame {
    
    private JTabbedPane tabbedPane;
    private JTextArea logArea;
    private GestioneOrdini gestioneOrdini;
    private BoundaryTempo boundaryTempo;
    
    // Componenti per Assegna Consegna
    private JTable tableFattorini;
    private JTable tableOrdini;
    private DefaultTableModel modelFattorini;
    private DefaultTableModel modelOrdini;
    
    // Componenti per Acquista Prodotto
    private JTextField txtIdPescheria;
    private JTextField txtIdProdotto; 
    private JTextField txtQuantita;
    private JTextField txtNumeroCarta;
    private JLabel lblPrezzoTotale;
    
    // Componenti per Modifica Ordine
    private JTextField txtIdOrdineModifica;
    private JTextField txtNuovaQuantita;
    
    public GUI() {
        gestioneOrdini = GestioneOrdini.getInstance();
        boundaryTempo = new BoundaryTempo();
        setupGUI();
    }
    
    private void setupGUI() {
        setTitle("Sistema Gestione Cooperativa - Pescherie & Ristoranti");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Header con informazioni sistema
        createHeader();
        
        // Pannello centrale con tabs
        createTabbedPane();
        
        // Area log in basso
        createLogArea();
        
        // Configurazione finestra
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setVisible(true);
        
        // Log iniziale
        aggiungiLog("Sistema avviato - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
    }
    
    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        JLabel titleLabel = new JLabel("🐟 Sistema Gestione Cooperativa");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Gestore Sistema | " + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userLabel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private void createTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Tab 1: Assegna Consegne (Cooperativa)
        tabbedPane.addTab("🚚 Assegna Consegne", createAssegnaConsegnePanel());
        
        // Tab 2: Acquista Prodotti (Ristorante)
        tabbedPane.addTab("🛒 Acquista Prodotti", createAcquistaProdottiPanel());
        
        // Tab 3: Modifica Ordini (Pescheria)
        tabbedPane.addTab("📝 Modifica Ordini", createModificaOrdiniPanel());
        
        // Tab 4: Gestione Sistema
        tabbedPane.addTab("⚙️ Sistema & Report", createSistemaPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createAssegnaConsegnePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Pannello superiore - Fattorini
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(new TitledBorder("Fattorini Disponibili"));
        
        modelFattorini = new DefaultTableModel(new String[]{"ID", "Nome", "Cognome", "Telefono", "Stato"}, 0);
        tableFattorini = new JTable(modelFattorini);
        tableFattorini.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollFattorini = new JScrollPane(tableFattorini);
        scrollFattorini.setPreferredSize(new Dimension(600, 150));
        
        JButton btnCaricaFattorini = new JButton("Carica Fattorini");
        btnCaricaFattorini.addActionListener(e -> caricaFattorini());
        
        topPanel.add(scrollFattorini, BorderLayout.CENTER);
        topPanel.add(btnCaricaFattorini, BorderLayout.SOUTH);
        
        // Pannello centrale - Ordini
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new TitledBorder("Ordini da Assegnare"));
        
        modelOrdini = new DefaultTableModel(new String[]{"ID", "Ristorante", "Prodotto", "Quantità", "Data", "Stato"}, 0);
        tableOrdini = new JTable(modelOrdini);
        tableOrdini.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollOrdini = new JScrollPane(tableOrdini);
        scrollOrdini.setPreferredSize(new Dimension(600, 150));
        
        JButton btnCaricaOrdini = new JButton("Carica Ordini");
        btnCaricaOrdini.addActionListener(e -> caricaOrdini());
        
        centerPanel.add(scrollOrdini, BorderLayout.CENTER);
        centerPanel.add(btnCaricaOrdini, BorderLayout.SOUTH);
        
        // Pannello inferiore - Azioni
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton btnAssegnaConsegna = new JButton("🚚 Assegna Consegna");
        btnAssegnaConsegna.setFont(new Font("Arial", Font.BOLD, 14));
        btnAssegnaConsegna.setBackground(new Color(76, 175, 80));
        btnAssegnaConsegna.setForeground(Color.WHITE);
        btnAssegnaConsegna.addActionListener(e -> assegnaConsegna());
        
        bottomPanel.add(btnAssegnaConsegna);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createAcquistaProdottiPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form per inserimento dati
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Dati Ordine"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // ID Pescheria
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID Pescheria:"), gbc);
        gbc.gridx = 1;
        txtIdPescheria = new JTextField(15);
        formPanel.add(txtIdPescheria, gbc);
        
        // ID Prodotto
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("ID Prodotto:"), gbc);
        gbc.gridx = 1;
        txtIdProdotto = new JTextField(15);
        formPanel.add(txtIdProdotto, gbc);
        
        // Quantità
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Quantità:"), gbc);
        gbc.gridx = 1;
        txtQuantita = new JTextField(15);
        formPanel.add(txtQuantita, gbc);
        
        // Prezzo totale (display)
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Prezzo Totale:"), gbc);
        gbc.gridx = 1;
        lblPrezzoTotale = new JLabel("€ 0.00");
        lblPrezzoTotale.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrezzoTotale.setForeground(new Color(76, 175, 80));
        formPanel.add(lblPrezzoTotale, gbc);
        
        // Pulsante calcola prezzo
        gbc.gridx = 2; gbc.gridy = 3;
        JButton btnCalcolaPrezzo = new JButton("Calcola Prezzo");
        btnCalcolaPrezzo.addActionListener(e -> calcolaPrezzo());
        formPanel.add(btnCalcolaPrezzo, gbc);
        
        // Pannello pagamento
        JPanel pagamentoPanel = new JPanel(new GridBagLayout());
        pagamentoPanel.setBorder(new TitledBorder("Pagamento"));
        GridBagConstraints gbcPag = new GridBagConstraints();
        gbcPag.insets = new Insets(10, 10, 10, 10);
        gbcPag.anchor = GridBagConstraints.WEST;
        
        gbcPag.gridx = 0; gbcPag.gridy = 0;
        pagamentoPanel.add(new JLabel("Numero Carta (16 cifre):"), gbcPag);
        gbcPag.gridx = 1;
        txtNumeroCarta = new JTextField(20);
        pagamentoPanel.add(txtNumeroCarta, gbcPag);
        
        // Pulsanti azione
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnAcquista = new JButton("🛒 Conferma Acquisto");
        btnAcquista.setFont(new Font("Arial", Font.BOLD, 14));
        btnAcquista.setBackground(new Color(33, 150, 243));
        btnAcquista.setForeground(Color.WHITE);
        btnAcquista.addActionListener(e -> acquistaProdotto());
        
        JButton btnReset = new JButton("Reset Form");
        btnReset.addActionListener(e -> resetFormAcquisto());
        
        buttonPanel.add(btnAcquista);
        buttonPanel.add(btnReset);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(pagamentoPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createModificaOrdiniPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form modifica ordine
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Modifica Ordine"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID Ordine:"), gbc);
        gbc.gridx = 1;
        txtIdOrdineModifica = new JTextField(20);
        formPanel.add(txtIdOrdineModifica, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nuova Quantità:"), gbc);
        gbc.gridx = 1;
        txtNuovaQuantita = new JTextField(20);
        formPanel.add(txtNuovaQuantita, gbc);
        
        // Pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnModifica = new JButton("📝 Modifica Ordine");
        btnModifica.setFont(new Font("Arial", Font.BOLD, 14));
        btnModifica.setBackground(new Color(255, 152, 0));
        btnModifica.setForeground(Color.WHITE);
        btnModifica.addActionListener(e -> modificaOrdine());
        
        buttonPanel.add(btnModifica);
        
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createSistemaPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Pannello scheduler
        JPanel schedulerPanel = new JPanel(new FlowLayout());
        schedulerPanel.setBorder(new TitledBorder("Gestione Report Automatici"));
        
        JButton btnAvviaScheduler = new JButton("▶️ Avvia Scheduler Report");
        btnAvviaScheduler.setBackground(new Color(76, 175, 80));
        btnAvviaScheduler.setForeground(Color.WHITE);
        btnAvviaScheduler.addActionListener(e -> avviaScheduler());
        
        JButton btnInviaReportManuale = new JButton("📊 Invia Report Manuale");
        btnInviaReportManuale.setBackground(new Color(156, 39, 176));
        btnInviaReportManuale.setForeground(Color.WHITE);
        btnInviaReportManuale.addActionListener(e -> inviaReportManuale());
        
        schedulerPanel.add(btnAvviaScheduler);
        schedulerPanel.add(btnInviaReportManuale);
        
        // Info sistema
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        infoPanel.setBorder(new TitledBorder("Informazioni Sistema"));
        
        infoPanel.add(new JLabel("Versione Sistema:"));
        infoPanel.add(new JLabel("v1.0 - Ingegneria del Software"));
        infoPanel.add(new JLabel("Ultimo Avvio:"));
        infoPanel.add(new JLabel(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
        infoPanel.add(new JLabel("Stato Scheduler:"));
        infoPanel.add(new JLabel("Pronto"));
        
        panel.add(schedulerPanel, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void createLogArea() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new TitledBorder("Log Sistema"));
        
        logArea = new JTextArea(8, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Courier New", Font.PLAIN, 11));
        logArea.setBackground(new Color(248, 248, 248));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        JButton clearLogBtn = new JButton("Pulisci Log");
        clearLogBtn.addActionListener(e -> logArea.setText(""));
        
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        bottomPanel.add(clearLogBtn, BorderLayout.EAST);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    // Metodi per Assegna Consegna
    private void caricaFattorini() {
        try {
            List<EntityFattorino> fattorini = FattorinoDAO.readAllFattorini();
            modelFattorini.setRowCount(0);
            
            for (EntityFattorino f : fattorini) {
                modelFattorini.addRow(new Object[]{
                    f.getIdFattorino(),
                    f.getNome(),
                    "Disponibile"
                });
            }
            aggiungiLog("Caricati " + fattorini.size() + " fattorini");
        } catch (Exception e) {
            mostraErrore("Errore caricamento fattorini: " + e.getMessage());
        }
    }
    
    private void caricaOrdini() {
        try {
            List<EntityOrdine> ordini = OrdineDAO.readOrdiniUltimoGiorno();
            modelOrdini.setRowCount(0);
            
            for (EntityOrdine o : ordini) {
                modelOrdini.addRow(new Object[]{
                    o.getIdOrdine(),
                    "Ristorante #" + o.getIdRistorante(),
                    "Prodotto #" + o.getIdProdotto(),
                    o.getQta(),
                });
            }
            aggiungiLog("Caricati " + ordini.size() + " ordini");
        } catch (Exception e) {
            mostraErrore("Errore caricamento ordini: " + e.getMessage());
        }
    }
    
    private void assegnaConsegna() {
        int selectedFattorino = tableFattorini.getSelectedRow();
        int selectedOrdine = tableOrdini.getSelectedRow();
        
        if (selectedFattorino == -1 || selectedOrdine == -1) {
            mostraErrore("Seleziona un fattorino e un ordine");
            return;
        }
        
        try {
            // Simula l'assegnazione (dovresti usare i tuoi metodi reali)
            String idFattorino = modelFattorini.getValueAt(selectedFattorino, 0).toString();
            String idOrdine = modelOrdini.getValueAt(selectedOrdine, 0).toString();
            
            // Qui dovresti chiamare il tuo metodo reale
            // gestioneOrdini.assegnaConsegna(ordineSelezionato, fattorinoSelezionato);
            
            aggiungiLog("Consegna assegnata: Ordine " + idOrdine + " → Fattorino " + idFattorino);
            JOptionPane.showMessageDialog(this, "Consegna assegnata con successo!", 
                "Successo", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            mostraErrore("Errore assegnazione: " + e.getMessage());
        }
    }
    
    // Metodi per Acquista Prodotto
    private void calcolaPrezzo() {
        try {
            int idPescheria = Integer.parseInt(txtIdPescheria.getText());
            int idProdotto = Integer.parseInt(txtIdProdotto.getText());
            double quantita = Double.parseDouble(txtQuantita.getText());
            
            ArrayList<String> results = gestioneOrdini.acquistaProdotto(idPescheria, idProdotto, quantita, 1);
            lblPrezzoTotale.setText("€ " + results.get(0));
            
            aggiungiLog("Prezzo calcolato: € " + results.get(0));
            
        } catch (NumberFormatException e) {
            mostraErrore("Inserire valori numerici validi");
        } catch (OperationException e) {
            mostraErrore("Errore calcolo prezzo: " + e.getMessage());
        } catch (Exception e) {
            mostraErrore("Errore inaspettato: " + e.getMessage());
        }
    }
    
    private void acquistaProdotto() {
        try {
            // Validazione input
            int idPescheria = Integer.parseInt(txtIdPescheria.getText());
            int idProdotto = Integer.parseInt(txtIdProdotto.getText());
            double quantita = Double.parseDouble(txtQuantita.getText());
            int idOrdine = Integer.parseInt(txtIdPescheria.getText()); // Simulazione ID ordine
            
            String numeroCarta = txtNumeroCarta.getText();
            
            if (idPescheria <= 0 || idProdotto <= 0 || quantita <= 0 || idOrdine <= 0) {
                throw new IllegalArgumentException("I valori devono essere positivi");
            }
            
            if (numeroCarta.length() != 16 || !numeroCarta.matches("\\d+")) {
                throw new IllegalArgumentException("Numero carta deve essere di 16 cifre");
            }
            
            // Conferma acquisto
            int conferma = JOptionPane.showConfirmDialog(this, 
                "Confermare l'acquisto di " + quantita + " unità del prodotto " + idProdotto + 
                "\nPrezzo: " + lblPrezzoTotale.getText() + "?",
                "Conferma Acquisto", JOptionPane.YES_NO_OPTION);
                
            if (conferma == JOptionPane.YES_OPTION) {
                // Simula pagamento
                JProgressBar progressBar = new JProgressBar();
                progressBar.setIndeterminate(true);
                JOptionPane.showMessageDialog(this, progressBar, "Pagamento in corso...", JOptionPane.INFORMATION_MESSAGE);
                
                // Processa ordine
                gestioneOrdini.inviaOrdine(idPescheria, idProdotto, quantita, idOrdine);
                gestioneOrdini.confermaOrdine(idOrdine);
                
                aggiungiLog("Ordine completato: Pescheria " + idPescheria + ", Prodotto " + idProdotto + ", Qty " + quantita);
                JOptionPane.showMessageDialog(this, "Acquisto completato con successo!", 
                    "Successo", JOptionPane.INFORMATION_MESSAGE);
                    
                resetFormAcquisto();
            }
            
        } catch (NumberFormatException e) {
            mostraErrore("Inserire valori numerici validi");
        } catch (IllegalArgumentException e) {
            mostraErrore(e.getMessage());
        } catch (OperationException e) {
            mostraErrore("Errore operazione: " + e.getMessage());
        } catch (Exception e) {
            mostraErrore("Errore inaspettato: " + e.getMessage());
        }
    }
    
    private void resetFormAcquisto() {
        txtIdPescheria.setText("");
        txtIdProdotto.setText("");
        txtQuantita.setText("");
        txtNumeroCarta.setText("");
        lblPrezzoTotale.setText("€ 0.00");
    }
    
    // Metodi per Modifica Ordine
    private void modificaOrdine() {
        try {
            int idOrdine = Integer.parseInt(txtIdOrdineModifica.getText());
            int quantitaAggiornata = Integer.parseInt(txtNuovaQuantita.getText());
            
            if (quantitaAggiornata <= 0) {
                throw new IllegalArgumentException("La quantità deve essere positiva");
            }
            
            gestioneOrdini.modificaOrdine(idOrdine, quantitaAggiornata);
            
            aggiungiLog("Ordine modificato: ID " + idOrdine + ", Nuova quantità: " + quantitaAggiornata);
            JOptionPane.showMessageDialog(this, "Ordine modificato con successo!", 
                "Successo", JOptionPane.INFORMATION_MESSAGE);
                
            txtIdOrdineModifica.setText("");
            txtNuovaQuantita.setText("");
            
        } catch (NumberFormatException e) {
            mostraErrore("Inserire valori numerici validi");
        } catch (IllegalArgumentException e) {
            mostraErrore(e.getMessage());
        } catch (OperationException e) {
            mostraErrore("Errore modifica: " + e.getMessage());
        } catch (Exception e) {
            mostraErrore("Errore inaspettato: " + e.getMessage());
        }
    }
    
    // Metodi per Sistema e Report
    private void avviaScheduler() {
        try {
            boundaryTempo.avviaScheduler();
            aggiungiLog("Scheduler report avviato - Invio automatico ogni lunedì alle 9:00");
            JOptionPane.showMessageDialog(this, "Scheduler avviato con successo!\nI report saranno inviati automaticamente ogni lunedì alle 9:00", 
                "Scheduler Attivo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            mostraErrore("Errore avvio scheduler: " + e.getMessage());
        }
    }
    
    private void inviaReportManuale() {
        try {
            gestioneOrdini.inviaReport();
            aggiungiLog("Report inviato manualmente");
            JOptionPane.showMessageDialog(this, "Report inviato con successo!", 
                "Report Inviato", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            mostraErrore("Errore invio report: " + e.getMessage());
        }
    }
    
    // Metodi utility
    private void aggiungiLog(String messaggio) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        logArea.append("[" + timestamp + "] " + messaggio + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    
    private void mostraErrore(String messaggio) {
        aggiungiLog("ERRORE: " + messaggio);
        JOptionPane.showMessageDialog(this, messaggio, "Errore", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
            	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new GUI();
        });
    }
}