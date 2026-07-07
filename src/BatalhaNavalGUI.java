import javax.swing.*;         // importa as ferramentas prontas do Java para criar janelas, botoes, caixas de mensagem, etc
import java.awt.*;            // importa ferramentas de layout (organizacao da tela) e cores
import java.util.Random;      // importa a ferramenta que gera numeros aleatorios (sorteio)

// Classe principal do jogo. Em Java, tudo fica dentro de uma classe.
public class BatalhaNavalGUI {

    // ==========================================================
    // METODO 1: o jogador posiciona os navios NA MAO, clicando
    // nos quadrados do proprio tabuleiro.
    // ==========================================================
    static void posicionarFrota(char[][] tab, int jogador, int TAM, int[] navios) {
        // tab      = matriz onde vamos marcar onde ficam os navios deste jogador
        // jogador  = numero do jogador (1 ou 2), so para exibir nas mensagens
        // TAM      = tamanho do tabuleiro (10)
        // navios   = lista com o tamanho de cada navio, ex: {4, 3, 2}

        JDialog dialog = new JDialog((Frame) null, "Jogador " + jogador + " - posicione sua frota", true);
        // cria uma janela (dialog) com titulo "Jogador X - posicione sua frota"
        // o "true" no final significa que essa janela trava o resto do programa ate ser fechada

        int[] idx = {0};
        // guarda qual navio da lista "navios" estamos posicionando agora (comeca no navio numero 0)
        // usamos um vetor de 1 posicao so para conseguir alterar esse numero depois, dentro do clique do botao

        boolean[] horizontal = {true};
        // guarda se o navio vai ser desenhado deitado (true) ou em pe (false); comeca deitado

        JLabel info = new JLabel("", SwingConstants.CENTER);
        // cria um texto vazio, centralizado, que vai mostrar instrucoes para o jogador

        JButton trocarOrient = new JButton("Orientacao: Horizontal (clique para trocar)");
        // cria o botao que troca a orientacao do navio (deitado/em pe)

        JPanel grade = new JPanel(new GridLayout(TAM, TAM));
        // cria um painel organizado em grade de TAM linhas por TAM colunas (o tabuleiro visual)

        JButton[][] botoes = new JButton[TAM][TAM];
        // cria uma matriz vazia para guardar o botao de cada quadrado do tabuleiro

        trocarOrient.addActionListener(e -> {
            // define o que acontece quando o jogador clica no botao de trocar orientacao
            horizontal[0] = !horizontal[0];
            // inverte o valor: se estava deitado vira em pe, e vice-versa
            trocarOrient.setText("Orientacao: " + (horizontal[0] ? "Horizontal" : "Vertical") + " (clique para trocar)");
            // atualiza o texto do botao para mostrar a orientacao atual
        });

        for (int i = 0; i < TAM; i++) {
            // percorre cada linha do tabuleiro, de 0 ate TAM-1
            for (int j = 0; j < TAM; j++) {
                // percorre cada coluna do tabuleiro, de 0 ate TAM-1
                JButton botao = new JButton();
                // cria um botao vazio para este quadrado (linha i, coluna j)

                int lin = i, col = j;
                // guarda a posicao deste botao em variaveis proprias
                // (precisamos disso porque "i" e "j" vao continuar mudando no loop,
                // mas cada botao precisa lembrar sua propria posicao para sempre)

                botao.addActionListener(e -> {
                    // define o que acontece quando o jogador clica neste quadrado especifico

                    int tam = navios[idx[0]];
                    // pega o tamanho do navio atual que estamos tentando posicionar

                    boolean cabe = true;
                    // comeca supondo que o navio cabe; vamos verificar a seguir

                    for (int k = 0; k < tam; k++) {
                        // percorre cada "pedaco" do navio, do inicio (k=0) ate o tamanho dele

                        int l = horizontal[0] ? lin : lin + k;
                        // se for horizontal, a linha nao muda; se for vertical, a linha aumenta a cada pedaco

                        int c = horizontal[0] ? col + k : col;
                        // se for horizontal, a coluna aumenta a cada pedaco; se for vertical, a coluna nao muda

                        if (l < 0 || l >= TAM || c < 0 || c >= TAM || tab[l][c] == 'N') cabe = false;
                        // se a posicao calculada saiu do tabuleiro OU ja tem outro navio ali, marca que nao cabe
                    }

                    if (!cabe) {
                        // se o navio nao coube na posicao clicada
                        info.setText("Posicao invalida! Escolha outra celula.");
                        // avisa o jogador que a posicao e invalida
                        return;
                        // sai do metodo aqui, sem fazer mais nada (nao deixa colocar o navio)
                    }

                    for (int k = 0; k < tam; k++) {
                        // percorre de novo cada pedaco do navio, agora para desenha-lo de verdade
                        int l = horizontal[0] ? lin : lin + k;
                        // calcula a linha de cada pedaco (igual fizemos antes)
                        int c = horizontal[0] ? col + k : col;
                        // calcula a coluna de cada pedaco (igual fizemos antes)
                        tab[l][c] = 'N';
                        // marca essa posicao na matriz como 'N' (navio)
                        botoes[l][c].setText("🚢");
                        // muda o texto do botao para o desenho de navio
                        botoes[l][c].setBackground(Color.GREEN);
                        // pinta o botao de verde, para o jogador ver onde colocou o navio
                    }

                    idx[0]++;
                    // avanca para o proximo navio da lista (soma 1 ao indice)

                    if (idx[0] == navios.length) {
                        // se ja passamos do ultimo navio da lista, significa que todos foram posicionados
                        dialog.dispose();
                        // fecha a janela de posicionamento, pois terminou
                    } else {
                        // se ainda sobrou navio para posicionar
                        info.setText("Jogador " + jogador + ": posicione o navio de tamanho " + navios[idx[0]]);
                        // atualiza a mensagem, avisando o tamanho do proximo navio
                    }
                });

                botoes[i][j] = botao;
                // guarda este botao na matriz de botoes, na posicao [i][j]
                grade.add(botao);
                // adiciona este botao na grade visual (para aparecer na tela)
            }
        }

        info.setText("Jogador " + jogador + ": posicione o navio de tamanho " + navios[0]);
        // define a mensagem inicial, avisando o tamanho do primeiro navio

        dialog.setLayout(new BorderLayout());
        // define que a janela vai se organizar em 5 areas: norte, sul, leste, oeste e centro

        dialog.add(info, BorderLayout.NORTH);
        // coloca a mensagem de texto na area de cima (norte) da janela

        dialog.add(grade, BorderLayout.CENTER);
        // coloca o tabuleiro (grade de botoes) no meio (centro) da janela

        dialog.add(trocarOrient, BorderLayout.SOUTH);
        // coloca o botao de trocar orientacao na area de baixo (sul) da janela

        dialog.setSize(600, 650);
        // define o tamanho da janela: 600 pixels de largura por 650 de altura

        dialog.setLocationRelativeTo(null);
        // centraliza a janela no meio da tela do computador

        dialog.setVisible(true);
        // mostra a janela na tela; como e um "dialog" travado, o programa espera aqui
        // ate o jogador terminar de posicionar todos os navios (ou fechar a janela)
    }

    // ==========================================================
    // METODO 2 (NOVO): posiciona a frota sozinho, sorteando as
    // posicoes dos navios, sem precisar de cliques do jogador.
    // ==========================================================
    static void posicionarFrotaAleatoria(char[][] tab, int TAM, int[] navios) {
        // tab    = matriz onde vamos marcar os navios sorteados
        // TAM    = tamanho do tabuleiro
        // navios = lista com o tamanho de cada navio

        Random rnd = new Random();
        // cria um "gerador de sorteios" que vamos usar para sortear posicoes

        for (int tam : navios) {
            // percorre cada tamanho de navio da lista, um de cada vez (ex: primeiro o 4, depois o 3, depois o 2)

            boolean posicionado = false;
            // guarda se este navio ja foi colocado com sucesso; comeca como "ainda nao"

            while (!posicionado) {
                // repete este bloco enquanto o navio ainda nao tiver sido posicionado

                boolean horizontal = rnd.nextBoolean();
                // sorteia true ou false para decidir se o navio fica deitado ou em pe

                int lin = rnd.nextInt(TAM);
                // sorteia um numero de linha entre 0 e TAM-1

                int col = rnd.nextInt(TAM);
                // sorteia um numero de coluna entre 0 e TAM-1

                boolean cabe = true;
                // comeca supondo que a posicao sorteada e valida

                for (int k = 0; k < tam; k++) {
                    // percorre cada pedaco do navio para verificar se cabe (mesma logica do metodo manual)
                    int l = horizontal ? lin : lin + k;
                    // calcula a linha de cada pedaco, dependendo da orientacao sorteada
                    int c = horizontal ? col + k : col;
                    // calcula a coluna de cada pedaco, dependendo da orientacao sorteada
                    if (l < 0 || l >= TAM || c < 0 || c >= TAM || tab[l][c] == 'N') cabe = false;
                    // se sair do tabuleiro ou encostar em outro navio, marca como invalido
                }

                if (cabe) {
                    // se a posicao sorteada for valida
                    for (int k = 0; k < tam; k++) {
                        // percorre de novo cada pedaco do navio para marca-lo na matriz
                        int l = horizontal ? lin : lin + k;
                        int c = horizontal ? col + k : col;
                        tab[l][c] = 'N';
                        // marca esta posicao como 'N' (navio) na matriz
                    }
                    posicionado = true;
                    // marca que este navio ja foi posicionado, para sair do "while"
                }
                // se "cabe" for falso, o "while" volta ao inicio e sorteia outra posicao
            }
        }
    }

    // ==========================================================
    // METODO 3 (NOVO): pergunta ao jogador se ele prefere
    // posicionar os navios manualmente ou deixar sortear.
    // ==========================================================
    static void definirFrota(char[][] tab, int jogador, int TAM, int[] navios) {
        // tab, jogador, TAM, navios: mesmos significados dos metodos anteriores

        int escolha = JOptionPane.showConfirmDialog(null,
                "Jogador " + jogador + ", deseja posicionar seus navios manualmente?\n" +
                        "(Sim = manual, Nao = sortear automaticamente)",
                "Posicionamento da frota",
                JOptionPane.YES_NO_OPTION);
        // mostra uma caixinha de pergunta com botoes "Sim" e "Nao"
        // o resultado (qual botao foi clicado) fica guardado na variavel "escolha"

        if (escolha == JOptionPane.YES_OPTION) {
            // se o jogador clicou em "Sim"
            posicionarFrota(tab, jogador, TAM, navios);
            // chama o metodo de posicionamento manual (clicando no tabuleiro)
        } else {
            // se o jogador clicou em "Nao" (ou fechou a caixinha)
            posicionarFrotaAleatoria(tab, TAM, navios);
            // chama o metodo que sorteia a posicao dos navios
            JOptionPane.showMessageDialog(null, "Frota do Jogador " + jogador + " sorteada automaticamente!");
            // avisa o jogador que a frota foi sorteada
        }
    }

    // ==========================================================
    // METODO PRINCIPAL: aqui o jogo realmente comeca e roda.
    // ==========================================================
    public static void main(String[] args) {
        // metodo especial: e o primeiro codigo que o Java executa quando o programa comeca

        int TAM = 10;
        // define o tamanho do tabuleiro: 10 linhas por 10 colunas

        int[] navios = {4, 3, 2};
        // define a frota: um navio de tamanho 4, um de tamanho 3 e um de tamanho 2

        int totalNavio = navios[0] + navios[1] + navios[2];
        // soma os tamanhos dos navios (4+3+2=9), que e o total de "pedacos" que precisam ser acertados para vencer

        int MAX_TENTATIVAS = 20;
        // define o numero maximo de tiros que cada jogador pode dar (NOVO)

        char[][] tab1 = new char[TAM][TAM];
        // cria a matriz que vai guardar onde estao os navios do Jogador 1

        char[][] tab2 = new char[TAM][TAM];
        // cria a matriz que vai guardar onde estao os navios do Jogador 2

        char[][] ataque1 = new char[TAM][TAM];
        // cria a matriz que vai guardar os tiros que o Jogador 1 deu no tabuleiro do Jogador 2

        char[][] ataque2 = new char[TAM][TAM];
        // cria a matriz que vai guardar os tiros que o Jogador 2 deu no tabuleiro do Jogador 1

        for (int i = 0; i < TAM; i++) {
            // percorre cada linha das matrizes
            for (int j = 0; j < TAM; j++) {
                // percorre cada coluna das matrizes
                tab1[i][j] = '~';
                // marca esta posicao do tabuleiro do Jogador 1 como agua (vazio)
                tab2[i][j] = '~';
                // marca esta posicao do tabuleiro do Jogador 2 como agua (vazio)
                ataque1[i][j] = '~';
                // marca esta posicao dos tiros do Jogador 1 como "ainda nao atirou aqui"
                ataque2[i][j] = '~';
                // marca esta posicao dos tiros do Jogador 2 como "ainda nao atirou aqui"
            }
        }

        // ---------- Cada jogador posiciona sua frota (manual ou sorteada) ----------

        JOptionPane.showMessageDialog(null, "Jogador 1, posicione sua frota.");
        // mostra uma mensagem avisando que e a vez do Jogador 1 posicionar os navios

        definirFrota(tab1, 1, TAM, navios);
        // chama o metodo que pergunta e organiza o posicionamento da frota do Jogador 1

        JOptionPane.showMessageDialog(null, "Passe o computador para o Jogador 2.");
        // mostra uma mensagem pedindo para passar o computador para o outro jogador

        definirFrota(tab2, 2, TAM, navios);
        // chama o metodo que pergunta e organiza o posicionamento da frota do Jogador 2

        JOptionPane.showMessageDialog(null, "Frotas posicionadas! Jogador 1 ataca primeiro.\n" +
                "Cada jogador tem no maximo " + MAX_TENTATIVAS + " tentativas.");
        // avisa que as duas frotas ja estao prontas e o jogo de ataques vai comecar

        // ---------- Variaveis que guardam o estado atual do jogo ----------

        int[] turno = {1};
        // guarda de quem e a vez agora (1 ou 2); comeca com o Jogador 1
        // (e um vetor de 1 posicao para poder mudar o valor dentro dos cliques dos botoes)

        int[] acertos1 = {0};
        // guarda quantos pedacos de navio o Jogador 1 ja acertou no Jogador 2; comeca em 0

        int[] acertos2 = {0};
        // guarda quantos pedacos de navio o Jogador 2 ja acertou no Jogador 1; comeca em 0

        int[] tentativas1 = {0};
        // (NOVO) guarda quantos tiros o Jogador 1 ja deu no total; comeca em 0

        int[] tentativas2 = {0};
        // (NOVO) guarda quantos tiros o Jogador 2 ja deu no total; comeca em 0

        // ---------- Monta a janela principal do jogo (onde acontecem os ataques) ----------

        JFrame janela = new JFrame("Batalha Naval - 2 Jogadores");
        // cria a janela principal do jogo, com titulo "Batalha Naval - 2 Jogadores"

        JLabel status = new JLabel("Vez do Jogador 1 - atire na frota do Jogador 2 (tentativa 1/" + MAX_TENTATIVAS + ")", SwingConstants.CENTER);
        // cria o texto de status no topo, mostrando de quem e a vez e quantas tentativas ja foram usadas

        JPanel grade = new JPanel(new GridLayout(TAM, TAM));
        // cria o painel em grade (o tabuleiro visual onde os jogadores vao atirar)

        JButton[][] botoes = new JButton[TAM][TAM];
        // cria a matriz que vai guardar o botao de cada quadrado do tabuleiro de ataque

        for (int i = 0; i < TAM; i++) {
            // percorre cada linha do tabuleiro
            for (int j = 0; j < TAM; j++) {
                // percorre cada coluna do tabuleiro

                JButton botao = new JButton("~");
                // cria um botao com o texto "~" (representando agua/desconhecido)

                int lin = i, col = j;
                // guarda a posicao deste botao especifico (linha e coluna)

                botao.addActionListener(e -> {
                    // define o que acontece quando o jogador clica neste quadrado para atirar

                    char[][] alvoNavios = (turno[0] == 1) ? tab2 : tab1;
                    // escolhe em qual tabuleiro de navios vamos verificar o tiro:
                    // se e a vez do Jogador 1, ataca o tabuleiro do Jogador 2, e vice-versa

                    char[][] alvoAtaque = (turno[0] == 1) ? ataque1 : ataque2;
                    // escolhe em qual matriz de tiros vamos anotar este ataque

                    if (alvoAtaque[lin][col] != '~') return;
                    // se este quadrado ja foi atacado antes (nao esta mais como '~'), nao faz nada e sai do metodo

                    boolean acertou = alvoNavios[lin][col] == 'N';
                    // verifica se, na posicao clicada, existe um navio ('N'); guarda o resultado em "acertou"

                    if (acertou) {
                        // se acertou um navio
                        alvoAtaque[lin][col] = 'X';
                        // marca esta posicao na matriz de tiros como 'X' (acerto)
                        botao.setText("🚢");
                        // muda o texto do botao para o desenho de navio
                        botao.setBackground(Color.RED);
                        // pinta o botao de vermelho, indicando acerto
                        if (turno[0] == 1) acertos1[0]++; else acertos2[0]++;
                        // soma 1 acerto para quem esta jogando agora
                    } else {
                        // se nao acertou (foi na agua)
                        alvoAtaque[lin][col] = 'O';
                        // marca esta posicao na matriz de tiros como 'O' (agua)
                        botao.setText("🌊");
                        // muda o texto do botao para o desenho de onda
                        botao.setBackground(Color.BLUE);
                        // pinta o botao de azul, indicando agua
                    }

                    if (turno[0] == 1) tentativas1[0]++; else tentativas2[0]++;
                    // (NOVO) soma 1 tentativa para quem acabou de atirar, tenha acertado ou nao

                    JOptionPane.showMessageDialog(janela, acertou ? "Acertou um navio!" : "Tiro na agua!");
                    // mostra uma mensagem avisando se foi acerto ou tiro na agua

                    int acertosAtual = (turno[0] == 1) ? acertos1[0] : acertos2[0];
                    // guarda quantos acertos o jogador atual ja tem, para comparar a seguir

                    if (acertosAtual == totalNavio) {
                        // se o jogador atual ja acertou a quantidade total de pedacos de navio (venceu)
                        JOptionPane.showMessageDialog(janela, "Jogador " + turno[0] + " venceu! Afundou toda a frota inimiga!");
                        // mostra a mensagem de vitoria
                        for (JButton[] linhaBotoes : botoes)
                            // percorre cada linha da matriz de botoes
                            for (JButton b : linhaBotoes) b.setEnabled(false);
                        // desativa cada botao dessa linha, para o jogo nao continuar sendo clicado
                        return;
                        // sai do metodo, encerrando esta jogada (o jogo terminou)
                    }

                    int tentativasAtual = (turno[0] == 1) ? tentativas1[0] : tentativas2[0];
                    // (NOVO) guarda quantas tentativas o jogador atual ja usou, para comparar a seguir

                    if (tentativasAtual >= MAX_TENTATIVAS) {
                        // (NOVO) se o jogador atual ja usou todas as tentativas permitidas
                        JOptionPane.showMessageDialog(janela,
                                "Jogador " + turno[0] + " atingiu o numero maximo de tentativas (" + MAX_TENTATIVAS + ")!\n" +
                                        "Fim de jogo.");
                        // mostra a mensagem avisando que o jogo acabou por limite de tentativas
                        for (JButton[] linhaBotoes : botoes)
                            // percorre cada linha da matriz de botoes
                            for (JButton b : linhaBotoes) b.setEnabled(false);
                        // desativa cada botao dessa linha, encerrando o jogo
                        return;
                        // sai do metodo, encerrando esta jogada (o jogo terminou)
                    }

                    turno[0] = (turno[0] == 1) ? 2 : 1;
                    // troca a vez: se era do Jogador 1, passa para o Jogador 2, e vice-versa

                    int proximaTentativa = ((turno[0] == 1) ? tentativas1[0] : tentativas2[0]) + 1;
                    // calcula qual sera o numero da proxima tentativa do jogador que vai jogar agora
                    // (soma 1 porque ele ainda nao deu esse tiro, isso e so para mostrar na tela)

                    status.setText("Vez do Jogador " + turno[0] + " - atire na frota do Jogador " + (turno[0] == 1 ? 2 : 1) +
                            " (tentativa " + proximaTentativa + "/" + MAX_TENTATIVAS + ")");
                    // atualiza o texto de status no topo da janela com a nova vez e o numero da tentativa

                    JOptionPane.showMessageDialog(janela, "Passe o computador para o Jogador " + turno[0]);
                    // mostra uma mensagem pedindo para passar o computador para o proximo jogador

                    char[][] novoAtaque = (turno[0] == 1) ? ataque1 : ataque2;
                    // escolhe a matriz de tiros do jogador que vai jogar agora, para redesenhar o tabuleiro dele

                    for (int x = 0; x < TAM; x++) {
                        // percorre cada linha do tabuleiro
                        for (int y = 0; y < TAM; y++) {
                            // percorre cada coluna do tabuleiro
                            char v = novoAtaque[x][y];
                            // pega o valor guardado nessa posicao ('X', 'O' ou '~')
                            botoes[x][y].setText(v == 'X' ? "🚢" : (v == 'O' ? "🌊" : "~"));
                            // atualiza o texto do botao de acordo com o valor: navio, agua ou vazio
                            botoes[x][y].setBackground(v == 'X' ? Color.RED : (v == 'O' ? Color.BLUE : null));
                            // atualiza a cor do botao de acordo com o valor: vermelho, azul ou cor padrao
                        }
                    }
                });

                botoes[i][j] = botao;
                // guarda este botao na matriz de botoes, na posicao [i][j]
                grade.add(botao);
                // adiciona este botao na grade visual (para aparecer na tela)
            }
        }

        janela.setLayout(new BorderLayout());
        // define que a janela principal vai se organizar em areas: norte, sul, centro, etc

        janela.add(status, BorderLayout.NORTH);
        // coloca o texto de status na area de cima (norte) da janela

        janela.add(grade, BorderLayout.CENTER);
        // coloca o tabuleiro (grade de botoes) no meio (centro) da janela

        janela.setSize(600, 650);
        // define o tamanho da janela: 600 pixels de largura por 650 de altura

        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // define que, ao fechar esta janela, o programa inteiro deve ser encerrado

        janela.setVisible(true);
        // mostra a janela principal na tela, deixando o jogo pronto para ser jogado
    }
}