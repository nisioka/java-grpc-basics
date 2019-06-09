package chat;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

/**
 * クライアントからサーバへリクエストを送る。
 */
public class ClientMain {
    public static void main(String[] args) throws InterruptedException {
        final int portNumber = 50051;
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", portNumber)
                .usePlaintext(true) // 簡単のためサーバ証明書なしのローカル通信とする。
                .build();

        var stub = ChatServiceGrpc.newStub(channel);

        // 非同期処理の結果を待つLatch。
        CountDownLatch client = new CountDownLatch(1);

        // ファイル名をリクエストする。
        var requestSender = stub.connect(new StreamObserver<Chat.Post>() {
            @Override
            public void onNext(Chat.Post value) {
                // レスポンスを表示する。
                System.out.println("[" + value.getName() + "] " + value.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                client.countDown();
            }
        });

        Scanner scanner = new Scanner(System.in);
        String input;
        while ((input = scanner.nextLine()) != null) {
            if (Objects.equals(input, ":quit")) {
                requestSender.onCompleted();
                return;
            }
            requestSender.onNext(Chat.Post.newBuilder().setName("userA").setMessage(input).build());
        }
        client.await();
    }
}
