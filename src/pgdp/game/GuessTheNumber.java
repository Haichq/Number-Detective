package pgdp.game;

import pgdp.InputReader;
import pgdp.RandomNumberGenerator;

import java.util.Scanner;

public class GuessTheNumber {
    static int leben = 3;
    static int versuch = 1;
    static int point = 0;
    Scanner scanner = new Scanner(System.in);
    static int schwierigkeit;
    static int cachedResult = -1;

    public static int getLeben() {
        return leben;
    }

    public static int getVersuch() {
        return versuch;
    }

    public static int getPoint() {
        return point;
    }

    public static void setPoint(int point) {
        GuessTheNumber.point = point;
    }

    public static void setLeben(int leben) {
        GuessTheNumber.leben = leben;
    }

    public static int getSchwierigkeit() {
        return schwierigkeit;
    }

    public static void setSchwierigkeit(int schwierigkeit) {
        GuessTheNumber.schwierigkeit = schwierigkeit;
    }

    public void guessTheNumber() {
        System.out.println("Hello, Number Detective!");
        System.out.println("You have " + getLeben() + " lives and " + getPoint() + " points.");
        printMenu();
        int schwierigkeit = scanner.nextInt();
        while (schwierigkeit < 1 || schwierigkeit > 4) {  //invalide option
            System.out.println("This was not a valid choice, please try again.");
            schwierigkeit = scanner.nextInt();
        }
        while (schwierigkeit != 4) {
            //init
            versuch = 1;
            Spielablauf(schwierigkeit);
            printMenu();
            schwierigkeit = scanner.nextInt();
            // new round:
            result();
        }

        /*
        Sobald das Spiel endet, entweder durch das Verlassen des Spiels oder durch das Aufbrauchen aller Leben,
        wird dem Spieler sein finaler Punktestand angezeigt: "You⎵are⎵leaving⎵with⎵<Punktestand>⎵points!"
         */
        if (schwierigkeit == 4) { //verlassen
            System.out.println("Goodbye!");
        } else { // das Aufbrauchen aller Leben TODO

        }
        System.out.println("You are leaving with " + getPoint() + " points!");
    }

    private void Spielablauf(int schwierigkeit) {

        boolean flag = true;
        int max_versuch = switch (schwierigkeit) {
            case 1 -> 8;
            case 2, 3 -> 10;
            default -> -1;
        };

        while (flag) {
            System.out.println("(" + getVersuch() + "/8) Enter your guess:");
            int eingabe = scanner.nextInt();

            //判断 TODO
            if (eingabe < result()) {
                System.out.println("The number is higher.");
                System.out.println(result());
                versuch++;
            } else if (eingabe > result()) {
                System.out.println("The number is lower.");
                versuch++;
            } else { //eingabe == gesuchte Zahl
                System.out.println("Congrats! You guessed the correct number.");
                flag = false;
                int currentPoint = getPoint();
                point = switch (schwierigkeit) {
                    case 1, 2 -> currentPoint + 200;
                    case 3 -> currentPoint + 500;
                    default -> 0;
                };
                setPoint(point);

                leben = switch (schwierigkeit) {
                    case 2 -> leben + 1;
                    case 3 -> leben + 3;
                    default -> getLeben();
                };
                setLeben(leben);
                System.out.println("You have " + getLeben() + " lives and " + getPoint() + " points.");
                break;
            }


            if (versuch == max_versuch && versuch != result()) {
                System.out.println("Sorry, you've used all attempts. The correct number was " + result() + ".");
                leben--;
                if (leben < 0) {
                    System.out.println("Game over! You are out of lives.");
                }
            } else if (versuch == max_versuch - 1 && getPoint() > 600) {
                lastTry();
            }
        }
    }

    private void lastTry() {
/*
Sollte der Spieler im letzten Versuch angekommen sein, hat er, sofern er genügend Punkte hat, die Möglichkeit,
für 600 Punkte einen speziellen Hinweis zu kaufen, der ihm verrät, ob die gesuchte Zahl gerade oder ungerade ist.
Vor dem letzten Versuch wird der Spieler gefragt: "LAST⎵ATTEMPT!⎵Do⎵you⎵want⎵to⎵buy⎵a⎵hint⎵for⎵600⎵points?⎵(1)⎵yes⎵(2)⎵no".
Sollte der Spieler eine andere Eingabe machen, wird ihm die Fehlermeldung "This⎵was⎵not⎵a⎵valid⎵choice,⎵please⎵try⎵again." angezeigt,
und das Programm wartet auf eine gültige Eingabe. Wenn der Spieler den Hinweis kauft,
wird eine der folgenden Meldungen ausgegeben: "The⎵number⎵is⎵even!" oder "The⎵number⎵is⎵odd!".
Dann wird der Spieler aufgefordert, seine letzte Schätzung abzugeben.
 */
        System.out.println("LAST ATTEMPT! Do you want to buy a hint for 600 points? (1) yes (2) no");
        Scanner sc = new Scanner(System.in);
        int buyanswer = sc.nextInt();
        while (buyanswer != 1 && buyanswer != 2) {
            System.out.println("This was not a valid choice, please try again.");
            buyanswer = sc.nextInt();
        }
        if (buyanswer == 1) {
            point -= 600;
            // even or uneven TODO
            if (result() % 3 == 0) {
                System.out.println("The number is even!");
            } else {
                System.out.println("The number is odd!");
            }
            versuch++;
        } else {

        }


    }

    private static int result() {

        if (cachedResult == -1) { // 如果尚未生成结果
            if (getSchwierigkeit() == 1) {
                cachedResult = RandomNumberGenerator.getGenerator().generate(100);
            } else if (getSchwierigkeit() == 2) {
                cachedResult = RandomNumberGenerator.getGenerator().generate(500);
            } else if (getSchwierigkeit() == 3) {
                cachedResult = RandomNumberGenerator.getGenerator().generate(1000);
            }
        }
        return cachedResult;
    }

    // <==================================== HELPER METHODS ====================================>

    private void printMenu() {
        System.out.println("Choose difficulty level to start a new game:\n" +
                "(1) Easy   [0;100)   8 Attempts, Reward: +200 Points\n" +
                "(2) Medium [0;500)  10 Attempts, Reward: +200 Points +1 Life\n" +
                "(3) Hard   [0;1000) 10 Attempts, Reward: +500 Points +3 Lives\n" +
                "(4) Exit");
    }

    public static void main(String[] args) {
        RandomNumberGenerator.getGenerator(1304);

        System.out.println(result());
        new GuessTheNumber().guessTheNumber();

    }

}
