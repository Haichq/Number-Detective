package pgdp.game;

import pgdp.InputReader;
import pgdp.RandomNumberGenerator;

import java.util.Scanner;

public class GuessTheNumber {
    int leben = 3;
    int versuch = 1;
    int point = 0;
    Scanner scanner = new Scanner(System.in);
    int schwierigkeit = -1;
    int cache = -1;
    int max_versuch = -1;

    public int getCache() {
        return cache;
    }

    public void setCache(int cache) {
        this.cache = cache;
    }

    public int getLeben() {
        return leben;
    }

    public int getVersuch() {
        return versuch;
    }

    public int getPoint() {
        return point;
    }

    public void setLeben(int leben) {
        this.leben = leben;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setSchwierigkeit(int schwierigkeit) {
        this.schwierigkeit = schwierigkeit;
    }

    public int getSchwierigkeit() {
        return schwierigkeit;
    }

    public void guessTheNumber() {
        System.out.println("Hello, Number Detective!");
        System.out.println("You have " + getLeben() + " lives and " + getPoint() + " points.");
        printMenu();
        int input = scanner.nextInt();
        while (input < 1 || input > 4) {  //invalide option
            System.out.println("This was not a valid choice, please try again.");
            input = scanner.nextInt();
        }
        setSchwierigkeit(input);
        max_versuch = switch (getSchwierigkeit()) {
            case 1 -> 8;
            case 2, 3 -> 10;
            default -> -1;
        };

        while (getSchwierigkeit() != 4 && getLeben() > 0) {

            versuch = 1; // 初始化尝试次数

            cache = -1;
            Spielablauf(); // 调用逻辑

            // 再次显示菜单并读取用户选择
            if (getLeben() > 0) {
                printMenu();

                input = scanner.nextInt();
                while (input < 1 || input > 4) {
                    System.out.println("This was not a valid choice, please try again.");
                    input = scanner.nextInt();
                }
                setSchwierigkeit(input); // 更新难度
                max_versuch = switch (getSchwierigkeit()) {
                    case 1 -> 8;
                    case 2, 3 -> 10;
                    default -> -1;
                };
            }
        }

        /*
        Sobald das Spiel endet, entweder durch das Verlassen des Spiels oder durch das Aufbrauchen aller Leben,
        wird dem Spieler sein finaler Punktestand angezeigt: "You⎵are⎵leaving⎵with⎵<Punktestand>⎵points!"
         */
        if (getSchwierigkeit() == 4) {
            System.out.println("Goodbye!");
        }

        System.out.println("You are leaving with " + getPoint() + " points!");
    }

    private void Spielablauf() {
        boolean flag = true;
        int target = result();
        while (flag) {

            System.out.println("(" + getVersuch() + "/" + max_versuch + ") Enter your guess:");
            int eingabe = scanner.nextInt();

            //判断 TODO
            if (getVersuch() == max_versuch ) {
                if (eingabe != target) {
                    System.out.println("Sorry, you've used all attempts. The correct number was " + target + ".");
                    leben--;
                    if (leben <= 0 || getPoint() < 0) {
                        System.out.println("Game over! You are out of lives.");
                        break;
                    } else {
                        System.out.println("You have " + getLeben() + " lives and " + getPoint() + " points.");
                    }
                    flag = false;
                }
            } //else if (getVersuch() != max_versuch) {
                if (eingabe < target) {
                    System.out.println("The number is higher.");
                    versuch++;
                } else if (eingabe > target) {
                    System.out.println("The number is lower.");
                    versuch++;
                } else { //eingabe == gesuchte Zahl
                    System.out.println("Congrats! You guessed the correct number.");
                    flag = false;
                    int currentPoint = getPoint();
                    // 更新积分
                    point += switch (getSchwierigkeit()) {
                        case 1, 2 -> 200;
                        case 3 -> 500;
                        default -> 0;
                    };
                    setPoint(point);

                    // 更新生命值
                    leben += switch (getSchwierigkeit()) {
                        case 2 -> 1;
                        case 3 -> 3;
                        default -> 0;
                    };
                    setLeben(leben);
                    System.out.println("You have " + getLeben() + " lives and " + getPoint() + " points.");
                }
           // }
            if (getVersuch() == max_versuch) {
                if (getPoint() >= 600) {
                    lastTry();
                }
            }
        }
    }

    private void lastTry() {
        System.out.println("LAST ATTEMPT! Do you want to buy a hint for 600 points? (1) yes (2) no");
        Scanner sc = new Scanner(System.in);
        int buyanswer = sc.nextInt();
        while (buyanswer != 1 && buyanswer != 2) {
            System.out.println("This was not a valid choice, please try again.");
            buyanswer = sc.nextInt();
        }
        if (buyanswer == 1) {
            point -= 600;
            setPoint(point);
            if (result() % 2 == 0) {
                System.out.println("The number is even!");
            } else {
                System.out.println("The number is odd!");
            }
        } else {
            System.out.println("No hint purchased.");
        }
    }

    private int result() {
        if (getCache() == -1) {
            if (getSchwierigkeit() == 1) {
                setCache(RandomNumberGenerator.getGenerator().generate(100));
                //System.out.print("level: 1 " + cache);
                return getCache();
            } else if (getSchwierigkeit() == 2) {
                setCache(RandomNumberGenerator.getGenerator().generate(500));
                //System.out.print("level: 2" + cache);
                return getCache();

            } else if (getSchwierigkeit() == 3) {
                setCache(RandomNumberGenerator.getGenerator().generate(1000));
                //System.out.print("level: 3" + cache);
                return getCache();
            }
        }

        return cache;
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
        new GuessTheNumber().guessTheNumber();

    }

}
