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
    static final String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    static final String[] commodities = new String[]{"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static int[][][] profits = new int[12][28][5];
    static boolean dataLoaded = false;

    public Main() {
    }

    public static void loadData() {
        int var1;
        for(int var0 = 0; var0 < 12; ++var0) {
            for(var1 = 0; var1 < 28; ++var1) {
                for(int var2 = 0; var2 < 5; ++var2) {
                    profits[var0][var1][var2] = 0;
                }
            }
        }

        File var15 = new File("Data_Files");
        if (var15.exists() && var15.isDirectory()) {
            for(var1 = 0; var1 < 12; ++var1) {
                String var10000 = months[var1];
                String var16 = var10000 + ".txt";
                File var3 = new File(var15, var16);
                if (var3.exists()) {
                    try {
                        BufferedReader var4 = new BufferedReader(new FileReader(var3));

                        String var5;
                        try {
                            while((var5 = var4.readLine()) != null) {
                                var5 = var5.trim();
                                if (!var5.isEmpty()) {
                                    String[] var6 = var5.split(",");
                                    if (var6.length >= 3) {
                                        int var7;
                                        int var8;
                                        try {
                                            var7 = Integer.parseInt(var6[0].trim());
                                            var8 = Integer.parseInt(var6[2].trim());
                                        } catch (NumberFormatException var12) {
                                            continue;
                                        }

                                        String var9 = var6[1].trim();
                                        int var10 = getCommodityIndex(var9);
                                        if (isValidDay(var7) && var10 != -1) {
                                            profits[var1][var7 - 1][var10] = var8;
                                        }
                                    }
                                }
                            }
                        } catch (Throwable var13) {
                            try {
                                var4.close();
                            } catch (Throwable var11) {
                                var13.addSuppressed(var11);
                            }

                            throw var13;
                        }

                        var4.close();
                    } catch (IOException var14) {
                    }
                }
            }

            dataLoaded = true;
        } else {
            dataLoaded = false;
        }
    }

    private static boolean isValidMonth(int var0) {
        return var0 >= 0 && var0 < 12;
    }

    private static boolean isValidDay(int var0) {
        return var0 >= 1 && var0 <= 28;
    }

    private static int getCommodityIndex(String var0) {
        if (var0 == null) {
            return -1;
        } else {
            for(int var1 = 0; var1 < commodities.length; ++var1) {
                if (commodities[var1].equals(var0)) {
                    return var1;
                }
            }

            return -1;
        }
    }

    public static String mostProfitableCommodityInMonth(int var0) {
        if (!isValidMonth(var0)) {
            return "INVALID_MONTH";
        } else {
            int var1 = 0;
            int var2 = Integer.MIN_VALUE;

            for(int var3 = 0; var3 < 5; ++var3) {
                int var4 = 0;

                for(int var5 = 0; var5 < 28; ++var5) {
                    var4 += profits[var0][var5][var3];
                }

                if (var4 > var2) {
                    var2 = var4;
                    var1 = var3;
                }
            }

            String var10000 = commodities[var1];
            return var10000 + " " + var2;
        }
    }

    public static int totalProfitOnDay(int var0, int var1) {
        if (isValidMonth(var0) && isValidDay(var1)) {
            int var2 = var1 - 1;
            int var3 = 0;

            for(int var4 = 0; var4 < 5; ++var4) {
                var3 += profits[var0][var2][var4];
            }

            return var3;
        } else {
            return -99999;
        }
    }

    public static int commodityProfitInRange(String var0, int var1, int var2) {
        int var3 = getCommodityIndex(var0);
        if (var3 != -1 && isValidDay(var1) && isValidDay(var2) && var1 <= var2) {
            int var4 = var1 - 1;
            int var5 = var2 - 1;
            int var6 = 0;

            for(int var7 = 0; var7 < 12; ++var7) {
                for(int var8 = var4; var8 <= var5; ++var8) {
                    var6 += profits[var7][var8][var3];
                }
            }

            return var6;
        } else {
            return -99999;
        }
    }

    public static int bestDayOfMonth(int var0) {
        if (!isValidMonth(var0)) {
            return -1;
        } else {
            int var1 = 1;
            int var2 = Integer.MIN_VALUE;

            for(int var3 = 1; var3 <= 28; ++var3) {
                int var4 = totalProfitOnDay(var0, var3);
                if (var4 > var2) {
                    var2 = var4;
                    var1 = var3;
                }
            }

            return var1;
        }
    }

    public static String bestMonthForCommodity(String var0) {
        int var1 = getCommodityIndex(var0);
        if (var1 == -1) {
            return "INVALID_COMMODITY";
        } else {
            int var2 = 0;
            int var3 = Integer.MIN_VALUE;

            for(int var4 = 0; var4 < 12; ++var4) {
                int var5 = 0;

                for(int var6 = 0; var6 < 28; ++var6) {
                    var5 += profits[var4][var6][var1];
                }

                if (var5 > var3) {
                    var3 = var5;
                    var2 = var4;
                }
            }

            return months[var2];
        }
    }

    public static int consecutiveLossDays(String var0) {
        int var1 = getCommodityIndex(var0);
        if (var1 == -1) {
            return -1;
        } else {
            int var2 = 0;
            int var3 = 0;

            for(int var4 = 0; var4 < 12; ++var4) {
                for(int var5 = 0; var5 < 28; ++var5) {
                    if (profits[var4][var5][var1] < 0) {
                        ++var3;
                        if (var3 > var2) {
                            var2 = var3;
                        }
                    } else {
                        var3 = 0;
                    }
                }
            }

            return var2;
        }
    }

    public static int daysAboveThreshold(String var0, int var1) {
        int var2 = getCommodityIndex(var0);
        if (var2 == -1) {
            return -1;
        } else {
            int var3 = 0;

            for(int var4 = 0; var4 < 12; ++var4) {
                for(int var5 = 0; var5 < 28; ++var5) {
                    if (profits[var4][var5][var2] > var1) {
                        ++var3;
                    }
                }
            }

            return var3;
        }
    }

    public static int biggestDailySwing(int var0) {
        if (!isValidMonth(var0)) {
            return -99999;
        } else {
            int[] var1 = new int[28];

            int var2;
            int var3;
            int var4;
            for(var2 = 0; var2 < 28; ++var2) {
                var3 = 0;

                for(var4 = 0; var4 < 5; ++var4) {
                    var3 += profits[var0][var2][var4];
                }

                var1[var2] = var3;
            }

            var2 = 0;

            for(var3 = 0; var3 < 27; ++var3) {
                var4 = var1[var3] - var1[var3 + 1];
                if (var4 < 0) {
                    var4 = -var4;
                }

                if (var4 > var2) {
                    var2 = var4;
                }
            }

            return var2;
        }
    }

    public static String compareTwoCommodities(String var0, String var1) {
        int var2 = getCommodityIndex(var0);
        int var3 = getCommodityIndex(var1);
        if (var2 != -1 && var3 != -1) {
            int var4 = 0;
            int var5 = 0;

            int var6;
            for(var6 = 0; var6 < 12; ++var6) {
                for(int var7 = 0; var7 < 28; ++var7) {
                    var4 += profits[var6][var7][var2];
                    var5 += profits[var6][var7][var3];
                }
            }

            if (var4 > var5) {
                var6 = var4 - var5;
                return var0 + " is better by " + var6;
            } else if (var5 > var4) {
                var6 = var5 - var4;
                return var1 + " is better by " + var6;
            } else {
                return "Equal";
            }
        } else {
            return "INVALID_COMMODITY";
        }
    }

    public static String bestWeekOfMonth(int var0) {
        if (!isValidMonth(var0)) {
            return "INVALID_MONTH";
        } else {
            int[][] var1 = new int[][]{{1, 7}, {8, 14}, {15, 21}, {22, 28}};
            int var2 = 0;
            int var3 = Integer.MIN_VALUE;

            for(int var4 = 0; var4 < var1.length; ++var4) {
                int var5 = var1[var4][0];
                int var6 = var1[var4][1];
                int var7 = 0;

                for(int var8 = var5; var8 <= var6; ++var8) {
                    int var9 = totalProfitOnDay(var0, var8);
                    var7 += var9;
                }

                if (var7 > var3) {
                    var3 = var7;
                    var2 = var4;
                }
            }

            return "Week " + (var2 + 1);

        }
    }

    public static void main(String[] args) {
        loadData();
        if (!dataLoaded) {
            System.out.println("WARNING: Data_Files folder not found or data not loaded!");
        } else {
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
}
