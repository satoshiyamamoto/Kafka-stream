package source.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import domain.model.WebSite;

@RestController
@EnableBinding(Source.class)
@SpringBootApplication
public class HttpSourceApplication {

	@Autowired
	private Source source;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void output(@RequestBody WebSite webSite) {
		source.output().send(MessageBuilder.withPayload(webSite).build());
	}

	public static void main(String[] args) {
		SpringApplication.run(HttpSourceApplication.class, args);
	}

}
