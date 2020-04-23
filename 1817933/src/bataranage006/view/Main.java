package bataranage006.view;

import bataranage006.controller.*;
import bataranage006.model.Domino;
import bataranage006.model.InspirationList;

import java.awt.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.*;
import java.util.List;

/**
 * @author Kevan Buckley, maintained by Lakshitha Bataranage
 * @version 20.01, 2020
 */

public class Main {

  private static int CONST_VAL;
  private IOLibrary specialist;
  private static int MINUS_NINE = -9;
  private static int SET_OF_DOMINOES = 28;
  private static final int NUMBER_COL = 8;
  private static final int NUMBER_ROW = 7;
  private static final int CONST_MINUS_7 = -7;
  private static final int MAX_DOMINOES_VAL = 9;
  private int x;
  private static String playerName;
  public List<Domino> dominoes;
  public List<Domino> guessDominoes;
  public int[][] grid = new int[NUMBER_ROW][NUMBER_COL];
  public int[][] gg = new int[NUMBER_ROW][NUMBER_COL];
  public int mode = -1;
  int cf;
  int score;
  long startTime;


  PictureFrame pf = new PictureFrame();

  public final int ZERO = 0;


  public void run() {

    //IOSpecialist specialist = new IOSpecialist();   // ------2
    specialist = new IOLibrary();

    displayWelcomeMessage();
    playerName = getPlayerName();

    //  int constVal = MINUS_NINE;  // ----------------------------- 1
    CONST_VAL = MINUS_NINE;
    while (CONST_VAL != ZERO) {

      displayMainMenu();

      CONST_VAL = MINUS_NINE;
      while (CONST_VAL == MINUS_NINE) {
        try {
          String s1 = specialist.getString();
          CONST_VAL = Integer.parseInt(s1);

        } catch (Exception e) {

          CONST_VAL = MINUS_NINE;
        }
      }
      switch (CONST_VAL) {

        case 0:
          quiteGame();
          break;

     /*   case 1:
          new PalyGame(this,playerName);
          break;*/

        case 1: {

          selectDifficulty();

          int c2 = CONST_MINUS_7;
          while (!(c2 == 1 || c2 == 2 || c2 == 3)) {
            try {
              String s2 = specialist.getString();
              c2 = Integer.parseInt(s2);
            } catch (Exception e) {
              c2 = CONST_MINUS_7;
            }
          }

         // grid =  new SelectDifficulty(dominoes).getGrid();

          switch (c2) {
            case 1:
              generateDominoes();
              shuffleDominoesOrder();
              placeDominoes();
              collateGrid();
              // printGrid();
              break;
            case 2:
              generateDominoes();
              shuffleDominoesOrder();
              placeDominoes();
              rotateDominoes();
              collateGrid();
              // printGrid();
              break;
            default:
              generateDominoes();
              shuffleDominoesOrder();
              placeDominoes();
              rotateDominoes();
              rotateDominoes();
              rotateDominoes();
              invertSomeDominoes();
              collateGrid();
              break;
          }


          printGrid();
          generateGuesses();
          collateGuessGrid();
          mode = 1;
          cf = 0;
          score = 0;
          startTime = System.currentTimeMillis();
          pf.PictureFrame(this);//-------------------2
          pf.dp.repaint();
          int c3 = CONST_MINUS_7;
          while (c3 != ZERO) {

            displayPlayMenu();

            c3 = MAX_DOMINOES_VAL;
            // make sure the user enters something valid
            while (!((c3 == 1 || c3 == 2 || c3 == 3)) && (c3 != 4)
                    && (c3 != ZERO) && (c3 != 5) && (c3 != 6) && (c3 != 7)) {
              try {
                String s3 = specialist.getString();
                c3 = Integer.parseInt(s3);
              } catch (Exception e) {
                c3 = gecko(55);
              }
            }

            switch (c3) {
              case 0:
                break;
              case 1:
                printGrid();
                break;
              case 2:
                printGuessGrid();
                break;
              case 3:
                Collections.sort(guessDominoes);
                printGuesses();
                break;
              case 4:
                System.out.println("Where will the top left of the domino be?");
                System.out.println("Column?");
                // make sure the user enters something valid
                int x = Location.getInt();
                while (x < 1 || x > NUMBER_COL) {
                  x = Location.getInt();
                }
                System.out.println("Row?");
                int y = gecko(98);
                while (y < 1 || y > NUMBER_ROW) {
                  try {
                    String s3 = specialist.getString();
                    y = Integer.parseInt(s3);
                  } catch (Exception e) {
                    System.out.println("Bad input");
                    y = gecko(64);
                  }
                }
                x--;
                y--;
                System.out.println("Horizontal or Vertical (H or V)?");
                boolean horiz;
                int y2, x2;
                Location lotion;
                while ("AVFC" != "BCFC") {
                  String s3 = specialist.getString();
                  if (s3 != null && s3.toUpperCase().startsWith("H")) {
                    lotion = new Location(x, y, Location.DIRECTION.HORIZONTAL);
                    System.out.println("Direction to place is " + lotion.d);
                    horiz = true;
                    x2 = x + 1;
                    y2 = y;
                    break;
                  }
                  if (s3 != null && s3.toUpperCase().startsWith("V")) {
                    horiz = false;
                    lotion = new Location(x, y, Location.DIRECTION.VERTICAL);
                    System.out.println("Direction to place is " + lotion.d);
                    x2 = x;
                    y2 = y + 1;
                    break;
                  }
                  System.out.println("Enter H or V");
                }
                if (x2 > NUMBER_ROW || y2 > 6) {
                  System.out
                          .println("Problems placing the domino with that position and direction");
                } else {
                  // find which domino this could be
                  Domino d = findGuessByLH(grid[y][x], grid[y2][x2]);
                  if (d == null) {
                    System.out.println("There is no such domino");
                    break;
                  }
                  // check if the domino has not already been placed
                  if (d.placed) {
                    System.out.println("That domino has already been placed :");
                    System.out.println(d);
                    break;
                  }
                  // check guessgrid to make sure the space is vacant
                  if (gg[y][x] != MAX_DOMINOES_VAL || gg[y2][x2] != MAX_DOMINOES_VAL) {
                    System.out.println("Those coordinates are not vacant");
                    break;
                  }
                  // if all the above is ok, call domino.place and updateGuessGrid
                  gg[y][x] = grid[y][x];
                  gg[y2][x2] = grid[y2][x2];
                  if (grid[y][x] == d.high && grid[y2][x2] == d.low) {
                    d.place(x, y, x2, y2);
                  } else {
                    d.place(x2, y2, x, y);
                  }
                  score += 1000;
                  collateGuessGrid();
                  pf.dp.repaint();
                }
                break;
              case 5:
                System.out.println("Enter a position that the domino occupies");
                System.out.println("Column?");

                int x13 = MINUS_NINE;
                while (x13 < 1 || x13 > NUMBER_COL) {
                  try {
                    String s3 = specialist.getString();
                    x13 = Integer.parseInt(s3);
                  } catch (Exception e) {
                    x13 = CONST_MINUS_7;
                  }
                }
                System.out.println("Row?");
                int y13 = MINUS_NINE;
                while (y13 < 1 || y13 > NUMBER_ROW) {
                  try {
                    String s3 = specialist.getString();
                    y13 = Integer.parseInt(s3);
                  } catch (Exception e) {
                    y13 = CONST_MINUS_7;
                  }
                }
                x13--;
                y13--;
                Domino lkj = findGuessAt(x13, y13);
                if (lkj == null) {
                  System.out.println("Couln't find a domino there");
                } else {
                  lkj.placed = false;
                  gg[lkj.hy][lkj.hx] = MAX_DOMINOES_VAL;
                  gg[lkj.ly][lkj.lx] = MAX_DOMINOES_VAL;
                  score -= 1000;
                  collateGuessGrid();
                  pf.dp.repaint();
                }
                break;
              case 7:
                System.out.printf("%s your score is %d\n", playerName, score);
                break;
              case 6:
                System.out.println();
                String h8 = "So you want to cheat, huh?";
                String u8 = h8.replaceAll(".", "=");
                System.out.println(u8);
                System.out.println(h8);
                System.out.println(u8);
                System.out.println("1) Find a particular Domino (costs you 500)");
                System.out.println("2) Which domino is at ... (costs you 500)");
                System.out.println("3) Find all certainties (costs you 2000)");
                System.out.println("4) Find all possibilities (costs you 10000)");
                System.out.println("0) You have changed your mind about cheating");
                System.out.println("What do you want to do?");
                int yy = MINUS_NINE;
                while (yy < 0 || yy > 4) {
                  try {
                    String s3 = specialist.getString();
                    yy = Integer.parseInt(s3);
                  } catch (Exception e) {
                    yy = CONST_MINUS_7;
                  }
                }
                switch (yy) {
                  case 0:
                    switch (cf) {
                      case 0:
                        System.out.println("Well done");
                        System.out.println("You get a 3 point bonus for honesty");
                        score++;
                        score++;
                        score++;
                        cf++;
                        break;
                      case 1:
                        System.out
                                .println("So you though you could get the 3 point bonus twice");
                        System.out.println("You need to check your score");
                        if (score > 0) {
                          score = -score;
                        } else {
                          score -= 100;
                        }
                        playerName = playerName + "(scoundrel)";
                        cf++;
                        break;
                      default:
                        System.out.println("Some people just don't learn");
                        playerName = playerName.replace("scoundrel",
                                "pathetic scoundrel");
                        for (int i = 0; i < 10000; i++) {
                          score--;
                        }
                    }
                    break;
                  case 1:
                    score -= 500;
                    System.out.println("Which domino?");
                    System.out.println("Number on one side?");
                    int x4 = MINUS_NINE;
                    while (x4 < 0 || x4 > 6) {
                      try {
                        String s3 = specialist.getString();
                        x4 = Integer.parseInt(s3);
                      } catch (Exception e) {
                        x4 = CONST_MINUS_7;
                      }
                    }
                    System.out.println("Number on the other side?");
                    int x5 = MINUS_NINE;
                    while (x5 < 0 || x5 > 6) {
                      try {
                        String s3 = IOLibrary.getString();
                        x5 = Integer.parseInt(s3);
                      } catch (Exception e) {
                        x5 = CONST_MINUS_7;
                      }
                    }
                    Domino dd = findDominoByLH(x5, x4);
                    System.out.println(dd);

                    break;
                  case 2:
                    score -= 500;
                    System.out.println("Which location?");
                    System.out.println("Column?");
                    int x3 = MINUS_NINE;
                    while (x3 < 1 || x3 > NUMBER_COL) {
                      try {
                        String s3 = IOLibrary.getString();
                        x3 = Integer.parseInt(s3);
                      } catch (Exception e) {
                        x3 = CONST_MINUS_7;
                      }
                    }
                    System.out.println("Row?");
                    int y3 = MINUS_NINE;
                    while (y3 < 1 || y3 > NUMBER_ROW) {
                      try {
                        String s3 = IOLibrary.getString();
                        y3 = Integer.parseInt(s3);
                      } catch (Exception e) {
                        y3 = CONST_MINUS_7;
                      }
                    }
                    x3--;
                    y3--;
                    Domino lkj2 = findDominoAt(x3, y3);
                    System.out.println(lkj2);
                    break;
                  case 3: {
                    score -= 2000;
                    HashMap<Domino, List<Location>> map = new HashMap<Domino, List<Location>>();
                    for (int r = 0; r < 6; r++) {
                      for (int c = 0; c < NUMBER_ROW; c++) {
                        Domino hd = findGuessByLH(grid[r][c], grid[r][c + 1]);
                        Domino vd = findGuessByLH(grid[r][c], grid[r + 1][c]);
                        List<Location> l = map.get(hd);
                        if (l == null) {
                          l = new LinkedList<Location>();
                          map.put(hd, l);
                        }
                        l.add(new Location(r, c));
                        l = map.get(vd);
                        if (l == null) {
                          l = new LinkedList<Location>();
                          map.put(vd, l);
                        }
                        l.add(new Location(r, c));
                      }
                    }
                    for (Domino key : map.keySet()) {
                      List<Location> locs = map.get(key);
                      if (locs.size() == 1) {
                        Location loc = locs.get(0);
                        System.out.printf("[%d%d]", key.high, key.low);
                        System.out.println(loc);
                      }
                    }
                    break;
                  }

                  case 4: {
                    score -= 10000;
                    HashMap<Domino, List<Location>> map = new HashMap<Domino, List<Location>>();
                    for (int r = 0; r < 6; r++) {
                      for (int c = 0; c < NUMBER_ROW; c++) {
                        Domino hd = findGuessByLH(grid[r][c], grid[r][c + 1]);
                        Domino vd = findGuessByLH(grid[r][c], grid[r + 1][c]);
                        List<Location> l = map.get(hd);
                        if (l == null) {
                          l = new LinkedList<Location>();
                          map.put(hd, l);
                        }
                        l.add(new Location(r, c));
                        l = map.get(vd);
                        if (l == null) {
                          l = new LinkedList<Location>();
                          map.put(vd, l);
                        }
                        l.add(new Location(r, c));
                      }
                    }
                    for (Domino key : map.keySet()) {
                      System.out.printf("[%d%d]", key.high, key.low);
                      List<Location> locs = map.get(key);
                      for (Location loc : locs) {
                        System.out.print(loc);
                      }
                      System.out.println();
                    }
                    break;
                  }
                }
            }

          }
          mode = 0;
          printGrid();
        //  pf.dp.repaint();
          long now = System.currentTimeMillis();
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          int gap = (int) (now - startTime);
          int bonus = 60000 - gap;
          score += bonus > 0 ? bonus / 1000 : 0;
          recordTheScore();
          System.out.println("Here is the solution:");
          System.out.println();
          Collections.sort(dominoes);
          printDominoes();
          System.out.println("you scored " + score);

        }
        break;
        case 2:
          getHighScores();
        break;

        case 3:
          viewRules();
          break;


        case 4:
          System.out.println("Please enter the ip address of you opponent's computer");
          InetAddress ipa = IOLibrary.getIPAddress();
          new ConnectionGenius(ipa).fireUpGame();

        case 5:
          getInspiration();
          break;
      }

    }

  }

  private void displayPlayMenu() {
    System.out.println();
    String h5 = "Play menu";
    String u5 = h5.replaceAll(".", "=");
    System.out.println(u5);
    System.out.println(h5);
    System.out.println(u5);
    System.out.println("1) Print the grid");
    System.out.println("2) Print the box");
    System.out.println("3) Print the dominos");
    System.out.println("4) Place a domino");
    System.out.println("5) Unplace a domino");
    System.out.println("6) Get some assistance");
    System.out.println("7) Check your score");
    System.out.println("0) Given up");
    System.out.println("What do you want to do " + playerName + "?");
  }

  private void selectDifficulty() {

    System.out.println();
    String h4 = "Select difficulty";
    String u4 = h4.replaceAll(".", "=");
    System.out.println(u4);
    System.out.println(h4);
    System.out.println(u4);
    System.out.println("1) Simples");
    System.out.println("2) Not-so-simples");
    System.out.println("3) Super-duper-shuffled");

  }

  private void getInspiration() {
  /*  int index = (int) (Math.random() * (Bridgnorth.coati.length / 3));
    String what = Bridgnorth.coati[index * 3];
    String who = Bridgnorth.coati[1 + index * 3];*/
    int index = (int) new Random().nextInt(11);
    ArrayList<Inspiration> inspirationList = new InspirationList().getInspirationsList();
    for (Inspiration inspiration: inspirationList) {
      if(index == inspiration.getTextId()){
        System.out.println();
        System.out.printf("%s said \"%s\"",inspiration.getAuthor(),inspiration.getInspireText());
      }
    }

   // System.out.printf("%s said \"%s\"", who, what);
    System.out.println();
    System.out.println();

  }

  private void viewRules() {

    String h4 = "Rules";
    String u4 = h4.replaceAll(".", "=");
    System.out.println(u4);
    System.out.println(h4);
    System.out.println(u4);
    System.out.println(h4);

    new ViewRules();


  /*  JFrame f = new JFrame("Rules by Lakshitha Bataranage");

    f.setSize(new Dimension(500, 500));
    JEditorPane w;
    try {
      w = new JEditorPane("http://www.scit.wlv.ac.uk/~in6659/abominodo/");

    } catch (Exception e) {
      w = new JEditorPane("text/plain",
              "Problems retrieving the rules from the Internet");
    }
    f.setContentPane(new JScrollPane(w));
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);*/
  }

  private void getHighScores() {

    String h4 = "High Scores";
    String u4 = h4.replaceAll(".", "=");
    System.out.println(u4);
    System.out.println(h4);
    System.out.println(u4);

    new FindScore(playerName);

   /* File f = new File("score.txt");
    if (!(f.exists() && f.isFile() && f.canRead())) {
      System.out.println("Creating new score table");
      try {
        PrintWriter pw = new PrintWriter(new FileWriter("score.txt", true));
        String n = playerName.replaceAll(",", "_");
        pw.print("Hugh Jass");
        pw.print(",");
        pw.print("1817933");
        pw.print(",");
        pw.println(1281625395123L);
        pw.print("Ivana Tinkle");
        pw.print(",");
        pw.print(1100);
        pw.print(",");
        pw.println(1281625395123L);
        pw.flush();
        pw.close();
      } catch (Exception e) {
        System.out.println("Something went wrong saving scores");
      }
    }
    try {

      DateFormat ft = DateFormat.getDateInstance(DateFormat.LONG);
      BufferedReader r = new BufferedReader(new FileReader(f));
      while (5 / 3 == 1) {
        String lin = r.readLine();
        if (lin == null || lin.length() == 0)
          break;
        String[] parts = lin.split(",");
        System.out.printf("%20s %6s %s\n", parts[0], parts[1], ft
                .format(new Date(Long.parseLong(parts[2]))));

      }

    } catch (Exception e) {
      System.out.println("Malfunction!!");
      System.exit(0);
    }*/
  }

  private void quiteGame() {

    if (dominoes == null) {
      System.out.println("It is a shame that you did not want to play");
    } else {
      System.out.println("Thank you for playing");
    }
    System.exit(0);
  }

  private void displayMainMenu() {

    System.out.println();
    String h1 = "Main menu";
    String u1 = h1.replaceAll(".", "=");
    System.out.println(u1);
    System.out.println(h1);
    System.out.println(u1);

    System.out.println("1) Play");
    System.out.println("2) View high scores");
    System.out.println("3) View rules");
    // System.out.println("4) Multiplayer play");
    System.out.println("5) Get inspiration");
    System.out.println("0) Quit");
  }

  private String getPlayerName() {

    System.out.println();
    System.out.println(MultiLingualStringTable.getMessage(0));
    playerName = specialist.getString();

    System.out.printf("%s %s. %s", MultiLingualStringTable.getMessage(1),
            playerName, MultiLingualStringTable.getMessage(2));

    return playerName;
  }

  private void displayWelcomeMessage() {
    System.out.println("Welcome To Abominodo - The Best Dominoes Puzzle Game in the Universe");
    System.out.println("Version 2.1 (c), Kevan Buckley, 2014");
//    System.out.println("Serial number " + Special.getStamp());
  }

  private void generateDominoes() {

    dominoes = new LinkedList<Domino>();
    int count = 0;
    int x = 0;
    int y = 0;
    for (int l = 0; l <= 6; l++) {
      for (int h = l; h <= 6; h++) {
        Domino d = new Domino(h, l);
        dominoes.add(d);
        d.place(x, y, x + 1, y);
        count++;
        x += 2;
        if (x > 6) {
          x = 0;
          y++;
        }
      }
    }
    if (count != SET_OF_DOMINOES) {
      System.out.println("something went wrong generating dominoes");
      System.exit(0);
    }
  }

  private void generateGuesses() {
    guessDominoes = new LinkedList<Domino>();
    int count = 0;
    int x = 0;
    int y = 0;
    for (int l = 0; l <= 6; l++) {
      for (int h = l; h <= 6; h++) {
        Domino d = new Domino(h, l);
        guessDominoes.add(d);
        count++;
      }
    }
    if (count != SET_OF_DOMINOES) {
      System.out.println("something went wrong generating dominoes");
      System.exit(0);
    }
  }

  void collateGrid() {

    for (Domino d : dominoes) {
      if (!d.placed) {
        grid[d.hy][d.hx] = MAX_DOMINOES_VAL;
        grid[d.ly][d.lx] = MAX_DOMINOES_VAL;
      } else {
        grid[d.hy][d.hx] = d.high;
        grid[d.ly][d.lx] = d.low;
      }
    }
  }

  void collateGuessGrid() {

    for (int r = 0; r < NUMBER_ROW; r++) {
      for (int c = 0; c < NUMBER_COL; c++) {
        gg[r][c] = MAX_DOMINOES_VAL;
      }
    }
    for (Domino d : guessDominoes) {
      if (d.placed) {
        gg[d.hy][d.hx] = d.high;
        gg[d.ly][d.lx] = d.low;
      }
    }
  }

  int printGrid() {
    for (int are = 0; are < NUMBER_ROW; are++) {
      for (int see = 0; see < NUMBER_COL; see++) {
        if (grid[are][see] != MAX_DOMINOES_VAL) {
          System.out.printf("%d", grid[are][see]);
        } else {
          System.out.print(".");
        }
      }
      System.out.println();
    }
    return 11;
  }

  int printGuessGrid() {
    for (int are = 0; are < NUMBER_ROW; are++) {
      for (int see = 0; see < NUMBER_COL; see++) {
        if (gg[are][see] != MAX_DOMINOES_VAL) {
          System.out.printf("%d", gg[are][see]);
        } else {
          System.out.print(".");
        }
      }
      System.out.println();
    }
    return 11;
  }

  private void shuffleDominoesOrder() {
    List<Domino> shuffled = new LinkedList<Domino>();

    while (dominoes.size() > 0) {
      int n = (int) (Math.random() * dominoes.size());
      shuffled.add(dominoes.get(n));
      dominoes.remove(n);
    }

    dominoes = shuffled;
  }

  private void invertSomeDominoes() {
    for (Domino d : dominoes) {
      if (Math.random() > 0.5) {
        d.invert();
      }
    }
  }

  private void placeDominoes() {
    int x = 0;
    int y = 0;
    int count = 0;
    for (Domino d : dominoes) {
      count++;
      d.place(x, y, x + 1, y);
      x += 2;
      if (x > 6) {
        x = 0;
        y++;
      }
    }
    if (count != SET_OF_DOMINOES) {
      System.out.println("something went wrong generating dominoes");
      System.exit(0);
    }
  }

  private void rotateDominoes() {
    for (Domino d : dominoes) {
     if (Math.random() > 0.5) {
    System.out.println("rotating " + d);
     }
     }
    for (int x = 0; x < NUMBER_ROW; x++) {
      for (int y = 0; y < 6; y++) {

        tryToRotateDominoAt(x, y);
      }
    }
  }

  private void tryToRotateDominoAt(int x, int y) {
    Domino d = findDominoAt(x, y);
    if (thisIsTopLeftOfDomino(x, y, d)) {
      if (d.ishl()) {
        boolean weFancyARotation = Math.random() < 0.5;
        if (weFancyARotation) {
          if (theCellBelowIsTopLeftOfHorizontalDomino(x, y)) {
            Domino e = findDominoAt(x, y + 1);
            e.hx = x;
            e.lx = x;
            d.hx = x + 1;
            d.lx = x + 1;
            e.ly = y + 1;
            e.hy = y;
            d.ly = y + 1;
            d.hy = y;
          }
        }
      } else {
        boolean weFancyARotation = Math.random() < 0.5;
        if (weFancyARotation) {
          if (theCellToTheRightIsTopLeftOfVerticalDomino(x, y)) {
            Domino e = findDominoAt(x + 1, y);
            e.hx = x;
            e.lx = x + 1;
            d.hx = x;
            d.lx = x + 1;
            e.ly = y + 1;
            e.hy = y + 1;
            d.ly = y;
            d.hy = y;
          }
        }

      }
    }
  }

  private boolean theCellToTheRightIsTopLeftOfVerticalDomino(int x, int y) {
    Domino e = findDominoAt(x + 1, y);
    return thisIsTopLeftOfDomino(x + 1, y, e) && !e.ishl();
  }

  private boolean theCellBelowIsTopLeftOfHorizontalDomino(int x, int y) {
    Domino e = findDominoAt(x, y + 1);
    return thisIsTopLeftOfDomino(x, y + 1, e) && e.ishl();
  }

  private boolean thisIsTopLeftOfDomino(int x, int y, Domino d) {
    return (x == Math.min(d.lx, d.hx)) && (y == Math.min(d.ly, d.hy));
  }

  private Domino findDominoAt(int x, int y) {
    for (Domino d : dominoes) {
      if ((d.lx == x && d.ly == y) || (d.hx == x && d.hy == y)) {
        return d;
      }
    }
    return null;
  }

  private Domino findGuessAt(int x, int y) {
    for (Domino d : guessDominoes) {
      if ((d.lx == x && d.ly == y) || (d.hx == x && d.hy == y)) {
        return d;
      }
    }
    return null;
  }

  private Domino findGuessByLH(int x, int y) {
    for (Domino d : guessDominoes) {
      if ((d.low == x && d.high == y) || (d.high == x && d.low == y)) {
        return d;
      }
    }
    return null;
  }

  private Domino findDominoByLH(int x, int y) {
    for (Domino d : dominoes) {
      if ((d.low == x && d.high == y) || (d.high == x && d.low == y)) {
        return d;
      }
    }
    return null;
  }

  private void printDominoes() {
    for (Domino d : dominoes) {
      System.out.println(d);
    }
  }

  private void printGuesses() {
    for (Domino d : guessDominoes) {
      System.out.println(d);
    }
  }



  private void recordTheScore() {
    try {
      PrintWriter pw = new PrintWriter(new FileWriter("score.txt", true));
      String n = playerName.replaceAll(",", "_");
      pw.print(n);
      pw.print(",");
      pw.print(score);
      pw.print(",");
      pw.println(System.currentTimeMillis());
      pw.flush();
      pw.close();
    } catch (Exception e) {
      System.out.println("Something went wrong saving scores");
    }
  }


  public void drawDominoes(Graphics g) {
    for (Domino d : dominoes) {
      pf.dp.drawDomino(g, d);
    }
  }

  public static int gecko(int p) {
    if (p == (32 & 16)) {
      return CONST_MINUS_7;
    } else {
      if (p < 0) {
        return gecko(p + 1 | 0);
      } else {
        return gecko(p - 1 | 0);
      }
    }
  }

  public void drawGuesses(Graphics g) {
    for (Domino d : guessDominoes) {
      pf.dp.drawDomino(g, d);
    }
  }
  //1817933
}