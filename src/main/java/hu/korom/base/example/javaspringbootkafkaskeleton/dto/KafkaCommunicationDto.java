package hu.korom.base.example.javaspringbootkafkaskeleton.dto;

public class KafkaCommunicationDto {
    private String topic;
    private String message;

    public KafkaCommunicationDto() {}
    public KafkaCommunicationDto(String topic, String message) {
        this.topic = topic;
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
