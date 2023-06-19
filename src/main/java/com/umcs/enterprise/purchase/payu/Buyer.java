package com.umcs.enterprise.purchase.payu;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class Buyer {
    private  String email;
    private  String phone;
    private  String firstName;
    private  String lastName;
    private  String language;
}
