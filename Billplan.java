有一个账，里面是每天的cost，然后设计一个记账function，说我们其他team 会call这个function 更新某天的增加的cost
然后 假设 只有需要记一个月账
我就简单假设这个 bill 是一个array，长度31，每天对应一个index，更新cost就简单的是bill[index] += newCost
当时不理解题目什么意思，就先简单提出讨论的基础，结果小哥说这样可以 我就ok了
然后是说现在给我一个月的某一天，要计算这个月到这天的全部cost
给了一个公式
planCost = Max(MinimumPlanCost, usageCost * percentage) （MinimumPlanCost & percentage 是constant）
usageCost = total cost in bill totalCost = usageCost + planCos

  public class BillingSystem {

    private static final int DAYS_IN_MONTH = 31;
    private double[] bill = new double[DAYS_IN_MONTH];
    private double minimumPlanCost;
    private double percentage;

    public BillingSystem(double minimumPlanCost, double percentage) {
        this.minimumPlanCost = minimumPlanCost;
        this.percentage = percentage;
    }

    // Updates the cost for a specific day (1-based index for day)
    public void updateDailyCost(int day, double additionalCost) {
        if (day < 1 || day > DAYS_IN_MONTH) {
            throw new IllegalArgumentException("Invalid day: " + day);
        }
        bill[day - 1] += additionalCost; // Subtract 1 for 0-based array indexing
    }

    // Calculates the total cost up to a given day
    public double calculateTotalCost(int day) {
        if (day < 1 || day > DAYS_IN_MONTH) {
            throw new IllegalArgumentException("Invalid day: " + day);
        }

        double usageCost = 0;
        for (int i = 0; i < day; i++) {
            usageCost += bill[i];
        }

        double planCost = Math.max(minimumPlanCost, usageCost * percentage);
        return usageCost + planCost;
    }

    // Main method for testing
    public static void main(String[] args) {
        BillingSystem billingSystem = new BillingSystem(100, 0.1); // Example constants

        // Update costs for some days
        billingSystem.updateDailyCost(1, 50);
        billingSystem.updateDailyCost(2, 75);
        billingSystem.updateDailyCost(10, 150);

        // Calculate total cost up to a given day
        double totalCost = billingSystem.calculateTotalCost(10);
        System.out.println("Total cost up to day 10: " + totalCost);
    }
}

===
然后增加了一个条件
一个月可以有多个plan，like form 0 - 10 是 一个plan，11 - 15 是一个 16 - 30 是一个
我就跟他讨论了一个data model
[ { start:0,end:10, MinimumPlanCost: v1, perc

  public class Plan {
    int startDay;
    int endDay;
    double minimumPlanCost;
    double percentage;

    public Plan(int startDay, int endDay, double minimumPlanCost, double percentage) {
        this.startDay = startDay;
        this.endDay = endDay;
        this.minimumPlanCost = minimumPlanCost;
        this.percentage = percentage;
    }
}
import java.util.ArrayList;
import java.util.List;

public class BillingSystem {

    private static final int DAYS_IN_MONTH = 31;
    private double[] bill = new double[DAYS_IN_MONTH];
    private List<Plan> plans;

    public BillingSystem() {
        this.plans = new ArrayList<>();
    }

    public void addPlan(Plan plan) {
        plans.add(plan);
    }

    public void updateDailyCost(int day, double additionalCost) {
        if (day < 1 || day > DAYS_IN_MONTH) {
            throw new IllegalArgumentException("Invalid day: " + day);
        }
        bill[day - 1] += additionalCost;
    }

    public double calculateTotalCost(int day) {
        if (day < 1 || day > DAYS_IN_MONTH) {
            throw new IllegalArgumentException("Invalid day: " + day);
        }

        double usageCost = 0;
        for (int i = 0; i < day; i++) {
            usageCost += bill[i];
        }

        double totalPlanCost = 0;
        for (Plan plan : plans) {
            if (day >= plan.startDay && day <= plan.endDay) {
                double planCost = Math.max(plan.minimumPlanCost, usageCost * plan.percentage);
                totalPlanCost += planCost;
                break; // Assuming plans do not overlap
            }
        }

        return usageCost + totalPlanCost;
    }

    // Main method for testing
    public static void main(String[] args) {
        BillingSystem billingSystem = new BillingSystem();

        // Define plans
        billingSystem.addPlan(new Plan(1, 10, 100, 0.1));
        billingSystem.addPlan(new Plan(11, 15, 150, 0.15));
        billingSystem.addPlan(new Plan(16, 30, 200, 0.2));

        // Update costs for some days
        billingSystem.updateDailyCost(1, 50);
        billingSystem.updateDailyCost(2, 75);
        billingSystem.updateDailyCost(10, 150);

        // Calculate total cost up to a given day
        double totalCost = billingSystem.calculateTotalCost(10);
        System.out.println("Total cost up to day 10: " + totalCost);
    }
}

