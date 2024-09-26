package com.milan.codechangepresentationgenerator.shared.regexpcollection;

public class RegexpsCollection {
    public final static String NAME_PATTERN = "^[a-zA-Z]{2,20}$";
    public final static String MIDDLE_NAME_PATTERN = "^[a-zA-Z]+$";

    public final static String EMAIL_PATTERN = "^[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}$";
    public final static String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    public final static String ADDRESS_PATTERN = "[\\w\\s]{2,50}";
    public final static String PHONE_PATTERN = "^(97|98)[0-9]{8}$";
    public final static String COUNTRY_PATTERN = "^[a-zA-Z ]+$";
    public final static String CITY_PATTERN = "^[a-zA-Z ]+$";
    public final static String STREET_PATTERN = "^[a-zA-Z0-9 ]+$";
    public final static String STREET_NUMBER_PATTERN = "^[a-zA-Z0-9]+$";


}
