package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        //building BN
        //getting input data
        NumberFormat nf = NumberFormat.getInstance(new Locale("uk", "UA"));
        int numCounter;
        int maxGrade = 0;
        File fileInputData = new File("G:\\.учеба\\ДНУ. Бакалавриат\\8 семестр\\.практика\\BN_practice\\src\\com\\company\\InputData.TXT");
        Scanner fileInputDataScanner = new Scanner(fileInputData);
        while (fileInputDataScanner.hasNextLine()) {
            String line = fileInputDataScanner.nextLine();
            String[] dataString = line.split(" ");
            if (!(dataString[0].equals("maxGrade"))) {
                System.out.println("Your input data is not enough! Check it!");
                System.exit(0);
            }
            if (dataString[0].equals("maxGrade")) {
                if (Integer.parseInt(dataString[1]) <= 0) {
                    System.out.println("Your maximum grade value is incorrect! Check it!");
                    System.exit(0);
                }
                maxGrade = Integer.parseInt(dataString[1]);
            }
        }
        fileInputDataScanner.close();

        File fileIncidenceMatrixCount = new File("G:\\.учеба\\ДНУ. Бакалавриат\\8 семестр\\.практика\\BN_practice\\src\\com\\company\\IncidenceMatrix.TXT");
        Scanner fileIncidenceMatrixCountScanner = new Scanner(fileIncidenceMatrixCount);
        int numVertexes = 0;
        int counter = 0;
        numCounter = 0;
        while (fileIncidenceMatrixCountScanner.hasNextLine()) {
            String line = fileIncidenceMatrixCountScanner.nextLine();
            String[] numbersString = line.split(" ");
            numVertexes = numbersString.length;
            if (counter != 0) {
                if (numVertexes != counter) {
                    System.out.println("Your incidence matrix is incorrect! Check it!");
                    System.exit(0);
                }
            }
            counter = numbersString.length;
            numCounter++;
        }
        if (numCounter != numVertexes) {
            System.out.println("Your incidence matrix is incorrect! Check it!");
            System.exit(0);
        }
        fileIncidenceMatrixCountScanner.close();

        //building graph
        File fileIncidenceMatrix = new File("G:\\.учеба\\ДНУ. Бакалавриат\\8 семестр\\.практика\\BN_practice\\src\\com\\company\\IncidenceMatrix.TXT");
        Scanner fileIncidenceMatrixScanner = new Scanner(fileIncidenceMatrix);
        int[][] incidenceMatrix = new int[numVertexes][numVertexes];
        numCounter = 0;
        while (fileIncidenceMatrixScanner.hasNextLine()) {
            String line = fileIncidenceMatrixScanner.nextLine();
            String[] numbersString = line.split(" ");
            int[] numbers = new int[numbersString.length];
            counter = 0;
            for (String number : numbersString) {
                numbers[counter++] = nf.parse(number.trim()).intValue();
            }
            for (int i = 0; i < numbers.length; i++) {
                if (!(numbers[i] == 0 || numbers[i] == 1 || numbers[i] == (-1))) {
                    System.out.println("Your incidence matrix is incorrect! Check it!");
                    System.exit(0);
                }
                incidenceMatrix[numCounter][i] = numbers[i];
            }
            numCounter++;
        }
        fileIncidenceMatrixScanner.close();

        int beginOfStructure = 0, endOfStructure = 0;
        for (int i = 1; i < numVertexes; i++) {
            if (incidenceMatrix[i - 1][i] == 0 && incidenceMatrix[i][i - 1] == 0) {
                for (int j = 0; j < numVertexes; j++) {
                    if ((incidenceMatrix[i - 1][j] == 1 && j > (i - 1)) || (incidenceMatrix[i - 1][j] == (-1) && j < (i - 1))) {
                        numCounter = 0;
                        for (int k = 0; k < numVertexes; k++) {
                            if (incidenceMatrix[j][k] == 1) {
                                numCounter++;
                            }
                        }
                        if (numCounter == 0) {
                            beginOfStructure = i - 1;
                        }
                    }
                }
            }
        }
        ArrayList<Integer> secondLayer = new ArrayList<>();
        for (int i = beginOfStructure; i < numVertexes; i++) {
            for (int j = 0; j < numVertexes; j++) {
                if (!(incidenceMatrix[i][j] == 1)) {
                    secondLayer.add(i);
                    break;
                }
            }
        }
        for (int i = secondLayer.get(0); i < numVertexes; i++) {
            for (int j = 0; j < numVertexes; j++) {
                if (incidenceMatrix[i][j] == (-1) && !secondLayer.contains(j)) {
                    if (!secondLayer.contains(i)) {
                        secondLayer.add(i);
                    }
                } else if (secondLayer.contains(j)) {
                    break;
                }
            }
        }
        endOfStructure = secondLayer.get(secondLayer.size() - 1);
        int numCompetences = 0, numTestQuestions = 0;
        int flagHasNoParent;
        for (int i = beginOfStructure; i < endOfStructure; i++) {
            flagHasNoParent = 0;
            for (int j = 0; j < numVertexes; j++) {
                if (incidenceMatrix[i][j] == 1) {
                    flagHasNoParent = 1;
                }
            }
            if (flagHasNoParent == 1) {
                numCompetences++;
            } else if (flagHasNoParent == 0) {
                numTestQuestions++;
            }
        }

        File fileTestsToCheck = new File("G:\\.учеба\\ДНУ. Бакалавриат\\8 семестр\\.практика\\BN_practice\\src\\com\\company\\Tests.TXT");
        Scanner fileTestsToCheckScanner = new Scanner(fileTestsToCheck);
        int numTests = 0;
        while (fileTestsToCheckScanner.hasNextLine()) {
            String line = fileTestsToCheckScanner.nextLine();
            String[] numbersString = line.split(" ");
            if (numbersString.length != numVertexes) {
                System.out.println("You have wrong number of tests! Check them!");
                System.exit(0);
            }
            numTests++;
        }
        fileTestsToCheckScanner.close();

        File fileTests = new File("G:\\.учеба\\ДНУ. Бакалавриат\\8 семестр\\.практика\\BN_practice\\src\\com\\company\\Tests.TXT");
        Scanner fileTestsScanner = new Scanner(fileTests);
        int[][] tests = new int[numTests][numVertexes];
        numCounter = 0;
        while (fileTestsScanner.hasNextLine()) {
            String line = fileTestsScanner.nextLine();
            String[] numbersString = line.split(" ");
            int[] numbers = new int[numbersString.length];
            counter = 0;
            for (String number : numbersString) {
                numbers[counter++] = nf.parse(number.trim()).intValue();
            }
            counter = 0;
            for (int i = 0; i < numVertexes; i++) {
                for (int j = 0; j < (maxGrade + 1); j++) {
                    if (numbers[i] == j) {
                        counter++;
                    }
                }
                if (counter == 0) {
                    System.out.println("Your competences are incorrect! Check them!");
                    System.exit(0);
                }
            }
            for (int i = 0; i < numbersString.length; i++) {
                tests[numCounter][i] = numbers[i];
            }
            numCounter++;
        }
        fileTestsScanner.close();

        //building probabilities
        int[] probabilities = new int[numVertexes];
        ArrayList<Data> dataFromList = new ArrayList<>();
        ArrayList<Integer> incomeVertexes = new ArrayList<Integer>();
        Data data;
        for (int i = 0; i < numVertexes; i++) {
            numCounter = 0;
            incomeVertexes.clear();
            for (int j = 0; j < numVertexes; j++) {
                if (incidenceMatrix[i][j] == (-1)) {
                    numCounter++;
                    incomeVertexes.add(j);
                }
            }
            probabilities[i] = (int) Math.pow((maxGrade + 1), numCounter);
            double[][] probabilitiesNum = new double[(probabilities[i])][maxGrade + 1];
            counter = 0;
            int numLineCounter;
            for (int k = 0; k < (probabilities[i]); k++) {
                for (int m = 0; m < (maxGrade + 1); m++) {
                    if (numCounter == 0) {
                        probabilitiesNum[k][m] = (1.0 / (double) (maxGrade + 1));
                    } else if (numCounter == 1) {
                        numLineCounter = 0;
                        for (int n = 0; n < numTests; n++) {
                            for (int z = 0; z < incomeVertexes.size(); z++) {
                                if (tests[n][incomeVertexes.get(z)] == k && tests[n][i] == m) {
                                    numLineCounter++;
                                }
                            }
                        }
                        probabilitiesNum[k][m] = (double) (numLineCounter) / (double) (numTests);
                    } else if (numCounter == 2) {
                        numLineCounter = 0;
                        for (int n = 0; n < numTests; n++) {
                            for (int z = 1; z < incomeVertexes.size(); z++) {
                                if (((maxGrade + 1) * tests[n][incomeVertexes.get(z - 1)] + tests[n][incomeVertexes.get(z)]) == k && tests[n][i] == m) {
                                    numLineCounter++;
                                }
                            }
                        }
                        probabilitiesNum[k][m] = (double) (numLineCounter) / (double) (numTests);
                    } else if (numCounter == 3) {
                        numLineCounter = 0;
                        for (int n = 0; n < numTests; n++) {
                            for (int z = 2; z < incomeVertexes.size(); z++) {
                                if (((maxGrade + 1) * tests[n][incomeVertexes.get(z - 2)] + (maxGrade + 1) * tests[n][incomeVertexes.get(z - 1)] + tests[n][incomeVertexes.get(z)]) == k && tests[n][i] == m) {
                                    numLineCounter++;
                                }
                            }
                        }
                        probabilitiesNum[k][m] = (double) (numLineCounter) / (double) (numTests);
                    }
                    counter++;
                }
            }
            for (int k = 0; k < (probabilities[i]); k++) {
                for (int m = 0; m < (maxGrade + 1); m++) {
                    System.out.print(probabilitiesNum[k][m] + " ");
                }
                System.out.println();
            }
            System.out.println();
            data = new Data(i, probabilitiesNum);
            dataFromList.add(i, data);
        }

        //algorithm
        if (beginOfStructure != 0) {
            ArrayList<Integer> structureToExclude = new ArrayList<>();
            double[][] tempTable = new double[1][maxGrade + 1];
            for (int i = beginOfStructure; i < beginOfStructure + numCompetences; i++) {
                structureToExclude.clear();
                for (int j = 0; j < numVertexes; j++) {
                    if (incidenceMatrix[i][j] == (-1)) {
                        if (!structureToExclude.contains(i)) {
                            structureToExclude.add(i);
                        }
                        if (!structureToExclude.contains(j)) {
                            structureToExclude.add(j);
                        }
                    }
                }
                for (int j = 1; j < structureToExclude.size(); j++) {
                    for (int k = 0; k < numVertexes; k++) {
                        if (incidenceMatrix[j][k] == 1 || incidenceMatrix[j][k] == (-1)) {
                            if (!structureToExclude.contains(k)) {
                                structureToExclude.add(k);
                            }
                        }
                    }
                }
                for (int j = 0; j < structureToExclude.size(); j++) {
                    if (structureToExclude.get(j)>=beginOfStructure) {
                        for (int z = 0; z < maxGrade + 1; z++) {
                            numCounter = 0;
                            for (int d = 0; d < numTests; d++) {
                                if (tests[d][j] == z) {
                                    numCounter++;
                                }
                            }
                            tempTable[0][z] = (double) (numCounter) / (double) (numTests);
                        }
                        data = new Data(i, tempTable);
                        dataFromList.set(i, data);
                    }
                }
            }
            for (int i = beginOfStructure + numCompetences; i < endOfStructure; i++) {
                //
            }
        }

        //Bayes theorem
        double sumNumerator, sumDenominator;
        double[][] posteriorProbabilities = new double[numTests][numCompetences];
        ArrayList<Integer> index = new ArrayList<>();
        for (int g = 0; g < numTests; g++) {
            for (int i = 0; i < numCompetences; i++) {
                sumNumerator = 0;
                sumDenominator = 0;
                index.clear();
                for (int j = 0; j < numVertexes; j++) {
                    if (incidenceMatrix[i][j] == 1) {
                        if (!index.contains(j)) {
                            index.add(j);
                        }
                        for (int k = 0; k < numVertexes; k++) {
                            if (incidenceMatrix[j][k] == (-1)) {
                                if (!index.contains(k)) {
                                    index.add(k);
                                }
                            }
                        }
                    }
                }

                for (int m = 0; m < (maxGrade + 1); m++) {
                    double tempSum = 1;
                    for (int k = numCompetences; k < (numCompetences + numTestQuestions); k++) {
                        if (index.contains(k)) {
                            numCounter = 0;
                            for (int h = 0; h < numVertexes; h++) {
                                if (incidenceMatrix[k][h] == (-1)) {
                                    numCounter++;
                                }
                            }
                            if (numCounter == 1) {
                                tempSum *= dataFromList.get(k).matrix[tests[g][i]][tests[g][k]];
                            } else if (numCounter == 2) {
                                tempSum *= dataFromList.get(k).matrix[(maxGrade + 1) * tests[g][i] + m][tests[g][k]];
                            } else if (numCounter == 3) {
                                //
                            }
                        }
                    }
                    for (int h = beginOfStructure; h < beginOfStructure+numCompetences; h++) {
                        if (index.contains(h)) {
                            if (h == i) {
                                tempSum *= dataFromList.get(h).matrix[0][tests[g][i]];
                            } else {
                                tempSum *= dataFromList.get(h).matrix[0][m];
                            }
                        }
                    }
                    sumNumerator += tempSum;
                }
                for (int n = 0; n < (maxGrade + 1); n++) {
                    for (int m = 0; m < (maxGrade + 1); m++) {
                        double tempSum = 1;
                        for (int k = numCompetences; k < (numCompetences + numTestQuestions); k++) {
                            if (index.contains(k)) {
                                numCounter = 0;
                                for (int h = 0; h < numVertexes; h++) {
                                    if (incidenceMatrix[k][h] == (-1)) {
                                        numCounter++;
                                    }
                                }
                                if (numCounter == 1) {
                                    tempSum *= dataFromList.get(k).matrix[n][tests[g][k]];
                                } else if (numCounter == 2) {
                                    tempSum *= dataFromList.get(k).matrix[(maxGrade + 1) * n + m][tests[g][k]];
                                } else if (numCounter == 3) {
                                    //
                                }
                            }
                        }
                        for (int h = beginOfStructure; h < beginOfStructure+numCompetences; h++) {
                            if (index.contains(h)) {
                                if (h == i) {
                                    tempSum *= dataFromList.get(h).matrix[0][n];
                                } else {
                                    tempSum *= dataFromList.get(h).matrix[0][m];
                                }
                            }
                        }
                        sumDenominator += tempSum;
                    }
                }
                posteriorProbabilities[g][i] = (sumNumerator) / (sumDenominator);
            }
        }

        try (FileWriter writer = new FileWriter("Output.TXT", false)) {
            for (int g = 0; g < numTests; g++) {
                writer.write("Test: \t");
                writer.write(Integer.toString(g + 1));
                writer.append('\n');
                writer.append('\n');
                for (int i = 0; i < numCompetences; i++) {
                    writer.write("Competence: \t\t\t");
                    writer.write(Integer.toString(i + 1));
                    writer.append('\n');
                    writer.write("Initial value of competence: \t");
                    writer.write(Integer.toString(tests[g][i]));
                    writer.append('\n');
                    writer.write("Initial probability: \t\t");
                    writer.write(Double.toString(dataFromList.get(i).matrix[0][tests[g][i]]));
                    writer.append('\n');
                    writer.write("Final probability: \t\t");
                    writer.write(Double.toString(posteriorProbabilities[g][i]));
                    writer.append('\n');
                    writer.append('\n');
                }
                writer.append('\n');
            }
        }
    }
}
