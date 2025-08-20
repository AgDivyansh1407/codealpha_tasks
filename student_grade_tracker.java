import java.util.ArrayList;
import java.util.Scanner;

public class StudentGradeManager {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        ArrayList<String> studentNames = new ArrayList<>();
        ArrayList<Integer> studentScores = new ArrayList<>();

        System.out.print("Enter number of students: ");
        int numStudents = scanner.nextInt();

        for (int i = 0; i < numStudents; i++) {
            System.out.print("Enter name of student " + (i + 1) + ": ");
            String name = scanner.next();
            System.out.print("Enter score of " + name + ": ");
            int score = scanner.nextInt();

            studentNames.add(name);
            studentScores.add(score);
        }

        int sum = 0;
        int highest = Integer.MIN_VALUE;
        int lowest = Integer.MAX_VALUE;

        for (int score : studentScores) {
            sum += score;
            if (score > highest) {
                highest = score;
            }
            if (score < lowest) {
                lowest = score;
            }
        }

        double average = (double) sum / studentScores.size();

        System.out.println("\n===== Student Grade Report =====");
        for (int i = 0; i < studentNames.size(); i++) {
            System.out.println(studentNames.get(i) + " : " + studentScores.get(i));
        }

        System.out.println("-------------------------------");
        System.out.println("Average Score: " + average);
        System.out.println("Highest Score: " + highest);
        System.out.println("Lowest Score: " + lowest);
    }
}

