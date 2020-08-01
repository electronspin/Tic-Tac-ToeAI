package tictactoe;

import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        TicTacToe tt = new TicTacToe();

        while (true) {
            String command = tt.getValidCommand();
            if (command == "exit") {
                break;
            }
            String[] strs = command.split(" ");

            Player player1 = tt.getPlayer(strs[1]);
            Player player2 = tt.getPlayer(strs[2]);

            tt.play(player1, player2);
        }
    }
}

class TicTacToe {
    Scanner scanner = new Scanner(System.in);
    Random random = new Random();

    int xcount = 0;
    int ocount = 0;
    String player1 = "";
    String player2 = "";

    char[][] array = new char[3][3];

    Player getPlayer(String str) {
        switch (str) {
            case "user":
                return new User();
            case "easy":
                return new Easy();
            case "medium":
                return new Medium();
            case "hard":
                return new hard();
        }
        return null;
    }

    void clear() {
        xcount = 0;
        ocount = 0;
        player1 = "";
        player2 = "";

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                array[i][j] = ' ';
            }
        }
    }

    void play(Player player1, Player player2) {

        clear();

        display(array);

        char turn = 'X';

        while (true) {
            Pos pos;
            if (turn == 'X') {
                pos = player1.getPos(turn, array);
            } else {
                pos = player2.getPos(turn, array);
            }
            if (pos == null) {
                continue;
            }

            array[pos.x][pos.y] = turn;
            xcount += turn == 'X' ? 1 : 0;
            ocount += turn == 'O' ? 1 : 0;

            display(array);

            turn = turn == 'X' ? 'O' : 'X';

            boolean xwin = isWin('X', array);
            boolean owin = isWin('O', array);

            String result;
            if (Math.abs(xcount - ocount) > 1 || xwin && owin) {
                result = "Impossible";
            } else if (xwin) {
                result = "X wins";
            } else if (owin) {
                result = "O wins";
            } else if (xcount + ocount == 9) {
                result = "Draw";
            } else {
                continue;
            }

            System.out.println(result);
            System.out.println();
            break;
        }
    }

    String getCommand(String line) {
        String[] strs = line.split(" ");
        if (strs.length >= 1) {
            if ("exit".equals(strs[0])) {
                return "exit";
            }
        }
        if (strs.length >= 3) {
            if (!"start".equals(strs[0])) {
                return "error";
            }

            String player1 = strs[1];
            String player2 = strs[2];

            if (!"user".equals(player1) && !"easy".equals(player1) && !"medium".equals(player1) && !"hard".equals(player1)) {
                return "error";
            }

            if (!"user".equals(player2) && !"easy".equals(player2) && !"medium".equals(player2) && !"hard".equals(player2)) {
                return "error";
            }

            return "start " + player1 + " " + player2;
        }
        return "error";
    }

    String getValidCommand() {
        while (true) {
            System.out.print("Input command: ");
            String line = scanner.nextLine();
            String command = getCommand(line);
            if ("error".equals(command)) {
                System.out.println("Bad parameters!");
                continue;
            }
            return command;
        }
    }

    private void display(char[][] array) {
        String line1 = "| " + array[0][0] + " " + array[0][1] + " " + array[0][2] + " |";
        String line2 = "| " + array[1][0] + " " + array[1][1] + " " + array[1][2] + " |";
        String line3 = "| " + array[2][0] + " " + array[2][1] + " " + array[2][2] + " |";

        System.out.println("---------");
        System.out.println(line1);
        System.out.println(line2);
        System.out.println(line3);
        System.out.println("---------");
    }

    static boolean isWin(char turn, char[][] array) {
        int i;
        int j;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3 && array[i][j] == turn; j++) {
            }
            if (j == 3) {
                return true;
            }
        }
        for (j = 0; j < 3; j++) {
            for (i = 0; i < 3 && array[i][j] == turn; i++) {
            }
            if (i == 3) {
                return true;
            }
        }
        for (i = 0; i < 3 && array[i][i] == turn; i++) {
        }
        if (i == 3) {
            return true;
        }
        for (i = 0; i < 3 && array[i][2 - i] == turn; i++) {
        }
        if (i == 3) {
            return true;
        }
        return false;
    }
}

class Pos {
    int x;
    int y;

    Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Pos inputToArray(Pos pos) {
        int xpos = 3 - pos.y;
        int ypos = pos.x - 1;
        return new Pos(xpos, ypos);
    }
}

class Player {
    Random random = new Random();

    Pos getPos(char turn, char[][] array) {
        Pos pos = new Pos(0, 0);
        while (true) {
            int number = random.nextInt(9);
            pos.x = number / 3;
            pos.y = number % 3;
            if (array[pos.x][pos.y] == ' ') {
                break;
            }
        }

        return pos;
    }
}

class User extends Player {
    Scanner scanner = new Scanner(System.in);

    @Override
    Pos getPos(char turn, char[][] array) {
        System.out.print("Enter the coordinates: ");
        String str = scanner.nextLine();
        String[] strs = str.split(" ");
        if (strs.length < 2) {
            return null;
        }

        String xstr = strs[0];
        String ystr = strs[1];

        if (!isInteger(xstr) || !isInteger(ystr)) {
            System.out.println("You should enter numbers!");
            return null;
        }

        int xpos = Integer.parseInt(xstr);
        int ypos = Integer.parseInt(ystr);

        if (!isRange(xpos) || !isRange(ypos)) {
            System.out.println("Coordinates should be from 1 to 3!");
            return null;
        }

        Pos pos = new Pos(xpos, ypos);
        pos = pos.inputToArray(pos);

        if (array[pos.x][pos.y] != ' ') {
            System.out.println("This cell is occupied! Choose another one!");
            return null;
        }

        return pos;
    }

    private boolean isRange(int pos) {
        if (pos >= 1 && pos <= 3) {
            return true;
        }
        return false;
    }

    private boolean isInteger(String input) {
        if (input.length() != 1) {
            return false;
        }
        char ch = input.charAt(0);
        if (ch >= '1' && ch <= '9') {
            return true;
        }
        return false;
    }
}

class Easy extends Player {

    @Override
    Pos getPos(char turn, char[][] array) {
        System.out.println("Making move level \"easy\"");
        return super.getPos(turn, array);
    }
}

class Medium extends Player {
    @Override
    Pos getPos(char turn, char[][] array) {
        System.out.println("Making move level \"medium\"");
        Pos pos = getWinPos('X', array);
        if (pos != null) {
            return pos;
        }
        pos = getWinPos('O', array);
        if (pos != null) {
            return pos;
        }
        return super.getPos(turn, array);
    }

    private Pos getWinPos(char turn, char[][] array) {
        int i;
        int j;
        int n;
        int blanki;
        int blankj;

        for (i = 0; i < 3; i++) {
            n = 0;
            blankj = -1;
            for (j = 0; j < 3; j++) {
                n += array[i][j] == turn ? 1 : 0;
                if (array[i][j] == ' ') {
                    blankj = j;
                }
            }
            if (n == 2 && blankj != -1) {
                return new Pos(i, blankj);
            }
        }

        for (j = 0; j < 3; j++) {
            n = 0;
            blanki = -1;
            for (i = 0; i < 3; i++) {
                n += array[i][j] == turn ? 1 : 0;
                if (array[i][j] == ' ') {
                    blanki = i;
                }
            }
            if (n == 2 && blanki != -1) {
                return new Pos(blanki, j);
            }
        }

        n = 0;
        blanki = -1;
        for (i = 0; i < 3; i++) {
            n += array[i][i] == turn ? 1 : 0;
            if (array[i][i] == ' ') {
                blanki = i;
            }
        }
        if (n == 2 && blanki != -1) {
            return new Pos(blanki, blanki);
        }

        n = 0;
        blanki = -1;
        for (i = 0; i < 3; i++) {
            n += array[i][2 - i] == turn ? 1 : 0;
            if (array[i][2 - i] == ' ') {
                blanki = i;
            }
        }
        if (n == 2 && blanki != -1) {
            return new Pos(blanki, 2 - blanki);
        }

        return null;
    }
}

class hard extends Player {

    @Override
    Pos getPos(char turn, char[][] array) {
        System.out.println("Making move level \"hard\"");
        Pos[] poslist = getMoveList(array);

        int maxpoint = -20;
        Pos maxpos = null;
        for (Pos pos: poslist) {
            int point = move(0, turn, pos, array);
            if (point > maxpoint) {
                maxpoint = point;
                maxpos = pos;
            }
        }
        return maxpos;
    }

    private int move(int level, char turn, Pos pos, char[][] array) {
        char[][] newarray = copy(array);

        if (level % 2 == 0) {
            newarray[pos.x][pos.y] = turn;

            if (TicTacToe.isWin(turn, newarray)) {
                return 10;
            }

            Pos[] poslist = getMoveList(newarray);
            if (poslist == null) {
                return 0;
            }
            int minpoint = 20;
            for (Pos newpos : poslist) {
                int point = move(level + 1, turn, newpos, newarray);
                if (point < minpoint) {
                    minpoint = point;
                }
            }
            return minpoint;
        } else {
            turn = turn == 'X' ? 'O' : 'X';
            newarray[pos.x][pos.y] = turn;

            if (TicTacToe.isWin(turn, newarray)) {
                return -10;
            }

            Pos[] poslist = getMoveList(newarray);
            if (poslist == null) {
                return 0;
            }
            int maxpoint = -20;
            for (Pos newpos : poslist) {
                int point = move(level + 1, turn, newpos, newarray);
                if (point > maxpoint) {
                    maxpoint = point;
                }
            }
            return maxpoint;
        }
    }

    private char[][] copy(char[][] array) {
        char[][] newarray = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newarray[i][j] = array[i][j];
            }
        }
        return newarray;
    }

    private Pos[] getMoveList(char[][] array) {
        int n = countMovable(array);
        if (n == 0) {
            return null;
        }
        Pos[] poslist = new Pos[n];
        int k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (array[i][j] == ' ') {
                    poslist[k++] = new Pos(i, j);
                }
            }
        }
        return poslist;
    }

    private int countMovable(char[][] array) {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (array[i][j] == ' ') {
                    count++;
                }
            }
        }
        return count;
    }
}
