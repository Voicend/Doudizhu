import javax.swing.*;

public class timeclick extends JTextField implements Runnable {
    public boolean stopped = false;
    @Override
    public void run() {
        for(int i=30;i>0;i--) {
            if(stopped)
                break;
            this.setText("倒计时"+i/10+i%10+"秒");
            try{
                Thread.sleep(500);
            }catch (Exception e){
                break;
            }
            if(stopped)
                break;
            try{
                Thread.sleep(500);
            }catch (Exception e){
                break;
            }
        }
        this.setVisible(false);
    }
}
