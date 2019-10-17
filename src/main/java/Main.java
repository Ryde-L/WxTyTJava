import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Main {
    public static final String ADB_PATH="D:/SoftWare/adb/adb";

    public static void main(String[] args) {

        BufferedImage bufferedImage=null;
        int width,height,distance;
        int failureCount=0;
        Random random=new Random();
        Point myPoint,nextPoint;
        while(true) {
            try {
                System.out.println();
                System.out.println(FindMyPosition.screenShotCount);
                Process process = Runtime.getRuntime().exec(ADB_PATH + " shell /system/bin/screencap -p /sdcard/screenshot.png");
                process.waitFor();
                process = Runtime.getRuntime().exec(ADB_PATH + " pull /sdcard/screenshot.png C:\\Users\\PC\\Desktop\\pp\\screenshot"+ FindMyPosition.screenShotCount+".png");
                process.waitFor();
                bufferedImage = ImageUtil.loadImage("C:\\Users\\PC\\Desktop\\pp\\screenshot"+ FindMyPosition.screenShotCount+".png");

                if (bufferedImage != null) {
                    width = bufferedImage.getWidth();
                    height = bufferedImage.getHeight();
                    bufferedImage = bufferedImage.getSubimage(0, height / 3, width, height / 3);
                    nextPoint = new FindBoxCenter2().getCenter(bufferedImage);
                    if(nextPoint==null){
                        System.out.println("游戏结束");
                        return;
                    }
                    myPoint = new FindMyPosition().getMyPosition(bufferedImage);
                    distance = (int) myPoint.distance(nextPoint);
                    int randomX = 500 + random.nextInt(100);
                    int randomY = 600 + random.nextInt(100);
                    String adbCommand = String.format(ADB_PATH + " shell input swipe %d %d %d %d %d", randomX, randomY, randomX, randomY, distance*2);
                    process = Runtime.getRuntime().exec(adbCommand);
                    FindMyPosition.screenShotCount++;
                    Thread.sleep(2000 + 300+random.nextInt(1000));
                }
            } catch (Exception e) {
                try {
                    e.printStackTrace();
                    Thread.sleep(1000);
                    failureCount++;
                    System.out.println("第" + failureCount + "次失败，1秒后重试");
                    if (failureCount > 5) {
                        System.out.println("5次失败，请手动重新开始");
                       System.exit(0);
                    }

                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }
}
