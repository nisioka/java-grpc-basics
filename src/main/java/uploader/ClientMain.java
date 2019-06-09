package uploader;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

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

        var stub = FileServiceGrpc.newStub(channel);

        // 非同期処理の結果を待つLatch。
        CountDownLatch client = new CountDownLatch(1);

        // ファイル名をリクエストする。
        var requestSender = stub.upload(new StreamObserver<UploadFile.FileResponse>() {
            @Override
            public void onNext(UploadFile.FileResponse value) {
                // レスポンスを表示する。
                // ただし、クライアントサイドストリーミングでは一度しかレスポンスは無い。
                System.out.println("response message: " + value.getSize());
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

        // 3度リクエストを投げる。
        requestSender.onNext(UploadFile.FileRequest.newBuilder().setName("uploadFile.txt").setData(ByteString.copyFromUtf8("ABC")).build());
        requestSender.onNext(UploadFile.FileRequest.newBuilder().setName("uploadFile.txt").setData(ByteString.copyFromUtf8("DEFG")).build());
        requestSender.onNext(UploadFile.FileRequest.newBuilder().setName("uploadFile.txt").setData(ByteString.copyFromUtf8("HIJKL")).build());
        requestSender.onCompleted();

        client.await();
    }
}
