package UIIG;

import javax.swing.*;

public class Card extends JLabel {
    public boolean canbechose;
    public boolean chosen;
    public boolean isup;
    public int number;
    public Card(ImageIcon imageIcon){
        this.setIcon(imageIcon);
        this.chosen = false;
        this.isup = false;
        this.canbechose = false;
    }
    private void up(){
        this.setBounds(this.getX(),this.getY()+20,105,150);
    }
    private void down(){
        this.setBounds(this.getX(),this.getY()-20,105,150);
    }
    public void updown(){
        if(chosen){
            if(isup){
                up();
                isup = false;
            }else {
                down();
                isup = true;
            }
            chosen = false;
        }
    }
}
