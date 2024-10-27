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
import hh.backend.mtgproject.domain.MtgUser;
import hh.backend.mtgproject.domain.MtgUserRepository;



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


	//################################################################################################
	// THIS FUNCTION HANDLES ALL FILE READING
	private static List<Card> readApiToCardList(String apiUrl, List<Card> oldCards, RestTemplate restTemplate) {


		List<Card> newCards = new ArrayList<Card>();

		ArrayList<String> list2 = new ArrayList<>();
		String[] array1 = {"a","b"};

		// SOURCE: Geeks for Geeks. How to Call or Consume External API in Spring Boot?
		// https://www.geeksforgeeks.org/how-to-call-or-consume-external-api-in-spring-boot/
		// ALSO HELP FROM CHATGPT
		Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);

		//List<Object> cardObjects = Arrays.asList(response);
		// SOURCE: CHATGPT
		List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.get("data");
		

		if (dataList != null) {
			for (Map<String, Object> item : dataList) {

				// FIX FOR MULTI-FACED CARDS (Saga+Creature etc.)
				// ALSO SKIP MULTI-PART CARD ENTRIES
				List<Map<String, Object>> faces = (List<Map<String, Object>>) item.get("card_faces");
				List<Map<String, Object>> parts = (List<Map<String, Object>>) item.get("all_parts");
				if (faces != null) {
					item = faces.get(0);
				} else if (parts != null) {
					continue;
				}

				// NAME, TYPE_LINE
				String name = (String) item.get("name");
				String typeText = (String) item.get("type_line");
				
				// ORACLE_TEXT HANDLING FOR NULL + TOO LONG
				String oracl = itemGetStringOrAltText(item, "oracle_text", "Oracle Text Missing");
				if (oracl.length() > 200) {
					oracl = oracl.substring(0, 199);
				}
				
				// IMAGE URL
				Map<String, Object> imagesListUrl = (Map<String, Object>) item.get("image_uris");
				String imgUrl = "";
				if (imagesListUrl != null) {
					imgUrl = (String) imagesListUrl.get("normal");
				}

				//String powerStr = (String) item.get("power");
				int power = itemGetIntOrAlt(item, "power");
				int toughness = itemGetIntOrAlt(item, "toughness");
				
				String manaCostStr = (String) item.get("mana_cost");
				if (manaCostStr.equals("")) {
					manaCostStr = "Free";
				}

				List<String> producedMana = (List<String>) item.get("produced_mana");

				// LIST TO ARRAY. SOURCE: Eng.Fouad on StackOverflow. Question - Convert list to array in Java [duplicate]
				// https://stackoverflow.com/questions/9572795/convert-list-to-array-in-java

				//String[] colorIds = {};
				List<String> colorIdList = (List<String>) item.get("color_identity");

				// if (colorIdList != null) {
				// 	colorIds = (new String[colorIdList.size()]);
				// 	for(int i = 0; i < colorIdList.size(); i++) {
				// 		colorIds[i] = colorIdList.get(i);
				// 	};
				// } // SOURCED PART ENDS

				

				//log.info(name);
				
				// MOST IMPORTANT PART
				// Create and add a new Card to the list
				Card card = new Card("a1a1", name, oracl,imgUrl,"Set of Setness",
			typeText, colorIdList, manaCostStr, producedMana, power, toughness);
				oldCards.add(card);
			}
		}
		// CHATGPT: END


		return oldCards;
	}





	private static String itemGetStringOrAltText(Map<String, Object> item, String keyword, String altText) {

		String newWord = (String) item.get(keyword);
		if (newWord == null) {
			newWord = "Oracle Text";
		}
		return newWord;
	}


	private static int itemGetIntOrAlt(Map<String, Object> item, String keyword) {

		String getStr = (String) item.get(keyword);
		int returnInt = -999;
		if (getStr != null) {
			returnInt = Integer.parseInt(getStr);
		}
		return returnInt;
	}



	//  testidatan luonti H2-testitietokantaan aina sovelluksen käynnistyessä
	@Bean
	public CommandLineRunner MtgAppRunner(CardRepository cardRepository, RestTemplate templ, MtgUserRepository userRepository) { 
		
		return (args) -> {

			
			ArrayList<String> list2 = new ArrayList<>();
			String[] array1 = {"a","b"};


			// ADD USERS TO DATABASE
			userRepository.save(new MtgUser("user1", "CoolUser", "Hi! I'm a cool MTG App user"));
			userRepository.save(new MtgUser("user2", "UserTwo", "I'm also a cool MTG App user"));

			// CALLING MY CUSTOM FUNC - API CALL + PARSE RESULTS
			// REPLACES 'cards' WITH NEW VERSION OF ITSELF
			// add: 

			//cards = readApiToCardList("https://api.scryfall.com/cards/search?q=+t=snake+set=neo", cards, templ);
			cards = readApiToCardList("https://api.scryfall.com/cards/search?q=+t=creature+set=neo", cards, templ);
			cards = readApiToCardList("https://api.scryfall.com/cards/search?q=+t=land+set=neo", cards, templ);
			cards = readApiToCardList("https://api.scryfall.com/cards/search?q=+t=artifact+set=neo", cards, templ);
			cards = readApiToCardList("https://api.scryfall.com/cards/search?q=+t=enchantment+set=neo", cards, templ);
			
			//cards = readApiToCardList("", cards, templ);
			
			// ADD THE TEST DATA: CARDS
			log.info("In App: Save a couple of cards");

			for (Card card : cards) {
				cardRepository.save(card);
			}

			//Card card1 = new Card("a1a1", "Cool Guy", "This guy is cool","https://cards.scryfall.io/normal/front/1/5/153b7197-57a7-4e38-bd4a-4550b9d22dd8.jpg?1562900088","Set of Setness",
			//"Creature - Cool Human", list2, "G", list2, 3, 4);

			//Card card2 = new Card("a1a1", "Cool Wolf", "This wolf is cool","https://cards.scryfall.io/normal/front/f/f/ff4661dd-2075-48c3-b19b-fc7f8aaba1b8.jpg?1562667998","Set of Setness",
			//"Creature - Cool Wolf", list2,"G", list2, 2, 3);

			
			


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
