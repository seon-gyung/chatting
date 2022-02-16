package site.metacoding.chat_v3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

    Socket socket;

    // 스레드
    Scanner sc;
    BufferedWriter writer;
    
    // 스레드
    BufferedReader reader;

    public MyClientSocket() {
        try {
            // 1. 소켓 연결
            socket = new Socket("192.168.0.84", 2000); // 루프백 주소 127.0.0.1 선생님 주소 192.168.0.132

            // 2. 스캐너 및 버퍼 달기
            sc = new Scanner(System.in);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // 3. 새로운 스레드 (읽기 전용)         
            new Thread(new 읽기전담스레드()).start();

            // 4. 메인 스레드 (쓰기 전용)
            while(true){
                String keyboardinputData = sc.nextLine();
                writer.write(keyboardinputData + "\n"); // 버퍼에 담기 // 데이터 전송할 때 \n 필요함.
                writer.flush(); // 버퍼에 담긴 것을 stream으로 흘려보내기. // 통신의 시작
            }

        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("통신 오류 발생 : " + e.getMessage());
        }
    }

    class 읽기전담스레드 implements Runnable{

        @Override
        // 3. 새로운 스레드 (읽기 전용) 
        public void run() {
            try {
                while(true){
                    String inputData = reader.readLine();
                    System.out.println("받은 메시지 : " + inputData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }    
    public static void main(String[] args) {
        new MyClientSocket();
    }
}