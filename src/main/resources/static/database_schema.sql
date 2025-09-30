-- Create AppUser table
CREATE TABLE app_user (
    app_id BIGSERIAL PRIMARY KEY,
    app_user_name VARCHAR(255) NOT NULL UNIQUE,
    app_password VARCHAR(255) NOT NULL,
    app_role VARCHAR(255) NOT NULL
);

-- Create MtgUser table
CREATE TABLE mtg_user (
    user_id BIGSERIAL PRIMARY KEY,
    mtg_user_name VARCHAR(255) NOT NULL UNIQUE,
    profile_name VARCHAR(255),
    profile_bio VARCHAR(255),
    app_user_id BIGINT UNIQUE,
    CONSTRAINT fk_app_user FOREIGN KEY (app_user_id) REFERENCES app_user(app_id)
);

-- Create Card table
CREATE TABLE card (
    id BIGSERIAL PRIMARY KEY,
    card_id VARCHAR(255),
    card_name VARCHAR(255),
    oracle_text VARCHAR(1000),
    image_url VARCHAR(255),
    set_name VARCHAR(255),
    type_text VARCHAR(255),
    color_identity TEXT[], -- PostgreSQL array for List<String>
    mana_cost VARCHAR(255),
    produced_mana TEXT[], -- PostgreSQL array for List<String>
    power INTEGER,
    toughness INTEGER
);

-- Create Deck table
CREATE TABLE deck (
    deck_id BIGSERIAL PRIMARY KEY,
    deck_name VARCHAR(255),
    deck_description VARCHAR(255),
    user_id BIGINT,
    CONSTRAINT fk_mtg_user FOREIGN KEY (user_id) REFERENCES mtg_user(user_id)
);

-- Create junction table for Deck-Card many-to-many relationship
CREATE TABLE deck_cards (
    deck_id BIGINT NOT NULL,
    card_id BIGINT NOT NULL,
    PRIMARY KEY (deck_id, card_id),
    CONSTRAINT fk_deck FOREIGN KEY (deck_id) REFERENCES deck(deck_id) ON DELETE CASCADE,
    CONSTRAINT fk_card FOREIGN KEY (card_id) REFERENCES card(id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_app_user_username ON app_user(username);
CREATE INDEX idx_mtg_user_username ON mtg_user(user_name);
CREATE INDEX idx_deck_user_id ON deck(user_id);
CREATE INDEX idx_card_card_name ON card(card_name);
CREATE INDEX idx_deck_cards_deck_id ON deck_cards(deck_id);
CREATE INDEX idx_deck_cards_card_id ON deck_cards(card_id);