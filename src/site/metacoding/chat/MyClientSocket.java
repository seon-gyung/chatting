package site.metacoding.chat;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

    Socket socket;
    BufferedWriter writer;
    Scanner sc;

    public MyClientSocket() {
        try {
            socket = new Socket("localhost", 1077);

            writer = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())
            );

            // 스캐너 달고
            Scanner sc = new Scanner(System.in);
            // 키보드 입력 받기 (반복)
            while(true){
                String inputData = sc.nextLine();
                writer.write(inputData + "\n"); // 윈도우 os는 \n을 써야 데이터가 보내짐
                writer.flush(); // 버퍼 비우기
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new MyClientSocket();
    }
}