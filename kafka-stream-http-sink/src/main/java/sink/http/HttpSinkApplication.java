package sink.http;

import domain.model.WebSite;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@EnableBinding(Sink.class)
@SpringBootApplication
public class HttpSinkApplication {

	@StreamListener(Sink.INPUT)
	public void input(@Payload WebSite webSite,
					  @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {
        String response = WebClient.create(webSite.getUrl().toString())
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .block();

		try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get("/Users/satoshi/Desktop",
                        webSite.getName() + "." + timestamp + ".html"))) {
			writer.write(response);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(HttpSinkApplication.class, args);
	}

}
