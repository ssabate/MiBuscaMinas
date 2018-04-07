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

    public Estat sumar() {
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

    private final static int NUM_BOTONS = 4;

    final static int DIM = 16;
    final static int NUM_BOMBAS = 40;
    private ZRectangle zrect;
    private ZEllipse zell;
    Image mshi = new ImageIcon("src/imagenes/10.png").getImage();
    private static ZRectangle[][] tablero = new ZRectangle[DIM][DIM];
    private boolean gameOver = false;
    private boolean ganador = false;
    boolean test = false;
    private int contMarcadas = 0;
    private int contMarcadasCorrectas = 0;
    private static int contClicadas = 0;

    public Tablero() {
        initUI();
    }

    private void initUI() {
        Random r = new Random();
        MovingAdapter ma = new MovingAdapter();

        addMouseMotionListener(ma);
        addMouseListener(ma);
        //addMouseWheelListener(new ScaleHandler());

        //zrect = new ZRectangle(50, 260, 25, 25);
//        zell = new ZEllipse(150, 260, 25, 25);
//        mshi = new ImageIcon("src/imagenes/10.png").getImage();
//        zrect = new ZRectangle(50, 260, mshi);
//        
        if (NUM_BOMBAS >= DIM * DIM) {
            JOptionPane.showMessageDialog(null, "Tablero de " + DIM + "x" + DIM + "=" + (DIM * DIM) + " casillas con " + NUM_BOMBAS + " bombas,\n reduce el número de bombas o aumenta\n el tamaño del tablero!!");
            System.exit(0);
        }
        //Omplo el taulell
        mshi = new ImageIcon("src/imagenes/10.png").getImage();
        int width = mshi.getWidth(null);
        //Omplo el taulell
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                tablero[i][j] = new ZRectangle(i * width, j * width, mshi);
            }
        }

        //Col·loco les bombes aleatòriament
        for (int k = 1; k <= NUM_BOMBAS;) {
            int i = r.nextInt(DIM);
            int j = r.nextInt(DIM);
            if (tablero[i][j].estat != Estat.BOMBA) {
                tablero[i][j].estat = Estat.BOMBA;
                actualizarCasillas(i, j);
                k++;
            }

        }

    }

    private static void tratarCelda(int i, int j) {
        if (tablero[i][j].clicada || tablero[i][j].marcada || tablero[i][j].estat == Estat.BOMBA) {
            return;
        }
        System.out.println("Trato: " + i + "-" + j);
        tablero[i][j].clicada = true;
        contClicadas++;
        tablero[i][j].setImg(tablero[i][j].buscaImagen());
        //if (tablero[i][j].estat == Estat.CERO) {
        actualizarVecinos(i, j);
        //}

    }

    private static void actualizarVecinos(int i, int j) {
        tratarCelda(dec(i), dec(j));
        if (j != 0) {
            tratarCelda(dec(i), j);
        }
        if (j != DIM - 1) {
            tratarCelda(dec(i), inc(j));
        }
        if (i != 0) {
            if (j != 0) {
                tratarCelda(i, dec(j));
            }
            if (j != DIM - 1) {
                tratarCelda(i, inc(j));
            }
        }
        if (i != DIM - 1) {
            if (j != 0) {
                tratarCelda(inc(i), dec(j));
            }
            tratarCelda(inc(i), j);
            if (j != DIM - 1) {
                tratarCelda(inc(i), inc(j));
            }
        }
    }

    public static void actualizarCasillas(int i, int j) {
        for (int k = dec(i); k <= inc(i); k++) {
            for (int l = dec(j); l <= inc(j); l++) {
                if (k != i || l != j) {
                    tablero[k][l].estat = tablero[k][l].estat.sumar();
                }
            }
//                switch(tablero[k][l].estat){
//                    case CERO:
//                        tablero[k][l].estat=tablero[k][l].estat.sumar();//Estat.UNO;
//                        break;
//                    case UNO:
//                        tablero[k][l].estat=Estat.DOS;
//                        break;
//                    case DOS:
//                        tablero[k][l].estat=Estat.TRES;
//                        break;
//                    case TRES:
//                        tablero[k][l].estat=Estat.CUATRO;
//                        break;
//                }

        }
    }

    private static int dec(int valor) {

        if (valor > 0) {
            valor--;
        }
        return valor;

    }

    private static int inc(int valor) {

        if (valor < DIM - 1) {
            valor++;
        }
        return valor;

    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //Imatge de casella verge
        mshi = new ImageIcon("src/imagenes/10.png").getImage();
        //Dibuixo un taulell en caselles amagades i un altre en descobertes
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (!gameOver) {
                    if (tablero[i][j].marcada) {
                        tablero[i][j].setImg(new ImageIcon("src/imagenes/11.png").getImage());
                    }
                    tablero[i][j].dibuja(g2d);
                } else {
                    if (tablero[i][j].marcada) {
                        if (tablero[i][j].estat == Estat.BOMBA) {
                            tablero[i][j].setImg(new ImageIcon("src/imagenes/11.png").getImage());
                        } else {
                            tablero[i][j].setImg(new ImageIcon("src/imagenes/12.png").getImage());
                        }
                        tablero[i][j].dibuja(g2d);
                    } else {
                        if (tablero[i][j].estat == Estat.BOMBA) {
                            tablero[i][j].setImg(new ImageIcon("src/imagenes/9.png").getImage());
                            tablero[i][j].dibuja(g2d);
                        } else {
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
        if (test) {
            for (int i = 0; i < DIM; i++) {
                for (int j = 0; j < DIM; j++) {
                    //if((i+j)%2==0)
                    //    g2d.setPaint(new Color(0,0,0));
                    //else g2d.setPaint(new Color(255,255,255));
                    //g2d.fillRect(i*25, j*25, 25, 25);
                    //g2d.drawImage(mshi, i * width, j * width, null);
                    //tablero[i][j]=new ZRectangle(i * width, j * width, mshi);
//                switch (tablero[i][j].estat) {
//                    case CERO:
//                        mshi = new ImageIcon("src/imagenes/0.png").getImage();
//                        break;
//                    case UNO:
//                        mshi = new ImageIcon("src/imagenes/1.png").getImage();
//                        break;
//                    case DOS:
//                        mshi = new ImageIcon("src/imagenes/2.png").getImage();
//                        break;
//                    case TRES:
//                        mshi = new ImageIcon("src/imagenes/3.png").getImage();
//                        break;
//                    case CUATRO:
//                        mshi = new ImageIcon("src/imagenes/4.png").getImage();
//                        break;
//                    case CINCO:
//                        mshi = new ImageIcon("src/imagenes/5.png").getImage();
//                        break;
//                    case SEIS:
//                        mshi = new ImageIcon("src/imagenes/6.png").getImage();
//                        break;
//                    case SIETE:
//                        mshi = new ImageIcon("src/imagenes/7.png").getImage();
//                        break;
//                    case OCHO:
//                        mshi = new ImageIcon("src/imagenes/8.png").getImage();
//                        break;
//                    case BOMBA:
//                        mshi = new ImageIcon("src/imagenes/9.png").getImage();
//                        break;
//                }
                    //tablero[i][j].img = mshi;
                    tablero[i][j].dibuja(g2d, DIM + 1, tablero[i][j].buscaImagen());
                }
            }

        }
        //---------------
        //Prova p = new Prova(g2d, new ImageIcon("src/imagenes/1.png").getImage(), 10, 250);
        //p.dibuixa();
        Font font = new Font("Serif", Font.BOLD, 15);
        g2d.setFont(font);

        g2d.setPaint(new Color(0, 0, 200));
        //g2d.fill(zrect);
        //zrect.dibuja(g2d);
        g2d.setPaint(new Color(0, 0, 0));
        //g2d.fill(zell);
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
        if(test) g2d.drawString(mensaje, 0, (DIM * 2 + 3) * mshi.getHeight(null));
        else g2d.drawString(mensaje, 0, (DIM + 2) * mshi.getHeight(null));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    class MovingAdapter extends MouseAdapter {

        private int x;
        private int y;
        private final int NUM_BOTONS = MouseInfo.getNumberOfButtons();

        @Override
        public void mousePressed(MouseEvent e) {
            if (gameOver) {
                return;
            }
            ZRectangle seleccionat = null;
            Image img = null;
            x = e.getX();
            y = e.getY();
            //Buscamos la casilla seleccionada
            int i = 0, j = 0;
            fuera:
            for (i = 0; i < tablero.length; i++) {
                for (j = 0; j < tablero[i].length; j++) {
                    if (tablero[i][j].isHit(x, y)) {
                        seleccionat = tablero[i][j];
                        break fuera;
                    }

                }
            }
//            int temp=i;
//            i=j;
//            j=temp;
            System.out.println("Seleccion: " + i + "-" + j);
            //Si han apretado el botón izquierdo
            if (e.getButton() == MouseEvent.BUTTON1) {
                //Si no está marcada actuamos con el clic
                if (seleccionat != null && !seleccionat.marcada && !seleccionat.clicada) {
                    contClicadas++;
                    switch (seleccionat.estat) {
                        case BOMBA:
                            gameOver = true;//JOptionPane.showMessageDialog(null, "Game over");
                            break;
                        default:
                            seleccionat.clicada = true;
                            seleccionat.setImg(seleccionat.buscaImagen());
                            //Inexplicablemente me cambia la i y la j cuando DIM es par
                            //if(DIM%2!=0) 
                            Tablero.actualizarVecinos(i, j);
                            //else Tablero.actualizarVecinos(j, i);
                            if (contClicadas == DIM * DIM - NUM_BOMBAS && contMarcadasCorrectas == NUM_BOMBAS) {
                                ganador = gameOver = true;
                            }
                    }
                    repaint();
                }
            } else //Si han apretado el botón derecho
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (seleccionat != null && !seleccionat.clicada) {
                    if (!seleccionat.marcada && contMarcadas == NUM_BOMBAS) {
                        return;
                    }
                    seleccionat.marcada = !seleccionat.marcada;
                    img = (seleccionat.marcada ? new ImageIcon("src/imagenes/11.png").getImage() : new ImageIcon("src/imagenes/10.png").getImage());
                    seleccionat.setImg(img);
                    contMarcadas += (seleccionat.marcada ? 1 : -1);
                    if (seleccionat.estat == Estat.BOMBA) {
                        contMarcadasCorrectas += (seleccionat.marcada ? 1 : -1);
                    }
                    if (contClicadas == DIM * DIM - NUM_BOMBAS && contMarcadasCorrectas == NUM_BOMBAS) {
                        ganador = gameOver = true;
                    }
                    repaint();
                }
            }
        }

//        @Override
//        public void mouseDragged(MouseEvent e) {
//            System.out.println(zell.getX() + "-" + zell.getY());
//            System.out.println(zrect.getX() + "-" + zrect.getY());
//            System.out.println();
//            doMove(e);
//        }
//
//        @Override
//        public void mouseReleased(MouseEvent e) {
//            if (Math.floor(zell.getX()) % 25f != 0) {
//                zell.setX(25.0f * (float) (Math.floor(zell.getX() / 25f)));
//            }
//            if (Math.floor(zell.getY()) % 25f != 0) {
//                zell.setY(25.0f * (float) (Math.floor(zell.getY() / 25f)));
//            }
//            if (Math.floor(zrect.getX()) % 25f != 0) {
//                zrect.setX(25.0f * (float) (Math.floor(zrect.getX() / 25f)));
//            }
//            if (Math.floor(zrect.getY()) % 25f != 0) {
//                zrect.setY(25.0f * (float) (Math.floor(zrect.getY() / 25f)));
//            }
//            repaint(); //To change body of generated methods, choose Tools | Templates.
//        }
//
//        private void doMove(MouseEvent e) {
//
//            int dx = e.getX() - x;
//            int dy = e.getY() - y;
//
//            if (zrect.isHit(x, y)) {
//                if (!zell.intersects(zrect.x + dx, zrect.y + dy, zrect.getHeight(), zrect.getWidth())) {
//                    zrect.addX(dx);
//                    zrect.addY(dy);
//                }
//                repaint();
//            }
//
//            if (zell.isHit(x, y)) {
//                if (!zrect.intersects(zell.x + dx, zell.y + dy, zell.getHeight(), zell.getWidth())) {
//                    zell.addX(dx);
//                    zell.addY(dy);
//                }
//                repaint();
//            }
//
//            x += dx;
//            y += dy;
//        }
    }

    class ZRectangle extends Rectangle2D.Float {

        private Graphics2D g2d;
        private Image img;
        private float x;
        private float y;
        private Estat estat;
        private boolean clicada = false;
        private boolean marcada = false;

        public ZRectangle(float xpos, float ypos, float width, float height) {
            x = xpos;
            y = ypos;
            setRect(x, y, width, height);
            estat = Estat.CERO;
        }

        public ZRectangle(float x, float y, Image imagen) {

            this(x, y, imagen.getWidth(null), imagen.getHeight(null));
            img = imagen;

        }

        public void dibuja(Graphics2D g) {

            g.fill(this);
            g.drawImage(img, (int) x, (int) y, null);

        }

        public ZRectangle copia(int numcas) {

            return new ZRectangle(this.x, this.y + img.getHeight(null) * numcas, this.img);
        }

        public void dibuja(Graphics2D g, int numcas, Image imagen) {
            ZRectangle copia = this.copia(numcas);
            g.fill(copia);
            g.drawImage(imagen, (int) copia.x, (int) copia.y, null);

        }

        public boolean isHit(float x, float y) {

            return getBounds2D().contains(x, y);
        }

        public void addX(float x) {

            this.x += x;
        }

        public void setX(float x) {

            this.x = x;
        }

        public void addY(float y) {

            this.y += y;
        }

        public void setY(float y) {

            this.y = y;
        }

        public void addWidth(float w) {

            this.width += w;
        }

        public void addHeight(float h) {

            this.height += h;
        }

        public void setImg(Image img) {
            this.img = img;
        }

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

    class ZEllipse extends Ellipse2D.Float {

        public ZEllipse(float x, float y, float width, float height) {

            this.setFrame(x, y, width, height);
        }

        public boolean isHit(float x, float y) {

            return getBounds2D().contains(x, y);
        }

        public void addX(float x) {

            this.x += x;
        }

        public void setX(float x) {

            this.x = x;
        }

        public void addY(float y) {

            this.y += y;
        }

        public void setY(float y) {

            this.y = y;
        }

        public void addWidth(float w) {

            this.width += w;
        }

        public void addHeight(float h) {

            this.height += h;
        }
    }

}

class Prova {

    private Graphics2D g2d;
    private Image img;
    private int x;
    private int y;

    public Prova(Graphics2D g, Image mshi, int xpos, int ypos) {
        g2d = g;
        img = mshi;
        x = xpos;
        y = ypos;
    }

    public void dibuixa() {

        Graphics2D g = g2d;
        //(Graphics2D) g2d.create();
        //g.drawRect(x, y, img.getWidth(null), img.getHeight(null));
        g.drawImage(img, x, y, null);
        //g.dispose();
    }
}
