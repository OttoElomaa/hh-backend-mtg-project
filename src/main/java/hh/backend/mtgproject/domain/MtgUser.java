package hh.backend.mtgproject.domain;

import java.util.List;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;


@Entity
public class MtgUser {


	// PRIMARY KEY OF "TABLE" == ENTITY
    // ENTITY INTERFACES WITH THE ACTUAL TABLE IN DATABASE
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable=false, updatable=false)
	private Long userId;

	@Column(name = "mtg_user_name", nullable=false, unique=true)
	private String userName;
	
	@Column
	private String profileName;
	@Column
	private String profileBio;


	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appUser_id", referencedColumnName = "appId")
    private AppUser appUser;

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


	public AppUser getAppUser() {
		return appUser;
	}


	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	
}
