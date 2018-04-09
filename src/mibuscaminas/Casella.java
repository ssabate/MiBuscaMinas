/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mibuscaminas;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import javax.swing.ImageIcon;

/**
 * Classe que representa les caselles del taulell
 *
 * @author profe
 */
public class Casella extends Rectangle2D.Float {
    
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
    
    /**
     * @return the img
     */
    public Image getImg() {
        return img;
    }

    /**
     * @param x the x to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @param y the y to set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @return the estat
     */
    public Estat getEstat() {
        return estat;
    }

    /**
     * @param estat the estat to set
     */
    public void setEstat(Estat estat) {
        this.estat = estat;
    }

    /**
     * @return the clicada
     */
    public boolean isClicada() {
        return clicada;
    }

    /**
     * @param clicada the clicada to set
     */
    public void setClicada(boolean clicada) {
        this.clicada = clicada;
    }

    /**
     * @return the marcada
     */
    public boolean isMarcada() {
        return marcada;
    }

    /**
     * @param marcada the marcada to set
     */
    public void setMarcada(boolean marcada) {
        this.marcada = marcada;
    }

    

    //Dibuixem la casella juntant la forma en la imatge que conté la seua propietat imatge
    public void dibuja(Graphics2D g) {
        g.fill(this);
        g.drawImage(getImg(), (int) x, (int) y, null);
    }

    //Retornem una nova casella en les mateixes propietats que la actual però situada unes caselles més avall 
    public Casella copia(int numcas) {
        return new Casella(this.x, this.y + getImg().getHeight(null) * numcas, this.getImg());
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
        switch (this.getEstat()) {
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
    
    public void incrementaEstat(){
        //Passem de l'estat actual de la casella al següent
        estat = estat.sumarUn();
    }
}

