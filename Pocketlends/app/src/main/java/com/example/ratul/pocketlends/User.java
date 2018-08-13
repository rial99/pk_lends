package com.example.ratul.pocketlends;

public class User {
    public final String Username;
    public final String Invest_amt;
    public final String Lend_amt;
    public final String Borrow_amt;



    public User(String username, String invest_amt, String lend_amt, String borrow_amt) {
        Username = username;
        Invest_amt = invest_amt;
        Lend_amt = lend_amt;
        Borrow_amt = borrow_amt;

    }
}
