import java.awt.Point;
        import java.awt.image.BufferedImage;
        import java.io.ByteArrayInputStream;
        import java.io.InputStream;
        import java.util.ArrayList;
        import java.util.List;

        import javax.imageio.ImageIO;
        import javax.swing.ImageIcon;
        import javax.swing.JFrame;
        import javax.swing.JLabel;

        import org.opencv.calib3d.Calib3d;
        import org.opencv.core.*;
        import org.opencv.imgproc.Imgproc;
        import org.opencv.video.Video;
        import org.opencv.features2d.FeatureDetector;
        import org.opencv.imgcodecs.Imgcodecs;

        import static org.opencv.imgproc.Imgproc.*;
        import static org.opencv.utils.Converters.vector_Point_to_Mat;

public class optic {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat image = Imgcodecs.imread("C:/Users/User/Downloads/mrizka.jpg");
        Mat lmaeft1 = Imgcodecs.imread("C:/Users/User/Downloads/valec.jpg");
        JFrame window = new JFrame();
        JFrame window1 = new JFrame();
        Mat vysledek = new Mat();
        int l = 100;
        int r = 130;
        float a = 255/(r-l);
        float b = l;
        Core.subtract(lmaeft1, new Scalar(b,b,b), vysledek);
        Core.multiply(vysledek, new Scalar(a,a,a), vysledek);
        Mat kelner = getStructuringElement(MORPH_RECT, new Size(10, 10));
        Core.absdiff(image,vysledek,vysledek);
        Imgproc.morphologyEx(vysledek, vysledek, MORPH_OPEN, kelner);
        Imgproc.morphologyEx(vysledek, vysledek, MORPH_DILATE , kelner);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat mat = new Mat();
        Imgproc.cvtColor(vysledek, vysledek, COLOR_RGB2GRAY);
        Util.imshow(vysledek, window);
        Imgproc.findContours(vysledek, contours,mat, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
        MatOfInt hulk = new MatOfInt();
        for (int c = 0; c < contours.size(); ++c) {
            Imgproc.convexHull(contours.get(c), hulk);
            int[] hh = hulk.toArray();
            List<org.opencv.core.Point> kkk = contours.get(c).toList();
            for (int i = 0; i < hh.length; ++i) {
                kkk.set(i, kkk.get(hh[i]));
            }
            MatOfPoint kull = new MatOfPoint();
            Imgproc.fillConvexPoly(image, new MatOfPoint(vector_Point_to_Mat(kkk.subList(0, hh.length))), new Scalar(13, 12, 123));
        }
        Util.imshow(image, window1);
    }
}

class Util {


    public static void imshow(Mat img, JFrame frame) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}