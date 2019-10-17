import java.awt.*;
import java.awt.image.BufferedImage;

public class FindBoxCenter2 {
    public static final int R_END = 28;
    public static final int G_END = 28;
    public static final int B_END = 28;

    public Point getCenter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int backgroundR = (image.getRGB(10, 10) & 0xff0000) >> 16;
        int backgroundG = (image.getRGB(10, 10) & 0xff00) >> 8;
        int backgroundB = (image.getRGB(10, 10) & 0xff);
        int nextBoxR = -1, nextBoxG = -1, nextBoxB = -1;
        int topPointX = 0, topPointY = 0;
        int leftX = 0, rightX = 0, bottomY = 0;
        int leftXBreakCount = 0, rightXBreakCount = 0, bottomYBreakCount = 0;
        boolean isFindBoxPixel = false;

        //灰色像素结束

        int endPixel = image.getRGB(10, 15);
        int R_FIRST = (endPixel & 0xff0000) >> 16;
        int G_FIRST = (endPixel & 0xff00) >> 8;
        int B_FIRST = (endPixel & 0xff);

        if (ColorUtil.isSimilarColor(R_END, G_END, B_END, R_FIRST, G_FIRST, B_FIRST, 25))
            return null;
        out:
        for (int i = 20; i < height; i += 2) {
            for (int j = 20; j < width; j += 2) {
                if (isFindBoxPixel && (j < topPointX - 200 || j > topPointX + 180)) {
                    //找到顶点后限定在顶点左右200的范围查找
                    continue;
                }
                int pixel = image.getRGB(j, i);
                int r = (pixel & 0xff0000) >> 16;
                int g = (pixel & 0xff00) >> 8;
                int b = (pixel & 0xff);
                if (!isFindBoxPixel) {
                    if (!ColorUtil.isSimilarColor(backgroundR, backgroundG, backgroundB, r, g, b, 10)&&!ColorUtil.isTransparentWhiteColor(r,g,b)) {
                        nextBoxR = r;
                        nextBoxG = g;
                        nextBoxB = b;
                        topPointX = j;
                        topPointY = i;//delete
                        leftX = topPointX;
                        rightX = topPointX;
                        bottomY=topPointY;
                        isFindBoxPixel = true;
                    }
                } else {

                    left:
                    for (int y = i + 1; ; y++) {
                        for (int x = leftX; x>0; x--) {
                            if (ColorUtil.isSimilarColor(nextBoxR, nextBoxG, nextBoxB, image.getRGB(x, y), 10)) {
                                leftX = x-1;
                                leftXBreakCount = 0;
                                if(y>bottomY)
                                  bottomY=y;
                            } else {
                                leftXBreakCount++;
                                if (leftXBreakCount > 25) {
                                    break left;
                                }
                                break;
                            }
                        }
                    }
                    right:
                    for (int y = i + 1; ; y++) {
                        for (int x = rightX; x<width; x++) {
                            if (ColorUtil.isSimilarColor(nextBoxR, nextBoxG, nextBoxB, image.getRGB(x, y),10)) {
                                rightX = x+1;
                                rightXBreakCount = 0;
                                if(y>bottomY)
                                    bottomY=y;
                            } else {
                                rightXBreakCount++;
                                if (rightXBreakCount > 25) {
                                    break right;
                                }
                                break;
                            }
                        }
                    }
                    bottom:
                    for (int y = bottomY,m=1;y<height ; y ++,m++) {
                        for(int x=topPointX-5;x<topPointX+5;x+=2){
                            if (ColorUtil.isSimilarColor(nextBoxR, nextBoxG, nextBoxB, image.getRGB(x, y), 5)
                                    ||ColorUtil.isSimilarColor(nextBoxR, nextBoxG, nextBoxB,image.getRGB(leftX+m,y),5)
                                    ||ColorUtil.isSimilarColor(nextBoxR, nextBoxG, nextBoxB,image.getRGB(leftX+m+1,y),5)
                                    ||ColorUtil.isSimilarColor(nextBoxR, nextBoxG, nextBoxB,image.getRGB(rightX-m,y),5)
                                    ||ColorUtil.isSimilarColor(nextBoxR, nextBoxG, nextBoxB,image.getRGB(rightX-m-1,y),5)) {
                                bottomY = y;
                                bottomYBreakCount = 0;
                            } else {
                                bottomYBreakCount++;
                                if(bottomY-topPointY<30){
                                    bottomYBreakCount=0;
                                    bottomY=y;
//                                nextBoxR = (image.getRGB(j, y) & 0xff0000) >> 16;
//                                nextBoxG = (image.getRGB(j, y) & 0xff00) >> 8;
//                                nextBoxB = (image.getRGB(j, y) & 0xff);
                                }
                                if (bottomYBreakCount > 100) {
                                    break bottom;
                                }
                        }

                        }
                    }
//                    bottom:
//                    for (int y = bottomY;y<height ; y ++) {
//                        if (ColorUtil.isSimilarColor(nextBoxR, nextBoxG, nextBoxB, image.getRGB(j, y), 5)) {
//                            bottomY = y;
//                            bottomYBreakCount = 0;
//                        } else {
//                            bottomYBreakCount++;
//                            if(bottomY-topPointY<30){
//                                bottomYBreakCount=0;
//                                bottomY=y;
////                                nextBoxR = (image.getRGB(j, y) & 0xff0000) >> 16;
////                                nextBoxG = (image.getRGB(j, y) & 0xff00) >> 8;
////                                nextBoxB = (image.getRGB(j, y) & 0xff);
//                            }
//                            if (bottomYBreakCount > 25) {
//                                break bottom;
//                            }
//                        }
//                    }
                    break out;
                }
            }//end for j
        }//end for i
       System.out.println("背景RGB R:"+backgroundR+" , G"+backgroundG+" , B"+backgroundB);
       System.out.println("目标箱子RGB R:"+nextBoxR+" , G"+nextBoxG+" , B"+nextBoxB);
        Graphics g = image.getGraphics();
        g.setColor(Color.RED);
        g.draw3DRect(leftX, topPointY, rightX - leftX, bottomY - topPointY, false);//盒子
        g.fillOval((rightX + leftX) / 2, (topPointY + bottomY) / 2, 5, 5);//中心点
        g.fillOval((rightX + leftX) / 2, topPointY, 5, 5);//顶点
        g.fillOval(100, 20, 10, 10);//第一个点
        int RGB_TOP = image.getRGB((rightX + leftX) / 2, topPointY);
        int rt = (RGB_TOP & 0xff0000) >> 16;
        int gt = (RGB_TOP & 0xff00) >> 8;
        int bt = (RGB_TOP & 0xff);
        System.out.println("rt" + rt + "  gt" + gt + "  bt" + bt);
        int RGB_center = image.getRGB((rightX + leftX) / 2, topPointY + 10);
        int rc = (RGB_center & 0xff0000) >> 16;
        int gc = (RGB_center & 0xff00) >> 8;
        int bc = (RGB_center & 0xff);
        System.out.println("rc" + rc + "  gc" + gc + "  bc" + bc);
        System.out.println("rF" + R_FIRST + "  gF" + G_FIRST + "  bF" + B_FIRST);

        return new Point((rightX + leftX) / 2, (topPointY + bottomY) / 2);

    }
}


