import echo.Echo;
import echo.EchoServiceGrpc;
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

        EchoServiceGrpc.EchoServiceBlockingStub stub = EchoServiceGrpc.newBlockingStub(channel);

        // "hello world."メッセージをリクエストする。
        Echo.EchoRequest request = Echo.EchoRequest.newBuilder().setMessage("hello world.").build();

        Echo.EchoResponse response = stub.echo(request);

        System.out.println("response message: " + response.getMessage());
    }
}
