package org.example;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This program calculates the total profits of
 * a single song from a CDBaby report and prints
 * out a given percentage of that value.
 *
 * @author Pat Madden
 */
public class Main {
  public static void main(String[] args) {
    List<String> lines;
    String song = "Green Eyes";
    double total = 0;
    double percentage = 0.05;
    File f = new File("/Users/vmadden/development/personal/demo/src/main/resources/20221202-DDSales.txt");

    try {
      // index 10 = track name, 4 = subtotal
      lines = Files.readAllLines(f.toPath()).stream()
          .filter(s -> !s.isEmpty())
          .filter(s -> s.split("\t")[10].equals(song))
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    for (String line : lines) {
      total += Double.parseDouble(line.split("\t")[4]);
    }

    double calculatedShare = round(total * percentage);
    System.out.println("$" + calculatedShare);
  }

  public static double round(double value) {
    return new BigDecimal(value)
        .setScale(2, RoundingMode.HALF_UP)
        .doubleValue();
  }
}