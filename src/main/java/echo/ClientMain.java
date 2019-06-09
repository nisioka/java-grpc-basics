package echo;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * クライアントからサーバへリクエストを送る。
 */
public class ClientMain {
    public static void main(String[] args) {
        final int portNumber = 50051;
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", portNumber)
                .usePlaintext(true) // 簡単のためサーバ証明書なしのローカル通信とする。
                .build();

        var stub = EchoServiceGrpc.newBlockingStub(channel);

        // "hello world."メッセージをリクエストする。
        var request = Echo.EchoRequest.newBuilder().setMessage("hello world.").build();

        var response = stub.echo(request);

        System.out.println("response message: " + response.getMessage());
    }
}
