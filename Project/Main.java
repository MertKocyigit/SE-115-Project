//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    static final int NUM_MONTHS = 12;
    static final int NUM_DAYS = 28;
    static final int NUM_COMMODITIES = 5;

    static final String[] months = new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    static final String[] commodities = new String[]{
            "Gold", "Oil", "Silver", "Wheat", "Copper"
    };

    static int[][][] profits = new int[NUM_MONTHS][NUM_DAYS][NUM_COMMODITIES];
    static boolean dataLoaded = false;

    public Main() {
    }

    public static void loadData() {
        int dayIndex;

        for (int monthIndex = 0; monthIndex < NUM_MONTHS; ++monthIndex) {
            for (dayIndex = 0; dayIndex < NUM_DAYS; ++dayIndex) {
                for (int commodityIndex = 0; commodityIndex < NUM_COMMODITIES; ++commodityIndex) {
                    profits[monthIndex][dayIndex][commodityIndex] = 0;
                }
            }
        }

        File dataFolder = new File("Data_Files");
        if (dataFolder.exists() && dataFolder.isDirectory()) {

            for (int monthIndex = 0; monthIndex < NUM_MONTHS; ++monthIndex) {
                String monthFileName = months[monthIndex] + ".txt";
                File monthFile = new File(dataFolder, monthFileName);

                if (monthFile.exists()) {
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(monthFile));
                        String line;

                        try {
                            while ((line = reader.readLine()) != null) {
                                line = line.trim();

                                if (!line.isEmpty()) {
                                    String[] parts = line.split(",");

                                    if (parts.length >= 3) {
                                        int dayNumber;
                                        int profitValue;

                                        try {
                                            dayNumber = Integer.parseInt(parts[0].trim());
                                            profitValue = Integer.parseInt(parts[2].trim());
                                        } catch (NumberFormatException parseException) {
                                            continue;
                                        }

                                        String commodityName = parts[1].trim();
                                        int commodityIndex = getCommodityIndex(commodityName);

                                        if (isValidDay(dayNumber) && commodityIndex != -1) {
                                            profits[monthIndex][dayNumber - 1][commodityIndex] = profitValue;
                                        }
                                    }
                                }
                            }
                        } catch (Throwable readThrowable) {
                            try {
                                reader.close();
                            } catch (Throwable closeThrowable) {
                                readThrowable.addSuppressed(closeThrowable);
                            }
                            throw readThrowable;
                        }

                        reader.close();
                    } catch (IOException ioException) {

                    }
                }
            }

            dataLoaded = true;
        } else {
            dataLoaded = false;
        }
    }

    private static boolean isValidMonth(int monthIndex) {
        return monthIndex >= 0 && monthIndex < NUM_MONTHS;
    }

    private static boolean isValidDay(int dayNumber) {
        return dayNumber >= 1 && dayNumber <= NUM_DAYS;
    }

    private static int getCommodityIndex(String commodityName) {
        if (commodityName == null) {
            return -1;
        } else {
            for (int commodityIndex = 0; commodityIndex < commodities.length; ++commodityIndex) {
                if (commodities[commodityIndex].equals(commodityName)) {
                    return commodityIndex;
                }
            }
            return -1;
        }
    }

    public static String mostProfitableCommodityInMonth(int monthIndex) {
        if (!isValidMonth(monthIndex)) {
            return "INVALID_MONTH";
        } else {
            int bestCommodityIndex = 0;
            int bestTotalProfit = Integer.MIN_VALUE;

            for (int commodityIndex = 0; commodityIndex < NUM_COMMODITIES; ++commodityIndex) {
                int commodityTotalProfit = 0;

                for (int dayIndex = 0; dayIndex < NUM_DAYS; ++dayIndex) {
                    commodityTotalProfit += profits[monthIndex][dayIndex][commodityIndex];
                }

                if (commodityTotalProfit > bestTotalProfit) {
                    bestTotalProfit = commodityTotalProfit;
                    bestCommodityIndex = commodityIndex;
                }
            }

            return commodities[bestCommodityIndex] + " " + bestTotalProfit;
        }
    }

    public static int totalProfitOnDay(int monthIndex, int dayNumber) {
        if (isValidMonth(monthIndex) && isValidDay(dayNumber)) {
            int dayIndex = dayNumber - 1;
            int totalProfit = 0;

            for (int commodityIndex = 0; commodityIndex < NUM_COMMODITIES; ++commodityIndex) {
                totalProfit += profits[monthIndex][dayIndex][commodityIndex];
            }

            return totalProfit;
        } else {
            return -99999;
        }
    }

    public static int commodityProfitInRange(String commodityName, int startDay, int endDay) {
        int commodityIndex = getCommodityIndex(commodityName);

        if (commodityIndex != -1 && isValidDay(startDay) && isValidDay(endDay) && startDay <= endDay) {
            int startDayIndex = startDay - 1;
            int endDayIndex = endDay - 1;
            int totalProfit = 0;

            for (int monthIndex = 0; monthIndex < NUM_MONTHS; ++monthIndex) {
                for (int dayIndex = startDayIndex; dayIndex <= endDayIndex; ++dayIndex) {
                    totalProfit += profits[monthIndex][dayIndex][commodityIndex];
                }
            }

            return totalProfit;
        } else {
            return -99999;
        }
    }

    public static int bestDayOfMonth(int monthIndex) {
        if (!isValidMonth(monthIndex)) {
            return -1;
        } else {
            int bestDay = 1;
            int bestProfit = Integer.MIN_VALUE;

            for (int dayNumber = 1; dayNumber <= NUM_DAYS; ++dayNumber) {
                int dayProfit = totalProfitOnDay(monthIndex, dayNumber);

                if (dayProfit > bestProfit) {
                    bestProfit = dayProfit;
                    bestDay = dayNumber;
                }
            }

            return bestDay;
        }
    }

    public static String bestMonthForCommodity(String commodityName) {
        int commodityIndex = getCommodityIndex(commodityName);

        if (commodityIndex == -1) {
            return "INVALID_COMMODITY";
        } else {
            int bestMonthIndex = 0;
            int bestMonthProfit = Integer.MIN_VALUE;

            for (int monthIndex = 0; monthIndex < NUM_MONTHS; ++monthIndex) {
                int monthTotalProfit = 0;

                for (int dayIndex = 0; dayIndex < NUM_DAYS; ++dayIndex) {
                    monthTotalProfit += profits[monthIndex][dayIndex][commodityIndex];
                }

                if (monthTotalProfit > bestMonthProfit) {
                    bestMonthProfit = monthTotalProfit;
                    bestMonthIndex = monthIndex;
                }
            }

            return months[bestMonthIndex];
        }
    }

    public static int consecutiveLossDays(String commodityName) {
        int commodityIndex = getCommodityIndex(commodityName);

        if (commodityIndex == -1) {
            return -1;
        } else {
            int maxLossStreak = 0;
            int currentLossStreak = 0;

            for (int monthIndex = 0; monthIndex < NUM_MONTHS; ++monthIndex) {
                for (int dayIndex = 0; dayIndex < NUM_DAYS; ++dayIndex) {
                    if (profits[monthIndex][dayIndex][commodityIndex] < 0) {
                        ++currentLossStreak;

                        if (currentLossStreak > maxLossStreak) {
                            maxLossStreak = currentLossStreak;
                        }
                    } else {
                        currentLossStreak = 0;
                    }
                }
            }

            return maxLossStreak;
        }
    }

    public static int daysAboveThreshold(String commodityName, int threshold) {
        int commodityIndex = getCommodityIndex(commodityName);

        if (commodityIndex == -1) {
            return -1;
        } else {
            int daysCount = 0;

            for (int monthIndex = 0; monthIndex < NUM_MONTHS; ++monthIndex) {
                for (int dayIndex = 0; dayIndex < NUM_DAYS; ++dayIndex) {
                    if (profits[monthIndex][dayIndex][commodityIndex] > threshold) {
                        ++daysCount;
                    }
                }
            }

            return daysCount;
        }
    }

    public static int biggestDailySwing(int monthIndex) {
        if (!isValidMonth(monthIndex)) {
            return -99999;
        } else {
            int[] dailyTotals = new int[NUM_DAYS];

            int dayIndex;
            int commodityIndex;
            int dailySum;

            for (dayIndex = 0; dayIndex < NUM_DAYS; ++dayIndex) {
                dailySum = 0;

                for (commodityIndex = 0; commodityIndex < NUM_COMMODITIES; ++commodityIndex) {
                    dailySum += profits[monthIndex][dayIndex][commodityIndex];
                }

                dailyTotals[dayIndex] = dailySum;
            }

            int biggestSwing = 0;

            for (dayIndex = 0; dayIndex < NUM_DAYS - 1; ++dayIndex) {
                int diff = dailyTotals[dayIndex] - dailyTotals[dayIndex + 1];

                if (diff < 0) {
                    diff = -diff;
                }

                if (diff > biggestSwing) {
                    biggestSwing = diff;
                }
            }

            return biggestSwing;
        }
    }

    public static String compareTwoCommodities(String commodityA, String commodityB) {
        int commodityIndexA = getCommodityIndex(commodityA);
        int commodityIndexB = getCommodityIndex(commodityB);

        if (commodityIndexA != -1 && commodityIndexB != -1) {
            int totalProfitA = 0;
            int totalProfitB = 0;

            for (int monthIndex = 0; monthIndex < NUM_MONTHS; ++monthIndex) {
                for (int dayIndex = 0; dayIndex < NUM_DAYS; ++dayIndex) {
                    totalProfitA += profits[monthIndex][dayIndex][commodityIndexA];
                    totalProfitB += profits[monthIndex][dayIndex][commodityIndexB];
                }
            }

            if (totalProfitA > totalProfitB) {
                int diff = totalProfitA - totalProfitB;
                return commodityA + " is better by " + diff;
            } else if (totalProfitB > totalProfitA) {
                int diff = totalProfitB - totalProfitA;
                return commodityB + " is better by " + diff;
            } else {
                return "Equal";
            }
        } else {
            return "INVALID_COMMODITY";
        }
    }

    public static String bestWeekOfMonth(int monthIndex) {
        if (!isValidMonth(monthIndex)) {
            return "INVALID_MONTH";
        } else {
            int[][] weekRanges = new int[][]{{1, 7}, {8, 14}, {15, 21}, {22, 28}};
            int bestWeekIndex = 0;
            int bestWeekProfit = Integer.MIN_VALUE;

            for (int weekIndex = 0; weekIndex < weekRanges.length; ++weekIndex) {
                int weekStartDay = weekRanges[weekIndex][0];
                int weekEndDay = weekRanges[weekIndex][1];
                int weekProfit = 0;

                for (int dayNumber = weekStartDay; dayNumber <= weekEndDay; ++dayNumber) {
                    int dayProfit = totalProfitOnDay(monthIndex, dayNumber);
                    weekProfit += dayProfit;
                }

                if (weekProfit > bestWeekProfit) {
                    bestWeekProfit = weekProfit;
                    bestWeekIndex = weekIndex;
                }
            }

            return "Week " + (bestWeekIndex + 1);
        }
    }

    public static void main(String[] args) {
        loadData();

    
            System.out.println("Data loaded successfully.\n");
            System.out.println("Sample Tests:\n");
            System.out.println("1. Most profitable in January: " + mostProfitableCommodityInMonth(0));
            System.out.println("2. Total profit on Jan 15: " + totalProfitOnDay(0, 15));
            System.out.println("3. Gold profit (days 1-14, all months): " + commodityProfitInRange("Gold", 1, 14));
            System.out.println("4. Best day in February: " + bestDayOfMonth(1));
            System.out.println("5. Best month for Gold: " + bestMonthForCommodity("Gold"));
            System.out.println("6. Longest loss streak for Oil: " + consecutiveLossDays("Oil"));
            System.out.println("7. Days Gold > 5000: " + daysAboveThreshold("Gold", 5000));
            System.out.println("8. Biggest swing in March: " + biggestDailySwing(2));
            System.out.println("9. Gold vs Oil: " + compareTwoCommodities("Gold", "Oil"));
            System.out.println("10. Best week in April: " + bestWeekOfMonth(3));

            System.out.println("\nError Handling Tests:");
            System.out.println("- Invalid month (15): " + mostProfitableCommodityInMonth(15));
            System.out.println("- Invalid commodity: " + bestMonthForCommodity("Coal"));
            System.out.println("- Invalid range (10 to 5): " + commodityProfitInRange("Gold", 10, 5));
        }
    
}

