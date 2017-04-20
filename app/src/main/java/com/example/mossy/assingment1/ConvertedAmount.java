package com.example.mossy.assingment1;

/**
 * Created by mossy on 20/04/2017.
 */

public class ConvertedAmount {
    double rate_1 = 1, rate_2 = 1, user_amount = 1, total_amount;

    ConvertedAmount() {
        calculate_total();
    }

    public void update_rates(double rate_1, double rate_2) {
        this.rate_1 = rate_1;
        this.rate_2 = rate_2;
        calculate_total();
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public double getUser_amount() {
        return user_amount;
    }

    public void setUser_amount(double user_amount) {
        this.user_amount = user_amount;
        calculate_total();
    }

    private void calculate_total() {
        total_amount = (rate_2 / rate_1) * user_amount;
    }
}
