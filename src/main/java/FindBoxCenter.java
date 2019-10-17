import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class FindBoxCenter {
    public static  final  int R_END=28;
    public static  final  int G_END=28;
    public static  final  int B_END=28;

   public Point getCenter(BufferedImage image){
       int width=image.getWidth();
       int height=image.getHeight();
       int backgroundR=(image.getRGB(10,10)&0xff0000)>>16;
       int backgroundG=(image.getRGB(10,10)&0xff00)>>8;
       int backgroundB=(image.getRGB(10,10)&0xff);
       int nextBoxR=-1,nextBoxG=-1,nextBoxB=-1;
       int maxX=0,minX=99999,maxY=0,minY=99999;
       int maxXOfY=0,minXOfY=0,maxYOfX=0,minYOfX=0;
       int breakCount=0;
       int topPointX=0;
       int topPointY=0;
       boolean isFindBoxPixel=false;
       List<Integer> xList=new ArrayList<Integer>(200000);
       List<Integer> yList=new ArrayList<Integer>(200000);

       //灰色像素结束

       int endPixel=image.getRGB(10,15);
       int R_FIRST = (endPixel & 0xff0000) >> 16;
       int G_FIRST = (endPixel & 0xff00) >> 8;
       int B_FIRST = (endPixel & 0xff);

       if(ColorUtil.isSimilarColor(R_END,G_END,B_END,R_FIRST,G_FIRST,B_FIRST,25))
           return null;

       out:for(int i=20;i<height;i+=2){
           for(int j=20;j<width;j+=2){
               if(isFindBoxPixel&&(j<topPointX-200||j>topPointX+180)){
                   //找到顶点后限定在顶点左右200的范围查找
                   continue ;
               }
               int pixel=image.getRGB(j,i);
               int r = (pixel & 0xff0000) >> 16;
               int g = (pixel & 0xff00) >> 8;
               int b = (pixel & 0xff);
               if(!isFindBoxPixel) {
                   if (!ColorUtil.isSimilarColor(backgroundR, backgroundG, backgroundB, r, g, b, 5)) {
                       int realMainColorPiexl = image.getRGB(j, i + 10);
                       nextBoxR = (realMainColorPiexl & 0xff0000) >> 16;
                       nextBoxG = (realMainColorPiexl & 0xff00) >> 8;
                       nextBoxB = (realMainColorPiexl & 0xff);
                       topPointX = j;
                       topPointY = i;//delete
                       isFindBoxPixel = true;
                   }
               }else{//
                   if (ColorUtil.isSimilarColor(nextBoxR, nextBoxG, nextBoxB, r, g, b, 5)){
                       xList.add(j);
                       yList.add(i);

                   }else{
                       breakCount++;
                       if (breakCount > 380*25 ) {
                           System.out.println("盒子扫描异常点太多，结束扫描");
                           break out;
                       }
                   }
               }
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
//       System.out.println("下一箱子的最大X：("+maxX+","+maxXOfY+") ，最小X：("+minX+","+minXOfY+")");
//       System.out.println("下一箱子的最大Y：("+maxYOfX+","+maxY+") ，最小Y：("+minYOfX+","+minY+")");
//       System.out.println("背景RGB R:"+backgroundR+" , G"+backgroundG+" , B"+backgroundB);
//       System.out.println("目标箱子RGB R:"+nextBoxR+" , G"+nextBoxG+" , B"+nextBoxB);
       Graphics g=image.getGraphics();
       g.setColor(Color.RED);
       g.draw3DRect(minX,minY,maxX-minX,maxY-minY,false);//盒子
       g.fillOval((maxX+minX)/2,(maxY+minY)/2,5,5);//中心点
       g.fillOval(topPointX,topPointY,5,5);//顶点
       g.fillOval(100,20,10,10);//第一个点
       int RGB_TOP=image.getRGB(topPointX,topPointY);
       int rt = (RGB_TOP & 0xff0000) >> 16;
       int gt = (RGB_TOP & 0xff00) >> 8;
       int bt = (RGB_TOP & 0xff);
       System.out.println("rt"+rt+"  gt"+gt+"  bt"+bt);
       int RGB_center=image.getRGB(topPointX,topPointY+10);
       int rc = (RGB_center & 0xff0000) >> 16;
       int gc = (RGB_center & 0xff00) >> 8;
       int bc = (RGB_center & 0xff);
       System.out.println("rc"+rc+"  gc"+gc+"  bc"+bc);
       System.out.println("rF"+R_FIRST+"  gF"+G_FIRST+"  bF"+B_FIRST);
       System.out.println();


       return new Point((maxYOfX+minYOfX)/2,(maxXOfY+minXOfY)/2);
   }
}
