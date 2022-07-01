package spring.example.beerclient;

public class TestRegex {

    public static void main(String[] args) {

        String num = "20002";

        System.out.println(num.matches("[0-9]+"));
    }
}
