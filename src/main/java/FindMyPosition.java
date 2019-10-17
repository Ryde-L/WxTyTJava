import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FindMyPosition {
    public static  final  int R_TARGER=40;
    public static  final  int G_TARGER=43;
    public static  final  int B_TARGER=86;
    public static int screenShotCount=1;
    public Point getMyPosition(BufferedImage image){
        int width=image.getWidth();
        int height=image.getHeight();
        int maxX=0,minX=99999,maxY=0,minY=99999;
        int maxXOfY=0,minXOfY=0,maxYOfX=0,minYOfX=0;
        int breakCount=0;
        boolean isFindMyPositionPixel=false;
        java.util.List<Integer> xList=new ArrayList<Integer>(700000);
        List<Integer> yList=new ArrayList<Integer>(700000);


        out:for(int i=100;i<height;i+=2){
            for(int j=50;j<width;j+=2){
                int pixel=image.getRGB(j,i);
                int r = (pixel & 0xff0000) >> 16;
                int g = (pixel & 0xff00) >> 8;
                int b = (pixel & 0xff);
                //找到棋子像素
                if(ColorUtil.isSimilarColor(R_TARGER,G_TARGER,B_TARGER,r,g,b,16)){
                            xList.add(j);
                            yList.add(i);
                            breakCount=0;
                            if(i>150)
                                isFindMyPositionPixel=true;
                }else{ //未找到棋子像素
                    breakCount++;
                    if(isFindMyPositionPixel&&breakCount>width*30){
                        System.out.println("查找我的位置结束，此时X："+j+" ，Y："+i);
                        break out;
                    }
                }//end else
            }//end for j
        }//end for i

        for (int i =0;i<xList.size();i++){
            int temp=xList.get(i);
            if(temp>maxX){
                maxX=temp;
                maxXOfY=yList.get(i);
            }
            if (temp<minX){
                minX=temp;
                minXOfY=yList.get(i);
            }
        }

        for (int i =0;i<yList.size();i++){
            int temp=yList.get(i);
            if(temp>maxY){
                maxY=temp;
                maxYOfX=xList.get(i);
            }
            if (temp<minY){
                minY=temp;
                minYOfX=xList.get(i);
            }
        }

//        System.out.println("我的位置的最大X：("+maxX+","+maxXOfY+") ，最小X：("+minX+","+minXOfY+")");
//        System.out.println("我的位置的最大Y：("+maxYOfX+","+maxY+") ，最小Y：("+minYOfX+","+minY+")");
        Graphics g=image.getGraphics();
        g.setColor(Color.RED);
        g.draw3DRect(minX,minY,maxX-minX,maxY-minY,true);
        g.fillOval((maxX+minX)/2,(maxY),5,5);
        ImageUtil.saveImage(image,"C:\\Users\\PC\\Desktop\\pp","myPosition"+screenShotCount);
        return new Point((maxYOfX+minYOfX)/2,(maxY));
    }
}
