/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mibuscaminas;
  
/**
 *
 * @author profe
 */
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