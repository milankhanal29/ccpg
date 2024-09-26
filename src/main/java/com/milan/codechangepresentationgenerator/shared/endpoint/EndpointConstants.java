package com.milan.codechangepresentationgenerator.shared.endpoint;

public class EndpointConstants {

    //user
    public static final String USER_BASE_URL = "/api/user";
    public static final String GET_USER_BY_ID = USER_BASE_URL + "/api/user";

    public static final String AUTH_BASE_URL = "/api/auth";

    // Admin Endpoints
    public static final String ADMIN_BASE_URL = "/api/admin";
    public static final String ADD_PRODUCT = ADMIN_BASE_URL + "/products/add-product";
    public static final String ADD_PRODUCT_CATEGORY = ADMIN_BASE_URL + "/product-categories/add";

    //product endpoints
    public static final String PRODUCT_BASE_URL = "/api/products";

    public static final String PRODUCT_CATEGORY_BASE_URL = "/api/product-categories";

    public static final String CATEGORIES_BASE_URL = "api/product-categories";


    public static final String IS_PRODUCT_IN_CART = "/isProductInCart";

    // Admin Endpoints

    //product endpoints


    //Brand endpoints

    public static final String BRAND_BASE_URL = "/api/brands";

    //    Common endpoints
    public static final String API_REGISTER = "/register";
    public static final String API_AUTHENTICATE = "/authenticate";


    public static final String GET_BY_ID = "/get-by-id/{id}";
    public static final String GET_ALL = "/all";


    public static final String ADD_ITEM = "/item-added";
    public static final String API_SAVE = "/save";

    public static final String API_CREATE = "/create";

    public static final String UPDATE_ITEM_QUANTITY = "";
    public static final String UPDATE = "/update";
    public static final String UPDATE_BY_ID = "/update/{id}";
    public static final String DELETE_BY_ID = "/delete/{id}";

    public static final String REMOVE = "/remove/{id}";
    public static final String CLEAR = "/clear";
    public static final String CLEAR_BY_ID = "/clear/{id}";
    public static final String FIND_USER = "/find-user";

    public static final String API_USER = "/api/auth";
    public static final String API_ADMIN = "/api/admin";
    public static final String API_CART = "/api/carts";
    public static final String API_PRODUCT = "/api/products";


    public static final String API_PRODUCTS_LIST_IDS = "/getProductsByIds";


    public static final String COUNT_BY_USER_ID = "/count/{userId}";
    public static final String UPDATE_BY_ID_WITH_LOGO = "/update-with-logo/{id}";
    public static final String RECEIVER_BASE_URL = "/api/receiver";


    public static final String GET_ORDER_BY_RECEIVER_ID = "/get-ordered-by-id/{receiverId}";
    public static final String API_ORDER = "/api/orders";
    public static final String API_RECEIVER = "/api/receiver";
    public static final String DECREASE_QUANTITY_BY_ID = "/decrease-quantity/{id}";
    public static final String GET_ALL_PRODUCTS = "get-all-product";
    public static final String API_PRODUCT_CATEGORIES = "/api/product-categories";
}
