package com.shred.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientDemo {
    public static void main(String[] args) throws IOException {
        while (true) {

            Socket socket = new Socket("127.0.0.1", 9999);

            OutputStream outputStream = socket.getOutputStream();

            System.out.println("请输入： ");

            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            outputStream.write(message.getBytes(StandardCharsets.UTF_8));

            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int read = inputStream.read(bytes);
            System.out.println("老板说：" + new String(bytes, 0, read).trim());

            socket.close();
        }

    }
}
