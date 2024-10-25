package hh.backend.mtgproject;

import java.util.ArrayList;
import java.util.Locale.Category;

import java.util.List;
import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


import hh.backend.mtgproject.domain.Card;
import hh.backend.mtgproject.domain.CardRepository;




@SpringBootApplication
public class MtgProjectApplication {


	// CHATGPT START
	private List<Card> cards = new ArrayList<>();

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	// CHATGPT END



	// CREATE NEW LOGGER ATTRIBUTE
	private static final Logger log = LoggerFactory.getLogger(MtgProjectApplication.class);  

	


	public static void main(String[] args) {

		SpringApplication.run(MtgProjectApplication.class, args);

	}


	//  testidatan luonti H2-testitietokantaan aina sovelluksen käynnistyessä
	@Bean
	public CommandLineRunner MtgAppRunner(CardRepository cardRepository, RestTemplate restTemplate) { 
		
		return (args) -> {

			ArrayList<String> list1 = new ArrayList<>();
			ArrayList<String> list2 = new ArrayList<>();


			// READ API STUFF. Source: CHATGPT
			String apiUrl = "https://api.scryfall.com/cards/search?q=+t=snake+set=neo";

			// SOURCE: Geeks for Geeks. How to Call or Consume External API in Spring Boot?
			// https://www.geeksforgeeks.org/how-to-call-or-consume-external-api-in-spring-boot/
			// ALSO HELP FROM CHATGPT
			Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);

			//List<Object> cardObjects = Arrays.asList(response);
			// SOURCE: CHATGPT
			List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.get("data");
			

			if (dataList != null) {
                for (Map<String, Object> item : dataList) {

                    String name = (String) item.get("name");
					//String oracl = (String) item.get("oracle_text");
					
					Map<String, Object> imagesListUrl = (Map<String, Object>) item.get("image_uris");
					String imgUrl = "";
					if (imagesListUrl != null) {
						imgUrl = (String) imagesListUrl.get("normal");
					}
					
					
					log.info(name);
                    //String imgSmall = (String) item.get("imgSmall"); // Extract the imgSmall URL

                    // Create and add a new Card to the list
                    Card card = new Card("a1a1", name, "Cool Card",imgUrl,"Set of Setness",
				"Creature - Cool Wolf", list1, list2, 2, 3);
                    cards.add(card);
                }
            }
			// CHATGPT: END
			
			// ADD THE TEST DATA: CARDS
			log.info("In App: Save a couple of cards");

			for (Card card : cards) {
				cardRepository.save(card);
			}

			Card card1 = new Card("a1a1", "Cool Guy", "This guy is cool","https://cards.scryfall.io/normal/front/1/5/153b7197-57a7-4e38-bd4a-4550b9d22dd8.jpg?1562900088","Set of Setness",
			"Creature - Cool Human", list1, list2, 3, 4);

			Card card2 = new Card("a1a1", "Cool Wolf", "This wolf is cool","https://cards.scryfall.io/normal/front/f/f/ff4661dd-2075-48c3-b19b-fc7f8aaba1b8.jpg?1562667998","Set of Setness",
			"Creature - Cool Wolf", list1, list2, 2, 3);


			//bookRepository.save(new Book("The Hobbit, or There and Back Again", "J. R. R. Tolkien", 1937L, "9780345445605", 25D, cat2));	
			cardRepository.save(card1);
			cardRepository.save(card2);

			


			// LOG THE INFO IMMEDIATELY FOR DEBUG
			// LOGGER IS GOOD: It can be TURNED ON FOR DEBUG, then TURNED OFF
			log.info("In app: Fetch all books");
			for (Card card : cardRepository.findAll()) {
				log.info(card.toString());
			}
			
			log.info("In app: Fetching complete");

		};
	}

}
