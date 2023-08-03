package hu.korom.base.example.javaspringbootkafkaskeleton.controller;

import java.nio.file.Path;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.korom.base.example.javaspringbootkafkaskeleton.dto.KafkaCommunicationDto;
import hu.korom.base.example.javaspringbootkafkaskeleton.exception.KafkaCommunicationException;
import hu.korom.base.example.javaspringbootkafkaskeleton.service.KafkaManagementService;

@RestController
public class KafkaCommunicationController {

    public static final String KAFKA_PUBLISH_ENDPOINT = "/publish";

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private KafkaManagementService kafkaManagementService;

    @Autowired
    public KafkaCommunicationController(KafkaManagementService kafkaManagementService) {
        this.kafkaManagementService = kafkaManagementService;
    }

    @PostMapping(value = KAFKA_PUBLISH_ENDPOINT)
    public ResponseEntity<KafkaCommunicationDto> publishToKafka(
            @RequestBody KafkaCommunicationDto kafkaCommunicationDto) {
        log.info("Incoming request to publish \"{}\" to \"{}\"", kafkaCommunicationDto.getMessage(),
                kafkaCommunicationDto.getTopic());

        try {
            kafkaManagementService.sendMessage(kafkaCommunicationDto.getTopic(), kafkaCommunicationDto.getMessage());
        } catch (Exception ex) {
            throw new KafkaCommunicationException(
                    String.format("Error while publishing. Message:%s; Topic: %s. Exception:%s",
                            kafkaCommunicationDto.getMessage(), kafkaCommunicationDto.getTopic(), ex.getMessage()),
                    "600");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(kafkaCommunicationDto);
    }

}
