package com.snippetTech.ratul.pocketlends;

public class User {
    public String Username;
    public String Invest_amt;
    public String Lend_amt;
    public String Borrow_amt;
    public String Interest_amt_L;
    public String Interest_amt_B;

    public User(String username, String invest_amt, String lend_amt, String borrow_amt,String interest_amt_L,String interest_amt_B) {
        Username = username;
        Invest_amt = invest_amt;
        Lend_amt = lend_amt;
        Borrow_amt = borrow_amt;
        Interest_amt_L = interest_amt_L;
        Interest_amt_B = interest_amt_B;
    }
}
