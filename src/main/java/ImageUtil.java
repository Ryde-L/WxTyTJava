import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageUtil {

    public static BufferedImage loadImage(String path) throws  Exception{
        BufferedImage bufferedImage=null;
        InputStream inputStream=null;
        File file=new File(path);
        if (file.exists()){
            try {
                inputStream=new BufferedInputStream(new FileInputStream(path));
                bufferedImage= ImageIO.read(inputStream);
            }finally {

                if (inputStream!=null){
                    inputStream.close();
                }
            }
        }else{
            throw new Exception("请正确选择图片路径");
        }
        return bufferedImage;
    }

    public static void saveImage(BufferedImage image, String path, String name){
        File file=new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        try {
            ImageIO.write(image,"png",new File(path+"//"+name+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
