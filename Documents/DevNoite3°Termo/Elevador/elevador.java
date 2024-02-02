package Elevador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// A classe 'elevador' estende JFrame, indicando que é uma janela gráfica.
public class elevador extends JFrame {

    // Botões para chamar os elevadores e para selecionar o andar desejado.
    private JButton[] botoesChamar;
    private JButton[] botoesAndar;

    // Área de exibição para mostrar informações sobre o elevador.
    private JTextArea display;

    // Painéis representando cada elevador na interface gráfica.
    private ElevadorPanel[] elevadores;

    // Arrays para armazenar a posição e a direção de cada elevador.
    private int[] posicaoElevadores;
    private int[] direcaoElevadores;

    // Índice do elevador atualmente selecionado.
    private int elevadorSelecionado;

    // Lista para armazenar os andares pelos quais cada elevador está passando
    private List<List<Integer>> andaresPercorridos;

    // Rótulos para mostrar o andar atual de cada elevador
    private JLabel labelElevador1;
    private JLabel labelElevador2;

    // Construtor da classe Elevador
    public elevador() {
        super("Sistema de Elevadores");

        // Inicialização de variáveis para os botões de chamada dos elevadores.
        botoesChamar = new JButton[2];
        botoesAndar = new JButton[10]; // Ajustar para 9 botões para incluir os andares -2 a 6
        display = new JTextArea();// Inicialização da área de exibição de informações sobre o elevador.
        elevadores = new ElevadorPanel[2];// Inicialização dos painéis que representarão visualmente cada elevador na interface gráfica.
        posicaoElevadores = new int[]{0, 0};// Inicialização das posições iniciais dos elevadores (0 representa o térreo).
        direcaoElevadores = new int[]{0, 0};// Inicialização das direções iniciais dos elevadores (0 para parado, -2 para descendo, 1 para subindo).
        elevadorSelecionado = 0; // Inicialização do índice do elevador atualmente selecionado (pode ser usado para interação do usuário).

        // Inicialização de uma lista para armazenar os andares pelos quais os elevadores passaram.
        andaresPercorridos = new ArrayList<>();

        // Configurando a interface gráfica
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel para os botões de chamada e andares
        JPanel botoesPanel = new JPanel(new GridLayout(10, 2, 10, 10)); // Ajustar para 9 botões
        for (int i = -2; i <= 6; i++) {
            final int andar = i;
            botoesAndar[i + 2] = new JButton(String.valueOf(andar));
            botoesAndar[i + 2].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int andarSolicitado = Integer.parseInt(((JButton) e.getSource()).getText());
                    moverElevador(elevadorSelecionado, andarSolicitado);
                }
            });
            botoesPanel.add(botoesAndar[i + 2]);

            botoesChamar[(i + 2) % 2] = new JButton("Chamar");// Criação de botões de chamada de elevador, alternando entre dois índices para diferentes andares.
            botoesChamar[(i + 2) % 2].setBackground(Color.BLUE); // Configuração da cor de fundo dos botões de chamada para azul.

            // Adicionando um ouvinte de ação aos botões de chamada.
            botoesChamar[(i + 2) % 2].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int elevadorMaisProximo = obterElevadorMaisProximo(andar);
                    moverElevador(elevadorMaisProximo, andar);
                }
            });
            botoesPanel.add(botoesChamar[(i + 2) % 2]);
        }

        // Painel para exibir os elevadores e o display
        JPanel elevadoresPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        for (int i = 0; i < 2; i++) {
            elevadores[i] = new ElevadorPanel(i + 1);
            elevadoresPanel.add(elevadores[i]);
        }

        // Adicionar rótulos para mostrar o andar atual de cada elevador
        labelElevador1 = new JLabel("Andar: 0");
        labelElevador2 = new JLabel("Andar: 0");

        panel.add(botoesPanel, BorderLayout.WEST);// Adicionando o painel de botões de chamada e seleção de andar à parte esquerda da janela.
        panel.add(elevadoresPanel, BorderLayout.CENTER);// Adicionando os painéis representando visualmente os elevadores ao centro da janela.
        panel.add(labelElevador1, BorderLayout.NORTH);// Adicionando um rótulo (labelElevador1) acima da janela, talvez indicando informações sobre o elevador.
        panel.add(new JScrollPane(display), BorderLayout.SOUTH);// Adicionando uma área de exibição (display) com barra de rolagem à parte inferior da janela.

        add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setVisible(true);
    }

    // Método para obter o elevador mais próximo de um determinado andar
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

    // Método para mover o elevador para um determinado andar
    private void moverElevador(final int indiceElevador, final int andar) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int andarAtual = posicaoElevadores[indiceElevador];
                int andarDestino = andar;
                direcaoElevadores[indiceElevador] = andarAtual < andarDestino ? 1 : -1;

                display.append("Elevador " + (indiceElevador + 1) + " está se movendo...\n");
                List<Integer> andaresPercorridosAtual = new ArrayList<>();

                while (andarAtual != andarDestino) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    andarAtual += direcaoElevadores[indiceElevador];
                    posicaoElevadores[indiceElevador] = andarAtual;

                    andaresPercorridosAtual.add(andarAtual);

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            elevadores[indiceElevador].setAndarAtual(andarAtual);
                            display.append("Elevador " + (indiceElevador + 1) +
                                    " está no " + (andarAtual >= 0 ? "andar " + andarAtual : "subsolo") + "\n");

                            // Atualizar rótulo de andar atual do elevador
                            if (indiceElevador == 0) {
                                labelElevador1.setText("Andar: " + andarAtual);
                            } else if (indiceElevador == 1) {
                                labelElevador2.setText("Andar: " + andarAtual);
                            }
                        }
                    });
                }

                display.append("Bem-vindo! Por favor, entre no Elevador " + (indiceElevador + 1) + "\n");
                JOptionPane.showMessageDialog(null, "Elevador " + (indiceElevador + 1) +
                        " chegou ao " + (andarAtual >= 0 ? "andar " + andarAtual : "subsolo"));

                // Adicionar lista de andares percorridos pelo elevador à lista global
                andaresPercorridos.add(andaresPercorridosAtual);
            }
        }).start();
    }

    public static void main(String[] args) {
        // Exibir JOptionPane de boas-vindas
        JOptionPane.showMessageDialog(null, "Bem-vindo ao elevador!");

        // Criar e iniciar o elevador
        SwingUtilities.invokeLater(() -> new elevador());
    }

    // Classe que representa o painel visual de um elevador
    class ElevadorPanel extends JPanel {
        private int andarAtual;
        private int numeroElevador;

        // Construtor da classe ElevadorPanel
        public ElevadorPanel(int numeroElevador) {
            this.numeroElevador = numeroElevador;
            setPreferredSize(new Dimension(150, 300));
            setBackground(Color.LIGHT_GRAY);
        }

        /*  Método para atualizar o andar atual do elevador
        public void setAndarAtual(int andarAtual) {
            this.andarAtual = andarAtual;
            repaint();
        }

        // Método para desenhar o painel do elevador
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            g.fillRect(30, 50, 90, 200);
            g.setColor(Color.BLACK);
            g.drawRect(30, 50, 90, 200);
            g.drawString("Elevador " + numeroElevador, 40, 30);
            g.drawString("Andar: " + andarAtual, 40, 270);*/
        }
    }

