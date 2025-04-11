-- Comment tablosu
CREATE TABLE comment
(
    comment_id UUID PRIMARY KEY,
    created_at TIMESTAMP,
    is_active  BOOLEAN DEFAULT false,
    product_id UUID,
    CONSTRAINT fk_comment_product FOREIGN KEY (product_id)
        REFERENCES product (product_id)
        ON DELETE CASCADE
);

-- Comment images tablosu (ElementCollection)
CREATE TABLE comment_images
(
    comment_id UUID NOT NULL,
    image_url  VARCHAR(255),
    CONSTRAINT fk_comment_images_comment FOREIGN KEY (comment_id)
        REFERENCES comment (comment_id)
        ON DELETE CASCADE,
    PRIMARY KEY (comment_id, image_url)
);

-- Content tablosu (comment içerikleri)
CREATE TABLE content
(
    content_id UUID PRIMARY KEY,
    user_name  VARCHAR(255) NOT NULL,
    message    TEXT         NOT NULL,
    created_at TIMESTAMP,
    comment_id UUID,
    CONSTRAINT fk_content_comment FOREIGN KEY (comment_id)
        REFERENCES comment (comment_id)
        ON DELETE CASCADE
);

-- Rate tablosu (Comment ile OneToOne ilişki içeriyor)
CREATE TABLE rate
(
    rate_id    UUID PRIMARY KEY,
    user_name  VARCHAR(255)     NOT NULL,
    user_id    VARCHAR(255)     NOT NULL,
    rate       DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP,
    product_id UUID,
    comment_id UUID UNIQUE, -- Birebir ilişkiyi garanti altına almak için UNIQUE
    is_active  BOOLEAN DEFAULT false,

    CONSTRAINT fk_rate_product FOREIGN KEY (product_id)
        REFERENCES product (product_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_rate_comment FOREIGN KEY (comment_id)
        REFERENCES comment (comment_id)
        ON DELETE CASCADE
);

-- Favorite products tablosu
CREATE TABLE favorite_products
(
    id         UUID PRIMARY KEY,
    user_id    UUID      NOT NULL,
    product_id UUID      NOT NULL,
    added_at   TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_favorite_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT fk_favorite_product FOREIGN KEY (product_id)
        REFERENCES product (product_id) ON DELETE CASCADE
);

CREATE TABLE notification
(
    notification_id UUID PRIMARY KEY,
    type            VARCHAR(255) NOT NULL,
    head            TEXT         NOT NULL,
    content         TEXT         NOT NULL,
    created_at      TIMESTAMP,
    is_active       BOOLEAN      NOT NULL DEFAULT TRUE
);