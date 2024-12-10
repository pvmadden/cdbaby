package org.example;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;

/**
 * This program calculates the total profits from
 * a list of songs from a CDBaby report and prints
 * out a given percentage of that value.
 *
 * @author Pat Madden
 */
public class Main {
  public static void main(String[] args) {
    double total = 0;
    final double percentage = 0.2;

    final int trackNameIndex = 10;
    final int subtotalIndex = 4;
    final int reportDateIndex = 0;

    File report = new File("/Users/vmadden/development/personal/cdbaby/src/main/resources/report_2024/2024DigitalDistributionDetails.txt");

    Set<String> songs = Set.of("Desert Scene", "Pour Que J'm'élance", "Driplets", "Montgomery", "Green Eyes", "Lavender", "Gremlins on VHS",
                                "Dusty's Lament", "Dove On The Ocean", "Into The Dark", "Mirror", "Layers", "Citrus Club", "100 Days", "Days Are Getting Darker", "Candlelit", "It's Okay Relapse");

    //Date since last payment
    LocalDate sinceDate = LocalDate.parse("07/11/2024", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    List<String> reportLines;

    try {
      //This reads all lines from the report, if line is referencing a song from the list, add it to the list to process.
      reportLines = Files.readAllLines(report.toPath()).stream()
          .filter(s -> !s.isEmpty())
          .filter(s -> songs.contains(s.split("\t")[trackNameIndex]))
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    for (String line : reportLines) {
      //For each line, see if date is after the last payment date.
      String[] lineSplit = line.split("\t");
      LocalDate date = getDateFromZeroIndex(lineSplit[reportDateIndex]);
      if(date.isAfter(sinceDate)) {
        total += Double.parseDouble(lineSplit[subtotalIndex]);
      }
    }

    double calculatedShare = round(total * percentage);
    System.out.println("$" + calculatedShare);
  }

  public static double round(double value) {
    return new BigDecimal(value)
        .setScale(2, RoundingMode.HALF_UP)
        .doubleValue();
  }

  public static LocalDate getDateFromZeroIndex(String strDate)
  {
    String date = strDate.split(" ")[0];
    String[] breakdown = date.split("/");
    int year = Integer.parseInt(breakdown[2]);
    int month = Integer.parseInt(breakdown[0]);
    int dayOfMonth = Integer.parseInt(breakdown[1]);
    LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
    return localDate;
  }
}