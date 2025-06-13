CREATE TABLE address
(
    address_id   UUID NOT NULL,
    contact_name VARCHAR(255),
    city         VARCHAR(255),
    state        VARCHAR(255),
    country      VARCHAR(255),
    address      VARCHAR(255),
    street       VARCHAR(255),
    zip_code     VARCHAR(255),
    CONSTRAINT pk_address PRIMARY KEY (address_id)
);

CREATE TABLE basket_item
(
    basket_id     UUID    NOT NULL,
    name          VARCHAR(255),
    category1     VARCHAR(255),
    category2     VARCHAR(255),
    price         VARCHAR(255),
    quantity      INTEGER NOT NULL,
    size          VARCHAR(255),
    color         VARCHAR(255),
    stock_size_id VARCHAR(255),
    stock_code    VARCHAR(255),
    image         VARCHAR(255),
    CONSTRAINT "pk_basketıtem" PRIMARY KEY (basket_id)
);

CREATE TABLE blogs
(
    id         UUID NOT NULL,
    title      VARCHAR(255),
    slug       VARCHAR(255),
    category   VARCHAR(255),
    image      VARCHAR(255),
    content    TEXT,
    lang       VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_blogs PRIMARY KEY (id)
);

CREATE TABLE buyer
(
    buyer_id             UUID NOT NULL,
    name                 VARCHAR(255),
    surname              VARCHAR(255),
    gsm_number           VARCHAR(255),
    email                VARCHAR(255),
    ip                   VARCHAR(255),
    identity_number      VARCHAR(255),
    last_login_date      VARCHAR(255),
    registration_date    VARCHAR(255),
    registration_address VARCHAR(255),
    CONSTRAINT pk_buyer PRIMARY KEY (buyer_id)
);

CREATE TABLE category
(
    category_id UUID NOT NULL,
    name        VARCHAR(255),
    lang        VARCHAR(255),
    image       VARCHAR(255),
    CONSTRAINT pk_category PRIMARY KEY (category_id)
);

CREATE TABLE category_sub_categories
(
    category_category_id UUID NOT NULL,
    sub_categories       VARCHAR(255)
);

CREATE TABLE color_size
(
    color_size_id UUID NOT NULL,
    color         VARCHAR(255),
    stock_code    VARCHAR(255),
    product_id    UUID,
    CONSTRAINT "pk_color_sıze" PRIMARY KEY (color_size_id)
);

CREATE TABLE color_size_images
(
    color_size_color_size_id UUID NOT NULL,
    images                   VARCHAR(255)
);

CREATE TABLE colors
(
    order_id UUID NOT NULL,
    name     VARCHAR(255),
    lang     VARCHAR(255),
    CONSTRAINT pk_colors PRIMARY KEY (order_id)
);

CREATE TABLE comment
(
    comment_id UUID NOT NULL,
    product_id UUID,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    is_active  BOOLEAN,
    CONSTRAINT pk_comment PRIMARY KEY (comment_id)
);

CREATE TABLE comment_images
(
    comment_id UUID NOT NULL,
    image_url  VARCHAR(255)
);

CREATE TABLE contacts
(
    contact_id UUID NOT NULL,
    name       VARCHAR(255),
    surname    VARCHAR(255),
    email      VARCHAR(255),
    message    VARCHAR(255),
    CONSTRAINT pk_contacts PRIMARY KEY (contact_id)
);

CREATE TABLE content
(
    content_id UUID NOT NULL,
    user_name  VARCHAR(255),
    message    VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    comment_id UUID,
    CONSTRAINT pk_content PRIMARY KEY (content_id)
);

CREATE TABLE favorite_products
(
    id         UUID NOT NULL,
    user_id    UUID,
    product_id UUID,
    added_at   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT "pk_favorıte_products" PRIMARY KEY (id)
);

CREATE TABLE notification
(
    notification_id UUID NOT NULL,
    type            VARCHAR(255),
    head            VARCHAR(255),
    content         VARCHAR(255),
    created_at      TIMESTAMP WITHOUT TIME ZONE,
    is_active       BOOLEAN,
    CONSTRAINT "pk_notıfıcatıon" PRIMARY KEY (notification_id)
);

CREATE TABLE orders
(
    order_id            UUID NOT NULL,
    buyer_id            UUID,
    shipping_address_id UUID,
    billing_address_id  UUID,
    total_price         DECIMAL,
    status              VARCHAR(255),
    payment_type        VARCHAR(255),
    payment_id          VARCHAR(255),
    created_at          TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_orders PRIMARY KEY (order_id)
);

CREATE TABLE orders_basket_items
(
    order_order_id         UUID NOT NULL,
    basket_items_basket_id UUID NOT NULL
);

CREATE TABLE product
(
    product_id     UUID    NOT NULL,
    name           VARCHAR(255),
    slug           VARCHAR(255),
    populate       BOOLEAN NOT NULL,
    new_season     BOOLEAN NOT NULL,
    category       VARCHAR(255),
    sub_category   VARCHAR(255),
    description    TEXT,
    price          DECIMAL,
    lang           VARCHAR(255),
    length         VARCHAR(255),
    total_stock    INTEGER,
    purchase_price DECIMAL,
    discount_price DECIMAL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE,
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    is_active      BOOLEAN,
    CONSTRAINT pk_product PRIMARY KEY (product_id)
);

CREATE TABLE rate
(
    rate_id    UUID             NOT NULL,
    user_name  VARCHAR(255),
    user_id    VARCHAR(255),
    rate       DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    is_active  BOOLEAN,
    product_id UUID,
    comment_id UUID,
    CONSTRAINT pk_rate PRIMARY KEY (rate_id)
);

CREATE TABLE sliders
(
    slider_image_id UUID NOT NULL,
    url             VARCHAR(255),
    lang            VARCHAR(255),
    CONSTRAINT "pk_slıders" PRIMARY KEY (slider_image_id)
);

CREATE TABLE stock_size
(
    stock_size_id UUID    NOT NULL,
    size          VARCHAR(255),
    stock         INTEGER NOT NULL,
    color_size_id UUID,
    CONSTRAINT "pk_stock_sıze" PRIMARY KEY (stock_size_id)
);

CREATE TABLE user_fav_products
(
    user_id      UUID NOT NULL,
    fav_products VARCHAR(255)
);

CREATE TABLE user_roles
(
    user_id UUID NOT NULL,
    roles   VARCHAR(255)
);

CREATE TABLE users
(
    id              UUID NOT NULL,
    first_name      VARCHAR(255),
    last_name       VARCHAR(255),
    email           VARCHAR(255),
    hashed_password VARCHAR(255),
    phone_number    VARCHAR(255),
    address         VARCHAR(255),
    created_at      TIMESTAMP WITHOUT TIME ZONE,
    updated_at      TIMESTAMP WITHOUT TIME ZONE,
    birth_date      date,
    last_login      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE orders_basket_items
    ADD CONSTRAINT "uc_orders_basket_ıtems_basketıtems_basket" UNIQUE (basket_items_basket_id);

ALTER TABLE orders
    ADD CONSTRAINT uc_orders_buyer UNIQUE (buyer_id);

ALTER TABLE orders
    ADD CONSTRAINT "uc_orders_bıllıng_address" UNIQUE (billing_address_id);

ALTER TABLE orders
    ADD CONSTRAINT "uc_orders_shıppıng_address" UNIQUE (shipping_address_id);

ALTER TABLE rate
    ADD CONSTRAINT uc_rate_comment UNIQUE (comment_id);

ALTER TABLE color_size
    ADD CONSTRAINT FK_COLOR_SIZE_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (product_id);

ALTER TABLE comment
    ADD CONSTRAINT FK_COMMENT_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (product_id);

ALTER TABLE content
    ADD CONSTRAINT FK_CONTENT_ON_COMMENT FOREIGN KEY (comment_id) REFERENCES comment (comment_id);

ALTER TABLE favorite_products
    ADD CONSTRAINT FK_FAVORITE_PRODUCTS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (product_id);

ALTER TABLE favorite_products
    ADD CONSTRAINT FK_FAVORITE_PRODUCTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_BILLING_ADDRESS FOREIGN KEY (billing_address_id) REFERENCES address (address_id);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_BUYER FOREIGN KEY (buyer_id) REFERENCES buyer (buyer_id);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_SHIPPING_ADDRESS FOREIGN KEY (shipping_address_id) REFERENCES address (address_id);

ALTER TABLE rate
    ADD CONSTRAINT FK_RATE_ON_COMMENT FOREIGN KEY (comment_id) REFERENCES comment (comment_id);

ALTER TABLE rate
    ADD CONSTRAINT FK_RATE_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (product_id);

ALTER TABLE stock_size
    ADD CONSTRAINT FK_STOCK_SIZE_ON_COLOR_SIZE FOREIGN KEY (color_size_id) REFERENCES color_size (color_size_id);

ALTER TABLE category_sub_categories
    ADD CONSTRAINT "fk_category_subcategorıes_on_category" FOREIGN KEY (category_category_id) REFERENCES category (category_id);

ALTER TABLE color_size_images
    ADD CONSTRAINT "fk_colorsıze_ımages_on_color_sıze" FOREIGN KEY (color_size_color_size_id) REFERENCES color_size (color_size_id);

ALTER TABLE comment_images
    ADD CONSTRAINT "fk_comment_ımages_on_comment" FOREIGN KEY (comment_id) REFERENCES comment (comment_id);

ALTER TABLE orders_basket_items
    ADD CONSTRAINT "fk_ordbasıte_on_basket_ıtem" FOREIGN KEY (basket_items_basket_id) REFERENCES basket_item (basket_id);

ALTER TABLE orders_basket_items
    ADD CONSTRAINT "fk_ordbasıte_on_order" FOREIGN KEY (order_order_id) REFERENCES orders (order_id);

ALTER TABLE user_fav_products
    ADD CONSTRAINT fk_user_favproducts_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_on_user FOREIGN KEY (user_id) REFERENCES users (id);