package site.metacoding.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServerSocket {

    ServerSocket serverSocket; // 리스너 : 연결되는지 주시하는 포트. (연결 = 세선)
    Socket socket; // 메시지 통신 : 통신하는 포트

    BufferedReader reader;

    public MyServerSocket() {
        try {
            // 1. 서버 소켓 생성 (리스너)
            // well known 포트 : 0~1023 사용 금지. well known 포트가 아니어도 1521(오라클 포트)같은 건 충돌나서 사용 금지
            serverSocket = new ServerSocket(1077);
            System.out.println("서버 소켓 생성됨");
            socket = serverSocket.accept(); // while을 돌면서 1077번 포트로 들어오는 것을 대기함. (랜덤 포트)
            reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            // 버퍼 읽는거 반복
            while(true){
                String inputData =  reader.readLine(); // 버퍼 읽기
                System.out.println("받은 메세지 : " + inputData);
                if(inputData.equals("종료")){
                    System.out.println("종료합니다");
                    break;
                }
            }
            // System.out.println("클라이언트 연결됨");
        } catch (Exception e) {
            System.out.println("통신 오류 발생 : " + e.getMessage());
            // e.printStackTrace(); 자세한 오류 내용
        }
    }

    public static void main(String[] args) {
        new MyServerSocket();
        System.out.println("메인 종료");
    }
}