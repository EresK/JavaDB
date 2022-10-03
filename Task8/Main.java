package Task8;

public class Main {
    public static void main(String[] args) {
        int threads_number = 8;

        try {
            threads_number = args.length > 0 ? Integer.parseInt(args[0]) : threads_number;
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }

        LeibnizSeriesBarrier series = new LeibnizSeriesBarrier(threads_number);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Signal caught");

            double res = series.CancelCalculation();
            System.out.println(res);
        }));

        series.Pi();
    }
}
