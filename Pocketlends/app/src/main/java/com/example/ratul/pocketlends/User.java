package com.example.ratul.pocketlends;

public class User {
    public final String Username;
    public final String Invest_amt;
    public final String Lend_amt;
    public final String Borrow_amt;

    public final int Id;
    public final int Trx_id;
    public final int Weight_id;


    public User(String username, String invest_amt, String lend_amt, String borrow_amt, int id, int trx_id, int weight_id) {
        Username = username;
        Invest_amt = invest_amt;
        Lend_amt = lend_amt;
        Borrow_amt = borrow_amt;
        Id = id;
        Trx_id = trx_id;
        Weight_id = weight_id;
    }
}
