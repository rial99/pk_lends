package com.example.ratul.pocketlends;

public class User {
    public final String Username;
    public final int Invest_amt;
    public final int Lend_amt;
    public final int Borrow_amt;

    public final int Id;
    public final int Trx_id;
    public final int Weight_id;

    public User(String username, int invest_amt, int lend_amt, int borrow_amt, int id, int trx_id, int weight_id) {
        Username = username;
        Invest_amt = invest_amt;
        Lend_amt = lend_amt;
        Borrow_amt = borrow_amt;
        Id = id;
        Trx_id = trx_id;
        Weight_id = weight_id;
    }
}
