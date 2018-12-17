package AlgorithmDE;

import sun.management.Agent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generation {
    private List<Point> InitialPopulation = new ArrayList<>();
    private List<Point> TemporaryPopulation = new ArrayList<>();
    private List<Point> AfterCrossGeneration = new ArrayList<>();
    private double F;
    private double CR;
    private int PopulationCount;
    private int Repeats;
    DecimalFormat df = new DecimalFormat("+#,##0.0000000000;-#");

    public List<Point> getActualPopulation() {
        return InitialPopulation;
    }
    public Generation(double F, double CR, int PopulationCount, int Repeats){
        this.F = F;
        this.CR = CR;
        this.PopulationCount = PopulationCount;
        this.Repeats = Repeats;
    }

    public void GenerateResultPopulation() {
        GenerateRandomPopulation(PopulationCount);
        for (int i = 0; i < Repeats; i++){
            MutateGeneration();
            CrossGeneration();
            InitialPopulation = Selection();
            clearLists();
        }
    }

    private void clearLists() {
        TemporaryPopulation.clear();
        AfterCrossGeneration.clear();
    }

    private List<Point> Selection() {
        List<Point> tempList = new ArrayList<>();
        for (int i = 0; i < PopulationCount; i++){
            if (Math.abs(InitialPopulation.get(i).getRosenBrock()) < Math.abs(AfterCrossGeneration.get(i).getRosenBrock())){
                tempList.add(InitialPopulation.get(i));
            }else{
                tempList.add(AfterCrossGeneration.get(i));
            }
        }
        return tempList;
    }

    private void CrossGeneration() {
        Random random = new Random();
        for (int i = 0, j = 1; i < PopulationCount - 1; i+=2, j+=2){
            if (random.nextDouble() <= CR){
                CrossTwoPoints(TemporaryPopulation.get(i), TemporaryPopulation.get(j));
            }
            else{
                AfterCrossGeneration.add(TemporaryPopulation.get(i));
                AfterCrossGeneration.add(TemporaryPopulation.get(j));
            }
        }
    }

    private void CrossTwoPoints(Point firstPoint, Point secondPoint) {
        Random random = new Random();
        int locus = random.nextInt(8) + 3;
        try{

            String firstX = String.valueOf(df.format(firstPoint.getX())).substring(0, locus) + String.valueOf(df.format(secondPoint.getX())).substring(locus);
            String secondX = String.valueOf(df.format(secondPoint.getX())).substring(0, locus) + String.valueOf(df.format(firstPoint.getX())).substring(locus);
            String firstY = String.valueOf(df.format(firstPoint.getY())).substring(0, locus) + String.valueOf(df.format(secondPoint.getY())).substring(locus);
            String secondY = String.valueOf(df.format(secondPoint.getY())).substring(0, locus) + String.valueOf(df.format(firstPoint.getY())).substring(locus);
            AfterCrossGeneration.add(new Point(Double.valueOf(firstX), Double.valueOf(firstY)));
            AfterCrossGeneration.add(new Point(Double.valueOf(secondX), Double.valueOf(secondY)));
        }catch (StringIndexOutOfBoundsException e){
            e.printStackTrace();
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    private void MutateGeneration() {
        for (int i = 0; i < PopulationCount; i++){
            TemporaryPopulation.add(MakeNewMutatedUnit(InitialPopulation.get(i), i));
        }
    }

    private Point MakeNewMutatedUnit(Point iteratedPoint, int i) {
        Random random = new Random();
        int firstIndex = random.nextInt(PopulationCount);
        int secondIndex = random.nextInt(PopulationCount);
        while (firstIndex == i){
            firstIndex = random.nextInt(PopulationCount);
        }
        while (secondIndex == i || secondIndex == firstIndex){
            secondIndex = random.nextInt(PopulationCount);
        }
        double x = iteratedPoint.getX() + (F*(InitialPopulation.get(firstIndex).getX() - InitialPopulation.get(secondIndex).getX()));
        double y = iteratedPoint.getY() + (F*(InitialPopulation.get(firstIndex).getY() - InitialPopulation.get(secondIndex).getY()));
        return new Point(x, y);
    }

    private void GenerateRandomPopulation(int Population) {
        Random random = new Random();
        for (int i = 0; i < Population; i++){
            InitialPopulation.add(new Point(random.nextDouble(), random.nextDouble()));
        }
    }

}
