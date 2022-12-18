import java.io.File;

public class Main {

    private static int newWidth = 300;

    public static void main(String[] args) {
//        String srcFolder = "/users/sortedmap/Desktop/src";
//        String dstFolder = "/users/sortedmap/Desktop/dst";

        String srcFolder = "D:\\test";
        String dstFolder = "D:\\test1";

        File srcDir = new File(srcFolder);

        long start = System.currentTimeMillis();

        File[] files = srcDir.listFiles();

        int step = files.length / 8;

        File[] files1 = new File[step];
        System.arraycopy(files, 0, files1, 0, files1.length);
        ImageResizer resizer1 = new ImageResizer(files1, dstFolder, start, newWidth);
        new Thread(resizer1).start();


        File[] files2 = new File[step];
        System.arraycopy(files, step, files2, 0, files2.length);
        ImageResizer resizer2 = new ImageResizer(files2, dstFolder, start, newWidth);
        new Thread(resizer2).start();

        File[] files3 = new File[step];
        System.arraycopy(files, step * 2, files3, 0, files3.length);
        ImageResizer resizer3 = new ImageResizer(files3, dstFolder, start, newWidth);
        new Thread(resizer3).start();

        File[] files4 = new File[step];
        System.arraycopy(files, step * 3, files4, 0, files4.length);
        ImageResizer resizer4 = new ImageResizer(files4, dstFolder, start, newWidth);
        new Thread(resizer4).start();

        File[] files5 = new File[step];
        System.arraycopy(files, step * 4, files5, 0, files5.length);
        ImageResizer resizer5 = new ImageResizer(files5, dstFolder, start, newWidth);
        new Thread(resizer5).start();

        File[] files6 = new File[step];
        System.arraycopy(files, step * 5, files6, 0, files6.length);
        ImageResizer resizer6 = new ImageResizer(files6, dstFolder, start, newWidth);
        new Thread(resizer6).start();

        File[] files7 = new File[step];
        System.arraycopy(files, step * 6, files7, 0, files7.length);
        ImageResizer resizer7 = new ImageResizer(files7, dstFolder, start, newWidth);
        new Thread(resizer7).start();

        File[] files8 = new File[files.length - step * 7];
        System.arraycopy(files, step * 7, files8, 0, files8.length);
        ImageResizer resizer8 = new ImageResizer(files8, dstFolder, start, newWidth);
        new Thread(resizer8).start();
    }
}
