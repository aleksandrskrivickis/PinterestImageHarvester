import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import java.io.*;

public class FaceDetector {

    public static void main(String[] args) {
        FaceDetector fd = new FaceDetector();
        fd.DetectOneFaceAndOverWriteCropOrDelete("friends.jpg");
    }

    public boolean DetectOneFaceAndOverWriteCropOrDelete(String imageFile){
            System.out.println("Using " + Core.NATIVE_LIBRARY_NAME + " library.");
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.out.println("\nRunning FaceDetector");

            CascadeClassifier faceDetector = new CascadeClassifier("C:\\Program Files\\OpenCV\\build\\etc\\haarcascades\\haarcascade_frontalface_alt.xml");
            Mat image = Imgcodecs
                    .imread(imageFile);
            Rect rectCrop = null;
            MatOfRect faceDetections = new MatOfRect();
            try{
                faceDetector.detectMultiScale(image, faceDetections);
            } catch(Exception ex){
                System.out.println("Exception - " + ex);
            }


            System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

            for (Rect rect : faceDetections.toArray()) {
                Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
                rectCrop = new Rect(rect.x + 1, rect.y + 1, rect.width - 1, rect.height - 1);
            }
        Mat markedImage = null;
        try {
            markedImage = new Mat(image, rectCrop);
        } catch (NullPointerException e){
            System.out.println("NullPointerException: " + e);
        }
            if (faceDetections.toArray().length == 1) {
                System.out.println(String.format("Writing %s", imageFile));
                Imgcodecs.imwrite(imageFile, markedImage);
                return true;
            } else {
                File file = new File(imageFile);
                file.delete();
                return false;
            }
    }
}