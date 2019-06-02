import echo.Echo;
import echo.EchoServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ClientMain {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext(true)
                .build();

        EchoServiceGrpc.EchoServiceBlockingStub stub = EchoServiceGrpc.newBlockingStub(channel);

        Echo.EchoRequest request = Echo.EchoRequest.newBuilder().setMessage("hello world.").build();

        Echo.EchoResponse response = stub.echo(request);

        System.out.println("response message: " + response.getMessage());
    }
}
