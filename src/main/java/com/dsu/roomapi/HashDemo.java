package com.dsu.roomapi;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class HashDemo {
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String first = encoder.encode("mina1234");
        String second = encoder.encode("mina1234");
        System.out.println(first.equals(second));
        System.out.println(encoder.matches("mina1234", first));
        System.out.println(encoder.matches("mina1235", first));

        for (int cost : new int[] {10, 12, 14}) {
            long t = System.nanoTime();
            new BCryptPasswordEncoder(cost).encode("mina1234");
            System.out.printf("cost %d: %d ms%n", cost,
                    (System.nanoTime() - t) / 1_000_000);
        }
    }
}