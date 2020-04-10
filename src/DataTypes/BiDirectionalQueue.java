package DataTypes;

public class BiDirectionalQueue<T> {
    //receiver will reply and receive
    //sender will send and accept
    private SynchronisedQueue<T> sender;
    private SynchronisedQueue<T> receiver;

    public BiDirectionalQueue(int size){
        sender = new SynchronisedQueue<>(size);
        receiver = new SynchronisedQueue<>(size);
    }

    public void send(T item){
        sender.add(item);
    }

    public void reply(T item){
        receiver.add(item);
    }

    public T receive(){
        return sender.remove();
    }

    public T accept(){
        return receiver.remove();
    }

    public int senderSize(){
        return sender.size();
    }

    public int receiverSize(){
        return receiver.size();
    }
}
