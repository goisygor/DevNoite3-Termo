import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class elevador extends JFrame {
    private JButton[] botoesChamar;
    private JButton[] botoesAndar;
    private JTextArea display;
    private ElevadorPanel[] elevadores;

    private int[] posicaoElevadores;
    private int[] direcaoElevadores;

    public elevador() {
        super("Sistema de Elevadores");

        // Inicializando variáveis
        botoesChamar = new JButton[2];
        botoesAndar = new JButton[6];
        display = new JTextArea();
        elevadores = new ElevadorPanel[2];
        posicaoElevadores = new int[]{0, 0}; // Posição inicial dos elevadores (Térreo)
        direcaoElevadores = new int[]{0, 0}; // 0 para parado, 1 para subindo, -1 para descendo

        // Configurando a interface gráfica
        JPanel panel = new JPanel(new BorderLayout());

        JPanel botoesPanel = new JPanel(new GridLayout(6, 2));
        for (int i = 0; i < 6; i++) {
            final int andar = i;
            botoesAndar[i] = new JButton(String.valueOf(i));
            botoesAndar[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int andarSolicitado = Integer.parseInt(((JButton) e.getSource()).getText());
                    moverElevador(0, andarSolicitado);
                }
            });
            botoesPanel.add(botoesAndar[i]);

            botoesChamar[i % 2] = new JButton("Chamar");
            botoesChamar[i % 2].setBackground(Color.GREEN);
            botoesChamar[i % 2].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int elevadorMaisProximo = obterElevadorMaisProximo(andar);
                    moverElevador(elevadorMaisProximo, andar);
                }
            });
            botoesPanel.add(botoesChamar[i % 2]);
        }

        JPanel elevadoresPanel = new JPanel(new GridLayout(1, 2));
        for (int i = 0; i < 2; i++) {
            elevadores[i] = new ElevadorPanel(i + 1); // Passando o número do elevador
            elevadoresPanel.add(elevadores[i]);
        }

        panel.add(botoesPanel, BorderLayout.WEST);
        panel.add(elevadoresPanel, BorderLayout.CENTER);
        panel.add(new JScrollPane(display), BorderLayout.SOUTH);

        add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setVisible(true);
    }

    // Retorna o índice do elevador mais próximo do andar especificado
    private int obterElevadorMaisProximo(int andar) {
        int elevadorMaisProximo = 0;
        int distanciaMinima = Math.abs(posicaoElevadores[0] - andar);
        for (int i = 1; i < posicaoElevadores.length; i++) {
            int distancia = Math.abs(posicaoElevadores[i] - andar);
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                elevadorMaisProximo = i;
            }
        }
        return elevadorMaisProximo;
    }

    // Move o elevador para o andar especificado
    private void moverElevador(final int indiceElevador, final int andar) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int andarAtual = posicaoElevadores[indiceElevador];
                int andarDestino = andar;
                direcaoElevadores[indiceElevador] = andarAtual < andarDestino ? 1 : -1;

                display.append("Elevador " + (indiceElevador + 1) + " está se movendo...\n");
                while (andarAtual != andarDestino) {
                    try {
                        Thread.sleep(2000); // Delay de 2 segundos por andar
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    andarAtual += direcaoElevadores[indiceElevador];
                    posicaoElevadores[indiceElevador] = andarAtual;
                    elevadores[indiceElevador].setAndarAtual(andarAtual);
                    display.setText(display.getText() + "Elevador " + (indiceElevador + 1) + " chegou ao " + (andarAtual >= 0 ? "andar " + andarAtual : "subsolo") + "\n");
                }
                display.append("Bem-vindo! Por favor, entre no Elevador " + (indiceElevador + 1) + "\n");
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new elevador();
            }
        });
    }
}

class ElevadorPanel extends JPanel {
    private int andarAtual;
    private int numeroElevador;

    public ElevadorPanel(int numeroElevador) {
        this.numeroElevador = numeroElevador;
        setPreferredSize(new Dimension(100, 300));
        setBackground(Color.LIGHT_GRAY);
    }

    public void setAndarAtual(int andarAtual) {
        this.andarAtual = andarAtual;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(20, 50, 60, 200); // Elevador
        g.setColor(Color.BLACK);
        g.drawRect(20, 50, 60, 200); // Contorno do Elevador
        g.drawString("Elevador " + numeroElevador, 10, 30); // Texto "Elevador"
        g.drawString("Andar: " + andarAtual, 10, 270); // Texto "Andar"
    }
}