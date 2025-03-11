-- Name: address; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.address
(
    address_id   uuid NOT NULL,
    address      character varying(255),
    city         character varying(255),
    contact_name character varying(255),
    country      character varying(255),
    state        character varying(255),
    street       character varying(255),
    zip_code     character varying(255)
);
ALTER TABLE public.address
    OWNER TO arzuamber_user;

-- Name: basket_item; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.basket_item
(
    basket_id     uuid    NOT NULL,
    category1     character varying(255),
    category2     character varying(255),
    color         character varying(255),
    name          character varying(255),
    price         character varying(255),
    quantity      integer NOT NULL,
    size          character varying(255),
    stock_code    character varying(255),
    stock_size_id character varying(255)
);
ALTER TABLE public.basket_item
    OWNER TO arzuamber_user;

-- Name: blogs; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.blogs
(
    id         uuid NOT NULL,
    category   character varying(255),
    content    character varying(255),
    created_at timestamp(6) without time zone,
    image      character varying(255),
    lang       character varying(255),
    slug       character varying(255),
    title      character varying(255)
);
ALTER TABLE public.blogs
    OWNER TO arzuamber_user;

-- Name: buyer; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.buyer
(
    buyer_id             uuid NOT NULL,
    email                character varying(255),
    gsm_number           character varying(255),
    identity_number      character varying(255),
    ip                   character varying(255),
    last_login_date      character varying(255),
    name                 character varying(255),
    registration_address character varying(255),
    registration_date    character varying(255),
    surname              character varying(255)
);
ALTER TABLE public.buyer
    OWNER TO arzuamber_user;

-- Name: category; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.category
(
    category_id uuid NOT NULL,
    lang        character varying(255),
    name        character varying(255),
    image       character varying(255)
);
ALTER TABLE public.category
    OWNER TO arzuamber_user;

-- Name: category_sub_categories; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.category_sub_categories
(
    category_category_id uuid NOT NULL,
    sub_categories       character varying(255)
);
ALTER TABLE public.category_sub_categories
    OWNER TO arzuamber_user;

-- Name: color_size; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.color_size
(
    color_size_id uuid NOT NULL,
    color         character varying(255),
    stock_code    character varying(255),
    total_stock   integer,
    product_id    uuid NOT NULL
);
ALTER TABLE public.color_size
    OWNER TO arzuamber_user;

-- Name: color_size_images; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.color_size_images
(
    color_size_color_size_id uuid NOT NULL,
    images                   character varying(255)
);
ALTER TABLE public.color_size_images
    OWNER TO arzuamber_user;

-- Name: colors; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.colors
(
    order_id uuid NOT NULL,
    lang     character varying(255),
    name     character varying(255)
);
ALTER TABLE public.colors
    OWNER TO arzuamber_user;

-- Name: contacts; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.contacts
(
    contact_id uuid NOT NULL,
    email      character varying(255),
    message    character varying(255),
    name       character varying(255),
    surname    character varying(255)
);
ALTER TABLE public.contacts
    OWNER TO arzuamber_user;


-- Name: images; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.images
(
    color_size_id uuid NOT NULL,
    images        character varying(255)
);
ALTER TABLE public.images
    OWNER TO arzuamber_user;

-- Name: orders; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.orders
(
    order_id            uuid NOT NULL,
    created_at          timestamp(6) without time zone,
    payment_id          character varying(255),
    status              character varying(255),
    total_price         numeric(38, 2),
    billing_address_id  uuid,
    buyer_id            uuid,
    shipping_address_id uuid,
    CONSTRAINT orders_status_check CHECK (((status)::text = ANY
                                           (ARRAY [('PENDING'::character varying)::text, ('AWAITING_PAYMENT'::character varying)::text, ('PAID'::character varying)::text, ('PROCESSING'::character varying)::text, ('AWAITING_SHIPMENT'::character varying)::text, ('AWAITING_FULFILLMENT'::character varying)::text, ('SHIPPED'::character varying)::text, ('IN_TRANSIT'::character varying)::text, ('OUT_FOR_DELIVERY'::character varying)::text, ('DELIVERED'::character varying)::text, ('FAILED_DELIVERY'::character varying)::text, ('CANCELLED'::character varying)::text, ('REFUNDED'::character varying)::text, ('RETURN_REQUESTED'::character varying)::text, ('RETURNED'::character varying)::text])))
);
ALTER TABLE public.orders
    OWNER TO arzuamber_user;

-- Name: orders_basket_items; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.orders_basket_items
(
    order_order_id         uuid NOT NULL,
    basket_items_basket_id uuid NOT NULL
);
ALTER TABLE public.orders_basket_items
    OWNER TO arzuamber_user;

-- Name: product; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.product
(
    product_id     uuid    NOT NULL,
    category       character varying(255),
    created_at     timestamp(6) without time zone,
    description    text,
    discount_price numeric(38, 2),
    lang           character varying(255),
    length         character varying(255),
    name           character varying(255),
    new_season     boolean NOT NULL,
    populate       boolean NOT NULL,
    price          numeric(38, 2),
    purchase_price numeric(38, 2),
    slug           character varying(255),
    sub_category   character varying(255),
    total_stock    integer,
    updated_at     timestamp(6) without time zone,
    is_active      boolean
);
ALTER TABLE public.product
    OWNER TO arzuamber_user;

-- Name: product_orders; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.product_orders
(
    order_id     uuid NOT NULL,
    address      character varying(255),
    city         character varying(255),
    color        character varying(255),
    country      character varying(255),
    email        character varying(255),
    first_name   character varying(255),
    last_name    character varying(255),
    phone_number character varying(255),
    product_id   character varying(255),
    quantity     integer,
    size         character varying(255),
    state        character varying(255),
    total_price  numeric(38, 2)
);
ALTER TABLE public.product_orders
    OWNER TO arzuamber_user;

-- Name: product_orders_seq; Type: SEQUENCE; Schema: public; Owner: arzuamber_user
CREATE SEQUENCE public.product_orders_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE public.product_orders_seq OWNER TO arzuamber_user;

-- Name: slider_image; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.slider_image
(
    slider_image_id uuid                   NOT NULL,
    url             character varying(255) NOT NULL,
    is_active       boolean                NOT NULL,
    lang            character varying(255) NOT NULL
);
ALTER TABLE public.slider_image
    OWNER TO arzuamber_user;

-- Name: stock_size; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.stock_size
(
    stock_size_id uuid    NOT NULL,
    size          character varying(255),
    stock         integer NOT NULL,
    color_size_id uuid    NOT NULL
);
ALTER TABLE public.stock_size
    OWNER TO arzuamber_user;

-- Name: user_roles; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.user_roles
(
    user_id uuid NOT NULL,
    roles   character varying(255),
    CONSTRAINT user_roles_roles_check CHECK (((roles)::text = ANY
                                              (ARRAY [('ADMIN'::character varying)::text, ('USER'::character varying)::text])))
);
ALTER TABLE public.user_roles
    OWNER TO arzuamber_user;

-- Name: users; Type: TABLE; Schema: public; Owner: arzuamber_user
CREATE TABLE public.users
(
    id              uuid NOT NULL,
    birth_date      date,
    created_at      timestamp(6) without time zone,
    email           character varying(255),
    first_name      character varying(255),
    hashed_password character varying(255),
    last_login      timestamp(6) without time zone,
    last_name       character varying(255),
    updated_at      timestamp(6) without time zone,
    address         character varying(255)
);
ALTER TABLE public.users
    OWNER TO arzuamber_user;