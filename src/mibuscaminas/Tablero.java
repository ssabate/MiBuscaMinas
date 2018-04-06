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
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


enum Estat {BOMBA, CERO, UNO, DOS, TRES, CUATRO };
/**
 *
 * @author profe
 */
public class Tablero extends JPanel {

    private final static int DIM=16;
    private final static int NUM_BOMBAS=16;
    private ZRectangle zrect;
    private ZEllipse zell;
    private Image mshi;
    private ZRectangle[][] tablero=new ZRectangle[DIM][DIM];

    public Tablero() {

        initUI();
    }

    private void initUI() {
        Random r=new Random();
        MovingAdapter ma = new MovingAdapter();

        addMouseMotionListener(ma);
        addMouseListener(ma);
        addMouseWheelListener(new ScaleHandler());

        //zrect = new ZRectangle(50, 260, 25, 25);
        zell = new ZEllipse(150, 260, 25, 25);
        mshi = new ImageIcon("src/imagenes/10.png").getImage();
        zrect = new ZRectangle(50, 260, mshi);
        
        //Omplo el taulell
        mshi = new ImageIcon("src/imagenes/10.png").getImage();
        int width = mshi.getWidth(null);
        //Omplo el taulell
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                tablero[i][j]=new ZRectangle(i * width, j * width, mshi);
            }
        }
        
        //Col·loco les bombes aleatòriament
        for (int k = 0; k < NUM_BOMBAS; ) {
            int i=r.nextInt(DIM);
            int j=r.nextInt(DIM);
            if(tablero[i][j].estat!=Estat.BOMBA){
                tablero[i][j].estat=Estat.BOMBA;
                actualizarCasillas(i, j);
                k++;
            }
            
        }

    }
    
    private void actualizarCasillas(int i, int j){
        for (int k = dec(i); k <= inc(i); k++) {
            for (int l = dec(j); l <= inc(j); l++)
            
                switch(tablero[k][l].estat){
                    case CERO:
                        tablero[k][l].estat=Estat.UNO;
                        break;
                    case UNO:
                        tablero[k][l].estat=Estat.DOS;
                        break;
                    case DOS:
                        tablero[k][l].estat=Estat.TRES;
                        break;
                    case TRES:
                        tablero[k][l].estat=Estat.CUATRO;
                        break;
                }
            
        }
    
    
    }
    
    private int dec(int valor){
    
        if(valor>0) valor--;
        return valor;
    
    }

    private int inc(int valor){
    
        if(valor<DIM-1) valor++;
        return valor;
    
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        //Imatge de casella verge
        mshi = new ImageIcon("src/imagenes/10.png").getImage();
        int width = mshi.getWidth(null);
        //Dibuixo un taulell
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                //if((i+j)%2==0)
                //    g2d.setPaint(new Color(0,0,0));
                //else g2d.setPaint(new Color(255,255,255));
                //g2d.fillRect(i*25, j*25, 25, 25);
                //g2d.drawImage(mshi, i * width, j * width, null);
                //tablero[i][j]=new ZRectangle(i * width, j * width, mshi);
                switch(tablero[i][j].estat){
                    case CERO:
                        mshi = new ImageIcon("src/imagenes/10.png").getImage();
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
                    case BOMBA:
                        mshi = new ImageIcon("src/imagenes/9.png").getImage();
                        break;
}
    tablero[i][j].img=mshi;
                tablero[i][j].dibuja(g2d);
            }
        }

        //---------------
        //Prova p = new Prova(g2d, new ImageIcon("src/imagenes/1.png").getImage(), 10, 250);
        //p.dibuixa();
        Font font = new Font("Serif", Font.BOLD, 40);
        g2d.setFont(font);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setPaint(new Color(0, 0, 200));
        //g2d.fill(zrect);
        //zrect.dibuja(g2d);
        g2d.setPaint(new Color(0, 200, 0));
        //g2d.fill(zell);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
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

    class ZRectangle extends Rectangle2D.Float {

        private Graphics2D g2d;
        private Image img;
        private float x;
        private float y;
        private Estat estat; 
        

        public ZRectangle(float xpos, float ypos, float width, float height) {
            x = xpos;
            y = ypos;
            setRect(x, y, width, height);
            estat=Estat.CERO;
        }

        public ZRectangle(float x, float y, Image imagen) {

            this(x, y, imagen.getWidth(null), imagen.getHeight(null));
            img = imagen;

        }

        public void dibuja(Graphics2D g) {

            g.fill(this);
            g.drawImage(img, (int)x, (int)y, null);

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

    class MovingAdapter extends MouseAdapter {

        private int x;
        private int y;

        @Override
        public void mousePressed(MouseEvent e) {

            x = e.getX();
            y = e.getY();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            System.out.println(zell.getX() + "-" + zell.getY());
            System.out.println(zrect.getX() + "-" + zrect.getY());
            System.out.println();
            doMove(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (Math.floor(zell.getX()) % 25f != 0) {
                zell.setX(25.0f * (float) (Math.floor(zell.getX() / 25f)));
            }
            if (Math.floor(zell.getY()) % 25f != 0) {
                zell.setY(25.0f * (float) (Math.floor(zell.getY() / 25f)));
            }
            if (Math.floor(zrect.getX()) % 25f != 0) {
                zrect.setX(25.0f * (float) (Math.floor(zrect.getX() / 25f)));
            }
            if (Math.floor(zrect.getY()) % 25f != 0) {
                zrect.setY(25.0f * (float) (Math.floor(zrect.getY() / 25f)));
            }
            repaint(); //To change body of generated methods, choose Tools | Templates.
        }

        private void doMove(MouseEvent e) {

            int dx = e.getX() - x;
            int dy = e.getY() - y;

            if (zrect.isHit(x, y)) {
                if (!zell.intersects(zrect.x + dx, zrect.y + dy, zrect.getHeight(), zrect.getWidth())) {
                    zrect.addX(dx);
                    zrect.addY(dy);
                }
                repaint();
            }

            if (zell.isHit(x, y)) {
                if (!zrect.intersects(zell.x + dx, zell.y + dy, zell.getHeight(), zell.getWidth())) {
                    zell.addX(dx);
                    zell.addY(dy);
                }
                repaint();
            }

            x += dx;
            y += dy;
        }
    }

    class ScaleHandler implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

            doScale(e);
        }

        private void doScale(MouseWheelEvent e) {

            int x = e.getX();
            int y = e.getY();

            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {

                if (zrect.isHit(x, y)) {

                    float amount = e.getWheelRotation() * 5f;
                    zrect.addWidth(amount);
                    zrect.addHeight(amount);
                    repaint();
                }

                if (zell.isHit(x, y)) {

                    float amount = e.getWheelRotation() * 5f;
                    zell.addWidth(amount);
                    zell.addHeight(amount);
                    repaint();
                }
            }
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


