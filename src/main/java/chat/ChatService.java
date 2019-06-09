package chat;

import io.grpc.stub.StreamObserver;

import java.util.*;

public class ChatService extends ChatServiceGrpc.ChatServiceImplBase {
    private Map<String, StreamObserver<Chat.Post>> users = new HashMap<>();

    @Override
    public StreamObserver<Chat.Post> connect(StreamObserver<Chat.Post> responseObserver) {

        /* レスポンスとして、Requestを返す。
        要するに、コールバックを返しているということ。 */
        return new StreamObserver<Chat.Post>() {
            @Override
            public void onNext(Chat.Post value) {
                // 接続してきたユーザをグローバルに保持する。
                users.put(value.getName(), responseObserver);
                // 全ユーザにポストを通知する。
                for (Map.Entry<String, StreamObserver<Chat.Post>> user : users.entrySet()) {
                    user.getValue().onNext(value);
                }
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                // 切断されたユーザを取り除く。
                for (Map.Entry<String, StreamObserver<Chat.Post>> user : users.entrySet()) {
                    if (Objects.equals(user.getValue(), responseObserver)){
                        users.remove(user.getKey());
                    }
                }
                responseObserver.onCompleted();
            }
        };
    }
}
