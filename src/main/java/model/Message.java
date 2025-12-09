package model;

public class Message {
    private String text;
    private int type;        // 0: join, 1: chat, 2: leave
    private String sender;
    private int count;

    // Constructor rỗng bắt buộc cho Decoder
    public Message() {}

    public Message(String text, int type, String sender, int count) {
        this.text = text;
        this.type = type;
        this.sender = sender;
        this.count = count;
    }

    // Getters and Setters
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}
