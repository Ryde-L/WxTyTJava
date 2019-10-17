import javax.swing.*;
import java.awt.image.BufferedImage;

public class ShowImg {
    public void show(BufferedImage bufferedImage){
        JFrame jframe=new JFrame("图片");
        JPanel jPanel=new JPanel();
        JLabel label=new JLabel();
        BufferedImage zoomImage=new BufferedImage(bufferedImage.getWidth()/3,bufferedImage.getHeight()/3, BufferedImage.TYPE_INT_RGB);
        zoomImage.getGraphics().drawImage(bufferedImage.getScaledInstance(bufferedImage.getWidth()/3,bufferedImage.getHeight()/3,java.awt.Image.SCALE_SMOOTH),0,0,null);
        label.setIcon(new ImageIcon(zoomImage));
        jframe.add(label);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.pack();
        jframe.setVisible(true);
    }
}
