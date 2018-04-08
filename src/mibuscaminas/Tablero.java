/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mibuscaminas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

//Diferents estats en que es poden trobar les caselles del taulell
enum Estat {
    BOMBA(-1),
    CERO(0),
    UNO(1),
    DOS(2),
    TRES(3),
    CUATRO(4),
    CINCO(5),
    SEIS(6),
    SIETE(7),
    OCHO(8);

    private final int valor;

    Estat(int v) {
        valor = v;
    }

    //Passem d'un estat al següent (sumem 1)
    public Estat sumarUn() {
        Estat seg = this;
        switch (valor) {
            case 0:
                seg = UNO;
                break;
            case 1:
                seg = DOS;
                break;
            case 2:
                seg = TRES;
                break;
            case 3:
                seg = CUATRO;
                break;
            case 4:
                seg = CINCO;
                break;
            case 5:
                seg = SEIS;
                break;
            case 6:
                seg = SIETE;
                break;
            case 7:
                seg = OCHO;
                break;
        }
        return seg;
    }

}

/**
 *
 * @author profe
 */
public class Tablero extends JPanel {

    //Número de files/columnes del taulell
    final static int DIM = 16;
    //Número de bombes del taulell
    final static int NUM_BOMBAS = 40;
    //Taulell de joc
    private static Casella[][] tablero = new Casella[DIM][DIM];
    //Per saber quan s'ha acabat el joc
    private boolean gameOver = false;
    //Per saber si hem guanyat o hem perdut
    private boolean ganador = false;
    //Quan estiguem testejant el posem a true, sinó false
    boolean test = false;
    //Comptador de caselles marcades en el botó dret del ratolí
    private int contMarcadas = 0;
    //Comptador de caselles marcades en el botó dret del ratolí i que contenen una bomba
    private int contMarcadasCorrectas = 0;
    //Comptador de caselles clicades o descobertes
    private static int contClicadas = 0;

    public Tablero() {
        initUI();
        accionsRatoli();
    }

    //Mètode que conté el codi per tractar les accions fetes en el ratolí
    public void accionsRatoli() {

        MouseAdapter mouseAdapter = new MouseAdapter() {
            //Quan fem clic en el ratolí sobre el taulell s'executarà el següent codi
            @Override
            public void mouseClicked(MouseEvent e) {

                //Variables locals del mètode
                Casella seleccionat = null;     //per guardar la acsella seleccionada del taulell
                Image img = null;               //imatge a posar a la casella
                int x = e.getX();               //coordenada x on hem clicat
                int y = e.getY();               //coordenada y on hem clicat

                //Si s'ha acabat la partida els clics no actuen
                if (gameOver) {
                    return;
                }

                //Busquem la casella seleccionada a partir de la coordenada x i y clicades
                int i = 0, j = 0;
                //Etiqueta que permet sortir des d'un for intern cap un d'extern
                fuera:
                for (i = 0; i < tablero.length; i++) {
                    for (j = 0; j < tablero[i].length; j++) {
                        //El mètode clicada(...) és true si és la casella seleccionada, quan la trobem parem     
                        if (tablero[i][j].clicada(x, y)) {
                            seleccionat = tablero[i][j];
                            //Sortim fora del primer for, gràcies a l'etiqueta
                            break fuera;
                        }
                    }
                }

                //Si han apretado el botón izquierdo
                if (e.getButton() == MouseEvent.BUTTON1) {
                    //Si hem seleccionat casella, i no está marcada ni clicada actuem en el clic
                    if (seleccionat != null && !seleccionat.marcada && !seleccionat.clicada) {
                        //Incrementem el comptador de caselles clicades
                        contClicadas++;
                        //Mirem què hi ha a la casella i actuem en conseqüència
                        switch (seleccionat.estat) {
                            case BOMBA:
                                //Si hi havia una bomba el joc s'ha acabat i hem perdut
                                gameOver = true;
                                break;
                            default:
                                //Si no marquem la casella com a clicada i li posem la imatge 
                                //correponent al seu estat
                                seleccionat.clicada = true;
                                seleccionat.setImg(seleccionat.buscaImagen());
                                //Mirem si s'han de mostrar els veïns 
                                Tablero.actualitzarVeins(i, j);
                                //Si és la última clicada i hem marcat totes les caselles en bomba el joc s'ha acabat
                                //i hem guanyat
                                if (contClicadas == DIM * DIM - NUM_BOMBAS && contMarcadasCorrectas == NUM_BOMBAS) {
                                    ganador = gameOver = true;
                                }
                        }
                        //Actualitzem el taulell
                        repaint();
                    }
                } else //Si han apretado el botón derecho --> per marcar la casella com a que té una bomba
                if (e.getButton() == MouseEvent.BUTTON3) {
                    //Si s'ha seleccionat alguna casella no clicada actuem
                    if (seleccionat != null && !seleccionat.clicada) {
                        //Si intentem marcar una quant ja les haviem marcat totes no ho dixem fer
                        if (!seleccionat.marcada && contMarcadas == NUM_BOMBAS) {
                            return;
                        }
                        //Marquem o desmarquem la casella
                        seleccionat.marcada = !seleccionat.marcada;
                        //Posem la imatge de marcada o desmarcada
                        img = (seleccionat.marcada ? new ImageIcon("src/imagenes/11.png").getImage() : new ImageIcon("src/imagenes/10.png").getImage());
                        seleccionat.setImg(img);
                        //Incrementem o decrementem els comptadors de marcades totals i correctes 
                        contMarcadas += (seleccionat.marcada ? 1 : -1);
                        if (seleccionat.estat == Estat.BOMBA) {
                            contMarcadasCorrectas += (seleccionat.marcada ? 1 : -1);
                        }
                        //Si és l'última marcada i totes les caselles en bomba són correctes el joc s'ha acabat
                        //i hem guanyat
                        if (contClicadas == DIM * DIM - NUM_BOMBAS && contMarcadasCorrectas == NUM_BOMBAS) {
                            ganador = gameOver = true;
                        }
                        //Actualitzem el taulell
                        repaint();
                    }
                }
            }

        };
        //Assignem al JPanel el codi anterior
        addMouseListener(mouseAdapter);

    }

    //Inicialitzacions a fer quan creem el taulell
    private void initUI() {
        //Per col·locar les bombes aleatòriament al taulell
        Random r = new Random();

        //Imatge inicial que posem a les caselles, que és la de la casella sense clicar
        Image mshi = new ImageIcon("src/imagenes/10.png").getImage();

        //Altura i amplada de la imatge, per posicionar-les bé al taulell
        int height = mshi.getWidth(null);
        int width = mshi.getWidth(null);

        //Si hi ha més o igual número de caselles en bombes que totals no cal ni començar a jugar
        if (NUM_BOMBAS >= DIM * DIM) {
            JOptionPane.showMessageDialog(null, "Tablero de " + DIM + "x" + DIM + "=" + (DIM * DIM) + " casillas con " + NUM_BOMBAS + " bombas,\n reduce el número de bombas o aumenta\n el tamaño del tablero!!");
            System.exit(0);
        }

        //Omplo el taulell
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                //Les noves caselles tenen l'estat a CERO
                tablero[i][j] = new Casella(i * width, j * height, mshi);
            }
        }

        //Col·loco les bombes aleatòriament
        int k = 1;
        while (k <= NUM_BOMBAS) {
            //Calculem una posició del taulell aleatòriament
            int i = r.nextInt(DIM);
            int j = r.nextInt(DIM);
            //Només tractarem la casella posant una bomba si no tenia ja una abans 
            if (tablero[i][j].estat != Estat.BOMBA) {
                tablero[i][j].estat = Estat.BOMBA;
                //Quan posem una bomba a una casella hem d'actualitzar l'estat de les que l'envolten
                actualitzarCaselles(i, j);
                k++;
            }

        }

    }

    //Recorrem les caselles que envolten a la que li hem posat una bomba i actualitzem el seu estat
    public static void actualitzarCaselles(int i, int j) {
        for (int k = dec(i); k <= inc(i); k++) {
            for (int l = dec(j); l <= inc(j); l++) {
                if (k != i || l != j) {
                    //Incrementem el número de bombes (estat) de la casella ja que en té una més al costat
                    tablero[k][l].estat = tablero[k][l].estat.sumarUn();
                }
            }
        }
    }

    //Si el valor no és cero el decrementem
    private static int dec(int valor) {
        if (valor > 0) {
            valor--;
        }
        return valor;

    }

    //Si el valor no és la última casella l'incrementem
    private static int inc(int valor) {
        if (valor < DIM - 1) {
            valor++;
        }
        return valor;
    }

    //Mètode que s'executa automàticament quan dibuixem el taulell
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //El que volem que passe al dibuixar ho separo en en una altre mètode
        dibuixa(g);
    }

    private void dibuixa(Graphics g) {
        //Convertim el gràfic normal a 2D i fixem les propietats
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //Imatge de casella verge
        Image mshi = new ImageIcon("src/imagenes/10.png").getImage();

        //Dibuixo el taulell en les caselles amagades i clicades
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                //Si el joc no s'ha acabat mostrem les caselles clicades, marcades i no clicades
                if (!gameOver) {
                    if (tablero[i][j].marcada) {
                        tablero[i][j].setImg(new ImageIcon("src/imagenes/11.png").getImage());
                    }
                    tablero[i][j].dibuja(g2d);
                } else {    //Si el joc s'ha acabat mostrem el resultat final
                    //Les caselles marcades canvien la imatge segons si tenien bomba o no
                    if (tablero[i][j].marcada) {
                        if (tablero[i][j].estat == Estat.BOMBA) {
                            tablero[i][j].setImg(new ImageIcon("src/imagenes/11.png").getImage());
                        } else {
                            tablero[i][j].setImg(new ImageIcon("src/imagenes/12.png").getImage());
                        }
                        tablero[i][j].dibuja(g2d);
                    } else { //Les no marcades
                        //Si tenen bomba les mostrem
                        if (tablero[i][j].estat == Estat.BOMBA) {
                            tablero[i][j].setImg(new ImageIcon("src/imagenes/9.png").getImage());
                            tablero[i][j].dibuja(g2d);
                        } else {
                            //I si no tenen bomba i estan clicades les mostrem, i sinó no
                            if (tablero[i][j].clicada) {
                                tablero[i][j].dibuja(g2d);
                                //tablero[i][j].dibuja(g2d, DIM + 1, tablero[i][j].buscaImagen());
                            } else {
                                tablero[i][j].setImg(new ImageIcon("src/imagenes/10.png").getImage());
                                tablero[i][j].dibuja(g2d);
                            }
                        }

                    }

                }
            }
        }
        //Si estem provant l'aplicació baix mostrem el taulell resolt
        if (test) {
            for (int i = 0; i < DIM; i++) {
                for (int j = 0; j < DIM; j++) {
                    tablero[i][j].dibuja(g2d, DIM + 1, tablero[i][j].buscaImagen());
                }
            }
        }

        //Baix de tot mostrem un text en l'estat de la partida
        Font font = new Font("Serif", Font.BOLD, 15);
        g2d.setFont(font);
        g2d.setPaint(new Color(0, 0, 0));
        String mensaje;
        if (!gameOver) {
            mensaje = "Bombas marcadas: " + contMarcadas + "/" + NUM_BOMBAS;
        } else {
            if (!ganador) {
                mensaje = "Has perdido...";
            } else {
                mensaje = "Has ganado!!";
            }
        }
        if (test) {
            g2d.drawString(mensaje, 0, (DIM * 2 + 3) * mshi.getHeight(null));
        } else {
            g2d.drawString(mensaje, 0, (DIM + 2) * mshi.getHeight(null));
        }
    }


    //Les caselles que envolten a la clicada s'han de tractar segons el seu estat
    private static void tractarCasella(int i, int j) {
        //Si està clicada, marcada, o conté una bomba no la tractarem
        if (tablero[i][j].clicada || tablero[i][j].marcada || tablero[i][j].estat == Estat.BOMBA) {
            return;
        }
        //Marquem la casella com a clicada i actualitzem el comptador
        tablero[i][j].clicada = true;
        contClicadas++;
        //Li posem la imatge adequada per indicar el seu estat
        tablero[i][j].setImg(tablero[i][j].buscaImagen());
        //Si l'estat de la casella és CERO actualitzem el seus veïns recursivament 
        if (tablero[i][j].estat == Estat.CERO) {
            actualitzarVeins(i, j);
        }

    }
    
    //Tractem les caselles que envolten a la que passem
    private static void actualitzarVeins(int i, int j) {
        tractarCasella(dec(i), dec(j));
        if (j != 0) {
            tractarCasella(dec(i), j);
        }
        if (j != DIM - 1) {
            tractarCasella(dec(i), inc(j));
        }
        if (i != 0) {
            if (j != 0) {
                tractarCasella(i, dec(j));
            }
            if (j != DIM - 1) {
                tractarCasella(i, inc(j));
            }
        }
        if (i != DIM - 1) {
            if (j != 0) {
                tractarCasella(inc(i), dec(j));
            }
            tractarCasella(inc(i), j);
            if (j != DIM - 1) {
                tractarCasella(inc(i), inc(j));
            }
        }
    }

    //Classe que representa les caselles del taulell
    class Casella extends Rectangle2D.Float {

        private Graphics2D g2d;
        private Image img;                  //imatge que posem a la casella
        private float x;                    //coordenada x de la casella
        private float y;                    //coordenada x de la casella
        private Estat estat;                //estat en el que es troba la casella
        private boolean clicada = false;    //per saber si l'han clicat
        private boolean marcada = false;    //per saber si l'han marcat

        //Creem la casella posant un requadre i l'estat a CERO
        public Casella(float xpos, float ypos, float width, float height) {
            x = xpos;
            y = ypos;
            setRect(x, y, width, height);
            estat = Estat.CERO;
        }

        //Creem la casella assignant imatge, que fixa també el tamany    
        public Casella(float x, float y, Image imagen) {
            this(x, y, imagen.getWidth(null), imagen.getHeight(null));
            img = imagen;
        }

        //Dibuixem la casella juntant la forma en la imatge que conté la seua propietat imatge
        public void dibuja(Graphics2D g) {
            g.fill(this);
            g.drawImage(img, (int) x, (int) y, null);
        }

        //Retornem una nova casella en les mateixes propietats que la actual però situada unes caselles més avall 
        public Casella copia(int numcas) {
            return new Casella(this.x, this.y + img.getHeight(null) * numcas, this.img);
        }

        //Dibuixem la casella juntant la forma en la imatge que passem com a paràmetre
        public void dibuja(Graphics2D g, int numcas, Image imagen) {
            Casella copia = this.copia(numcas);
            g.fill(copia);
            g.drawImage(imagen, (int) copia.x, (int) copia.y, null);
        }

        //Indica si les coordenades x i y formen part de la casella
        public boolean clicada(float x, float y) {
            return getBounds2D().contains(x, y);
        }

        public void setImg(Image img) {
            this.img = img;
        }

        //Retornem la imatge que representa l'estat de la casella
        public Image buscaImagen() {
            Image mshi = null;
            switch (this.estat) {
                case CERO:
                    mshi = new ImageIcon("src/imagenes/0.png").getImage();
                    break;
                case UNO:
                    mshi = new ImageIcon("src/imagenes/1.png").getImage();
                    break;
                case DOS:
                    mshi = new ImageIcon("src/imagenes/2.png").getImage();
                    break;
                case TRES:
                    mshi = new ImageIcon("src/imagenes/3.png").getImage();
                    break;
                case CUATRO:
                    mshi = new ImageIcon("src/imagenes/4.png").getImage();
                    break;
                case CINCO:
                    mshi = new ImageIcon("src/imagenes/5.png").getImage();
                    break;
                case SEIS:
                    mshi = new ImageIcon("src/imagenes/6.png").getImage();
                    break;
                case SIETE:
                    mshi = new ImageIcon("src/imagenes/7.png").getImage();
                    break;
                case OCHO:
                    mshi = new ImageIcon("src/imagenes/8.png").getImage();
                    break;
                case BOMBA:
                    mshi = new ImageIcon("src/imagenes/9.png").getImage();
                    break;
            }
            return mshi;
        }
    }
}
