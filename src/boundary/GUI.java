package boundary;

import entity.EntityOrdine;
import entity.EntityFattorino;
import control.GestioneOrdini;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * BoundaryCooperativa.java
 *
 * GUI Swing “moderna” per il gestore della cooperativa “O’ sole mio”.
 * Mostra due tab principali:
 *  1) Visualizza Ordini
 *  2) Assegna Consegna
 *
 * Utilizza i colori principali:
 *  - Blu navy:  #1C355E
 *  - Beige       #F5F0E6
 *
 * Associa le azioni a GestioneOrdini (control).
 */
public class GUI extends JFrame {

    // COLORI PRINCIPALI (in HEX)
    private static final Color COLORE_BLU   = Color.decode("#1C355E");
    private static final Color COLORE_BEIGE = Color.decode("#F5F0E6");

    // Formato data per query
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // Control
    public GestioneOrdini gestione;

    // Componenti GUI (visibile in tutto il frame)
    private JTable tableOrdini;
    private DefaultTableModel modelOrdini;

    private JList<EntityOrdine> listOrdini;           // Nella sezione “Assegna Consegna”
    private DefaultListModel<EntityOrdine> modelListOrdini;

    private JComboBox<EntityFattorino> comboFattorini; // Selezione fattorino
    private JButton btnAssegna;

    public GUI() {
        super("Gestore Cooperativa - O' Sole Mio");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 600);
        this.setLocationRelativeTo(null);

        // Imposta il Look & Feel di sistema
        setSystemLookAndFeel();

        // Istanzia il control
        gestione = new GestioneOrdini();

        // Configura il pannello principale
        initComponents();
    }

    /**
     * Imposta il Look & Feel di sistema.
     */
    private void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Se vuoi forzare alcuni colori nel caso di Look&Feel metal:
            UIManager.put("Table.selectionBackground", COLORE_BLU.darker());
            UIManager.put("Table.selectionForeground", Color.WHITE);
        } catch (Exception e) {
            System.err.println("Impossibile impostare L&F: " + e.getMessage());
        }
    }

    /**
     * Crea e dispone tutti i componenti della finestra (header, tabs, pannelli).
     */
    private void initComponents() {
        // 1) HEADER con logo
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLORE_BEIGE);
        header.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setIcon(loadLogo("/logo.png")); // Percorso della risorsa logo (da compattaClasspath)
        header.add(lblLogo, BorderLayout.CENTER);

        this.add(header, BorderLayout.NORTH);

        // 2) TABBED PANE (due pannelli principali)
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(COLORE_BEIGE);
        tabbedPane.setForeground(COLORE_BLU);

        // Pannello “Visualizza Ordini”
        JPanel panelVisualizza = buildPanelVisualizzaOrdini();
        tabbedPane.addTab("Visualizza Ordini", panelVisualizza);

        // Pannello “Assegna Consegna”
        JPanel panelAssegna = buildPanelAssegnaConsegna();
        tabbedPane.addTab("Assegna Consegna", panelAssegna);

        this.add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Carica il logo da risorse. Il file /logo.png deve essere nella cartella di risorse del progetto.
     */
    private ImageIcon loadLogo(String resourcePath) {
        try {
            InputStream is = getClass().getResourceAsStream(resourcePath);
            if (is == null) {
                throw new IOException("Risorsa logo non trovata: " + resourcePath);
            }
            Image img = ImageIO.read(is);
            // Ridimensiona il logo a larghezza 300 mantenendo proporzioni
            int width = 300;
            int height = (int) (img.getHeight(null) * (300.0 / img.getWidth(null)));
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (IOException e) {
            System.err.println("Errore caricamento logo: " + e.getMessage());
            return new ImageIcon(); // Icona vuota in caso di errore
        }
    }

    /**
     * Costruisce il pannello “Visualizza Ordini”.
     * Contiene una JTable con gli ordini del giorno precedente (data = oggi - 1).
     */
    private JPanel buildPanelVisualizzaOrdini() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(COLORE_BEIGE);
        p.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Titolo
        JLabel lblTitolo = new JLabel("Elenco Ordini del Giorno Precedente");
        lblTitolo.setForeground(COLORE_BLU);
        lblTitolo.setFont(lblTitolo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitolo.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(lblTitolo, BorderLayout.NORTH);

        // Table model (colonne: ID, Ristorante, Data, QuantitàTotale, Stato)
        modelOrdini = new DefaultTableModel(new String[]{"ID", "Ristorante", "Data", "Quantità", "Stato"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tableOrdini = new JTable(modelOrdini);
        tableOrdini.setFillsViewportHeight(true);
        tableOrdini.setBackground(Color.WHITE);
        tableOrdini.setForeground(COLORE_BLU);
        tableOrdini.getTableHeader().setBackground(COLORE_BLU);
        tableOrdini.getTableHeader().setForeground(Color.WHITE);
        tableOrdini.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tableOrdini);
        p.add(scroll, BorderLayout.CENTER);

        // Carica i dati di partenza
        refreshListaOrdini();

        return p;
    }

    /**
     * Richiede al control gli ordini del giorno precedente e popola la JTable.
     */
    private void refreshListaOrdini() {
        // Calcola “giorno precedente”
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date ieri = new Date(cal.getTimeInMillis());

        List<EntityOrdine> ordini = gestione.readOrdiniByData(ieri);
        modelOrdini.setRowCount(0); // svuota

        for (EntityOrdine o : ordini) {
            String dataFormattata = sdf.format(o.getData());
            // Puoi aggiungere altre colonne (stato, dettaglio, ecc.)
            modelOrdini.addRow(new Object[]{
                    o.getId(),
                    o.getRistorante().getNome(), // supponendo che EntityOrdine abbia getRistorante()
                    dataFormattata,
                    o.getQuantitaTotale(),
                    o.getStato()
            });
        }
    }

    /**
     * Costruisce il pannello “Assegna Consegna”.
     * Contiene:
     *  - JList di ordini (del giorno precedente non ancora assegnati)
     *  - JComboBox di fattorini disponibili
     *  - Pulsante “Assegna” per creare la consegna
     */
    private JPanel buildPanelAssegnaConsegna() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(COLORE_BEIGE);
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.BOTH;

        // 1) Label Ordini
        JLabel lblOrdini = new JLabel("Seleziona Ordini da Assegnare:");
        lblOrdini.setForeground(COLORE_BLU);
        lblOrdini.setFont(lblOrdini.getFont().deriveFont(Font.BOLD, 14f));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        p.add(lblOrdini, gbc);

        // 2) Lista ordini (JList)
        modelListOrdini = new DefaultListModel<>();
        listOrdini = new JList<>(modelListOrdini);
        listOrdini.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listOrdini.setBackground(Color.WHITE);
        listOrdini.setForeground(COLORE_BLU);

        JScrollPane scrollOrdini = new JScrollPane(listOrdini);
        scrollOrdini.setPreferredSize(new Dimension(200, 250));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weighty = 1.0;
        p.add(scrollOrdini, gbc);

        // 3) Label Fattorino
        JLabel lblFattorino = new JLabel("Seleziona Fattorino:");
        lblFattorino.setForeground(COLORE_BLU);
        lblFattorino.setFont(lblFattorino.getFont().deriveFont(Font.BOLD, 14f));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weighty = 0;
        p.add(lblFattorino, gbc);

        // 4) ComboBox Fattorini
        comboFattorini = new JComboBox<>();
        comboFattorini.setBackground(Color.WHITE);
        comboFattorini.setForeground(COLORE_BLU);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        p.add(comboFattorini, gbc);

        // 5) Pulsante “Assegna”
        btnAssegna = new JButton("Assegna Consegna");
        btnAssegna.setBackground(COLORE_BLU);
        btnAssegna.setForeground(Color.WHITE);
        btnAssegna.setFont(btnAssegna.getFont().deriveFont(Font.BOLD, 14f));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.weightx = 0; gbc.weighty = 0;
        p.add(btnAssegna, gbc);

        // Carica dati iniziali
        refreshListaOrdiniAssegna();
        refreshListaFattorini();

        // Associazione listener al pulsante
        btnAssegna.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assegnaConsegnaAction();
            }
        });

        return p;
    }

    /**
     * Rimette a zero (+ ricarica) i dati in JList ordini per la tab “Assegna Consegna”.
     * Vengono caricati SOLO gli ordini NON ancora assegnati.
     */
    private void refreshListaOrdiniAssegna() {
        modelListOrdini.clear();

        // Giorno precedente
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date ieri = new Date(cal.getTimeInMillis());

        // Supponiamo che la business logic sappia filtrare solo “non assegnati”
        List<EntityOrdine> ordini = gestione.readOrdiniNonAssegnatiByData(ieri);
        for (EntityOrdine o : ordini) {
            modelListOrdini.addElement(o);
        }
    }

    /**
     * Ricarica il ComboBox dei fattorini disponibili.
     */
    private void refreshListaFattorini() {
        comboFattorini.removeAllItems();
        List<EntityFattorino> fattorini = gestione.readFattoriniDisponibili();
        for (EntityFattorino f : fattorini) {
            comboFattorini.addItem(f);
        }
    }

    /**
     * Azione scattata quando l’utente clicca “Assegna Consegna”.
     * Prende gli ordini selezionati e il fattorino, poi chiama il control per creare la consegna.
     */
    private void assegnaConsegnaAction() {
        List<EntityOrdine> selectedOrdini = listOrdini.getSelectedValuesList();
        EntityFattorino selectedFattorino = (EntityFattorino) comboFattorini.getSelectedItem();

        if (selectedOrdini.isEmpty() || selectedFattorino == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleziona almeno un ordine e un fattorino!",
                    "Errore di selezione",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Estrai solo gli ID ordini
        List<Integer> idOrdini = new ArrayList<>();
        for (EntityOrdine o : selectedOrdini) {
            idOrdini.add(o.getId());
        }

        // Giorno corrente (“dataOdierna” per la consegna)
        Date oggi = new Date(System.currentTimeMillis());

        boolean ok = gestione.creaConsegna(idOrdini, selectedFattorino.getId(), oggi);
        if (ok) {
            JOptionPane.showMessageDialog(
                    this,
                    "Consegna assegnata con successo a “" + selectedFattorino.getNome() + "”!",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Aggiorna le liste (ordini e tabella)
            refreshListaOrdiniAssegna();
            refreshListaOrdini();
            refreshListaFattorini();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Errore durante la creazione della consegna.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Launch della GUI.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BoundaryCooperativa gui = new BoundaryCooperativa();
            gui.setVisible(true);
        });
    }
}
