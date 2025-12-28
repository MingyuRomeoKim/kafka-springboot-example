package kr.co.kimmingyu.kafka_demo;

import kr.co.kimmingyu.kafka_demo.service.KafkaProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutputCaptureExtension.class)
class KafkaIntegrationTest {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Test
    @DisplayName("Kafka 메시지 전송 및 수신 통합 테스트")
    void testSendAndConsume(CapturedOutput output) {
        // given
        String topic = "demo-topic";
        String message = "IntegrationTestMessage";

        // when
        kafkaProducer.sendMessage(topic, message);

        // then
        // 로그에 "Consumed message: IntegrationTestMessage" 가 찍힐 때까지 기다림 (최대 10초)
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            assertThat(output.getOut()).contains("Consumed message: " + message);
        });
    }
}
