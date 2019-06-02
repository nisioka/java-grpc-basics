package server;

import echo.Echo;
import echo.EchoServiceGrpc;
import io.grpc.stub.StreamObserver;

public class EchoService extends EchoServiceGrpc.EchoServiceImplBase {

    @Override
    public void echo(Echo.EchoRequest request, StreamObserver<Echo.EchoResponse> responseObserver) {
        // リクエストからmessageを受け取り、そのままレスポンスのmessageとしてセットする。
        Echo.EchoResponse echo = Echo.EchoResponse.newBuilder().setMessage(request.getMessage()).build();
        responseObserver.onNext(echo);
        responseObserver.onCompleted();
    }
}
