package hh.backend.mtgproject.domain;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;


@Entity
public class MtgUser {


	// PRIMARY KEY OF "TABLE" == ENTITY
    // ENTITY INTERFACES WITH THE ACTUAL TABLE IN DATABASE
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;


	private String userName;
	private String profileName;
	private String profileBio;


	@OneToMany(cascade = CascadeType.ALL, mappedBy ="user")
	private List<Deck> decks;


	public MtgUser() {
	}


	public MtgUser(String userName, String profileName, String profileBio) {
		
		this.userName = userName;
		this.profileName = profileName;
		this.profileBio = profileBio;
	}


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}


	public String getProfileBio() {
		return profileBio;
	}

	public void setProfileBio(String profileBio) {
		this.profileBio = profileBio;
	}


	public List<Deck> getDecks() {
		return decks;
	}

	public void setDecks(List<Deck> decks) {
		this.decks = decks;
	}


	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", profileName=" + profileName + ", profileBio="
				+ profileBio + ", decks=" + decks + "]";
	}

	
}
