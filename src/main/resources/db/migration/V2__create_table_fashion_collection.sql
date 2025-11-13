CREATE TABLE fashion_collection
(
    id              UUID    NOT NULL,
    collection_name VARCHAR(255),
    slug            VARCHAR(255),
    image           VARCHAR(255),
    description     TEXT,
    is_active       BOOLEAN NOT NULL,
    CONSTRAINT "pk_fashıoncollectıon" PRIMARY KEY (id)
);

CREATE TABLE fashion_collection_products
(
    fashion_collection_id UUID NOT NULL,
    product_id            UUID NOT NULL,
    CONSTRAINT "pk_fashıon_collectıon_products" PRIMARY KEY (fashion_collection_id, product_id)
);

ALTER TABLE fashion_collection_products
    ADD CONSTRAINT "fk_fascolpro_on_fashıon_collectıon" FOREIGN KEY (fashion_collection_id) REFERENCES fashion_collection (id);

ALTER TABLE fashion_collection_products
    ADD CONSTRAINT fk_fascolpro_on_product FOREIGN KEY (product_id) REFERENCES product (product_id);