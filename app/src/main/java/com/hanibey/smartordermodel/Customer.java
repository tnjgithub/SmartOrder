package com.hanibey.smartordermodel;


public class Customer {

    public String Title;
    public String ClientCount;
    public String SubscriptionDate;
    public String Currency;


    public Customer() { }

    public Customer(String title, String clientCount, String subscriptionDate, String currency) {
        super();
        this.Title = title;
        this.ClientCount = clientCount;
        this.SubscriptionDate = subscriptionDate;
        this.Currency = currency;
    }

}
