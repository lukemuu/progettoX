package boundary;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
 
public class MainGUI extends JFrame {
 
    public MainGUI() {
        setTitle("Gestione Sistema");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
 
        // Layout principale
        setLayout(new GridLayout(5, 1));
 
        // Pulsanti per accedere ai vari boundary
        JButton btnPescheria = new JButton("Gestione Pescheria");
        JButton btnCooperativa = new JButton("Gestione Cooperativa");
        JButton btnRistorante = new JButton("Gestione Ristorante");
        JButton btnTempo = new JButton("Gestione Tempo");
        JButton btnEsci = new JButton("Esci");
 
        // Aggiungi i pulsanti al frame
        add(btnPescheria);
        add(btnCooperativa);
        add(btnRistorante);
        add(btnTempo);
        add(btnEsci);
 
        // Listener per i pulsanti
        btnPescheria.addActionListener(e -> mostraPescheriaGUI());
        btnCooperativa.addActionListener(e -> mostraCooperativaGUI());
        btnRistorante.addActionListener(e -> mostraRistoranteGUI());
        btnTempo.addActionListener(e -> mostraTempoGUI());
        btnEsci.addActionListener(e -> System.exit(0));
 
        setVisible(true);
    }
 
    private void mostraPescheriaGUI() {
        JFrame frame = new JFrame("Gestione Pescheria");
        frame.setSize(400, 200);
        frame.setLayout(new GridLayout(2, 1));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 
        JButton btnModificaOrdine = new JButton("Modifica Ordine");
        JButton btnIndietro = new JButton("Indietro");
 
        frame.add(btnModificaOrdine);
        frame.add(btnIndietro);
 
        btnModificaOrdine.addActionListener(e -> {
            // Richiama il metodo modificaOrdine del BoundaryPescheria
            BoundaryPescheria.main(new String[]{});
        });
        btnIndietro.addActionListener(e -> frame.dispose());
 
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
 
    private void mostraCooperativaGUI() {
        JFrame frame = new JFrame("Gestione Cooperativa");
        frame.setSize(400, 200);
        frame.setLayout(new GridLayout(2, 1));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 
        JButton btnAssegnaConsegna = new JButton("Assegna Consegna");
        JButton btnIndietro = new JButton("Indietro");
 
        frame.add(btnAssegnaConsegna);
        frame.add(btnIndietro);
 
        btnAssegnaConsegna.addActionListener(e -> {
            // Richiama il metodo assegnaConsegna del BoundaryCooperativa
            BoundaryCooperativa.main(new String[]{});
        });
        btnIndietro.addActionListener(e -> frame.dispose());
 
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
 
    private void mostraRistoranteGUI() {
        JFrame frame = new JFrame("Gestione Ristorante");
        frame.setSize(400, 200);
        frame.setLayout(new GridLayout(2, 1));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 
        JButton btnAcquistaProdotto = new JButton("Acquista Prodotto");
        JButton btnIndietro = new JButton("Indietro");
 
        frame.add(btnAcquistaProdotto);
        frame.add(btnIndietro);
 
        btnAcquistaProdotto.addActionListener(e -> {
            // Richiama il metodo acquistaProdotto del BoundaryRistorante
            BoundaryRistorante.main(new String[]{});
        });
        btnIndietro.addActionListener(e -> frame.dispose());
 
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
 
    private void mostraTempoGUI() {
        JFrame frame = new JFrame("Gestione Tempo");
        frame.setSize(400, 200);
        frame.setLayout(new GridLayout(2, 1));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 
        JButton btnAvviaScheduler = new JButton("Avvia Scheduler");
        JButton btnIndietro = new JButton("Indietro");
 
        frame.add(btnAvviaScheduler);
        frame.add(btnIndietro);
 
        btnAvviaScheduler.addActionListener(e -> {
            // Richiama il metodo avviaScheduler del BoundaryTempo
            BoundaryTempo boundaryTempo = new BoundaryTempo();
            boundaryTempo.avviaScheduler();
            JOptionPane.showMessageDialog(frame, "Scheduler avviato con successo!");
        });
        btnIndietro.addActionListener(e -> frame.dispose());
 
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
}
