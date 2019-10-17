public class ColorUtil {
    public static boolean isSimilarColor(int R_target,int G_target,int B_target,int R_test,int G_test,int B_test,int range){
        if(Math.abs(R_test - R_target)<=range
                &&Math.abs(G_test - G_target)<=range
                &&Math.abs(B_test - B_target)<=range)
            return true;
        return false;
    }

    public static boolean isSimilarColor(int RGB_target,int RGB_test,int range){
        int R_target=(RGB_target&0xff0000)>>16;
        int G_target=(RGB_target&0xff00)>>8;
        int B_target=(RGB_target&0xff);
        int R_test=(RGB_test&0xff0000)>>16;
        int G_test=(RGB_test&0xff00)>>8;
        int B_test=(RGB_test&0xff);
        return isSimilarColor(R_target,G_target, B_target, R_test, G_test, B_test, range);
    }

    public static boolean isSimilarColor(int R_target,int G_target,int B_target,int RGB_test,int range){
        int R_test=(RGB_test&0xff0000)>>16;
        int G_test=(RGB_test&0xff00)>>8;
        int B_test=(RGB_test&0xff);
        return isSimilarColor(R_target,G_target, B_target, R_test, G_test, B_test, range);
    }

    public static boolean isTransparentWhiteColor(int R_target,int G_target,int B_target){
        System.out.println("检测到透明白");
        int R_TransparentWhite=210;
        int G_TransparentWhite=230;
        int B_TransparentWhite=240;
        if(Math.abs(R_TransparentWhite - R_target)<=20
                &&Math.abs(G_TransparentWhite-G_target)<=20
                &&Math.abs(B_TransparentWhite-B_target)<=15)
            return true;
        return false;
    }
}
