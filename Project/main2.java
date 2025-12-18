import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class main2 {

    static final int NUM_MONTHS = 12;
    static final int NUM_DAYS = 28;
    static final int NUM_COMMODITIES = 5;

    static final String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    static final String[] commodities = {
            "Gold", "Oil", "Silver", "Wheat", "Copper"
    };

    static int[][][] profits = new int[NUM_MONTHS][NUM_DAYS][NUM_COMMODITIES];

    public static void loadData() {

        for (int month = 0; month < NUM_MONTHS; month++) {
            for (int day = 0; day < NUM_DAYS; day++) {
                for (int commodity = 0; commodity < NUM_COMMODITIES; commodity++) {
                    profits[month][day][commodity] = 0;
                }
            }
        }

        File baseFolder = new File("Data_Files");

        if (!baseFolder.exists() || !baseFolder.isDirectory()) {

            return;
        }

        for (int month = 0; month < NUM_MONTHS; month++) {
            File file = new File(baseFolder, months[month] + ".txt");
            if (!file.exists()) continue;

            BufferedReader reader = null;

            try {
                reader = new BufferedReader(new FileReader(file));
                String line;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.split(",");
                    if (parts.length < 3) continue;

                    int day;
                    int profit;

                    try {
                        day = Integer.parseInt(parts[0].trim());
                        profit = Integer.parseInt(parts[2].trim());
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    String commodityName = parts[1].trim();
                    int commodityIndex = getCommodityIndex(commodityName);

                    if (isValidDay(day) && commodityIndex != -1) {
                        profits[month][day - 1][commodityIndex] = profit;
                    }
                }

            } catch (IOException e) {
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                }
            }
        }


    }

    private static boolean isValidMonth(int month) {
        return month >= 0 && month < NUM_MONTHS;
    }

    private static boolean isValidDay(int day) {
        return day >= 1 && day <= NUM_DAYS;
    }

    private static int getCommodityIndex(String commodity) {
        if (commodity == null) return -1;
        for (int i = 0; i < NUM_COMMODITIES; i++) {
            if (commodities[i].equals(commodity)) return i;
        }
        return -1;
    }

    public static String mostProfitableCommodityInMonth(int month) {
        if (!isValidMonth(month)) return "INVALID_MONTH";

        int bestIndex = 0;
        int bestProfit = Integer.MIN_VALUE;

        for (int i = 0; i < NUM_COMMODITIES; i++) {
            int sum = 0;
            for (int j = 0; j < NUM_DAYS; j++) {
                sum += profits[month][j][i];
            }
            if (sum > bestProfit) {
                bestProfit = sum;
                bestIndex = i;
            }
        }

        return commodities[bestIndex] + " " + bestProfit;
    }

    public static int totalProfitOnDay(int month, int day) {
        if (!isValidMonth(month) || !isValidDay(day)) return -1;

        int total = 0;
        for (int i = 0; i < NUM_COMMODITIES; i++) {
            total += profits[month][day - 1][i];
        }
        return total;
    }

    public static int commodityProfitInRange(String commodity, int fromDay, int toDay) {
        int commodityIndex = getCommodityIndex(commodity);
        if (commodityIndex == -1 || !isValidDay(fromDay) || !isValidDay(toDay) || fromDay > toDay) {
            return -1;
        }

        int sum = 0;
        for (int month = 0; month < NUM_MONTHS; month++) {
            for (int day = fromDay - 1; day <= toDay - 1; day++) {
                sum += profits[month][day][commodityIndex];
            }
        }

        return sum;
    }

    public static int bestDayOfMonth(int month) {
        if (!isValidMonth(month)) return -1;

        int bestDay = 1;
        int bestProfit = Integer.MIN_VALUE;

        for (int day = 1; day <= NUM_DAYS; day++) {
            int total = totalProfitOnDay(month, day);
            if (total > bestProfit) {
                bestProfit = total;
                bestDay = day;
            }
        }

        return bestDay;
    }

    public static String bestMonthForCommodity(String commodity) {
        int commodityIndex = getCommodityIndex(commodity);
        if (commodityIndex == -1) return "INVALID_COMMODITY";

        int bestMonth = 0;
        int bestProfit = Integer.MIN_VALUE;

        for (int month = 0; month < NUM_MONTHS; month++) {
            int sum = 0;
            for (int day = 0; day < NUM_DAYS; day++) {
                sum += profits[month][day][commodityIndex];
            }
            if (sum > bestProfit) {
                bestProfit = sum;
                bestMonth = month;
            }
        }

        return months[bestMonth];
    }

    public static int consecutiveLossDays(String commodity) {
        int commodityIndex = getCommodityIndex(commodity);
        if (commodityIndex == -1) return -1;

        int maxStreak = 0;
        int currentStreak = 0;

        for (int month = 0; month < NUM_MONTHS; month++) {
            for (int day = 0; day < NUM_DAYS; day++) {
                if (profits[month][day][commodityIndex] < 0) {
                    currentStreak++;
                    if (currentStreak > maxStreak) maxStreak = currentStreak;
                } else {
                    currentStreak = 0;
                }
            }
        }

        return maxStreak;
    }

    public static int daysAboveThreshold(String commodity, int threshold) {
        int commodityIndex = getCommodityIndex(commodity);
        if (commodityIndex == -1) return -1;

        int count = 0;
        for (int month = 0; month < NUM_MONTHS; month++) {
            for (int day = 0; day < NUM_DAYS; day++) {
                if (profits[month][day][commodityIndex] > threshold) count++;
            }
        }

        return count;
    }

    public static int biggestDailySwing(int month) {
        if (!isValidMonth(month)) return -1;

        int maxSwing = 0;

        for (int day = 0; day < NUM_DAYS - 1; day++) {
            int today = 0;
            int nextDay = 0;

            for (int c = 0; c < NUM_COMMODITIES; c++) {
                today += profits[month][day][c];
                nextDay += profits[month][day + 1][c];
            }

            int diff = today - nextDay;
            if (diff < 0) diff = -diff;
            if (diff > maxSwing) maxSwing = diff;
        }

        return maxSwing;
    }

    public static String compareTwoCommodities(String c1, String c2) {
        int index1 = getCommodityIndex(c1);
        int index2 = getCommodityIndex(c2);

        if (index1 == -1 || index2 == -1) return "INVALID_COMMODITY";

        int sum1 = 0;
        int sum2 = 0;

        for (int month = 0; month < NUM_MONTHS; month++) {
            for (int day = 0; day < NUM_DAYS; day++) {
                sum1 += profits[month][day][index1];
                sum2 += profits[month][day][index2];
            }
        }

        if (sum1 > sum2) return c1 + " is better by " + (sum1 - sum2);
        else if (sum2 > sum1) return c2 + " is better by " + (sum2 - sum1);
        return "Equal";
    }

    public static String bestWeekOfMonth(int month) {
        if (!isValidMonth(month)) return "INVALID_MONTH";

        int bestWeek = 1;
        int bestSum = Integer.MIN_VALUE;

        for (int week = 0; week < 4; week++) {
            int weekSum = 0;
            int startDay = week * 7 + 1;
            int endDay = startDay + 6;

            for (int day = startDay; day <= endDay; day++) {
                weekSum += totalProfitOnDay(month, day);
            }

            if (weekSum > bestSum) {
                bestSum = weekSum;
                bestWeek = week + 1;
            }
        }

        return "Week " + bestWeek;
    }


}
