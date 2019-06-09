package uploader;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;

public class FileService extends FileServiceGrpc.FileServiceImplBase {

    @Override
    public StreamObserver<UploadFile.FileRequest> upload(StreamObserver<UploadFile.FileResponse> responseObserver) {
        var blob = new ArrayList<ByteString>();

        /* レスポンスとして、Requestを返す。
        要するに、コールバックを返しているということ。 */
        return new StreamObserver<UploadFile.FileRequest>() {
            @Override
            public void onNext(UploadFile.FileRequest value) {
                // データを保持するのみ。全てのリクエストが終わる（onComplete）まで処理しないため。
                blob.add(value.getData());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                // リクエストが全て完了したので処理を行う。
                // ここでは、アップロードされたファイルのデータサイズを算出してレスポンスしている。
                int size = blob.stream().mapToInt(ByteString::size).sum();
                responseObserver.onNext(UploadFile.FileResponse.newBuilder().setSize(size).build());
                responseObserver.onCompleted();
            }
        };
    }
}
